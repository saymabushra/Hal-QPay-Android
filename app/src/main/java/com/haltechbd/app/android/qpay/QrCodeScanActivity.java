package com.haltechbd.app.android.qpay;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetMasterKeyAndAccountNumber;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class QrCodeScanActivity extends Activity {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
String mStrBankBin,mStrUrlForQrCode,mStrMethodName,strEncryptDestinationAccountNumberFromQr,mStrServicePackage,
        mStrDestinationWallet,mStrDestinationName,strSourceWallet, mStrEncryptMerchatWalletForOtp, mStrCustomerWalletByQrCode, mStrEncryptCustomerWalletByQrCode;  // fro main
    String strEncryptMerchantAccountNumber,strMerchantAccountNumber,mStrDestinationAccountHolderName;  // for MP customer
String mStrEncryptCustomerWallet,mStrSourceWallet,strEncryptPIN,mStrCustomerWallet;  //fpr Mp
EncryptionDecryption encryptionDecryption = new EncryptionDecryption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        mStrServicePackage = GlobalData.getStrPackage();

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                ActivityCompat.requestPermissions(QrCodeScanActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    surfaceView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(300);
                            String strQR = qrCodes.valueAt(0).displayValue;
                            cameraSource.stop();
                            GlobalData.setStrScanQRDate(strQR);
                           // Toast.makeText(QrCodeScanActivity.this, strQR, Toast.LENGTH_SHORT).show();

                            //-------------------------Main Activity Quick Pay ------------------------------------------
                            if(GlobalData.getStrPageRecord().equalsIgnoreCase("Main"))
                            {

                                //=====================================================
                                strSourceWallet=GlobalData.getStrWallet();
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {
                                    String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                    mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                    String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                    if (mStrBankBin.equalsIgnoreCase("006"))  //For Huawei
                                    {
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    } else if (mStrBankBin.equalsIgnoreCase("002")) //For QPay
                                    {
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    }

                                    String strEncryptAccountNumberAndMasterKeyByQrCode = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                    if (!strEncryptAccountNumberAndMasterKeyByQrCode.equalsIgnoreCase("")) {
                                        int intIndex = strEncryptAccountNumberAndMasterKeyByQrCode.indexOf("*");
                                        if (intIndex == -1) {
                                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                            mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                            mAlertDialogBuilder.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                            mAlertDialog.show();
                                        } else {
                                            String[] parts = strEncryptAccountNumberAndMasterKeyByQrCode.split("\\*");
                                            strEncryptDestinationAccountNumberFromQr = parts[0];
                                            // strDestinationMasterKeyFromQr = parts[1];
                                            String strMerchantEncryptName = parts[1];


                                            //############################## Source Merchant ##############################
                                            //############################## Source Merchant ##############################
                                            if (mStrServicePackage.equalsIgnoreCase("1312050001")) {
                                                try {
                                                    //################ Destination Account Number ####################
                                                    mStrDestinationWallet = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, GlobalData.getStrSessionId());

                                                    String strAccountTypeCode = mStrDestinationWallet.substring(3, 5);
                                                    //####################### Destination Merchant(M2M-FM) ############################
                                                    //####################### Destination Merchant(M2M-FM) ############################
                                                    //####################### Destination Merchant(M2M-FM) ############################
                                                    if (strAccountTypeCode.equalsIgnoreCase("11")) {
                                                        //################ Merchant Name ####################
                                                        //mStrDestinationName = GetMerchantName.getMerchantName(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
                                                        mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//                                        String[] parts01 = mStrDestinationName.split("\\*");
//                                        String strName = parts01[0];
//                                        final String strType = parts01[1];


                                                        //################ Show Dialog ####################
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                        myAlert.setTitle("MERCHANT INFO");
                                                        myAlert.setMessage("MERCHANT NAME" + "\n" + mStrDestinationName + "\n" + "MERCHANT WALLET" + "\n" + mStrDestinationWallet);
                                                        myAlert.setPositiveButton(
                                                                "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        if (strType.equalsIgnoreCase(mStrMerchantRank)) {
//                                                            GlobalData.setStrSourceWallet(mStrSourceWallet);
//                                                            GlobalData.setStrDestinationWallet(mStrDestinationWallet);
//                                                            GlobalData.setStrDestinationWalletName(mStrDestinationName);
//                                                            startActivity(new Intent(MainActivity.this, FM_M2M_Quick.class));
//                                                        } else {
                                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                                        myAlert.setMessage("Account Type not match. Please scan correct account type.");
                                                                        myAlert.setNegativeButton(
                                                                                "OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.cancel();
                                                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog = myAlert.create();
                                                                        alertDialog.show();
//                                                        }

                                                                    }
                                                                });
                                                        myAlert.setNegativeButton(
                                                                "CANCEL",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();

                                                    }
                                                    //####################### Destination Customer(M2C-MP_Through_C_C2M_AddToFav) ############################
                                                    //####################### Destination Customer(M2C-MP_Through_C_C2M_AddToFav) ############################
                                                    //####################### Destination Customer(M2C-MP_Through_C_C2M_AddToFav) ############################
                                                    if (strAccountTypeCode.equalsIgnoreCase("12")) {
//                                        String strDestinationRank = mStrDestinationWallet.substring(13, 14);
//                                        if (mStrMerchantRank.equalsIgnoreCase("VOUPAY")) {
//                                            if (strDestinationRank.equalsIgnoreCase("1")) {
                                                        //################ Customer Name ####################
                                                        //mStrDestinationName = GetMerchantName.getMerchantName(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
                                                        mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
                                                        //################ Show Dialog ####################
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                        myAlert.setTitle("CUSTOMER INFO");
                                                        myAlert.setMessage("CUSTOMER NAME" + "\n" + mStrDestinationName + "\n" + "CUSTOMER WALLET" + "\n" + mStrDestinationWallet);
                                                        myAlert.setPositiveButton(
                                                                "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        try {
                                                                            GlobalData.setStrSourceWallet(strSourceWallet);
                                                                            GlobalData.setStrDestinationWallet(mStrDestinationWallet);
                                                                            GlobalData.setStrDestinationWalletName(mStrDestinationName);
                                                                            mStrEncryptMerchatWalletForOtp = encryptionDecryption.Encrypt(strSourceWallet, GlobalData.getStrSessionId());
                                                                            mStrCustomerWalletByQrCode = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, GlobalData.getStrSessionId());
                                                                            mStrEncryptCustomerWalletByQrCode = encryptionDecryption.Encrypt(mStrCustomerWalletByQrCode, GlobalData.getStrSessionId());
                                                                            String strEncryptPIN = encryptionDecryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                                                                            startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M_Quick.class));
                                                                            finish();
                                                                            ///-----------------####################################----------------------------------
//                                                                    Intent i = new Intent(QPayMenuNew.this, MP_Through_M_C2M_Quick.class);
//                                                                    Bundle bundle = new Bundle();
//                                                                    bundle.putString("source", mStrSourceWallet.toString());
//                                                                    bundle.putString("destination", mStrDestinationAccountNumberFromQr);
//                                                                    i.putExtras(bundle);
//                                                                    startActivity(i);

                                                                            ///-------------------=====##############################==================-------------------
                                                                            sendCustomerOtp(
                                                                                    mStrEncryptMerchatWalletForOtp,
                                                                                    strEncryptPIN,
                                                                                    mStrEncryptCustomerWalletByQrCode);
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                });
                                                        myAlert.setNegativeButton(
                                                                "CANCEL",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
//                                            }
//                                            else {
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                                                myAlert.setMessage("Account Type not match. Please scan correct account type.");
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            }
//                                        }


                                                    }

                                                } catch (Exception exception) {
                                                    exception.printStackTrace();
                                                }
                                            }


                                            //############################## Source Customer ##############################
                                            if (mStrServicePackage.equalsIgnoreCase("1205190003")) {
                                                try {
                                                    //################ Destination Account Number ####################
                                                    // mStrDestinationWallet = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
                                                    mStrDestinationWallet = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, GlobalData.getStrSessionId());
                                                    String strAccountTypeCode = mStrDestinationWallet.substring(3, 5);
                                                    //####################### Destination Merchant(C2M-MP_Through_C_C2M_AddToFav) ############################
                                                    //####################### Destination Merchant(C2M-MP_Through_C_C2M_AddToFav) ############################
                                                    //####################### Destination Merchant(C2M-MP_Through_C_C2M_AddToFav) ############################
                                                    if (strAccountTypeCode.equalsIgnoreCase("11")) {
                                                        //################ Merchant Name ####################
                                                        //mStrDestinationName = GetMerchantName.getMerchantName(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
                                                        mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//

                                                        //################ Show Dialog ####################
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                        myAlert.setTitle("MERCHANT INFO");
                                                        myAlert.setMessage("MERCHANT NAME" + "\n" + mStrDestinationName + "\n" + "MERCHANT WALLET" + "\n" + mStrDestinationWallet);
                                                        myAlert.setPositiveButton(
                                                                "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        GlobalData.setStrSourceWallet(strSourceWallet);

                                                                        GlobalData.setStrDestinationWallet(mStrDestinationWallet);
                                                                        GlobalData.setStrDestinationWalletName(mStrDestinationName);
                                                                        startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M_Quick.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        myAlert.setNegativeButton(
                                                                "CANCEL",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                    }


                                                    //####################### Destination if Customer ############################
                                                    //####################### Destination if Customer ############################
                                                    if (strAccountTypeCode.equalsIgnoreCase("12")) {

                                                        mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
                                                        //################ Show Dialog ####################
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                        myAlert.setTitle("BENEFICIARY INFO");
                                                        myAlert.setMessage("BENEFICIARY NAME" + "\n" + mStrDestinationName + "\n" + "BENEFICIARY WALLET" + "\n" + mStrDestinationWallet);
                                                        myAlert.setPositiveButton(
                                                                "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
//                                                            GlobalData.setStrSourceWallet(mStrSourceWallet);
//                                                            GlobalData.setStrDestinationWallet(mStrDestinationWallet);
//                                                            GlobalData.setStrDestinationWalletName(mStrDestinationName);
//                                                            startActivity(new Intent(MainActivity.this, FT_C2C_Quick.class));

                                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                                        myAlert.setMessage("Account Type not match. Please scan correct account type.");
                                                                        myAlert.setNegativeButton(
                                                                                "OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.cancel();
                                                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog = myAlert.create();
                                                                        alertDialog.show();

                                                                    }
                                                                });
                                                        myAlert.setNegativeButton(
                                                                "CANCEL",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
//
                                                    }


                                                } catch (Exception exception) {
                                                    exception.printStackTrace();
                                                }
                                            }


                                        }


                                    } else {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    }
                                }

                                //=====================================================


                            }
                            //=========================MP Thrugh customer ================================
                            else if(GlobalData.getStrPageRecord().equalsIgnoreCase("MP_Through_C_C2M"))
                            {

                                //Encrypt QR Code
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {

                                }
                                String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                if (mStrBankBin.equalsIgnoreCase("006")) {//For Huawei
                                    mStrUrlForQrCode = GlobalData.getStrUrl();
                                    mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                } else if (mStrBankBin.equalsIgnoreCase("002")) {//For QPay
                                    mStrUrlForQrCode = GlobalData.getStrUrl();
                                    mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                }

                                String strEncryptDestinationAccountNumberAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                if (!strEncryptDestinationAccountNumberAndMasterKey.equalsIgnoreCase("")) {
                                    int intIndex02 = strEncryptDestinationAccountNumberAndMasterKey.indexOf("*");
                                    if (intIndex02 == -1) {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    } else {
                                        String[] parts = strEncryptDestinationAccountNumberAndMasterKey.split("\\*");
                                        strEncryptMerchantAccountNumber = parts[0];//96745482897185504726639965371045
                                        // strMerchantMasterKey = parts[1];//0021200000007
                                        String strMerchantEncryptName= parts[1];

                                        try {
                                            //################ Merchant Account Number ####################
                                            //################ Merchant Account Number ####################
                                            //################ Merchant Account Number ####################
                                            strMerchantAccountNumber = encryptionDecryption.Decrypt(strEncryptMerchantAccountNumber, GlobalData.getStrSessionId());
                                            String strAccountTypeCode = strMerchantAccountNumber.substring(3, 5);
                                            if (strAccountTypeCode.equalsIgnoreCase("11"))
                                            {
                                                //################ Merchant Name ####################
                                                //################ Merchant Name ####################
                                                //################ Merchant Name ####################
                                                // mStrDestinationAccountHolderName = GetMerchantName.getMerchantName(strEncryptMerchantAccountNumber,strMerchantMasterKey);
                                                mStrDestinationAccountHolderName =encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//                                String[] parts01 = mStrDestinationAccountHolderName.split("\\*");
//                                String strName = parts01[0];
//                                final String strType = parts01[1];

//                                String strDestinationRank = mStrSourceWallet.substring(13, 14);
//                                if (strDestinationRank.equalsIgnoreCase("1")) {
//                                    SourceRank = "VOUPAY";
//                                } else if (strDestinationRank.equalsIgnoreCase("2")) {
//                                    SourceRank = "CANPAY";
//                                } else if (strDestinationRank.equalsIgnoreCase("3")) {
//                                    SourceRank = "SIMBIL";
//                                }
                                                //################ Show Dialog ####################
                                                //################ Show Dialog ####################
                                                //################ Show Dialog ####################
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                myAlert.setTitle("MERCHANT INFO");
                                                myAlert.setMessage("MERCHANT NAME" + "\n" + mStrDestinationAccountHolderName + "\n" + "MERCHANT WALLET" + "\n" + strMerchantAccountNumber);
                                                myAlert.setPositiveButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                             //   mEditTextMerchantAccount.setText(strMerchantAccountNumber);
                                                                GlobalData.setStrMpAccountNoTextForMPCustomer(strMerchantAccountNumber);
                                                                startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M.class));
                                                                finish();
//                                                if (strType.equalsIgnoreCase(SourceRank)) {
//                                                    mEditTextMerchantAccount.setText(strMerchantAccountNumber);
//                                                } else {
//                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                                                    myAlert.setMessage("Account Type not match. Please scan correct account type.");
//                                                    myAlert.setNegativeButton(
//                                                            "OK",
//                                                            new DialogInterface.OnClickListener() {
//                                                                public void onClick(DialogInterface dialog, int id) {
//                                                                    dialog.cancel();
//                                                                }
//                                                            });
//                                                    AlertDialog alertDialog = myAlert.create();
//                                                    alertDialog.show();
//                                                }
                                                            }
                                                        });
                                                myAlert.setNegativeButton(
                                                        "CANCEL",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            } else {
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                myAlert.setMessage("Please Scan Merchant QR");
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            }


                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    }

                                } else {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, MP_Through_C_C2M.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                }

                            }
                            //====================================================
                            //=========================MP Thrugh Mp for customer ================================
                            else if(GlobalData.getStrPageRecord().equalsIgnoreCase("MP_Through_M_C2M"))
                            {
                                mStrSourceWallet=GlobalData.getStrMPAccountNofromDropdwnMP();
                                //Encrypt QR Code
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {
                                    String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                    mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                    String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                    if (mStrBankBin.equalsIgnoreCase("006")) {//For Huawei
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    } else if (mStrBankBin.equalsIgnoreCase("002")) {//For QPay
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    }

                                    String strEncryptDestinationWalletAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                    if (!strEncryptDestinationWalletAndMasterKey.equalsIgnoreCase("")) {
                                        int intIndex02 = strEncryptDestinationWalletAndMasterKey.indexOf("*");
                                        if (intIndex02 == -1) {
                                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                            mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                            mAlertDialogBuilder.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M.class));
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                            mAlertDialog.show();
                                        } else {
                                            String[] parts = strEncryptDestinationWalletAndMasterKey.split("\\*");
                                            mStrEncryptCustomerWallet = parts[0];//
                                            String strEncryptCustomerName = parts[1];//96745482897185504726639965371045

                                            try {
                                                mStrEncryptMerchatWalletForOtp = encryptionDecryption.Encrypt(mStrSourceWallet, GlobalData.getStrSessionId());
                                                mStrCustomerWalletByQrCode = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
                                                mStrEncryptCustomerWalletByQrCode = encryptionDecryption.Encrypt(mStrCustomerWalletByQrCode, GlobalData.getStrSessionId());
                                                strEncryptPIN=encryptionDecryption.Encrypt(GlobalData.getStrPin(),GlobalData.getStrSessionId());
                                                String strAccountTypeCode = mStrCustomerWalletByQrCode.substring(3, 5);
                                                if (strAccountTypeCode.equalsIgnoreCase("12")) {
                                                    //String strDestinationRank = mStrCustomerWalletByQrCode.substring(13, 14);
                                                    //---------------------------------

                                                    //################ Customer Wallet ####################
                                                    mStrCustomerWallet = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
                                                    //################ Customer Name ####################
                                                    //################ Customer Name ####################
                                                    //################ Customer Name ####################
                                                    String strCustomerName = encryptionDecryption.Decrypt(strEncryptCustomerName, GlobalData.getStrSessionId());
                                                    //################ Show Dialog ####################
                                                    //################ Show Dialog ####################
                                                    //################ Show Dialog ####################
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                    myAlert.setTitle("CUSTOMER INFO");
                                                    myAlert.setMessage("CUSTOMER NAME" + "\n" + strCustomerName + "\n" + "CUSTOMER WALLET" + "\n" + mStrCustomerWallet);
                                                    myAlert.setPositiveButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                   // mEditTextCustomerWallet.setText(mStrCustomerWallet);
                                                                    GlobalData.setStrCustomerWalletForMPTestView(mStrCustomerWallet);

                                                                    dialog.cancel();
                                                                    sendCustomerOtp(
                                                                            mStrEncryptMerchatWalletForOtp,
                                                                            strEncryptPIN,
                                                                            mStrEncryptCustomerWalletByQrCode);
                                                                    startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M.class));
                                                                    finish();
                                                                }
                                                            });
                                                    myAlert.setNegativeButton(
                                                            "CANCEL",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();




                                                }
                                                else
                                                {
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                    myAlert.setMessage("Please Scan Customer QR");
                                                    myAlert.setNegativeButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();
                                                }
                                               // mEditTextCustomerWallet.setText(mStrCustomerWallet);
                                                GlobalData.setStrCustomerWalletForMPTestView(mStrCustomerWallet);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    } else {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, MP_Through_M_C2M.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    }

                                }
                            }
                            //==========================================================================
                            //=========================CCT ================================
                            else if(GlobalData.getStrPageRecord().equalsIgnoreCase("Customer_Cash_Withdrowal"))
                            {

                                //Encrypt QR Code
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_Withdrowal.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {

                                }
                                String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                if (mStrBankBin.equalsIgnoreCase("006")) {//For Huawei
                                    mStrUrlForQrCode = GlobalData.getStrUrl();
                                    mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                } else if (mStrBankBin.equalsIgnoreCase("002")) {//For QPay
                                    mStrUrlForQrCode = GlobalData.getStrUrl();
                                    mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                }

                                String strEncryptDestinationAccountNumberAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                if (!strEncryptDestinationAccountNumberAndMasterKey.equalsIgnoreCase("")) {
                                    int intIndex02 = strEncryptDestinationAccountNumberAndMasterKey.indexOf("*");
                                    if (intIndex02 == -1) {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_Withdrowal.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    } else {
                                        String[] parts = strEncryptDestinationAccountNumberAndMasterKey.split("\\*");
                                        strEncryptMerchantAccountNumber = parts[0];//96745482897185504726639965371045
                                        // strMerchantMasterKey = parts[1];//0021200000007
                                        String strMerchantEncryptName= parts[1];

                                        try {
                                            //################ Merchant Account Number ####################
                                            //################ Merchant Account Number ####################
                                            //################ Merchant Account Number ####################
                                            strMerchantAccountNumber = encryptionDecryption.Decrypt(strEncryptMerchantAccountNumber, GlobalData.getStrSessionId());
                                            String strAccountTypeCode = strMerchantAccountNumber.substring(3, 5);
                                            if (strAccountTypeCode.equalsIgnoreCase("11"))
                                            {
                                                //################ Merchant Name ####################
                                                //################ Merchant Name ####################
                                                //################ Merchant Name ####################
                                                // mStrDestinationAccountHolderName = GetMerchantName.getMerchantName(strEncryptMerchantAccountNumber,strMerchantMasterKey);
                                                mStrDestinationAccountHolderName =encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//                                String[] parts01 = mStrDestinationAccountHolderName.split("\\*");
//                                String strName = parts01[0];
//                                final String strType = parts01[1];

//                                String strDestinationRank = mStrSourceWallet.substring(13, 14);
//                                if (strDestinationRank.equalsIgnoreCase("1")) {
//                                    SourceRank = "VOUPAY";
//                                } else if (strDestinationRank.equalsIgnoreCase("2")) {
//                                    SourceRank = "CANPAY";
//                                } else if (strDestinationRank.equalsIgnoreCase("3")) {
//                                    SourceRank = "SIMBIL";
//                                }
                                                //################ Show Dialog ####################
                                                //################ Show Dialog ####################
                                                //################ Show Dialog ####################
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                myAlert.setTitle("AGENT INFO");
                                                myAlert.setMessage("AGENT NAME" + "\n" + mStrDestinationAccountHolderName + "\n" + "AGENT WALLET" + "\n" + strMerchantAccountNumber);
                                                myAlert.setPositiveButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                //   mEditTextMerchantAccount.setText(strMerchantAccountNumber);
                                                                GlobalData.setStrCCTFORTextAgentNO(strMerchantAccountNumber);
                                                                startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_Withdrowal.class));
                                                                finish();
//                                                if (strType.equalsIgnoreCase(SourceRank)) {
//                                                    mEditTextMerchantAccount.setText(strMerchantAccountNumber);
//                                                } else {
//                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                                                    myAlert.setMessage("Account Type not match. Please scan correct account type.");
//                                                    myAlert.setNegativeButton(
//                                                            "OK",
//                                                            new DialogInterface.OnClickListener() {
//                                                                public void onClick(DialogInterface dialog, int id) {
//                                                                    dialog.cancel();
//                                                                }
//                                                            });
//                                                    AlertDialog alertDialog = myAlert.create();
//                                                    alertDialog.show();
//                                                }
                                                            }
                                                        });
                                                myAlert.setNegativeButton(
                                                        "CANCEL",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_Withdrowal.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            } else {
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                myAlert.setMessage("Please Scan Merchant QR");
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_Withdrowal.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            }


                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    }

                                } else {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_Withdrowal.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                }

                            }
                            //====================================================
                            else if(GlobalData.getStrPageRecord().equalsIgnoreCase("Customer_Cash_withdrowal_Through_Agent"))
                            {
                                mStrSourceWallet=GlobalData.getStrMPAccountNofromDropdwnMP();
                                //Encrypt QR Code
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {
                                    String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                    mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                    String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                    if (mStrBankBin.equalsIgnoreCase("006")) {//For Huawei
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    } else if (mStrBankBin.equalsIgnoreCase("002")) {//For QPay
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    }

                                    String strEncryptDestinationWalletAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                    if (!strEncryptDestinationWalletAndMasterKey.equalsIgnoreCase("")) {
                                        int intIndex02 = strEncryptDestinationWalletAndMasterKey.indexOf("*");
                                        if (intIndex02 == -1) {
                                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                            mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                            mAlertDialogBuilder.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                            mAlertDialog.show();
                                        } else {
                                            String[] parts = strEncryptDestinationWalletAndMasterKey.split("\\*");
                                            mStrEncryptCustomerWallet = parts[0];//
                                            String strEncryptCustomerName = parts[1];//96745482897185504726639965371045

                                            try {
                                                mStrEncryptMerchatWalletForOtp = encryptionDecryption.Encrypt(mStrSourceWallet, GlobalData.getStrSessionId());
                                                mStrCustomerWalletByQrCode = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
                                                mStrEncryptCustomerWalletByQrCode = encryptionDecryption.Encrypt(mStrCustomerWalletByQrCode, GlobalData.getStrSessionId());
                                                strEncryptPIN=encryptionDecryption.Encrypt(GlobalData.getStrPin(),GlobalData.getStrSessionId());
                                                String strAccountTypeCode = mStrCustomerWalletByQrCode.substring(3, 5);
                                                if (strAccountTypeCode.equalsIgnoreCase("12")) {
                                                    //String strDestinationRank = mStrCustomerWalletByQrCode.substring(13, 14);
                                                    //---------------------------------

                                                    //################ Customer Wallet ####################
                                                    mStrCustomerWallet = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
                                                    //################ Customer Name ####################
                                                    //################ Customer Name ####################
                                                    //################ Customer Name ####################
                                                    String strCustomerName = encryptionDecryption.Decrypt(strEncryptCustomerName, GlobalData.getStrSessionId());
                                                    //################ Show Dialog ####################
                                                    //################ Show Dialog ####################
                                                    //################ Show Dialog ####################
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                    myAlert.setTitle("CUSTOMER INFO");
                                                    myAlert.setMessage("CUSTOMER NAME" + "\n" + strCustomerName + "\n" + "CUSTOMER WALLET" + "\n" + mStrCustomerWallet);
                                                    myAlert.setPositiveButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    // mEditTextCustomerWallet.setText(mStrCustomerWallet);
                                                                    GlobalData.setStrCustomerWalletForCCTTrrouhgAgent(mStrCustomerWallet);

                                                                    dialog.cancel();
                                                                    sendCustomerOtp(
                                                                            mStrEncryptMerchatWalletForOtp,
                                                                            strEncryptPIN,
                                                                            mStrEncryptCustomerWalletByQrCode);
                                                                    startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                                    finish();
                                                                }
                                                            });
                                                    myAlert.setNegativeButton(
                                                            "CANCEL",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();




                                                }
                                                else
                                                {
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                    myAlert.setMessage("Please Scan Customer QR");
                                                    myAlert.setNegativeButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();
                                                }
                                                // mEditTextCustomerWallet.setText(mStrCustomerWallet);
                                                GlobalData.setStrCustomerWalletForCCTTrrouhgAgent(mStrCustomerWallet);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    } else {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    }

                                }
                            }
                            //====================================================
                            else if(GlobalData.getStrPageRecord().equalsIgnoreCase("Fund_Managemnet_Agent"))
                            {

                                //Encrypt QR Code
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, Fund_Management.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {

                                }
                                String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                if (mStrBankBin.equalsIgnoreCase("006")) {//For Huawei
                                    mStrUrlForQrCode = GlobalData.getStrUrl();
                                    mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                } else if (mStrBankBin.equalsIgnoreCase("002")) {//For QPay
                                    mStrUrlForQrCode = GlobalData.getStrUrl();
                                    mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                }

                                String strEncryptDestinationAccountNumberAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                if (!strEncryptDestinationAccountNumberAndMasterKey.equalsIgnoreCase("")) {
                                    int intIndex02 = strEncryptDestinationAccountNumberAndMasterKey.indexOf("*");
                                    if (intIndex02 == -1) {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, Fund_Management.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    } else {
                                        String[] parts = strEncryptDestinationAccountNumberAndMasterKey.split("\\*");
                                        strEncryptMerchantAccountNumber = parts[0];//96745482897185504726639965371045
                                        // strMerchantMasterKey = parts[1];//0021200000007
                                        String strMerchantEncryptName= parts[1];

                                        try {
                                            //################ Merchant Account Number ####################
                                            //################ Merchant Account Number ####################
                                            //################ Merchant Account Number ####################
                                            strMerchantAccountNumber = encryptionDecryption.Decrypt(strEncryptMerchantAccountNumber, GlobalData.getStrSessionId());
                                            String strAccountTypeCode = strMerchantAccountNumber.substring(3, 5);
                                            if (strAccountTypeCode.equalsIgnoreCase("11"))
                                            {
                                                //################ Merchant Name ####################
                                                //################ Merchant Name ####################
                                                //################ Merchant Name ####################
                                                // mStrDestinationAccountHolderName = GetMerchantName.getMerchantName(strEncryptMerchantAccountNumber,strMerchantMasterKey);
                                                mStrDestinationAccountHolderName =encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//                                String[] parts01 = mStrDestinationAccountHolderName.split("\\*");
//                                String strName = parts01[0];
//                                final String strType = parts01[1];

//                                String strDestinationRank = mStrSourceWallet.substring(13, 14);
//                                if (strDestinationRank.equalsIgnoreCase("1")) {
//                                    SourceRank = "VOUPAY";
//                                } else if (strDestinationRank.equalsIgnoreCase("2")) {
//                                    SourceRank = "CANPAY";
//                                } else if (strDestinationRank.equalsIgnoreCase("3")) {
//                                    SourceRank = "SIMBIL";
//                                }
                                                //################ Show Dialog ####################
                                                //################ Show Dialog ####################
                                                //################ Show Dialog ####################
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                myAlert.setTitle("AGENT INFO");
                                                myAlert.setMessage("AGENT NAME" + "\n" + mStrDestinationAccountHolderName + "\n" + "AGENT WALLET" + "\n" + strMerchantAccountNumber);
                                                myAlert.setPositiveButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                //   mEditTextMerchantAccount.setText(strMerchantAccountNumber);
                                                                GlobalData.setStrFMAgentNO(strMerchantAccountNumber);
                                                                startActivity(new Intent(QrCodeScanActivity.this, Fund_Management.class));
                                                                finish();
//                                                if (strType.equalsIgnoreCase(SourceRank)) {
//                                                    mEditTextMerchantAccount.setText(strMerchantAccountNumber);
//                                                } else {
//                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                                                    myAlert.setMessage("Account Type not match. Please scan correct account type.");
//                                                    myAlert.setNegativeButton(
//                                                            "OK",
//                                                            new DialogInterface.OnClickListener() {
//                                                                public void onClick(DialogInterface dialog, int id) {
//                                                                    dialog.cancel();
//                                                                }
//                                                            });
//                                                    AlertDialog alertDialog = myAlert.create();
//                                                    alertDialog.show();
//                                                }
                                                            }
                                                        });
                                                myAlert.setNegativeButton(
                                                        "CANCEL",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(QrCodeScanActivity.this, Fund_Management.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            } else {
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                myAlert.setMessage("Please Scan Merchant QR");
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(QrCodeScanActivity.this, Fund_Management.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            }


                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    }

                                } else {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, Fund_Management.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                }

                            }
                            //====================================================
                            else if(GlobalData.getStrPageRecord().equalsIgnoreCase("Cash_In_Agent"))
                            {
                                mStrSourceWallet=GlobalData.getStrMPAccountNofromDropdwnMP();
                                //Encrypt QR Code
                                String mStrQrCodeContents = strQR;
                                int intIndex01 = mStrQrCodeContents.indexOf(":");
                                if (intIndex01 == -1) {
                                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                    mAlertDialogBuilder.setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    startActivity(new Intent(QrCodeScanActivity.this, Cash_IN.class));
                                                    finish();
                                                }
                                            });
                                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                    mAlertDialog.show();
                                } else {
                                    String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
                                    mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
                                    String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==

                                    if (mStrBankBin.equalsIgnoreCase("006")) {//For Huawei
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    } else if (mStrBankBin.equalsIgnoreCase("002")) {//For QPay
                                        mStrUrlForQrCode = GlobalData.getStrUrl();
                                        mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
                                    }

                                    String strEncryptDestinationWalletAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
                                    if (!strEncryptDestinationWalletAndMasterKey.equalsIgnoreCase("")) {
                                        int intIndex02 = strEncryptDestinationWalletAndMasterKey.indexOf("*");
                                        if (intIndex02 == -1) {
                                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                            mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                            mAlertDialogBuilder.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            startActivity(new Intent(QrCodeScanActivity.this, Cash_IN.class));
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                            mAlertDialog.show();
                                        } else {
                                            String[] parts = strEncryptDestinationWalletAndMasterKey.split("\\*");
                                            mStrEncryptCustomerWallet = parts[0];//
                                            String strEncryptCustomerName = parts[1];//96745482897185504726639965371045

                                            try {
                                                mStrEncryptMerchatWalletForOtp = encryptionDecryption.Encrypt(mStrSourceWallet, GlobalData.getStrSessionId());
                                                mStrCustomerWalletByQrCode = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
                                                mStrEncryptCustomerWalletByQrCode = encryptionDecryption.Encrypt(mStrCustomerWalletByQrCode, GlobalData.getStrSessionId());
                                                strEncryptPIN=encryptionDecryption.Encrypt(GlobalData.getStrPin(),GlobalData.getStrSessionId());
                                                String strAccountTypeCode = mStrCustomerWalletByQrCode.substring(3, 5);
                                                if (strAccountTypeCode.equalsIgnoreCase("12")) {
                                                    //String strDestinationRank = mStrCustomerWalletByQrCode.substring(13, 14);
                                                    //---------------------------------

                                                    //################ Customer Wallet ####################
                                                    mStrCustomerWallet = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
                                                    //################ Customer Name ####################
                                                    //################ Customer Name ####################
                                                    //################ Customer Name ####################
                                                    String strCustomerName = encryptionDecryption.Decrypt(strEncryptCustomerName, GlobalData.getStrSessionId());
                                                    //################ Show Dialog ####################
                                                    //################ Show Dialog ####################
                                                    //################ Show Dialog ####################
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                    myAlert.setTitle("CUSTOMER INFO");
                                                    myAlert.setMessage("CUSTOMER NAME" + "\n" + strCustomerName + "\n" + "CUSTOMER WALLET" + "\n" + mStrCustomerWallet);
                                                    myAlert.setPositiveButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    // mEditTextCustomerWallet.setText(mStrCustomerWallet);
                                                                    GlobalData.setStrCashInCustomerNO(mStrCustomerWallet);

                                                                    dialog.cancel();
//                                                                    sendCustomerOtp(
//                                                                            mStrEncryptMerchatWalletForOtp,
//                                                                            strEncryptPIN,
//                                                                            mStrEncryptCustomerWalletByQrCode);
                                                                    startActivity(new Intent(QrCodeScanActivity.this, Cash_IN.class));
                                                                    finish();
                                                                }
                                                            });
                                                    myAlert.setNegativeButton(
                                                            "CANCEL",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(QrCodeScanActivity.this, Cash_IN.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();




                                                }
                                                else
                                                {
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(QrCodeScanActivity.this);
                                                    myAlert.setMessage("Please Scan Customer QR");
                                                    myAlert.setNegativeButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(QrCodeScanActivity.this, Cash_IN.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();
                                                }
                                                // mEditTextCustomerWallet.setText(mStrCustomerWallet);
                                                GlobalData.setStrCashInCustomerNO(mStrCustomerWallet);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    } else {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(QrCodeScanActivity.this);
                                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
                                        mAlertDialogBuilder.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(QrCodeScanActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();
                                    }

                                }
                            }
                            //====================================================

                        }
                    });
                }
//                else
//                {
//                    startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
//                    finish();
//                }
            }
        });
    }

    //########################## Customer OTP  ############################
    public void sendCustomerOtp(String strEncryptMerchantWallet,
                                String strEncryptMerchantPin,
                                String strEncryptCustomerWallet) {
        String METHOD_NAME = "QPAY_GenerateOTP_Res";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GenerateOTP_Res";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//        QPAY_GenerateOTP_Res
//        MerchantNo:
//        MerchantPIN:
//        CustomerNo:
//        strMasterKey:
        PropertyInfo encryptMerchantWallet = new PropertyInfo();
        encryptMerchantWallet.setName("E_MerchantNo");
        encryptMerchantWallet.setValue(strEncryptMerchantWallet);
        encryptMerchantWallet.setType(String.class);
        request.addProperty(encryptMerchantWallet);

        PropertyInfo encryptMerchantPin = new PropertyInfo();
        encryptMerchantPin.setName("E_MerchantPIN");
        encryptMerchantPin.setValue(strEncryptMerchantPin);
        encryptMerchantPin.setType(String.class);
        request.addProperty(encryptMerchantPin);

        PropertyInfo encryptCustomerWallet = new PropertyInfo();
        encryptCustomerWallet.setName("E_CustomerNo");
        encryptCustomerWallet.setValue(strEncryptCustomerWallet);
        encryptCustomerWallet.setType(String.class);
        request.addProperty(encryptCustomerWallet);

        PropertyInfo encryptMasterKey = new PropertyInfo();
        encryptMasterKey.setName("UserID");
        encryptMasterKey.setValue(GlobalData.getStrUserId());
        encryptMasterKey.setType(String.class);
        request.addProperty(encryptMasterKey);


        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(GlobalData.getStrDeviceId());
        masterKey.setType(String.class);
        request.addProperty(masterKey);


        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objSendCustomerOtp = null;
        String strSendCustomerWalletResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 1000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objSendCustomerOtp = envelope.getResponse();
            strSendCustomerWalletResponse = objSendCustomerOtp.toString();
//            mStrServerResponse = strSendCustomerWalletResponse;
        } catch (Exception exception) {
//            mStrServerResponse = strSendCustomerWalletResponse;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(QrCodeScanActivity.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
