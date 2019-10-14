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
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;


public class ChangePin extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;


    // initialize all ui components
    private EditText mEditTextPin, mEditTextNewPin;
    private Button mBtnChangePin;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrAccountNumber, mStrPin,
            mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptNewPin,
            mStrServerResponse;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkOs();
        // initialize all ui components
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextPin = findViewById(R.id.editTextChangePinCurrentPin);
        mEditTextNewPin = findViewById(R.id.editTextChangePinNewPin);
        mBtnChangePin = findViewById(R.id.btnChangePinChangePin);
        mBtnChangePin.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewOtpShowServerResponse);

        mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();



        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnChangePin) {
            if (mEditTextPin.getText().toString().length() == 0) {
                mEditTextPin.setError("Field cannot be empty");
            } else if (mEditTextPin.getText().toString().length() < 4) {
                mEditTextPin.setError("Must be 4 characters in length");
            } else if (mEditTextNewPin.getText().toString().length() == 0) {
                mEditTextNewPin.setError("Field cannot be empty");
            } else if (mEditTextNewPin.getText().toString().length() < 4) {
                mEditTextNewPin.setError("Must be 4 characters in length");
            } else {
//                disableUiComponentAfterClick();
                try {
                    mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, GlobalData.getStrSessionId());
                    mStrEncryptPin = encryption.Encrypt(mStrPin, GlobalData.getStrSessionId());
                    mStrEncryptNewPin = encryption.Encrypt(mEditTextNewPin.getText().toString(), GlobalData.getStrSessionId());

                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(ChangePin.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            String strChangePinResponse = CheckDeviceActiveStatus.doChangePin(
                                    mStrEncryptAccountNumber,
                                    mStrEncryptPin,
                                    mStrEncryptNewPin
                            );
                            int intIndexChangePin = strChangePinResponse.indexOf("successfull");
                            if (intIndexChangePin == -1) {
                                mStrServerResponse = strChangePinResponse;
                            } else {
                                mStrServerResponse = "Successfully change PIN. Check SMS for new PIN.";
                            }
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                        //####################### Show Dialog ####################
                                        //####################### Show Dialog ####################
                                        //####################### Show Dialog ####################
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(ChangePin.this);
                                        if(mStrServerResponse.equalsIgnoreCase(""))
                                        {
                                            mStrServerResponse="Try Again";
                                        }
                                        myAlert.setMessage(mStrServerResponse);
                                        myAlert.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        enableUiComponentAfterClick();
                                                        startActivity(new Intent(ChangePin.this, Login.class));
                                                        finish();
                                                    }
                                                });
                                        AlertDialog alertDialog = myAlert.create();
                                        alertDialog.show();
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


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(ChangePin.this);
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
        mEditTextPin.setEnabled(true);
        mEditTextNewPin.setEnabled(true);
        mBtnChangePin.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextPin.setEnabled(false);
        mEditTextNewPin.setEnabled(false);
        mBtnChangePin.setEnabled(false);
//        mEditTextNewPin.setBackgroundColor(Color.DKGRAY);
    }

    //########################## Logout ############################
    //########################## Logout ############################
    //########################## Logout ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:0");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ChangePin.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(ChangePin.this, Login.class)
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
      //  GlobalData.setStrEncryptAccountNumber("");
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

    private void disableUiComponentAfterClick() {
        mEditTextPin.setEnabled(false);
        mEditTextNewPin.setEnabled(false);
        mBtnChangePin.setEnabled(false);
    }

    private void enableUiComponentAfterClick() {
        mEditTextPin.setEnabled(true);
        mEditTextNewPin.setEnabled(true);
        mBtnChangePin.setEnabled(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ChangePin.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
