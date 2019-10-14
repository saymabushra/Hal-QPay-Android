package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.DeviceRegistration;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetMasterKeyAndAccountNumber;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class TagDevice extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;


    // initialize all ui components
    private EditText mEditTextUserId, mEditTextMobileNumber;
    private Button mBtnTagDevice;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrDeviceId, mStrEncryptUserId, mStrEncryptMobileNumber,
            mStrEncryptDeviceId, mStrTagDeviceResponse,mStrEncryptNumberOfTransaction,mStrEncryptOtpDuration,strEncryptAccountNumber;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_device);
        checkOs();
        initUI();
    }

    private void initUI() {
        mEditTextUserId = findViewById(R.id.editTextDeviceTaggingUserId);
       // mEditTextMobileNumber = findViewById(R.id.editTextDeviceTaggingMobileNumber);
        mBtnTagDevice = findViewById(R.id.btnDeviceTaggingTagDevice);
        mBtnTagDevice.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewDeviceTaggingServerResponse);
        mStrDeviceId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        try {
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class, String.class );
//            mStrDeviceId = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );
//        }
//        catch (Exception ignored)
//        {
//        }

        mEditTextUserId.setText(GlobalData.getStrUserId());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnTagDevice) {
            if (mEditTextUserId.getText().toString().length() == 0) {
                mEditTextUserId.setError("Field cannot be empty");
            }

            else {
                String strMasterKeyAndAccountNumberByUserId = GetMasterKeyAndAccountNumber.getMasterKeyByUserId(mEditTextUserId.getText().toString(), GlobalData.getStrRegistrationSecurityKey());
                int intIndex = strMasterKeyAndAccountNumberByUserId.indexOf("*");
                if (intIndex == -1) {
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TagDevice.this);
                    myAlert.setMessage(strMasterKeyAndAccountNumberByUserId);
                    myAlert.setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(TagDevice.this, Login.class));
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();
                } else {
                    try {
                        String[] partResponse = strMasterKeyAndAccountNumberByUserId.split("\\*");
                        mStrMasterKey = partResponse[0];//REQUEST_ID:18022800000014
                         strEncryptAccountNumber = partResponse[1];//RESPONSE_ID :18022800000019
                        String strMsisdn= partResponse[2];//RESPONSE_ID :18022800000019

                       // mStrEncryptMobileNumber = encryption.Encrypt(mEditTextMobileNumber.getText().toString(), mStrMasterKey);
                       // mStrEncryptDeviceId = encryption.Encrypt(mStrDeviceId, mStrMasterKey);
                        mStrEncryptNumberOfTransaction = encryption.Encrypt("1", mStrMasterKey);
                        mStrEncryptOtpDuration = encryption.Encrypt("5", mStrMasterKey);
                        //--------------------------------------------------------------------------------------
                        // Initialize progress dialog
                        mProgressDialog = ProgressDialog.show(TagDevice.this, null, "Processing request...", false, true);
                        // Cancel progress dialog on back key press
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {

                                GenerateOtp(strEncryptAccountNumber,mStrEncryptNumberOfTransaction,mStrEncryptOtpDuration,mEditTextUserId.getText().toString(),mStrDeviceId);
////
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                if (mStrTagDeviceResponse.equalsIgnoreCase("Check your SMS for OTP.")) {
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TagDevice.this);
                                                    myAlert.setMessage("Check your SMS for OTP.");
                                                    myAlert.setNegativeButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    GlobalData.setStrUserId(mEditTextUserId.getText().toString());
                                                                    startActivity(new Intent(TagDevice.this, TagDevice_OTP.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();
                                                } else {
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    if(mStrTagDeviceResponse.equalsIgnoreCase(""))
                                                    {
                                                        mStrTagDeviceResponse="Request is in Process";
                                                    }
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TagDevice.this);
                                                    myAlert.setMessage(mStrTagDeviceResponse);
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
                        //---------------------------------------------------------------------------------
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            }
        }

    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(TagDevice.this);
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
        mEditTextUserId.setEnabled(true);
       // mEditTextMobileNumber.setEnabled(true);
        mBtnTagDevice.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextUserId.setEnabled(false);
       /// mEditTextMobileNumber.setEnabled(false);
        mBtnTagDevice.setEnabled(false);
    }

    //########################## Logout ############################
    //########################## Logout ############################
    //########################## Logout ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(TagDevice.this, Login.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(TagDevice.this, Login.class)
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

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public void GenerateOtp(String strEncryptAccountNumber,
                            String strEncryptMaxNumberOfTransaction,
                            String strEncryptDurationInMin,
                            String userid,String deviceid) {
        METHOD_NAME = "QPAY_GenerateOTP_With_transaction_Duration_WithoutPIN";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GenerateOTP_With_transaction_Duration_WithoutPIN";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo encryptAccountNumber = new PropertyInfo();
        encryptAccountNumber.setName("E_AccountNo");
        encryptAccountNumber.setValue(strEncryptAccountNumber);
        encryptAccountNumber.setType(String.class);
        request.addProperty(encryptAccountNumber);



        PropertyInfo encryptMaxNumberOfTransaction = new PropertyInfo();
        encryptMaxNumberOfTransaction.setName("E_MaximiumTransactionNo");
        encryptMaxNumberOfTransaction.setValue(strEncryptMaxNumberOfTransaction);
        encryptMaxNumberOfTransaction.setType(String.class);
        request.addProperty(encryptMaxNumberOfTransaction);

        PropertyInfo encryptDurationInMin = new PropertyInfo();
        encryptDurationInMin.setName("E_TransactionDurationInMiniute");
        encryptDurationInMin.setValue(strEncryptDurationInMin);
        encryptDurationInMin.setType(String.class);
        request.addProperty(encryptDurationInMin);

        PropertyInfo encryptUserId = new PropertyInfo();
        encryptUserId.setName("UserID");
        encryptUserId.setValue(userid);
        encryptUserId.setType(String.class);
        request.addProperty(encryptUserId);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(deviceid);
        masterKey.setType(String.class);
        request.addProperty(masterKey);


        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objGenerateOtp = null;
        String strOtpResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 20000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objGenerateOtp = envelope.getResponse();
            strOtpResponse = objGenerateOtp.toString();
            int intIndex = strOtpResponse.indexOf("Your OTP IS");
            if (intIndex == -1) {
                if(strOtpResponse.startsWith("Request on process*"))
                {
                    mStrTagDeviceResponse = "Check your SMS for OTP.";
                }
                else {
                    mStrTagDeviceResponse = strOtpResponse;
                }
            } else {
                mStrTagDeviceResponse = "Check your SMS for OTP.";



            }
        } catch (Exception exception) {
            mStrTagDeviceResponse = "Check your SMS for OTP.";
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(TagDevice.this, Login.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
