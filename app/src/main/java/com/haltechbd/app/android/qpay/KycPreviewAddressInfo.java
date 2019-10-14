package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;


public class KycPreviewAddressInfo extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private TextView mTextViewDistrict, mTextViewThana, mTextViewPresentAddress, mTextViewPermanentAddress, mTextViewOfficeAddress, mTextViewShowServerResponse;
    private Button mBtnUpdateAddress;
    private String mStrAccountNumber, mStrPin, mStrMasterKey,
            mStrEncryptAccountNumber, mStrEncryptPin,
            mStrDistirctId, mStrEncryptDistrictId, mStrThanaId, mStrEncryptThanaId,
            mStrPresentAddress, mStrPermanentAddress, mStrOfficeAddress, mStrServerResponse,strParamete,
            strPresentAdd,strParmanetAdd,strOfficeAddres,strThanaAName,strThanaAID,strDistrictID,strDistrictName;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_preview_address_info);
        checkOs();
        // initialize all ui components
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mTextViewDistrict = findViewById(R.id.textViewDistrict);
        mTextViewThana = findViewById(R.id.textViewThana);
        mTextViewPresentAddress = findViewById(R.id.textViewPresentAddress);
        mTextViewPermanentAddress = findViewById(R.id.textViewPermanentAddress);
        mTextViewOfficeAddress = findViewById(R.id.textViewOfficeAddress);
        mBtnUpdateAddress = findViewById(R.id.btnKycAddressInfoUpdateAddress);
        mBtnUpdateAddress.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewUpdateKycAddressInfoShowServerResponse);

        mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnUpdateAddress) {

//            Intent intent = new Intent(KycPreviewAddressInfo.this, KycUpdateAddressInfo.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            startActivity(new Intent(KycPreviewAddressInfo.this, KycUpdateAddressInfo.class));
            finish();
           // startActivity(new Intent(KycPreviewAddressInfo.this, KycUpdateAddressInfo.class));
//            if (mEditTextPresentAddress.getText().toString().length() == 0) {
//                mEditTextPresentAddress.setError("Field cannot be empty");
//            } else if (mEditTextPermanentAddress.getText().toString().length() == 0) {
//                mEditTextPermanentAddress.setError("Field cannot be empty");
//            } else if (mEditTextOfficeAddress.getText().toString().length() == 0) {
//                mEditTextOfficeAddress.setError("Field cannot be empty");
//            } else {
////                disableUiComponentAfterClick();
//                try {
//                    mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, mStrMasterKey);
//                    mStrEncryptPin = encryption.Encrypt(mStrPin, mStrMasterKey);
//                    mStrPresentAddress = encryption.Encrypt(mEditTextPresentAddress.getText().toString(), mStrMasterKey);
//                    mStrPermanentAddress = encryption.Encrypt(mEditTextPermanentAddress.getText().toString(), mStrMasterKey);
//                    mStrOfficeAddress = encryption.Encrypt(mEditTextOfficeAddress.getText().toString(), mStrMasterKey);
//
//                    // Initialize progress dialog
//                    mProgressDialog = ProgressDialog.show(KycPreviewAddressInfo.this, null, "Processing request...", false, true);
//                    // Cancel progress dialog on back key press
//                    mProgressDialog.setCancelable(true);
//
//                    Thread t = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            mStrServerResponse = InsertKycAddressInfo.insertAddressInfo(
//                                    mStrEncryptAccountNumber,
//                                    mStrEncryptPin,
//                                    mStrEncryptThanaId,
//                                    mStrPresentAddress,
//                                    mStrPermanentAddress,
//                                    mStrOfficeAddress,
//                                    mStrMasterKey);
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    try {
//                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                            mProgressDialog.dismiss();
//                                            if (mStrServerResponse.equalsIgnoreCase("Update")) {
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewAddressInfo.this);
//                                                myAlert.setMessage("Successfully save address info.");
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            } else {
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewAddressInfo.this);
//                                                myAlert.setMessage(mStrServerResponse);
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
//
//                                        }
//                                    } catch (Exception e) {
//                                        // TODO: handle exception
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                    t.start();
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
        }

    }


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            try {
                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(),  GlobalData.getStrSessionId());
                    mStrPresentAddress = encryption.Encrypt(mTextViewPresentAddress.getText().toString(), GlobalData.getStrSessionId());
                    mStrPermanentAddress = encryption.Encrypt(mTextViewPermanentAddress.getText().toString(), GlobalData.getStrSessionId());
                    mStrOfficeAddress = encryption.Encrypt(mTextViewOfficeAddress.getText().toString(), GlobalData.getStrSessionId());
                   strParamete=encryption.Encrypt("G", GlobalData.getStrSessionId());
                mStrEncryptThanaId=encryption.Encrypt("", GlobalData.getStrSessionId());
            mProgressDialog = ProgressDialog.show(KycPreviewAddressInfo.this, null, "Loading...", false, true);

            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {

                    String strGet= InsertKyc.insertAddressInfo(
                                    mStrEncryptAccountNumber,
                                    mStrEncryptPin,
                                    mStrEncryptThanaId,
                                    mStrPresentAddress,
                                    mStrPermanentAddress,
                                    mStrOfficeAddress,
                                    strParamete);


                    //-----------------------------------------------
                    mStrServerResponse = strGet;
                    if (mStrServerResponse.equalsIgnoreCase("No Data Found")) {

                    } else {
                        // strDecryptDOB + "*" + strDecryptOccupation + "*" + strDecryptOrg_Name + "*" + strDecryptGender

                        String[] parts = mStrServerResponse.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                        strPresentAdd = parts[0];//Login Successfully
                        strParmanetAdd = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                        strOfficeAddres = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                        strThanaAName = parts[3];
                        strThanaAID = parts[4];
                        strDistrictName = parts[5];
                        strDistrictID = parts[6];


                        GlobalData.setStrKYCAddinfoPresent(strPresentAdd);
                        GlobalData.setStrKYCAddinfoParmanent(strParmanetAdd);
                        GlobalData.setStrKYCAddinfoOffice(strOfficeAddres);
                        GlobalData.setStrKYCAddInfoThanaNAme(strThanaAName);
                        GlobalData.setStrKYCAddInfoThanaId(strThanaAID);
                        GlobalData.setStrKYCAddInfoDistrictName(strDistrictName);
                        GlobalData.setStrKYCAddInfoDistrictID(strDistrictID);


                    }
                    //---------------------------------------


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                    //mTextViewShowServerResponse.setText(mStrServerResponse);
                                    mTextViewPresentAddress.setText(strPresentAdd);
                                    mTextViewPermanentAddress.setText(strParmanetAdd);
                                    mTextViewOfficeAddress.setText(strOfficeAddres);
                                    mTextViewDistrict.setText(strDistrictName);
                                    mTextViewThana.setText(strThanaAName);
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    });
                }
            });

            t.start();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycPreviewAddressInfo.this);
            mAlertDialogBuilder.setTitle("No Internet Connection");
            mAlertDialogBuilder.setMessage("It looks like your internet connection is off. Please turn it on and try again.");
            mAlertDialogBuilder.setNegativeButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
            mAlertDialog.show();
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void enableUiComponents() {
//        mSpinnerDistrict.setEnabled(true);
//        mSpinnerThana.setEnabled(true);
//        mEditTextPresentAddress.setEnabled(true);
//        mEditTextPermanentAddress.setEnabled(true);
//        mEditTextOfficeAddress.setEnabled(true);
//        mBtnSaveAddress.setEnabled(true);
    }

    private void disableUiComponents() {
//        mSpinnerDistrict.setEnabled(false);
//        mSpinnerThana.setEnabled(false);
//        mEditTextPresentAddress.setEnabled(false);
//        mEditTextPermanentAddress.setEnabled(false);
//        mEditTextOfficeAddress.setEnabled(false);
//        mBtnSaveAddress.setEnabled(false);
    }

    //########################## Logout ############################
    //########################## Logout ############################
    //########################## Logout ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:678");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(KycPreviewAddressInfo.this, Kyc.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycPreviewAddressInfo.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearDataFromGlobal() {
        GlobalData.setStrDeviceId("");
        GlobalData.setStrDeviceName("");
        GlobalData.setStrUserId("");
        GlobalData.setStrEncryptUserId("");
        GlobalData.setStrPin("");
        GlobalData.setStrEncryptPin("");
        GlobalData.setStrMasterKey("");
        GlobalData.setStrPackage("");
        GlobalData.setStrEncryptPackage("");
        GlobalData.setStrAccountNumber("");
        //  GlobalData.setStrEncryptAccountNumber("");
        GlobalData.setStrWallet("");
        GlobalData.setStrAccountHolderName("");
        GlobalData.setStrSessionId("");
        GlobalData.setStrQrCodeContent("");
    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(KycPreviewAddressInfo.this, Kyc.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
