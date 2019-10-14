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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class KycPreviewPersonalInfo extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;


    // initialize all ui components
    private TextView mTextViewDateOfBirth, mTextViewOccupation, mTextViewOrganization, mTextViewGender;
    private Button mBtnUpdatePersonalInfo;
    private TextView mTextViewShowServerResponse;
    private String mStrGender, mStrMasterKey, mStrEncryptAccountNumber, mStrEncryptPin,
            mStrEncryptDateOfBirth, mStrEncryptOccupation, mStrEncryptOrganizationName,
            mStrEncryptGender, mStrServerResponse, mStrCurrentDate;
    String strParamete,strDOB,ORG_name,Gender,Occupation;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_preview_personal_info);
        checkOs();
        // initialize all ui components
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mTextViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        mTextViewOccupation = findViewById(R.id.textViewOccupation);
        mTextViewOrganization = findViewById(R.id.textViewOrganization);
        mTextViewGender = findViewById(R.id.textViewGender);
        mBtnUpdatePersonalInfo = findViewById(R.id.btnKycUpdatePersonalInfo);
        mBtnUpdatePersonalInfo.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewKycUpdatePersonalInfoShowServerResponse);
        //mStrMasterKey = GlobalData.getStrMasterKey();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }


    @Override
    public void onClick(View v) {
        if (v == mBtnUpdatePersonalInfo) {

//            Intent intent = new Intent(KycPreviewPersonalInfo.this, KycUpdatePersonalInfo.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(KycPreviewPersonalInfo.this, KycUpdatePersonalInfo.class));
            finish();
           // startActivity(new Intent(KycPreviewPersonalInfo.this, KycUpdatePersonalInfo.class));
//            if (mEditTextDateOfBirth.getText().toString().length() == 0) {
//                mEditTextDateOfBirth.setError("Field cannot be empty");
//            } else if (mEditTextOccupation.getText().toString().length() == 0) {
//                mEditTextOccupation.setError("Field cannot be empty");
//            } else if (mEditTextOrganizationName.getText().toString().length() == 0) {
//                mEditTextOrganizationName.setError("Field cannot be empty");
//            } else {
////                disableUiComponentAfterClick();
//                try {
//                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), mStrMasterKey);
//                    mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), mStrMasterKey);
//                    mStrEncryptDateOfBirth = encryption.Encrypt(mEditTextDateOfBirth.getText().toString(), mStrMasterKey);
//                    mStrEncryptOccupation = encryption.Encrypt(mEditTextOccupation.getText().toString(), mStrMasterKey);
//                    mStrEncryptOrganizationName = encryption.Encrypt(mEditTextOrganizationName.getText().toString(), mStrMasterKey);
//                    mStrEncryptGender = encryption.Encrypt(mStrGender, mStrMasterKey);
//
//                    // Initialize progress dialog
//                    mProgressDialog = ProgressDialog.show(KycPreviewPersonalInfo.this, null, "Processing request...", false, true);
//                    // Cancel progress dialog on back key press
//                    mProgressDialog.setCancelable(true);
//
//                    Thread t = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
////                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
//                            insertPersonalInfo(
//                                    mStrEncryptAccountNumber,
//                                    mStrEncryptPin,
//                                    mStrEncryptDateOfBirth,
//                                    mStrEncryptOccupation,
//                                    mStrEncryptOrganizationName,
//                                    mStrEncryptGender,
//                                    mStrMasterKey
//                            );
////                            SystemClock.sleep(10000);
////                    } else {
////                        mTextViewShowServerResponse.setText("Session Expire, Please Login Again");
////                    }
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    try {
//                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                            mProgressDialog.dismiss();
//                                            //####################### Show Dialog ####################
//                                            //####################### Show Dialog ####################
//                                            //####################### Show Dialog ####################
//                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewPersonalInfo.this);
//                                            myAlert.setMessage(mStrServerResponse);
//                                            myAlert.setNegativeButton(
//                                                    "OK",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            dialog.cancel();
//                                                        }
//                                                    });
//                                            AlertDialog alertDialog = myAlert.create();
//                                            alertDialog.show();
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
        if (isNetworkConnected())
        {
            enableUiComponents();
            try {

                mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                mStrEncryptDateOfBirth = encryption.Encrypt(mTextViewDateOfBirth.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptOccupation = encryption.Encrypt(mTextViewOccupation.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptOrganizationName = encryption.Encrypt(mTextViewOrganization.getText().toString(), GlobalData.getStrSessionId());
                mStrEncryptGender = encryption.Encrypt(mTextViewGender.getText().toString(), GlobalData.getStrSessionId());
                strParamete=encryption.Encrypt("G", GlobalData.getStrSessionId());

                mProgressDialog = ProgressDialog.show(KycPreviewPersonalInfo.this, null, "Loading...", false, true);

                mProgressDialog.setCancelable(true);

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {

                // Initialize progress dialog
//                mProgressDialog = ProgressDialog.show(KycPreviewPersonalInfo.this, null, "Processing request...", false, true);
//                // Cancel progress dialog on back key press
//                mProgressDialog.setCancelable(true);
//
//                Thread t = new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
               //------------------------------
                String strGet= InsertKyc.insertPersonalInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptDateOfBirth,
                        mStrEncryptOccupation,
                        mStrEncryptOrganizationName,
                        mStrEncryptGender,
                        strParamete);

                //-----------------------------------------------
                        mStrServerResponse = strGet;
                        if (mStrServerResponse.equalsIgnoreCase("No Data Found")) {

                        } else {
                            // strDecryptDOB + "*" + strDecryptOccupation + "*" + strDecryptOrg_Name + "*" + strDecryptGender

                            String[] parts = mStrServerResponse.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                            strDOB = parts[0];//Login Successfully
                            Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                            ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                            Gender = parts[3];


                            GlobalData.setStrKYCPersonalInfoDOB(strDOB);
                            GlobalData.setStrKYCPersonalInfoOrg(ORG_name);
                            GlobalData.setStrKYCPersonalInfoGender(Gender);
                            GlobalData.setStrKYCPersonalInfoOccupation(Occupation);


                        }
                        //---------------------------------------

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                        //mTextViewShowServerResponse.setText(mStrServerResponse);
                                        mTextViewDateOfBirth.setText(strDOB);
                                        mTextViewOrganization.setText(ORG_name);
                                        mTextViewGender.setText(Gender);
                                        mTextViewOccupation.setText(Occupation);
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        });
                    }
                });

                t.start();
            }
            catch (Exception ex)
            {

            }
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycPreviewPersonalInfo.this);
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
//        mEditTextDateOfBirth.setEnabled(true);
//        mEditTextOccupation.setEnabled(true);
//        mEditTextOrganizationName.setEnabled(true);
//        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextDateOfBirth.setEnabled(false);
//        mEditTextOccupation.setEnabled(false);
//        mEditTextOrganizationName.setEnabled(false);
//        mBtnSubmit.setEnabled(false);
//        mBtnSubmit.setEnabled(false);
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
                startActivity(new Intent(KycPreviewPersonalInfo.this, Kyc.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycPreviewPersonalInfo.this, Login.class)
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

//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch (view.getId()) {
//            case R.id.radio_male:
//                if (checked)
//                    mStrGender = "M";
//                Toast.makeText(KycPreviewPersonalInfo.this, "Male", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.radio_female:
//                if (checked)
//                    mStrGender = "F";
//                Toast.makeText(KycPreviewPersonalInfo.this, "Female", Toast.LENGTH_LONG).show();
//                break;
//        }
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(KycPreviewPersonalInfo.this, Kyc.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}

