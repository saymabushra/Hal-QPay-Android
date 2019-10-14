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
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.Registration;


public class UserIdSubmission extends AppCompatActivity implements View.OnClickListener {
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private EditText mEditTextUserId;
    private Button mBtnSubmit;
    private String mStrEncryptUserId, mStrEncryptAccountNumber,
            mStrSetUserIdResponse, mStrServerResponse;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_id_submission);
        checkOs();
        initUI();
    }

    private void initUI() {
        mEditTextUserId = findViewById(R.id.editTextUserId);
        mBtnSubmit = findViewById(R.id.btnSubmitUserId);
        mBtnSubmit.setOnClickListener(this);

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
            } else {
//                mEditTextUserId.setEnabled(false);
//                mBtnSubmit.setEnabled(false);

                try {
                  //  mStrEncryptUserId = encryption.Encrypt(mEditTextUserId.getText().toString(), GlobalData.getStrMasterKey());
                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrMasterKey());

                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(UserIdSubmission.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                            mStrSetUserIdResponse = Registration.setUserId(mEditTextUserId.getText().toString(), mStrEncryptAccountNumber, GlobalData.getStrDeviceId());
                            if (mStrSetUserIdResponse.equalsIgnoreCase("Update")) {
                                mStrServerResponse = "success";
                            } else {
                                mStrServerResponse = mStrSetUserIdResponse;
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
                                                GlobalData.setStrUserId(mEditTextUserId.getText().toString());
                                                startActivity(new Intent(UserIdSubmission.this, PinSubmission.class));
                                                finish();
                                            } else {
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                {
                                                    mStrServerResponse="Request is in process";
                                                }
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(UserIdSubmission.this);
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


                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(UserIdSubmission.this, TokenSubmission.class));
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

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(UserIdSubmission.this);
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
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextUserId.setEnabled(false);
        mBtnSubmit.setEnabled(false);
    }

    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(UserIdSubmission.this, TokenSubmission.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
