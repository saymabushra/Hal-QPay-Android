package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class MP_Through_M_C2M_Quick extends AppCompatActivity implements OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private EditText mEditTextSourceWallet, mEditTextAmount, mEditTextCustomerOtp,
            mEditTextCustomerReference, mEditTextCustomerWallet;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrEncryptMerchantWallet, mStrEncryptCustomerWallet, mStrServerResponse,
            mStrEncryptMerchantPin, mStrEncryptAmount, mStrEncryptCustomerOtp,
            mStrEncryptCustomerReference;
    String source,destination,m_TextPIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_by_merchant_quick);

//        //#################################
//        Bundle bundle = getIntent().getExtras();
//
//        // Extract the data
//        if (bundle != null) {
//            source = bundle.getString("source");
//            destination = bundle.getString("destination");
//
//        }
//
//        //##################################

        checkOs();
        initUI();
    }

    private void initUI() {
        mEditTextSourceWallet = findViewById(R.id.editTextPaymentByMerchantQuickSourceWallet);
        mEditTextSourceWallet.setText(GlobalData.getStrSourceWallet());
//        mEditTextSourceWallet.setText(source);
        mEditTextAmount = findViewById(R.id.editTextPaymentByMerchantQuickAmount);

        mEditTextCustomerOtp = findViewById(R.id.editTextPaymentByMerchantQuickCustomerOtp);
        mEditTextCustomerOtp.requestFocus();
        mEditTextCustomerReference = findViewById(R.id.editTextPaymentByMerchantQuickCustomerReference);
        mEditTextCustomerWallet = findViewById(R.id.editTextPaymentByMerchantQuickCustomerWallet);
        mEditTextCustomerWallet.setText(GlobalData.getStrDestinationWallet());
//        mEditTextCustomerWallet.setText(destination);
        mBtnSubmit = findViewById(R.id.btnPaymentByMerchantQuickSubmit);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewPaymentByMerchantQuickServerResponse);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }


    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mEditTextSourceWallet.getText().toString().length() == 0) {
                mEditTextSourceWallet.setError("Field cannot be empty");
            } else if (mEditTextAmount.getText().toString().length() == 0) {
                mEditTextAmount.setError("Field cannot be empty");
            } else if (mEditTextCustomerOtp.getText().toString().length() == 0) {
                mEditTextCustomerOtp.setError("Field cannot be empty");
            } else if (mEditTextCustomerOtp.getText().toString().length() < 5) {
                mEditTextCustomerOtp.setError("Must be 5 characters in length");
            } else if (mEditTextCustomerWallet.getText().toString().length() == 0) {
                mEditTextCustomerWallet.setError("Field cannot be empty");
            } else {
                try {
                    mStrEncryptMerchantWallet = encryptionDecryption.Encrypt(mEditTextSourceWallet.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptMerchantPin = encryptionDecryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    mStrEncryptAmount = encryptionDecryption.Encrypt(mEditTextAmount.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptCustomerOtp = encryptionDecryption.Encrypt(mEditTextCustomerOtp.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptCustomerReference = encryptionDecryption.Encrypt(mEditTextCustomerReference.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptCustomerWallet = encryptionDecryption.Encrypt(mEditTextCustomerWallet.getText().toString(), GlobalData.getStrSessionId());

                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_M_C2M_Quick.this);
                    myAlert.setMessage("Enter Your Transaction PIN");
                    // Set up the input
                    final EditText input = new EditText(this);

                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    myAlert.setView(input);
                    myAlert.setNeutralButton(
                            "Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    m_TextPIN = input.getText().toString();
                                    //--------------------------------------------------------
                                    String strTransactionPIN = CheckTransactionPIN(m_TextPIN,mStrEncryptCustomerWallet);

                                    if(strTransactionPIN.equalsIgnoreCase("Match")) {
                   //================================================================
                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(MP_Through_M_C2M_Quick.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            doMakePaymentByMerchant(
                                    mStrEncryptMerchantWallet,
                                    mStrEncryptMerchantPin,
                                    mStrEncryptAmount,
                                    mStrEncryptCustomerOtp,
                                    mStrEncryptCustomerReference,
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
                                                mStrServerResponse="Request is in Process";
                                            }
                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_M_C2M_Quick.this);
                                            myAlert.setMessage(mStrServerResponse);
                                            myAlert.setNegativeButton(
                                                    "Close",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                            // GlobalData.setStrDestinationWallet(null);
                                                            startActivity(new Intent(MP_Through_M_C2M_Quick.this, MainActivity.class));
                                                            finish();

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
                    //===========================================
                                    }
                                    else
                                    {
                                        if(strTransactionPIN.equalsIgnoreCase("Customer Transaction PIN is not set.Please set Trnasaction PIN first"))
                                        {
                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_M_C2M_Quick.this);
                                            myAlert.setMessage("Customer Transaction PIN is not set.Please set Trnasaction PIN first");
                                            myAlert.setNeutralButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                           // clearEditText();

                                                        }
                                                    });
                                            AlertDialog alertDialog = myAlert.create();
                                            alertDialog.show();
                                        }
                                        else {
                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_M_C2M_Quick.this);
                                            myAlert.setMessage("Transaction PIN not Match!");
                                            myAlert.setNeutralButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                            clearEditText();
                                                        }
                                                    });
                                            AlertDialog alertDialog = myAlert.create();
                                            alertDialog.show();
                                        }
                                    }

                                }
                            });
                    myAlert.setNegativeButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                    startActivity(new Intent(MP_Through_M_C2M_Quick.this, MainActivity.class));
                                    finish();
                                    clearDataFromGlobal();
                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void doMakePaymentByMerchant(String strEncryptMerchantWallet,
                                        String strEncryptMerchantPin,
                                        String strEncryptAmount,
                                        String strEncryptCustomerOtp,
                                        String strEncryptCustomerReference,
                                        String strEncryptCustomerWallet) {


        METHOD_NAME = "QPAY_Merchant_Payment_Through_Agent";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Merchant_Payment_Through_Agent";
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

        PropertyInfo encryptMerchantWallet = new PropertyInfo();
        encryptMerchantWallet.setName("E_MerchantAccNo");
        encryptMerchantWallet.setValue(strEncryptMerchantWallet);
        encryptMerchantWallet.setType(String.class);
        request.addProperty(encryptMerchantWallet);

        PropertyInfo encryptMerchantPin = new PropertyInfo();
        encryptMerchantPin.setName("E_MerchantPIN");
        encryptMerchantPin.setValue(strEncryptMerchantPin);
        encryptMerchantPin.setType(String.class);
        request.addProperty(encryptMerchantPin);

        PropertyInfo encryptAmount = new PropertyInfo();
        encryptAmount.setName("E_Amount");
        encryptAmount.setValue(strEncryptAmount);
        encryptAmount.setType(String.class);
        request.addProperty(encryptAmount);

        PropertyInfo encryptCustomerOtp = new PropertyInfo();
        encryptCustomerOtp.setName("E_CustomerOTP");
        encryptCustomerOtp.setValue(strEncryptCustomerOtp);
        encryptCustomerOtp.setType(String.class);
        request.addProperty(encryptCustomerOtp);

        PropertyInfo encryptCustomerReference = new PropertyInfo();
        encryptCustomerReference.setName("E_CustomerRefID");
        encryptCustomerReference.setValue(strEncryptCustomerReference);
        encryptCustomerReference.setType(String.class);
        request.addProperty(encryptCustomerReference);

        PropertyInfo encryptCustomerWallet = new PropertyInfo();
        encryptCustomerWallet.setName("E_CustomerAccount");
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

//    public void sendCustomerOtp(String strEncryptMerchantWallet,
//                                String strEncryptMerchantPin,
//                                String strEncryptCustomerWallet,
//                                String strMasterKey) {
//        METHOD_NAME = "QPAY_GenerateOTP_Res";
//        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GenerateOTP_Res";
//        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
//        // Declare the version of the SOAP request
//        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//
////        QPAY_GenerateOTP_Res
////        MerchantNo:
////        MerchantPIN:
////        CustomerNo:
////        strMasterKey:
//        PropertyInfo encryptMerchantWallet = new PropertyInfo();
//        encryptMerchantWallet.setName("MerchantNo");
//        encryptMerchantWallet.setValue(strEncryptMerchantWallet);
//        encryptMerchantWallet.setType(String.class);
//        request.addProperty(encryptMerchantWallet);
//
//        PropertyInfo encryptMerchantPin = new PropertyInfo();
//        encryptMerchantPin.setName("MerchantPIN");
//        encryptMerchantPin.setValue(strEncryptMerchantPin);
//        encryptMerchantPin.setType(String.class);
//        request.addProperty(encryptMerchantPin);
//
//        PropertyInfo encryptCustomerWallet = new PropertyInfo();
//        encryptCustomerWallet.setName("CustomerNo");
//        encryptCustomerWallet.setValue(strEncryptCustomerWallet);
//        encryptCustomerWallet.setType(String.class);
//        request.addProperty(encryptCustomerWallet);
//
//        PropertyInfo masterKey = new PropertyInfo();
//        masterKey.setName("strMasterKey");
//        masterKey.setValue(strMasterKey);
//        masterKey.setType(String.class);
//        request.addProperty(masterKey);
//
//        envelope.dotNet = true;
//
//        envelope.setOutputSoapObject(request);
//        Log.v("myApp:", request.toString());
//        envelope.implicitTypes = true;
//        Object objSendCustomerOtp = null;
//        String strSendCustomerWalletResponse = "";
//
//        try {
//            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 1000000);
//            androidHttpTransport.call(SOAP_ACTION, envelope);
//            objSendCustomerOtp = envelope.getResponse();
//            strSendCustomerWalletResponse = objSendCustomerOtp.toString();
//            mStrServerResponse = strSendCustomerWalletResponse;
//        } catch (Exception exception) {
//            mStrServerResponse = strSendCustomerWalletResponse;
//        }
//    }


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_M_C2M_Quick.this);
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


    private void enableUiComponents() {
        mEditTextSourceWallet.setEnabled(false);
        mEditTextAmount.setEnabled(true);
        mEditTextCustomerOtp.setEnabled(true);
        mEditTextCustomerReference.setEnabled(true);
        mEditTextCustomerWallet.setEnabled(false);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextSourceWallet.setEnabled(false);
        mEditTextAmount.setEnabled(false);
        mEditTextCustomerOtp.setEnabled(false);
        mEditTextCustomerReference.setEnabled(false);
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
                startActivity(new Intent(MP_Through_M_C2M_Quick.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(MP_Through_M_C2M_Quick.this, Login.class)
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
       // GlobalData.setStrEncryptAccountNumber("");
        GlobalData.setStrWallet("");
        GlobalData.setStrAccountHolderName("");
        GlobalData.setStrSessionId("");
        GlobalData.setStrQrCodeContent("");


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

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
            startActivity(new Intent(MP_Through_M_C2M_Quick.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

