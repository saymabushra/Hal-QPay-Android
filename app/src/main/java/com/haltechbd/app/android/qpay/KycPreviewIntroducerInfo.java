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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;


public class KycPreviewIntroducerInfo extends AppCompatActivity implements View.OnClickListener {
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private TextView mTextViewIntroducerName, mTextViewIntroducerMobileNumber,
            mTextViewIntroducerAddress, mTextViewOccupation, mTextViewRemark;
    private Button mBtnUpdate;
    private TextView mTextViewShowServerResponse;
    private String mStrAccountNumber, mStrPin, mStrMasterKey,
            mStrEncryptAccountNumber, mStrEncryptPin,
            mStrEncryptIntroducerName, mStrEncryptIntroducerMobileNumber, mStrEncryptIntroducerAddress,
            mStrEncryptIntroducerOccupation, mStrEncryptRemark, mStrServerResponse,strParamete;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_preview_introducer_info);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mTextViewIntroducerName = findViewById(R.id.textViewIntroducerName);
        mTextViewIntroducerMobileNumber = findViewById(R.id.textViewIntroducerMobileNumber);
        mTextViewIntroducerAddress = findViewById(R.id.textViewIntroducerAddress);
        mTextViewOccupation = findViewById(R.id.textViewIntroducerOccupation);
        mTextViewRemark = findViewById(R.id.textViewIntroducerRemark);
        mBtnUpdate = findViewById(R.id.btnKycUpdateIntroducerInfo);
        mBtnUpdate.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewKycUpdateIntroducerInfoShowServerResponse);

        mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnUpdate) {
            startActivity(new Intent(KycPreviewIntroducerInfo.this, KycUpdateIntroducerInfo.class));
            finish();
//            if (mEditTextIntroducerName.getText().toString().length() == 0) {
//                mEditTextIntroducerName.setError("Field cannot be empty");
//            } else if (mEditTextIntroducerMobileNumber.getText().toString().length() == 0) {
//                mEditTextIntroducerMobileNumber.setError("Field cannot be empty");
//            } else if (mEditTextIntroducerMobileNumber.getText().toString().length() < 11) {
//                mEditTextIntroducerMobileNumber.setError("Must be 11 characters in length");
//            } else if (mEditTextIntroducerAddress.getText().toString().length() == 0) {
//                mEditTextIntroducerAddress.setError("Field cannot be empty");
//            } else if (mEditTextOccupation.getText().toString().length() == 0) {
//                mEditTextOccupation.setError("Field cannot be empty");
//            } else if (mEditTextRemark.getText().toString().length() == 0) {
//                mEditTextRemark.setError("Field cannot be empty");
//            } else {
////                disableUiComponentAfterClick();
//                try {
//                    mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, mStrMasterKey);
//                    mStrEncryptPin = encryption.Encrypt(mStrPin, mStrMasterKey);
//                    mStrEncryptIntroducerName = encryption.Encrypt(mEditTextIntroducerName.getText().toString(), mStrMasterKey);
//                    mStrEncryptIntroducerMobileNumber = encryption.Encrypt(mEditTextIntroducerMobileNumber.getText().toString(), mStrMasterKey);
//                    mStrEncryptIntroducerAddress = encryption.Encrypt(mEditTextIntroducerAddress.getText().toString(), mStrMasterKey);
//                    mStrEncryptIntroducerOccupation = encryption.Encrypt(mEditTextOccupation.getText().toString(), mStrMasterKey);
//                    mStrEncryptRemark = encryption.Encrypt(mEditTextRemark.getText().toString(), mStrMasterKey);
//
//                    // Initialize progress dialog
//                    mProgressDialog = ProgressDialog.show(KycPreviewIntroducerInfo.this, null, "Processing request...", false, true);
//                    // Cancel progress dialog on back key press
//                    mProgressDialog.setCancelable(true);
//
//                    Thread t = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            mStrServerResponse = InsertKycIntroducerInfo.insertIntroducerInfo(
//                                    mStrEncryptAccountNumber,
//                                    mStrEncryptPin,
//                                    mStrEncryptIntroducerName,
//                                    mStrEncryptIntroducerMobileNumber,
//                                    mStrEncryptIntroducerAddress,
//                                    mStrEncryptIntroducerOccupation,
//                                    mStrEncryptRemark,
//                                    mStrMasterKey);
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    try {
//                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                            mProgressDialog.dismiss();
//                                            if (mStrServerResponse.equalsIgnoreCase("Update")) {
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewIntroducerInfo.this);
//                                                myAlert.setMessage("Successfully save introducer info.");
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            } else {
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewIntroducerInfo.this);
//                                                myAlert.setMessage(mStrServerResponse);
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            }
//
//                                        }
//                                    } catch (Exception e) {
//                                        // TODO: handle exception
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                    t.start();
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
        }

    }


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();

            try {
                mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, GlobalData.getStrSessionId());
                mStrEncryptPin = encryption.Encrypt(mStrPin, GlobalData.getStrSessionId());
                mStrEncryptIntroducerName = encryption.Encrypt(mTextViewIntroducerName.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptIntroducerMobileNumber = encryption.Encrypt(mTextViewIntroducerMobileNumber.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptIntroducerAddress = encryption.Encrypt(mTextViewIntroducerAddress.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptIntroducerOccupation = encryption.Encrypt(mTextViewOccupation.getText().toString(), GlobalData.getStrSessionId());

                mStrEncryptRemark = encryption.Encrypt(mTextViewRemark.getText().toString(), GlobalData.getStrSessionId());

                strParamete=encryption.Encrypt("G", GlobalData.getStrSessionId());

                mStrServerResponse = InsertKyc.insertIntroducerInfo(
                        mStrEncryptAccountNumber,
                        mStrEncryptPin,mStrEncryptIntroducerName,
                        mStrEncryptIntroducerMobileNumber,
                        mStrEncryptIntroducerAddress,
                        mStrEncryptIntroducerOccupation,
                        mStrEncryptRemark,
                        strParamete);

                if (mStrServerResponse.equalsIgnoreCase("No Data Found")) {

                }
                else
                {
                    String[] parts = mStrServerResponse.split("\\*");
                    String strNomineeName = parts[0];
                    String strNomieeMobileNumber = parts[1];
                    String strNomieeRelation = parts[2];
                    String strNomieePercentage = parts[3];
                    String strRemarks = parts[4];
                    mTextViewIntroducerName.setText(strNomineeName);
                    mTextViewIntroducerMobileNumber.setText(strNomieeMobileNumber);
                    mTextViewIntroducerAddress.setText(strNomieeRelation);
                    mTextViewOccupation.setText(strNomieePercentage);
                    mTextViewRemark.setText(strRemarks);

                    GlobalData.setStrIntroducerName(strNomineeName);
                    GlobalData.setStrIntroducerPhNo(strNomieeMobileNumber);
                    GlobalData.setStrIntroducerAddress(strNomieeRelation);
                    GlobalData.setStrIntroducerOccupation(strNomieePercentage);
                    GlobalData.setStrIntroducerRemarks(strRemarks);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycPreviewIntroducerInfo.this);
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
//        mEditTextIntroducerName.setEnabled(true);
//        mEditTextIntroducerMobileNumber.setEnabled(true);
//        mEditTextIntroducerAddress.setEnabled(true);
//        mEditTextOccupation.setEnabled(true);
//        mEditTextRemark.setEnabled(true);
//        mBtnSaveIntroducerInfo.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextIntroducerName.setEnabled(false);
//        mEditTextIntroducerMobileNumber.setEnabled(false);
//        mEditTextIntroducerAddress.setEnabled(false);
//        mEditTextOccupation.setEnabled(false);
//        mEditTextRemark.setEnabled(false);
//        mBtnSaveIntroducerInfo.setEnabled(false);
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
                startActivity(new Intent(KycPreviewIntroducerInfo.this, Kyc.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycPreviewIntroducerInfo.this, Login.class)
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
            startActivity(new Intent(KycPreviewIntroducerInfo.this, Kyc.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}

