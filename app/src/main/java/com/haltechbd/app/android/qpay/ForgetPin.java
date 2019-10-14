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
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetMasterKeyAndAccountNumber;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Method;

public class ForgetPin extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;
    String strUserId="";
    String mStrDeviceId="";

    // initialize all ui components
    private EditText mEditTextUserId, mEditTextEmail, mEditTextMobileNumber;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrEncryptUserId, mStrEncryptEmail, mStrEncryptMobileNumber, mStrServerResponse;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pin);
        checkOs();
        // initialize all ui components
        initUI();
    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextUserId = findViewById(R.id.editTextForgetPinUserId);
        mEditTextEmail = findViewById(R.id.editTextForgetPinEmail);
        mEditTextMobileNumber = findViewById(R.id.editTextForgetPinMobileNumber);
        mBtnSubmit = findViewById(R.id.btnForgetPinSubmit);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewForgetPinShowServerResponse);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mEditTextUserId.getText().toString().length() == 0) {
                mEditTextUserId.setError("Field cannot be empty");
            } else if (mEditTextEmail.getText().toString().length() == 0) {
                mEditTextEmail.setError("Field cannot be empty");
            } else if (mEditTextMobileNumber.getText().toString().length() == 0) {
                mEditTextMobileNumber.setError("Field cannot be empty");
            } else if (mEditTextMobileNumber.getText().toString().length() < 11) {
                mEditTextMobileNumber.setError("Must be 11 characters in length");
            } else {
                disableUiComponentAfterClick();

                try {
                    //################# Get Plain Value ###################
                    //################# Get Plain Value ###################
                    //################# Get Plain Value ###################
                    mStrDeviceId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

//                    try {
//                        Class<?> c = Class.forName("android.os.SystemProperties");
//                        Method get = c.getMethod("get", String.class, String.class );
//                        mStrDeviceId = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );
//                    }
//                    catch (Exception ignored)
//                    {
//                    }
                     strUserId = mEditTextUserId.getText().toString();
                    String strEmail = mEditTextEmail.getText().toString();
                    String strMobileNumber = mEditTextMobileNumber.getText().toString();
                    //################# Get MasterKey And Encrypt Account Number ###################
                    //################# Get MasterKey And Encrypt Account Number ###################
                    //################# Get MasterKey And Encrypt Account Number ###################
                    String strMasterKeyAndMasterKey = GetMasterKeyAndAccountNumber.getMasterKeyByUserId(strUserId, GlobalData.getStrRegistrationSecurityKey());

                    if (strMasterKeyAndMasterKey.equalsIgnoreCase("Master key is null")) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(ForgetPin.this);
                        myAlert.setTitle("INFO");
                        myAlert.setMessage("Wrong User ID, Please Try Again.");
                        myAlert.setNegativeButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = myAlert.create();
                        alertDialog.show();
                    } else {
                        String[] parts = strMasterKeyAndMasterKey.split("\\*");//50450421468144240195181588474166*80ADhgbE0WaX+yA0WDpExA==
                        //################# Get MasterKey ######################
                        //################# Get MasterKey ######################
                        //################# Get MasterKey ######################
                        mStrMasterKey = parts[0];//50450421468144240195181588474166
                        String strEncryptAccountNumber = parts[1];//80ADhgbE0WaX+yA0WDpExA==
                        //################# Encrypt Values #####################
                        //################# Encrypt Values #####################
                        //################# Encrypt Values #####################
                       // mStrEncryptUserId = encryption.Encrypt(strUserId, mStrMasterKey);
                        mStrEncryptEmail = encryption.Encrypt(strEmail, mStrMasterKey);
                        mStrEncryptMobileNumber = encryption.Encrypt(strMobileNumber, mStrMasterKey);

                        // Initialize progress dialog
                        mProgressDialog = ProgressDialog.show(ForgetPin.this, null, "Processing request...", false, true);
                        // Cancel progress dialog on back key press
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                                getPin(strUserId, mStrEncryptEmail, mStrEncryptMobileNumber, mStrDeviceId);
//                                SystemClock.sleep(10000);
//                    } else {
//                        mTextViewShowServerResponse.setText("Session Expire, Please LoginNew Again");
//                    }
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
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(ForgetPin.this);
                                                myAlert.setMessage(mStrServerResponse);
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                enableUiComponentAfterClick();
                                                                if(mStrServerResponse.equalsIgnoreCase("PIN is send to your mobile no.Please check"))
                                                                {
                                                                    startActivity(new Intent(ForgetPin.this, Login.class));
                                                                }
                                                                else
                                                                {}
//                                                                startActivity(new Intent(ForgetPin.this, LoginNew.class));
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
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        }

    }

    // method for get balance
    public void getPin(String strEncryptUserId,
                       String strEncryptEmail,
                       String strEncryptMobileNumber,
                       String strDevice) {
        METHOD_NAME = "QPAY_ForgetPassWord ";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_ForgetPassWord ";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo encryptEmail = new PropertyInfo();
        encryptEmail.setName("E_Email");
        encryptEmail.setValue(strEncryptEmail);
        encryptEmail.setType(String.class);
        request.addProperty(encryptEmail);


        PropertyInfo encryptMobileNumber = new PropertyInfo();
        encryptMobileNumber.setName("E_PhoneNo");
        encryptMobileNumber.setValue(strEncryptMobileNumber);
        encryptMobileNumber.setType(String.class);
        request.addProperty(encryptMobileNumber);

        PropertyInfo encryptUserId = new PropertyInfo();
        encryptUserId.setName("UserID");
        encryptUserId.setValue(strEncryptUserId);
        encryptUserId.setType(String.class);
        request.addProperty(encryptUserId);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(strDevice);
        masterKey.setType(String.class);
        request.addProperty(masterKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objForgetPin = null;
        String strForgetPinResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objForgetPin = envelope.getResponse();
            strForgetPinResponse = objForgetPin.toString();
            int intIndexOtpResponse = strForgetPinResponse.indexOf("PIN is send");
            if (intIndexOtpResponse == -1) {
                mStrServerResponse = "Wrong UserID, Email or PIN, Please try again with correct credentials.";
            } else {
                mStrServerResponse = strForgetPinResponse;
            }
        } catch (Exception exception) {
            mStrServerResponse = "Network Error!!!";
        }
    }

    private void disableUiComponentAfterClick() {
        mEditTextUserId.setEnabled(false);
        mEditTextEmail.setEnabled(false);
        mEditTextMobileNumber.setEnabled(false);
        mBtnSubmit.setEnabled(false);
    }

    private void enableUiComponentAfterClick() {
        mEditTextUserId.setEnabled(true);
        mEditTextEmail.setEnabled(true);
        mEditTextMobileNumber.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ForgetPin.this, Login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(ForgetPin.this);
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
        mEditTextUserId.setEnabled(true);
        mEditTextEmail.setEnabled(true);
        mEditTextMobileNumber.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextUserId.setEnabled(false);
        mEditTextEmail.setEnabled(false);
        mEditTextMobileNumber.setEnabled(false);
        mBtnSubmit.setEnabled(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ForgetPin.this, Login.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

