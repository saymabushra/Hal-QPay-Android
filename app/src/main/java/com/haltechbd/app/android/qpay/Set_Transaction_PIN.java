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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.Registration;


public class Set_Transaction_PIN extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog mProgressDialog = null;
    private EncryptionDecryption encryption = new EncryptionDecryption();

    private TextView mTextViewShowServerResponse;
    private String mStrEncryptPin, mStrEncryptAccountNumber, mStrServerResponse;
    private EditText mEditText01, mEditText02, mEditText03, mEditText04;
    private Button mBtnSetTransationPin;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_pin);
        checkOs();
        initUI();
    }

    private void initUI() {
       // mEditTextPin = findViewById(R.id.editTextPin);
//        mBtnSubmit = findViewById(R.id.btnSubmitPin);
//        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewPinShowServerResponse);

        //================================
        mEditText01 = findViewById(R.id.editText01);
        mEditText01.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mEditText01.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = mEditText01.getText().length();

                if (textlength1 >= 1) {
                    mEditText02.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });
        mEditText02 = findViewById(R.id.editText02);
        mEditText02.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mEditText02.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = mEditText02.getText().length();

                if (textlength1 >= 1) {
                    mEditText03.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });
        mEditText03 = findViewById(R.id.editText03);
        mEditText03.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mEditText03.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = mEditText03.getText().length();

                if (textlength1 >= 1) {
                    mEditText04.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });
        mEditText04 = findViewById(R.id.editText04);
        mEditText04.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mBtnSetTransationPin = findViewById(R.id.btn);
        mBtnSetTransationPin.setOnClickListener(this);

        //==================================

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
          if (v == mBtnSetTransationPin) {
            String str01 = mEditText01.getText().toString();
            String str02 = mEditText02.getText().toString();
            String str03 = mEditText03.getText().toString();
            String str04 = mEditText04.getText().toString();
            String strTransationPin = str01 + str02 + str03 + str04;
           // Toast.makeText(TransactionPinActivity.this, strTransationPin, Toast.LENGTH_LONG).show();

            try {
                mStrEncryptPin = encryption.Encrypt(strTransationPin, GlobalData.getStrSessionId());
                mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());

                // Initialize progress dialog
                mProgressDialog = ProgressDialog.show(Set_Transaction_PIN.this, null, "Processing request...", false, true);
                // Cancel progress dialog on back key press
                mProgressDialog.setCancelable(true);

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                        String strSetPinResponse = Registration.setTransactionPin(mStrEncryptPin, mStrEncryptAccountNumber, GlobalData.getStrUserId());
                        if (strSetPinResponse.equalsIgnoreCase("Update")) {
                            mStrServerResponse = "Your Transaction PIN is saved Successfully";
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
                                            mStrServerResponse="Try Again";
                                        }
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Set_Transaction_PIN.this);
                                        myAlert.setMessage(mStrServerResponse);
                                        myAlert.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        startActivity(new Intent(Set_Transaction_PIN.this, MainActivity.class));
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

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Set_Transaction_PIN.this);
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
        //mEditTextPin.setEnabled(true);
        //mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextPin.setEnabled(false);
//        mBtnSubmit.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Set_Transaction_PIN.this, MainActivity.class));
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

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Set_Transaction_PIN.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }





}
