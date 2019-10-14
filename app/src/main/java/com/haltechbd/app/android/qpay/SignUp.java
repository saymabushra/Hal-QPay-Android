package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Method;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private EditText mEditTextAccountHolderFullName, mEditTextMobileNumber, mEditTextEmail;
    private Spinner mSpinnerBankBin;
    private Button mBtnRegister;
    private TextView mTextViewShowServerResponse;
    private String mStrEncryptAccountHolderFullName, mStrEncryptMobileNumber, mStrEncryptEmail,
            mStrEncryptBankBin, mStrEncryptAccountType, mStrEncryptDeviceId, mStrEncryptDeviceName,
            mStrEncryptRegistrationSecurityKey, mStrBankBin, mStrAccountType, mStrDeviceName,
            mStrDeviceId, mStrMasterKey, mStrEncryptAccountNumber, mStrEncryptToken, mStrServerResponse;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextAccountHolderFullName = findViewById(R.id.editTextRegistrationAccountHolderFullName);
        mEditTextMobileNumber = findViewById(R.id.editTextRegistrationMobileNumber);
        mEditTextEmail = findViewById(R.id.editTextRegistrationEmail);
        mSpinnerBankBin = findViewById(R.id.spinnerRegistrationBankBin);
        mSpinnerBankBin.setOnItemSelectedListener(onItemSelectedListenerForBankBin);
        mBtnRegister = findViewById(R.id.btnRegistrationRegistration);
        mTextViewShowServerResponse = findViewById(R.id.txtViewRegistrationShowServerResponse);

        // Enable click event on buttons
        mBtnRegister.setOnClickListener(this);
        mStrDeviceName = Build.MODEL;
        mStrDeviceId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        String serialnum = null;
//
//        try {
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class, String.class );
//            mStrDeviceId = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );
//        }
//        catch (Exception ignored)
//        {
//        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnRegister) {
            if (mEditTextAccountHolderFullName.getText().toString().length() == 0) {
                mEditTextAccountHolderFullName.setError("Field cannot be empty");
            } else if (mEditTextMobileNumber.getText().toString().length() == 0) {
                mEditTextMobileNumber.setError("Field cannot be empty");
            } else if (mEditTextMobileNumber.getText().toString().length() < 11) {
                mEditTextMobileNumber.setError("Must be 11 characters in length");
            } else if (mEditTextEmail.getText().toString().length() == 0) {
                mEditTextEmail.setError("Field cannot be empty");
            } else {
//                mEditTextAccountHolderFullName.setEnabled(false);
//                mEditTextMobileNumber.setEnabled(false);
//                mEditTextEmail.setEnabled(false);
//                mBtnRegister.setEnabled(false);

                try {
                    mStrEncryptAccountHolderFullName = encryption.Encrypt(mEditTextAccountHolderFullName.getText().toString(), GlobalData.getStrRegistrationSecurityKey());
                    mStrEncryptMobileNumber = encryption.Encrypt(mEditTextMobileNumber.getText().toString(), GlobalData.getStrRegistrationSecurityKey());
                    mStrEncryptEmail = encryption.Encrypt(mEditTextEmail.getText().toString(), GlobalData.getStrRegistrationSecurityKey());

                    //######################### BankBin & AccountType ##########################
                    mStrEncryptBankBin = encryption.Encrypt(mStrBankBin, GlobalData.getStrRegistrationSecurityKey());
                    mStrEncryptAccountType = encryption.Encrypt(mStrAccountType, GlobalData.getStrRegistrationSecurityKey());
                    //############################################################

                    mStrEncryptDeviceId = encryption.Encrypt(mStrDeviceId, GlobalData.getStrRegistrationSecurityKey());
                    mStrEncryptDeviceName = encryption.Encrypt(mStrDeviceName, GlobalData.getStrRegistrationSecurityKey());
                    mStrEncryptRegistrationSecurityKey = encryption.Encrypt(GlobalData.getStrRegistrationSecurityKey(), GlobalData.getStrRegistrationSecurityKeyEncryptionKey());


                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(SignUp.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                            doRegistration(mStrEncryptMobileNumber, mStrEncryptBankBin, mStrEncryptAccountType,
                                    mStrEncryptAccountHolderFullName, mStrEncryptEmail, mStrEncryptDeviceId,
                                    mStrEncryptDeviceName, mStrEncryptRegistrationSecurityKey);
//                    } else {
//                        mTextViewShowServerResponse.setText("Session Expire, Please LoginNew Again");
//                    }
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                            if (mStrServerResponse.equalsIgnoreCase("success"))
                                            {

                                                startActivity(new Intent(SignUp.this, TokenSubmission.class));
                                                finish();
                                            } else {
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                {
                                                    mStrServerResponse="Request is in Process";
                                                }
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(SignUp.this);
                                                myAlert.setMessage(mStrServerResponse);
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();

                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            }

                                        }
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
                            });
                        }
                    });

                    t.start();


                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            }
        }


    }


    // method for get balance
    public void doRegistration(String strEncryptMobileNumber,
                               String strEncryptBankBin,
                               String strEncryptAccountType,
                               String strEncryptAccountHolderName,
                               String strEncryptEmail,
                               String strEncryptDeviceId,
                               String strEncryptDeviceName,
                               String strSecurityKey
    ) {
        METHOD_NAME = "QPAY_Registration ";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Registration ";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo mobileNumber = new PropertyInfo();
        mobileNumber.setName("MobileNo");
        mobileNumber.setValue(strEncryptMobileNumber);
        mobileNumber.setType(String.class);
        request.addProperty(mobileNumber);

        PropertyInfo bankBin = new PropertyInfo();
        bankBin.setName("BankBin");
        bankBin.setValue(strEncryptBankBin);
        bankBin.setType(String.class);
        request.addProperty(bankBin);

        PropertyInfo accountType = new PropertyInfo();
        accountType.setName("AccountType");
        accountType.setValue(strEncryptAccountType);
        accountType.setType(String.class);
        request.addProperty(accountType);

        PropertyInfo accountHolderFullName = new PropertyInfo();
        accountHolderFullName.setName("Name");
        accountHolderFullName.setValue(strEncryptAccountHolderName);
        accountHolderFullName.setType(String.class);
        request.addProperty(accountHolderFullName);

        PropertyInfo email = new PropertyInfo();
        email.setName("EmailAddress");
        email.setValue(strEncryptEmail);
        email.setType(String.class);
        request.addProperty(email);

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName("Device_ID");
        deviceId.setValue(strEncryptDeviceId);
        deviceId.setType(String.class);
        request.addProperty(deviceId);

        PropertyInfo deviceName = new PropertyInfo();
        deviceName.setName("Device_Name");
        deviceName.setValue(strEncryptDeviceName);
        deviceName.setType(String.class);
        request.addProperty(deviceName);

        PropertyInfo securityKey = new PropertyInfo();
        securityKey.setName("key");
        securityKey.setValue(strSecurityKey);
        securityKey.setType(String.class);
        request.addProperty(securityKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objRegistrationResponse = null;
        String strRegistrationResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000000);
            Log.v("URL: ", GlobalData.getStrUrl().replaceAll(" ", "%20"));
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objRegistrationResponse = envelope.getResponse();
            //########################## Get Response ############################
            //########################## Get Response ############################
            //########################## Get Response ############################
            strRegistrationResponse = objRegistrationResponse.toString();
            int intIndex01 = strRegistrationResponse.indexOf("Your account activation code");
            if (intIndex01 == -1) {
                int intIndex02 = strRegistrationResponse.indexOf("Request on process");
                if (intIndex02 == -1) {
                    mStrServerResponse = strRegistrationResponse;
                } else {
                    mStrServerResponse = strRegistrationResponse;
                }
            } else {
                //########################## Get Token ############################
                //########################## Get Token ############################
                //########################## Get Token ############################
                String[] partResponse = strRegistrationResponse.split("\\*");//Your account activation code is 284837,*AP180319174830
                String strMessage = partResponse[0];//Your account activation code is 284837,
                String strReferenceId = partResponse[1];//AP180319174830

//                String[] partMessage = strMessage.split("\\,");
//                String strTokenMsg = partMessage[0];//RESPONSE_MESSAGE :Your account activation code is 136124
//                String strReference = partMessage[1];//*AP180228164205

                String strToken = strMessage.replaceAll("[^-?0-9]+", "");
                GlobalData.setStrToken(strToken);
                GlobalData.setStrAccountHolderName(mEditTextAccountHolderFullName.getText().toString());
                GlobalData.setStrMobileNo(mEditTextMobileNumber.getText().toString());
                GlobalData.setStrEmailAddress(mEditTextEmail.getText().toString());
                GlobalData.setStrDeviceId(mStrDeviceId);
                mStrServerResponse = "success";
            }

        } catch (Exception exception) {
            mTextViewShowServerResponse.setText(exception.toString());
        }

    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(SignUp.this);
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

    AdapterView.OnItemSelectedListener onItemSelectedListenerForBankBin = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mStrBankBin = mSpinnerBankBin.getSelectedItem().toString();
            if (mStrBankBin.equalsIgnoreCase("Customer")) {
              //  mStrBankBin = "Huawei";
                mStrBankBin = "QPAY";
                mStrAccountType = "Saving";
            } else if (mStrBankBin.equalsIgnoreCase("Merchant")) {
               // mStrBankBin = "Huawei";
                mStrBankBin = "QPAY";
                mStrAccountType = "Current";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enableUiComponents() {
        mEditTextAccountHolderFullName.setEnabled(true);
        mEditTextMobileNumber.setEnabled(true);
        mEditTextEmail.setEnabled(true);
        mBtnRegister.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextAccountHolderFullName.setEnabled(false);
        mEditTextMobileNumber.setEnabled(false);
        mEditTextEmail.setEnabled(false);
        mBtnRegister.setEnabled(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(SignUp.this, Login.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
