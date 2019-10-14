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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class KycUpdateParentsAndSpouseInfo extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;


    // initialize all ui components
    private EditText mEditTextFatherName, mEditTextMotherName, mEditTextSpouseTitle, mEditTextSpouseName;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptFatherName,
            mStrEncryptMotherName, mStrEncryptSpouseTitle, mStrEncryptSpouseName,
            mStrServerResponse,strParamete;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_update_parents_and_spouse_info);
        checkOs();
        // initialize all ui components
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextFatherName = findViewById(R.id.editTextKycParentsAndSpouseInfoFatherName);
        mEditTextMotherName = findViewById(R.id.editTextKycParentsAndSpouseInfoMotherName);
        mEditTextSpouseTitle = findViewById(R.id.editTextKycParentsAndSpouseInfoSpouseTitle);
        mEditTextSpouseName = findViewById(R.id.editTextKycParentsAndSpouseInfoSpouseName);
        mBtnSubmit = findViewById(R.id.btnKycParentsAndSpouseUpdateSubmit);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewKycParentsAndSpouseInfoServerResponse);
        mStrMasterKey = GlobalData.getStrMasterKey();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
        mEditTextFatherName.setText(GlobalData.getStrKYCPersonalAndSpourFathername());
        mEditTextMotherName.setText(GlobalData.getStrKYCPersonalAndSpouseMotherName());
        mEditTextSpouseTitle.setText(GlobalData.getStrKYCPersonalAndSpouseTitle());
        mEditTextSpouseName.setText(GlobalData.getStrKYCPersonalAndSpouseName());

    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mEditTextFatherName.getText().toString().length() == 0) {
                mEditTextFatherName.setError("Field cannot be empty");
            } else if (mEditTextMotherName.getText().toString().length() == 0) {
                mEditTextMotherName.setError("Field cannot be empty");
            } else if (mEditTextSpouseName.getText().toString().length() == 0) {
                mEditTextSpouseName.setError("Field cannot be empty");
            } else if (mEditTextSpouseName.getText().toString().length() == 0) {
                mEditTextSpouseName.setError("Field cannot be empty");
            } else {
//                disableUiComponentAfterClick();
                try {
                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    mStrEncryptFatherName = encryption.Encrypt(mEditTextFatherName.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptMotherName = encryption.Encrypt(mEditTextMotherName.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptSpouseTitle = encryption.Encrypt(mEditTextSpouseTitle.getText().toString(), GlobalData.getStrSessionId());
                    mStrEncryptSpouseName = encryption.Encrypt(mEditTextSpouseName.getText().toString(), GlobalData.getStrSessionId());
                    strParamete=encryption.Encrypt("U", GlobalData.getStrSessionId());
                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(KycUpdateParentsAndSpouseInfo.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                          String strGet= InsertKyc.insertParentsAndSpouseInfo(
                                    mStrEncryptAccountNumber,
                                    mStrEncryptPin,
                                    mStrEncryptFatherName,
                                    mStrEncryptMotherName,
                                    mStrEncryptSpouseTitle,
                                    mStrEncryptSpouseName,
                                  strParamete
                            );

                            mStrServerResponse = strGet;
                            if (mStrServerResponse.equalsIgnoreCase("Update"))
                            {
                                mStrServerResponse = "Parents/Spouse Info update succesfully.";
                            }
                            else
                            {
                                mStrServerResponse = strGet;
                            }

//                            SystemClock.sleep(10000);
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
                                            int intIndex = mStrServerResponse.indexOf("Parents/Spouse Info update succesfully.");
                                            if (intIndex == -1) {
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateParentsAndSpouseInfo.this);
                                                myAlert.setMessage(mStrServerResponse);
                                                myAlert.setNegativeButton(
                                                        "Close",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
//                                                            enableUiComponentAfterClick();

                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            } else {
                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                {
                                                    mStrServerResponse="Request is in process";
                                                }
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateParentsAndSpouseInfo.this);
                                                myAlert.setMessage(mStrServerResponse);
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
//                                                                Intent intent = new Intent(KycUpdateParentsAndSpouseInfo.this, KycPreviewParentsAndSpouseInfo.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                startActivity(intent);
                                                                startActivity(new Intent(KycUpdateParentsAndSpouseInfo.this, KycPreviewParentsAndSpouseInfo.class));
                                                                finish();
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



    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdateParentsAndSpouseInfo.this);
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
        mEditTextFatherName.setEnabled(true);
        mEditTextMotherName.setEnabled(true);
        mEditTextSpouseTitle.setEnabled(true);
        mEditTextSpouseName.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextFatherName.setEnabled(false);
        mEditTextMotherName.setEnabled(false);
        mEditTextSpouseTitle.setEnabled(false);
        mEditTextSpouseName.setEnabled(false);
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
                startActivity(new Intent(KycUpdateParentsAndSpouseInfo.this, KycPreviewParentsAndSpouseInfo.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycUpdateParentsAndSpouseInfo.this, Login.class)
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
            startActivity(new Intent(KycUpdateParentsAndSpouseInfo.this, KycPreviewParentsAndSpouseInfo.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}


