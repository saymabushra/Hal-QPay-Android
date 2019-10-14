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
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.Registration;


public class PinSubmission extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog mProgressDialog = null;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private EditText mEditTextPin;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrEncryptPin, mStrEncryptAccountNumber, mStrServerResponse;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_submission);
        checkOs();
        initUI();
    }

    private void initUI() {
        mEditTextPin = findViewById(R.id.editTextPin);
        mBtnSubmit = findViewById(R.id.btnSubmitPin);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewPinShowServerResponse);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mEditTextPin.getText().toString().length() == 0) {
                mEditTextPin.setError("Field cannot be empty");
            } else if (mEditTextPin.getText().toString().length() < 4) {
                mEditTextPin.setError("Must be 4 characters in length");
            } else {
//                mEditTextPin.setEnabled(false);
//                mBtnSubmit.setEnabled(false);

                try {
                    mStrEncryptPin = encryption.Encrypt(mEditTextPin.getText().toString(), GlobalData.getStrMasterKey());
                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrMasterKey());

                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(PinSubmission.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                            String strSetPinResponse = Registration.setPin(mStrEncryptPin, mStrEncryptAccountNumber, GlobalData.getStrUserId());
                            if (strSetPinResponse.equalsIgnoreCase("Update")) {
                                mStrServerResponse = "Thank you for Signing Up. Now you can Login using your User ID and PIN.";
                            } else {
                                mStrServerResponse = strSetPinResponse;
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
                                            //####################### Show Dialog ####################
                                            //####################### Show Dialog ####################
                                            //####################### Show Dialog ####################
                                            if(mStrServerResponse.equalsIgnoreCase(""))
                                            {
                                                mStrServerResponse="Try again";
                                            }
                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(PinSubmission.this);
                                            myAlert.setMessage(mStrServerResponse);
                                            myAlert.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            startActivity(new Intent(PinSubmission.this, Login.class));
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog alertDialog = myAlert.create();
                                            alertDialog.show();
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

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(PinSubmission.this);
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
        mEditTextPin.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextPin.setEnabled(false);
        mBtnSubmit.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(PinSubmission.this, UserIdSubmission.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(PinSubmission.this, UserIdSubmission.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
