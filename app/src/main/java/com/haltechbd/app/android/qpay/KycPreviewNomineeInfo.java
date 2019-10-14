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

public class KycPreviewNomineeInfo extends AppCompatActivity implements View.OnClickListener {
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private TextView mTextViewNomineeName, mTextViewNomineeMobileNumber,
            mTextViewRelation, mTextViewPercent, mTextViewRemark;
    private Button mBtnUpdate;
    private TextView mTextViewShowServerResponse;
    private String mStrAccountNumber, mStrPin, mStrMasterKey,
            mStrEncryptAccountNumber, mStrEncryptPin,
            mStrEncryptNomineeName, mStrEncryptNomineeMobileNumber, mStrEncryptRelation,
            mStrEncryptPercent, mStrEncryptRemark, mStrServerResponse,strParamete;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_preview_nominee_info);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mTextViewNomineeName = findViewById(R.id.textViewNomineeName);
        mTextViewNomineeMobileNumber = findViewById(R.id.textViewMobileNumber);
        mTextViewRelation = findViewById(R.id.textViewRelation);
        mTextViewPercent = findViewById(R.id.textViewPercent);
        mTextViewRemark = findViewById(R.id.textViewRemark);
        mBtnUpdate = findViewById(R.id.btnKycUpdateNomineeInfo);
        mBtnUpdate.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewKycUpdateNomineeInfoShowServerResponse);

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
            startActivity(new Intent(KycPreviewNomineeInfo.this, KycUpdateNomineeInfo.class));
            finish();
//            if (mEditTextNomineeName.getText().toString().length() == 0) {
//                mEditTextNomineeName.setError("Field cannot be empty");
//            } else if (mEditTextNomineeMobileNumber.getText().toString().length() == 0) {
//                mEditTextNomineeMobileNumber.setError("Field cannot be empty");
//            } else if (mEditTextNomineeMobileNumber.getText().toString().length() < 11) {
//                mEditTextNomineeMobileNumber.setError("Must be 11 characters in length");
//            } else if (mEditTextRelation.getText().toString().length() == 0) {
//                mEditTextRelation.setError("Field cannot be empty");
//            } else if (mEditTextPercent.getText().toString().length() == 0) {
//                mEditTextPercent.setError("Field cannot be empty");
//            } else if (mEditTextRemark.getText().toString().length() == 0) {
//                mEditTextRemark.setError("Field cannot be empty");
//            } else {
////                disableUiComponentAfterClick();
//                try {
//                    mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, mStrMasterKey);
//                    mStrEncryptPin = encryption.Encrypt(mStrPin, mStrMasterKey);
//                    mStrEncryptNomineeName = encryption.Encrypt(mEditTextNomineeName.getText().toString(), mStrMasterKey);
//                    mStrEncryptNomineeMobileNumber = encryption.Encrypt(mEditTextNomineeMobileNumber.getText().toString(), mStrMasterKey);
//                    mStrEncryptRelation = encryption.Encrypt(mEditTextRelation.getText().toString(), mStrMasterKey);
//                    mStrEncryptPercent = encryption.Encrypt(mEditTextPercent.getText().toString(), mStrMasterKey);
//                    mStrEncryptRemark = encryption.Encrypt(mEditTextRemark.getText().toString(), mStrMasterKey);
//
//                    // Initialize progress dialog
//                    mProgressDialog = ProgressDialog.show(KycPreviewNomineeInfo.this, null, "Processing request...", false, true);
//                    // Cancel progress dialog on back key press
//                    mProgressDialog.setCancelable(true);
//
//                    Thread t = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            mStrServerResponse = InsertKycNomineeInfo.insertNomineeInfo(
//                                    mStrEncryptAccountNumber,
//                                    mStrEncryptPin,
//                                    mStrEncryptNomineeName,
//                                    mStrEncryptNomineeMobileNumber,
//                                    mStrEncryptRelation,
//                                    mStrEncryptPercent,
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
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewNomineeInfo.this);
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
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewNomineeInfo.this);
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

                mStrEncryptNomineeName = encryption.Encrypt(mTextViewNomineeName.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptNomineeMobileNumber = encryption.Encrypt(mTextViewNomineeMobileNumber.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptRelation = encryption.Encrypt(mTextViewRelation.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptPercent = encryption.Encrypt(mTextViewPercent.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptRemark = encryption.Encrypt(mTextViewRemark.getText().toString(), GlobalData.getStrSessionId());

                strParamete=encryption.Encrypt("G", GlobalData.getStrSessionId());

                mStrServerResponse = InsertKyc.insertNomineeInfo(
                        mStrEncryptAccountNumber,
                        mStrEncryptPin,mStrEncryptNomineeName,
                        mStrEncryptNomineeMobileNumber,
                        mStrEncryptRelation,
                        mStrEncryptPercent,
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
                    mTextViewNomineeName.setText(strNomineeName);
                    mTextViewNomineeMobileNumber.setText(strNomieeMobileNumber);
                    mTextViewRelation.setText(strNomieeRelation);
                    mTextViewPercent.setText(strNomieePercentage);
                    mTextViewRemark.setText(strRemarks);

                    GlobalData.setStrNomineeName(strNomineeName);
                    GlobalData.setStrNomineePhNo(strNomieeMobileNumber);
                    GlobalData.setStrNomineeRelation(strNomieeRelation);
                    GlobalData.setStrNomineePercentage(strNomieePercentage);
                    GlobalData.setStrNomineeRemarks(strRemarks);

//                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycPreviewContactInfo.this);
//                mAlertDialogBuilder.setMessage(mStrServerResponse);
//                mAlertDialogBuilder.setNegativeButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                mAlertDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycPreviewNomineeInfo.this);
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
//        mEditTextNomineeName.setEnabled(true);
//        mEditTextNomineeMobileNumber.setEnabled(true);
//        mEditTextRelation.setEnabled(true);
//        mEditTextPercent.setEnabled(true);
//        mEditTextRemark.setEnabled(true);
//        mBtnSaveNomineeInfo.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextNomineeName.setEnabled(false);
//        mEditTextNomineeMobileNumber.setEnabled(false);
//        mEditTextRelation.setEnabled(false);
//        mEditTextPercent.setEnabled(false);
//        mEditTextRemark.setEnabled(false);
//        mBtnSaveNomineeInfo.setEnabled(false);
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
                startActivity(new Intent(KycPreviewNomineeInfo.this, Kyc.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycPreviewNomineeInfo.this, Login.class)
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(KycPreviewNomineeInfo.this, Kyc.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
