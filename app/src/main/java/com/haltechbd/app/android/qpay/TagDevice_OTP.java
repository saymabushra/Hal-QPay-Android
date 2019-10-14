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

import java.lang.reflect.Method;

public class TagDevice_OTP extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;


    // initialize all ui components
    private EditText mEditTextUserId, mEditTextOTP;
    private Button mBtnTagDevice;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrDeviceId, mStrEncryptUserId, mStrEncryptMobileNumber,
            mStrEncryptDeviceId, mStrTagDeviceResponse,UserId,mStrEncryptOTP;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_device_otp);
        checkOs();
        initUI();
    }

    private void initUI() {
      //  mEditTextUserId = findViewById(R.id.editTextDeviceTaggingUserId);
        mEditTextOTP = findViewById(R.id.editTextDeviceTaggingOTP);
        mBtnTagDevice = findViewById(R.id.btnDeviceTaggingTagDeviceOTP);
        mBtnTagDevice.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewDeviceTaggingServerResponseOTP);
        mStrDeviceId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

//        try {
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class, String.class );
//            mStrDeviceId = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );
//        }
//        catch (Exception ignored)
//        {
//        }
        UserId=GlobalData.getStrUserId();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnTagDevice) {
           if (mEditTextOTP.getText().toString().length() == 0) {
               mEditTextOTP.setError("Field cannot be empty");
            } else if (mEditTextOTP.getText().toString().length() < 5) {
               mEditTextOTP.setError("Must be 5 characters in length");
            } else {
                String strMasterKeyAndAccountNumberByUserId = GetMasterKeyAndAccountNumber.getMasterKeyByUserId(UserId, GlobalData.getStrRegistrationSecurityKey());
                int intIndex = strMasterKeyAndAccountNumberByUserId.indexOf("*");
                if (intIndex == -1) {
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TagDevice_OTP.this);
                    myAlert.setMessage(strMasterKeyAndAccountNumberByUserId);
                    myAlert.setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(TagDevice_OTP.this, Login.class));
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();
                }
                else {
                    try {
                        String[] partResponse = strMasterKeyAndAccountNumberByUserId.split("\\*");
                        mStrMasterKey = partResponse[0];//REQUEST_ID:18022800000014
                        String strEncryptAccountNumber = partResponse[1];//RESPONSE_ID :18022800000019
                        mStrEncryptMobileNumber= partResponse[2];

                        mStrEncryptOTP = encryption.Encrypt(mEditTextOTP.getText().toString(), mStrMasterKey);
                       // mStrEncryptDeviceId = encryption.Encrypt(mStrDeviceId, mStrMasterKey);

                        //--------------------------------------------------------------------------------------
                        // Initialize progress dialog
                        mProgressDialog = ProgressDialog.show(TagDevice_OTP.this, null, "Processing request...", false, true);
                        // Cancel progress dialog on back key press
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                                mStrTagDeviceResponse = DeviceRegistration.tagDeviceByUserId(
                                        UserId,
                                        mStrEncryptMobileNumber,
                                        mStrDeviceId,mStrEncryptOTP);
//                    } else {
//                        mTextViewShowServerResponse.setText("Session Expire, Please Login Again");
//                    }
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                if (mStrTagDeviceResponse.equalsIgnoreCase("Update")) {
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TagDevice_OTP.this);
                                                    myAlert.setMessage("Successfully tag device.");
                                                    myAlert.setNegativeButton(
                                                            "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    startActivity(new Intent(TagDevice_OTP.this, Login.class));
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
                                                        mStrTagDeviceResponse="Try Againg!!";
                                                    }
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TagDevice_OTP.this);
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
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(TagDevice_OTP.this);
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
       // mEditTextUserId.setEnabled(true);
        mEditTextOTP.setEnabled(true);
        mBtnTagDevice.setEnabled(true);
    }

    private void disableUiComponents() {
       // mEditTextUserId.setEnabled(false);
        mEditTextOTP.setEnabled(false);
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
                startActivity(new Intent(TagDevice_OTP.this, TagDevice.class));
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(TagDevice_OTP.this, TagDevice.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
