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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetMasterKeyAndAccountNumber;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.Registration;

import java.lang.reflect.Method;


public class TokenSubmission extends AppCompatActivity implements View.OnClickListener {
    private EncryptionDecryption Encdecryption = new EncryptionDecryption();
    //private Encryption encryption = new Encryption();
    private ProgressDialog mProgressDialog = null;

    // initialize all ui components
    private EditText mEditTextToken;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrDeviceName, mStrDeviceId, mStrMasterKey,
            mStrEncryptAccountNumber, mStrEncryptToken, mStrAccountNumber, mStrServerResponse;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.token_submission);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextToken = findViewById(R.id.editTextToken);
        mBtnSubmit = findViewById(R.id.btnSubmitToken);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewTokenShowServerResponse);
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

        //mEditTextToken.setText(GlobalData.getStrToken());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mEditTextToken.getText().toString().length() == 0) {
                mEditTextToken.setError("Field cannot be empty");
            } else {
//                mEditTextToken.setEnabled(false);
//                mBtnSubmit.setEnabled(false);

                //########################## Get MasterKey & Encrypt Account Number ############################
                //########################## Get MasterKey & Encrypt Account Number ############################
                //########################## Get MasterKey & Encrypt Account Number ############################
                String strMasterKeyAndAccountNumberByDeviceId = GetMasterKeyAndAccountNumber.getMasterKeyAndAccountNumberByCompositeKey(mStrDeviceId, GlobalData.getStrAccountHolderName(),GlobalData.getStrMobileNo(),GlobalData.getStrEmailAddress());//99792525063258043845161478830524*2WWtXiRqc+0yI10y28UqUA==
                int intIndexToken = strMasterKeyAndAccountNumberByDeviceId.indexOf("*");
                if (intIndexToken == -1) {
                    if (strMasterKeyAndAccountNumberByDeviceId.equalsIgnoreCase("Master key is null")) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(TokenSubmission.this);
                        myAlert.setMessage("Master Key is null.");
                        myAlert.setNegativeButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = myAlert.create();
                        alertDialog.show();
                    } else if (strMasterKeyAndAccountNumberByDeviceId.equalsIgnoreCase("Invalid Key")) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(TokenSubmission.this);
                        myAlert.setMessage("Invalid Master Key.");
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
                } else {
                    String[] parts = strMasterKeyAndAccountNumberByDeviceId.split("\\*");
                    mStrMasterKey = parts[0];//99792525063258043845161478830524
                    GlobalData.setStrMasterKey(mStrMasterKey);
                    mStrEncryptAccountNumber = parts[1];//2WWtXiRqc+0yI10y28UqUA==

                    try {
                        mStrAccountNumber = Encdecryption.Decrypt(mStrEncryptAccountNumber, mStrMasterKey);
                        GlobalData.setStrAccountNumber(mStrAccountNumber);
                        mStrEncryptToken = Encdecryption.Encrypt(mEditTextToken.getText().toString(), mStrMasterKey);

                        // Initialize progress dialog
                        mProgressDialog = ProgressDialog.show(TokenSubmission.this, null, "Processing request...", false, true);
                        // Cancel progress dialog on back key press
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                                //########################## Check TokenSubmission ############################
                                //########################## Check TokenSubmission ############################
                                //########################## Check TokenSubmission ############################
                                String strCheckTokenResponse = Registration.checkToken(mStrEncryptAccountNumber, mStrEncryptToken);
                                if (strCheckTokenResponse.equalsIgnoreCase("Success")) {
                                    mStrServerResponse = "success";
                                } else {
                                    mStrServerResponse = "Token submission fail. Please try again.";
                                }
//                    } else {
//                        mTextViewShowServerResponse.setText("Session Expire, Please Login Again");
//                    }
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                if (mStrServerResponse.equalsIgnoreCase("success")) {
                                                    startActivity(new Intent(TokenSubmission.this, UserIdSubmission.class));
                                                    finish();
                                                } else {
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(TokenSubmission.this);
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

                    } catch (Exception e) {
                        e.printStackTrace();
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
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(TokenSubmission.this);
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
        mEditTextToken.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextToken.setEnabled(false);
        mBtnSubmit.setEnabled(false);
    }

    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(TokenSubmission.this, SignUp.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(TokenSubmission.this, SignUp.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
