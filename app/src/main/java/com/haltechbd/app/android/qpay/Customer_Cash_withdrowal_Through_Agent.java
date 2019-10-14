package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.StringTokenizer;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class Customer_Cash_withdrowal_Through_Agent extends AppCompatActivity implements OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    ArrayList<String> arrayListWalletType = new ArrayList<String>();
    ArrayList<String> arrayListWallet = new ArrayList<String>();

    private Spinner mSpinnerMerchantWallet;
    private EditText mEditTextAmount, mEditTextCustomerOtp,
            mEditTextCustomerReference, mEditTextCustomerWallet;
    private ImageButton mImgBtnScanQr,mImgBtnPrint;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrEncryptMerchantWallet,
            mStrCustomerMasterKeyByQrCode, mStrCustomerWallet, mStrCustomerWalletByQrCode,
            strUrlForQrCode, mStrEncryptCustomerWallet,
            strMethodName, strBankBin, mStrServerResponse,
            mStrEncryptMerchantPin, mStrEncryptAmount,
            mStrEncryptCustomerOtp, mStrEncryptCustomerReference,m_TextPIN,
            mStrEncryptCustomerWalletByQrCode,
            mStrEncryptMerchatWalletForOtp, mStrSourceWallet, mStrMerchantRank,strEncryptOTP,strEncryptPIN;


    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_cash_withdral_through_agent);
        checkOs();
        initUI();

    }

    private void initUI() {
        mSpinnerMerchantWallet = findViewById(R.id.spinnerCCTByAgentWallet);
        mEditTextAmount = findViewById(R.id.editTextCCTByMerchantAmount);

        mEditTextCustomerOtp = findViewById(R.id.editTextCCTByMerchantCustomerOtp);
        mEditTextCustomerOtp.requestFocus();

        mImgBtnScanQr = findViewById(R.id.imgBtnCCTByMerchantScanQrCustomerWallet);
        mImgBtnScanQr.setOnClickListener(this);
        mEditTextCustomerWallet = findViewById(R.id.editTextCCTByMerchantCustomerWallet);
        mBtnSubmit = findViewById(R.id.btnCCTByMerchantSubmit);
        mBtnSubmit.setOnClickListener(this);
//        mImgBtnPrint=findViewById(R.id.imgBtnCCTByMerchantPrint);
//        mImgBtnPrint.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewCCTByMerchantServerResponse);
        mStrMerchantRank = GlobalData.getStrAccountRank();
        mEditTextCustomerWallet.setText(GlobalData.getStrCustomerWalletForCCTTrrouhgAgent());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mImgBtnScanQr) {
//            mEditTextCustomerWallet.setFocusable(false);
//            mEditTextCustomerWallet.setEnabled(false);
            try {
//                Intent intent = new Intent(ACTION_SCAN);
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                startActivityForResult(intent, 0);
                startActivity(new Intent(Customer_Cash_withdrowal_Through_Agent.this, QrCodeScanActivity.class));
                finish();
                GlobalData.setStrPageRecord("Customer_Cash_withdrowal_Through_Agent");

            } catch (ActivityNotFoundException anfe) {
//                showDialog(MP_Through_M_C2M.this, "No Scanner Found", "Download a QR Scanner App?", "Yes",
//                        "No").show();
                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
                myAlert.setMessage("No Scanner Found");
                myAlert.setPositiveButton(
                        "Scan Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();
            }
        }
        if (v == mBtnSubmit) {
            if (mEditTextAmount.getText().toString().length() == 0) {
                mEditTextAmount.setError("Field cannot be empty");
            } else if (mEditTextCustomerOtp.getText().toString().length() == 0) {
                mEditTextCustomerOtp.setError("Field cannot be empty");
            } else if (mEditTextCustomerOtp.getText().toString().length() < 5) {
                mEditTextCustomerOtp.setError("Must be 5 characters in length");
            } else if (mEditTextCustomerWallet.getText().toString().length() == 0) {
                mEditTextCustomerWallet.setError("Field cannot be empty");
            } else {

                //===============================================================

                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
                myAlert.setMessage("Enter Your Transaction PIN");
                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                myAlert.setView(input);
                myAlert.setNeutralButton(
                        "Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                m_TextPIN = input.getText().toString();
                                //--------------------------------------------------------
                                try {
                                    mStrEncryptMerchantWallet = encryptionDecryption.Encrypt(mStrSourceWallet, GlobalData.getStrSessionId());
                                    mStrEncryptMerchantPin = encryptionDecryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                                    mStrEncryptAmount = encryptionDecryption.Encrypt(mEditTextAmount.getText().toString(), GlobalData.getStrSessionId());
                                    mStrEncryptCustomerOtp = encryptionDecryption.Encrypt(mEditTextCustomerOtp.getText().toString(), GlobalData.getStrSessionId());
                                   // mStrEncryptCustomerReference = encryptionDecryption.Encrypt(mEditTextCustomerReference.getText().toString(), GlobalData.getStrSessionId());
                                    mStrEncryptCustomerWallet = encryptionDecryption.Encrypt(mEditTextCustomerWallet.getText().toString(), GlobalData.getStrSessionId());
                                }
                                catch (Exception e)
                                {}

                                String strTransactionPIN = CheckTransactionPIN(m_TextPIN,mStrEncryptCustomerWallet);

                                if(strTransactionPIN.equalsIgnoreCase("Match")) {
                                    try {


                                        // Initialize progress dialog
                                        mProgressDialog = ProgressDialog.show(Customer_Cash_withdrowal_Through_Agent.this, null, "Processing request...", false, true);
                                        // Cancel progress dialog on back key press
                                        mProgressDialog.setCancelable(true);

                                        Thread t = new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                doMakeCCTByAgent(
                                                        mStrEncryptMerchantWallet,
                                                        mStrEncryptMerchantPin,
                                                        mStrEncryptAmount,
                                                        mStrEncryptCustomerOtp,
                                                        mStrEncryptCustomerWallet);
                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        try {
                                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                                mProgressDialog.dismiss();
                                                                //####################### Show Dialog ####################
                                                                //####################### Show Dialog ####################
                                                                //####################### Show Dialog ####################
                                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                                {
                                                                    mStrServerResponse="Request is in process";
                                                                }
                                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
                                                                myAlert.setMessage(mStrServerResponse);
                                                                myAlert.setNeutralButton(
                                                                        "Continue",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                clearEditText();
                                                                            }
                                                                        });
                                                                myAlert.setNegativeButton(
                                                                        "Close",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                startActivity(new Intent(Customer_Cash_withdrowal_Through_Agent.this, MainActivity.class));
                                                                                finish();
                                                                                clearDataFromGlobal();
                                                                            }
                                                                        });
                                                                AlertDialog alertDialog = myAlert.create();
                                                                alertDialog.show();

                                                            }

                                                        } catch (Exception e) {
                                                            // TODO: handle exception
                                                        }
                                                        // update ui info ( show response message )
                                                    }
                                                });
                                            }
                                        });
                                        t.start();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    //-----------------------------------------------------
                                }
                                else
                                {
                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
                                    myAlert.setMessage("Transaction PIN not Match!");
                                    myAlert.setNeutralButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                  //  clearEditText();
                                                }
                                            });
                                    AlertDialog alertDialog = myAlert.create();
                                    alertDialog.show();
                                }

                            }
                        });
                myAlert.setNegativeButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                startActivity(new Intent(Customer_Cash_withdrowal_Through_Agent.this, MainActivity.class));
                                finish();
                                clearDataFromGlobal();
                            }
                        });
                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();


            }
        }
//        if(v==mImgBtnPrint)
//        {
//            try
//            {
//                // --------------------------------
////                Intent i = new Intent(
////                        Customer_Cash_withdrowal_Through_Agent.this,
////                        PrinterSettings.class);
////                Bundle bundle = new Bundle();
////                bundle.putString("print",
////                        strPrint);
////                // Add the bundle to the
////                // intent
////                i.putExtras(bundle);
////                startActivity(i);
////                // --------------------------------
//
//            }
//            catch (Exception ec)
//            {
//
//            }
//
//        }
    }

    public void doMakeCCTByAgent(String strEncryptMerchantWallet,
                                        String strEncryptMerchantPin,
                                        String strEncryptAmount,
                                        String strEncryptCustomerOtp,
                                        String strEncryptCustomerWallet) {


        METHOD_NAME = "QPAY_Customer_Cash_Withdrawal_Through_Agent";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Customer_Cash_Withdrawal_Through_Agent";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//        QPAY_Merchant_Payment_Through_Agent
//        MerchantAccNo:
//        MerchantPIN:
//        Amount:
//        CustomerOTP:
//        CustomerRefID:
//        CustomerAccount:
//        strMasterKey:


        PropertyInfo encryptCustomerWallet = new PropertyInfo();
        encryptCustomerWallet.setName("E_CustomerAcc_no");
        encryptCustomerWallet.setValue(strEncryptCustomerWallet);
        encryptCustomerWallet.setType(String.class);
        request.addProperty(encryptCustomerWallet);

        PropertyInfo encryptCustomerOtp = new PropertyInfo();
        encryptCustomerOtp.setName("E_Customer_OTP");
        encryptCustomerOtp.setValue(strEncryptCustomerOtp);
        encryptCustomerOtp.setType(String.class);
        request.addProperty(encryptCustomerOtp);

        PropertyInfo encryptAmount = new PropertyInfo();
        encryptAmount.setName("E_Amount");
        encryptAmount.setValue(strEncryptAmount);
        encryptAmount.setType(String.class);
        request.addProperty(encryptAmount);

        PropertyInfo encryptMerchantWallet = new PropertyInfo();
        encryptMerchantWallet.setName("E_AgentAccountNO");
        encryptMerchantWallet.setValue(strEncryptMerchantWallet);
        encryptMerchantWallet.setType(String.class);
        request.addProperty(encryptMerchantWallet);

        PropertyInfo encryptMerchantPin = new PropertyInfo();
        encryptMerchantPin.setName("E_AgentPIN");
        encryptMerchantPin.setValue(strEncryptMerchantPin);
        encryptMerchantPin.setType(String.class);
        request.addProperty(encryptMerchantPin);



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
        Object objPaymentByMerchant = null;
        String strPaymentByMerchantResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 20000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objPaymentByMerchant = envelope.getResponse();
            strPaymentByMerchantResponse = objPaymentByMerchant.toString();
            mStrServerResponse = strPaymentByMerchantResponse;

//            Merchant Payment successful on 20-Mar-18 04:21:10Sent to: 00611000000561 Amount Tk.180.00
//            Balance: Tk.92,300.00
//            TXN ID: 18032000000098
//            QPAY//Merchant Payment Received From: 00612000000532 on 20-Mar-18 04:21:10
//            Amount: Tk.180.00
//            Balance: Tk.7,700.00
//            TXN ID: 18032000000098
//            QPAY//*AP180320162409


//            Merchant are not allow for this transactionQPAY,*AP180320160638

//            The customer OTP is wrong please insert correct OTP.,*AP180320161231

            int intIndex = mStrServerResponse.indexOf("successful");
            if (intIndex == -1) {
                int intIndex1 = mStrServerResponse.indexOf("not allow");
                if (intIndex1 == -1) {

                } else {
                    String[] parts = mStrServerResponse.split(",");
                    String strResponse = parts[0];
                    String strExtra = parts[1];
                    mStrServerResponse = strResponse;
                }
                int intIndex2 = mStrServerResponse.indexOf("wrong");
                if (intIndex2 == -1) {

                } else {
                    String[] parts = mStrServerResponse.split(",");
                    String strResponse = parts[0];
                    String strExtra = parts[1];
                    mStrServerResponse = strResponse;
                }
            } else {
                String[] parts = mStrServerResponse.split("//");
                String strResponse = parts[0];//
                String strExtra01 = parts[1];
                String strExtra02 = parts[2];
                mStrServerResponse = strResponse;
            }
        } catch (Exception exception) {
            mStrServerResponse = strPaymentByMerchantResponse;
        }
    }

    public String CheckTransactionPIN(String strPIN,String EncryptCustomerWallet)
    {
        String strSendCustomerWalletResponse = "";
        try {

        String strEncryptPIN=encryptionDecryption.Encrypt(strPIN, GlobalData.getStrSessionId());

        METHOD_NAME = "QPAY_Check_Transaction_PIN";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Check_Transaction_PIN";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//        QPAY_GenerateOTP_Res
//        MerchantNo:
//        MerchantPIN:
//        CustomerNo:
//        strMasterKey:
        PropertyInfo encryptMerchantWallet = new PropertyInfo();
        encryptMerchantWallet.setName("E_PIN");
        encryptMerchantWallet.setValue(strEncryptPIN);
        encryptMerchantWallet.setType(String.class);
        request.addProperty(encryptMerchantWallet);

        PropertyInfo encryptMerchantPin = new PropertyInfo();
        encryptMerchantPin.setName("E_Account_No");
        encryptMerchantPin.setValue(EncryptCustomerWallet);
        encryptMerchantPin.setType(String.class);
        request.addProperty(encryptMerchantPin);



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



            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 10000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objSendCustomerOtp = envelope.getResponse();
            strSendCustomerWalletResponse = objSendCustomerOtp.toString();
//            mStrServerResponse = strSendCustomerWalletResponse;
        } catch (Exception exception) {
//            mStrServerResponse = strSendCustomerWalletResponse;
        }
        return  strSendCustomerWalletResponse;
    }


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            //######################### Spinner Account Type#########################
            //######################### Spinner Account Type#########################
            //######################### Spinner Account Type#########################
            // Initialize progress dialog
            mProgressDialog = ProgressDialog.show(Customer_Cash_withdrowal_Through_Agent.this, null, "Loading Bank...", false, true);
            // Cancel progress dialog on back key press
            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    // Background code should be in here
                    loadSpinnerWallet();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                    ArrayAdapter<String> adapterWallet = new ArrayAdapter<String>(Customer_Cash_withdrowal_Through_Agent.this,
                                            android.R.layout.simple_spinner_item, arrayAccountName);
                                    adapterWallet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    mSpinnerMerchantWallet.setAdapter(adapterWallet);
                                    mSpinnerMerchantWallet.setOnItemSelectedListener(onItemSelectedListenerForWallet);
                                }

                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            // update ui info ( show response message )
                        }
                    });
                }
            });

            t.start();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
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

    //######################### Show Account Type #########################
    //######################### Show Account Type #########################
    //######################### Show Account Type #########################
    private void loadSpinnerWallet() {

        try {
          String  mStrEncryptAccountNumber = encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
            String strGetAccountList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
            // String strAllWalletTypeAndNumber = GetAllCustomerWallet.getAllWallets();
            if (strGetAccountList != null && !strGetAccountList.isEmpty()) {
                if(strGetAccountList.contains("*"))
                {
                    StringTokenizer strToken = new StringTokenizer(strGetAccountList, "*");
                    ArrayList<String> arrayListWalletTypeAndNumber = new ArrayList<String>();
                    for (int j = 0; j <= strToken.countTokens(); j++) {
                        while (strToken.hasMoreElements()) {
                            arrayListWalletTypeAndNumber.add(strToken.nextToken());
                        }
                    }
                    for (int i = 0; i <= arrayListWalletTypeAndNumber.size() - 1; i++) {
                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(arrayListWalletTypeAndNumber.get(i), ";");

                        String strName = tokensAllTransactionFinal.nextToken();
                        String strIsdefult = tokensAllTransactionFinal.nextToken();
                        String strAccountNo = tokensAllTransactionFinal.nextToken();
                        String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                        String strIsVerified=tokensAllTransactionFinal.nextToken();
                        String strBankID=tokensAllTransactionFinal.nextToken();
                        String strBalance=tokensAllTransactionFinal.nextToken();
                        String strQRcode=tokensAllTransactionFinal.nextToken();

                        if(strBankAccounStatus.equalsIgnoreCase("A")) {
                            if(strIsVerified.equalsIgnoreCase("Y")) {
                                if(strIsdefult.equalsIgnoreCase("Y"))
                                {
                                    arrayAccountName.add(strName+"(Default)");
                                }
                                else
                                {
                                    arrayAccountName.add(strName);

                                }
                                arrayAccountDefuly.add(strIsdefult);
                                arrayBankAccountNo.add(strAccountNo);
                                arrayBankAccountStatus.add(strBankAccounStatus);
                                arrayBankAccountIsVerified.add(strIsVerified);
                                arrayBankID.add(strBankID);
                            }
                        }
                        //arrayListWalletType.add(tokenWalletTypeAndAccount.nextToken());
                        //arrayListWallet.add(tokenWalletTypeAndAccount.nextToken());
                    }
                }
                else
                {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
                    mAlertDialogBuilder.setMessage(strGetAccountList);
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
            else {
                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_withdrowal_Through_Agent.this);
                mAlertDialogBuilder.setMessage("No Account Found.");
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
        catch (Exception ex)
        {

        }

//        ArrayList<String> arrayListWalletTypeAndNumber = new ArrayList<String>();
//        if (mStrMerchantRank.equalsIgnoreCase("VOUPAY")) {
//            arrayListWalletTypeAndNumber.add("VOUCHER PAY" + "-" + GlobalData.getStrAccountNumber() + "1");
//        } else if (mStrMerchantRank.equalsIgnoreCase("CANPAY")) {
//            arrayListWalletTypeAndNumber.add("CANTEEN PAY" + "-" + GlobalData.getStrAccountNumber() + "1");
//        } else if (mStrMerchantRank.equalsIgnoreCase("SIMBIL")) {
//            arrayListWalletTypeAndNumber.add("SIM BILL" + "-" + GlobalData.getStrAccountNumber() + "1");
//        }
//        for (int i = 0; i <= arrayListWalletTypeAndNumber.size() - 1; i++) {
//            StringTokenizer tokenWalletTypeAndAccount = new StringTokenizer(arrayListWalletTypeAndNumber.get(i), "-");
//            arrayListWalletType.add(tokenWalletTypeAndAccount.nextToken());
//            arrayListWallet.add(tokenWalletTypeAndAccount.nextToken());
//        }

    }

    //######################### Account Number #########################
    //######################### Account Number #########################
    //######################### Account Number #########################
    AdapterView.OnItemSelectedListener onItemSelectedListenerForWallet = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mStrSourceWallet = String.valueOf(arrayBankAccountNo.get(position));
            GlobalData.setStrMPAccountNofromDropdwnMP(mStrSourceWallet);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void enableUiComponents() {
        mSpinnerMerchantWallet.setEnabled(true);
        mEditTextAmount.setEnabled(true);
        mEditTextCustomerOtp.setEnabled(true);

        mImgBtnScanQr.setEnabled(true);
        mEditTextCustomerWallet.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mSpinnerMerchantWallet.setEnabled(false);
        mEditTextAmount.setEnabled(false);
        mEditTextCustomerOtp.setEnabled(false);

        mImgBtnScanQr.setEnabled(false);
        mEditTextCustomerWallet.setEnabled(false);
        mBtnSubmit.setEnabled(false);
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
                startActivity(new Intent(Customer_Cash_withdrowal_Through_Agent.this, MainActivity.class));
                finish();
                clearDataFromGlobal();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(MP_Through_M_C2M.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearDataFromGlobal() {
//        GlobalData.setStrDeviceId("");
//        GlobalData.setStrDeviceName("");
//        GlobalData.setStrUserId("");
//        GlobalData.setStrEncryptUserId("");
//        GlobalData.setStrPin("");
//        GlobalData.setStrEncryptPin("");
//        GlobalData.setStrMasterKey("");
//        GlobalData.setStrPackage("");
//        GlobalData.setStrEncryptPackage("");
//        GlobalData.setStrAccountNumber("");
//       // GlobalData.setStrEncryptAccountNumber("");
//        GlobalData.setStrWallet("");
//        GlobalData.setStrAccountHolderName("");
        GlobalData.setStrCustomerWalletForCCTTrrouhgAgent("");
        GlobalData.setStrPageRecord("");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message,
                                          CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    Log.v("Tracing  Value: ", "Error!!!");
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                //Encrypt QR Code
//                String mStrQrCodeContents = intent.getStringExtra("SCAN_RESULT");
//                int intIndex01 = mStrQrCodeContents.indexOf(":");
//                if (intIndex01 == -1) {
//                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_M_C2M.this);
//                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
//                    mAlertDialogBuilder.setNegativeButton(
//                            "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                    mAlertDialog.show();
//                } else {
//                    String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
//                    strBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
//                    String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==
//
//                    if (strBankBin.equalsIgnoreCase("006")) {//For Huawei
//                        strUrlForQrCode = GlobalData.getStrUrl();
//                        strMethodName = "QPAY_Get_Account_BY_QR_CARD";
//                    } else if (strBankBin.equalsIgnoreCase("002")) {//For QPay
//                        strUrlForQrCode = GlobalData.getStrUrl();
//                        strMethodName = "QPAY_Get_Account_BY_QR_CARD";
//                    }
//
//                    String strEncryptDestinationWalletAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
//                    if (!strEncryptDestinationWalletAndMasterKey.equalsIgnoreCase("")) {
//                        int intIndex02 = strEncryptDestinationWalletAndMasterKey.indexOf("*");
//                        if (intIndex02 == -1) {
//                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_M_C2M.this);
//                            mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
//                            mAlertDialogBuilder.setNegativeButton(
//                                    "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                            mAlertDialog.show();
//                        } else {
//                            String[] parts = strEncryptDestinationWalletAndMasterKey.split("\\*");
//                            mStrEncryptCustomerWallet = parts[0];//
//                            String strEncryptCustomerName = parts[1];//96745482897185504726639965371045
//
//                            try {
//                                mStrEncryptMerchatWalletForOtp = encryptionDecryption.Encrypt(mStrSourceWallet, GlobalData.getStrSessionId());
//                                mStrCustomerWalletByQrCode = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
//                                mStrEncryptCustomerWalletByQrCode = encryptionDecryption.Encrypt(mStrCustomerWalletByQrCode, GlobalData.getStrSessionId());
//                                 strEncryptPIN=encryptionDecryption.Encrypt(GlobalData.getStrPin(),GlobalData.getStrSessionId());
//                                String strAccountTypeCode = mStrCustomerWalletByQrCode.substring(3, 5);
//                                if (strAccountTypeCode.equalsIgnoreCase("12")) {
//                                    //String strDestinationRank = mStrCustomerWalletByQrCode.substring(13, 14);
//                                    //---------------------------------
//
//                                            //################ Customer Wallet ####################
//                                            mStrCustomerWallet = encryptionDecryption.Decrypt(mStrEncryptCustomerWallet, GlobalData.getStrSessionId());
//                                            //################ Customer Name ####################
//                                            //################ Customer Name ####################
//                                            //################ Customer Name ####################
//                                            String strCustomerName = encryptionDecryption.Decrypt(strEncryptCustomerName, GlobalData.getStrSessionId());
//                                            //################ Show Dialog ####################
//                                            //################ Show Dialog ####################
//                                            //################ Show Dialog ####################
//                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_M_C2M.this);
//                                            myAlert.setTitle("CUSTOMER INFO");
//                                            myAlert.setMessage("CUSTOMER NAME" + "\n" + strCustomerName + "\n" + "CUSTOMER WALLET" + "\n" + mStrCustomerWallet);
//                                            myAlert.setPositiveButton(
//                                                    "OK",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            mEditTextCustomerWallet.setText(mStrCustomerWallet);
////                                                    mEditTextAmount.setSelection(1);
//                                                            dialog.cancel();
//                                                            sendCustomerOtp(
//                                                                    mStrEncryptMerchatWalletForOtp,
//                                                                    strEncryptPIN,
//                                                                    mStrEncryptCustomerWalletByQrCode);
//                                                        }
//                                                    });
//                                            myAlert.setNegativeButton(
//                                                    "CANCEL",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            dialog.cancel();
//                                                        }
//                                                    });
//                                            AlertDialog alertDialog = myAlert.create();
//                                            alertDialog.show();
//
//
//
//
//                                }
//                                else
//                                    {
//                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_M_C2M.this);
//                                    myAlert.setMessage("Please Scan Customer QR");
//                                    myAlert.setNegativeButton(
//                                            "OK",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            });
//                                    AlertDialog alertDialog = myAlert.create();
//                                    alertDialog.show();
//                                }
//                                mEditTextCustomerWallet.setText(mStrCustomerWallet);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    } else {
//                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_M_C2M.this);
//                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
//                        mAlertDialogBuilder.setNegativeButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                        mAlertDialog.show();
//                    }
//
//                }
//            }
//        }
//    }

    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void clearEditText() {
        mEditTextAmount.setText("");
        mEditTextCustomerReference.setText("");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Customer_Cash_withdrowal_Through_Agent.this, MainActivity.class));
            finish();
            clearDataFromGlobal();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
