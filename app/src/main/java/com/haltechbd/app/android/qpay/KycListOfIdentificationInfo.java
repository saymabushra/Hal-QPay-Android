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
import android.widget.ListView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetBalance;
import com.haltechbd.app.android.qpay.utils.GetQrCodeContent;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;
import com.haltechbd.app.android.qpay.utils.MyCustomAdapterForIdentification;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class KycListOfIdentificationInfo extends AppCompatActivity implements View.OnClickListener {
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private TextView mTextViewDocumentType, mTextViewIdentificationNumber, mTextViewRemark;
    private Button mBtnUpdate;
    private TextView mTextViewShowServerResponse;
    ListView mListMenuServices;
    private String mStrMasterKey, mStrAccountNumber, mStrPin,
            mStrEncryptAccountNumber, mStrEncryptPin, mStrIdentificationId,
            mStrEncryptIdentificationId, mStrEncryptIdentificationNumber, mStrEncryptRemark,
            mStrServerResponse;
    ArrayList<String> arrayAccountName = new ArrayList<String>();
    MyCustomAdapterForIdentification adapter;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identificaiton_document);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mListMenuServices = (ListView) findViewById(R.id.kycIdentificationListview);
//        mTextViewDocumentType = findViewById(R.id.textViewDocumentType);
//        mTextViewIdentificationNumber = findViewById(R.id.textViewIdentificationNumber);
//        mTextViewRemark = findViewById(R.id.textViewRemark);
//        mBtnUpdate = findViewById(R.id.btnKycUpdateIndentificationInfo);
//        mBtnUpdate.setOnClickListener(this);
//        mTextViewShowServerResponse = findViewById(R.id.textViewKycIndentificationInfoShowServerResponse);

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
            startActivity(new Intent(KycListOfIdentificationInfo.this, KycUpdateIdentificationInfo.class));
            finish();
//            if (mEditTextIdentificationNumber.getText().toString().length() == 0) {
//                mEditTextIdentificationNumber.setError("Field cannot be empty");
//            } else if (mEditTextRemark.getText().toString().length() == 0) {
//                mEditTextRemark.setError("Field cannot be empty");
//            } else {
////                disableUiComponentAfterClick();
//
//                try {
//                    mStrEncryptIdentificationId = encryption.Encrypt(mStrIdentificationId, mStrMasterKey);
//                    mStrEncryptIdentificationNumber = encryption.Encrypt(mEditTextIdentificationNumber.getText().toString(), mStrMasterKey);
//                    mStrEncryptRemark = encryption.Encrypt(mEditTextRemark.getText().toString(), mStrMasterKey);
//
//                    // Initialize progress dialog
//                    mProgressDialog = ProgressDialog.show(KycPreviewIdentificationInfo.this, null, "Processing request...", false, true);
//                    // Cancel progress dialog on back key press
//                    mProgressDialog.setCancelable(true);
//
//                    Thread t = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            mStrServerResponse = InsertKycIdentificationInfo.insertIndentificationInfo(
//                                    mStrEncryptAccountNumber,
//                                    mStrEncryptPin,
//                                    mStrEncryptIdentificationId,
//                                    mStrEncryptIdentificationNumber,
//                                    mStrEncryptRemark,
//                                    mStrMasterKey);
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    try {
//                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                            mProgressDialog.dismiss();
//                                            if(mStrServerResponse.equalsIgnoreCase("Update")){
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewIdentificationInfo.this);
//                                                myAlert.setMessage("Successfully save identification info.");
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            }else{
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycPreviewIdentificationInfo.this);
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
            LoadAccountList();
//            try {
//                mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, mStrMasterKey);
//                mStrEncryptPin = encryption.Encrypt(mStrPin, mStrMasterKey);
//                mStrServerResponse = GetKycIdentificationInfo.getIdentificationInfo(
//                        mStrEncryptAccountNumber,
//                        mStrEncryptPin,
//                        mStrMasterKey);
//                loadSpinner();
//                ArrayAdapter<String> adapterDistrictName = new ArrayAdapter<String>(KycPreviewIdentificationInfo.this,
//                        android.R.layout.simple_spinner_item, arrayListIdentification);
//                adapterDistrictName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mSpinnerIdentificationDocType.setAdapter(adapterDistrictName);
//                mSpinnerIdentificationDocType.setOnItemSelectedListener(onItemSelectedListenerForIdentificationDocType);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycListOfIdentificationInfo.this);
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


    public void LoadAccountList()
    {
        try
        {

            //========================Get Account list========================
            mStrEncryptAccountNumber=encryption.Encrypt(GlobalData.getStrAccountNumber(),GlobalData.getStrSessionId());
            mStrEncryptPin=encryption.Encrypt(mStrPin,GlobalData.getStrSessionId());
            String strGetAccountList= InsertKyc.getIdentificationInfo(mStrEncryptAccountNumber,mStrEncryptPin);

            if(strGetAccountList.contains("&")) {
                StringTokenizer tokensTransaction = new StringTokenizer(strGetAccountList, "&");
                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                    while (tokensTransaction.hasMoreElements()) {


                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), "*");
                        // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());

                        String strIdentificationiD= tokensAllTransactionFinal.nextToken();
                        String strIdentificationName = tokensAllTransactionFinal.nextToken();

                        arrayAccountName.add(strIdentificationName);
                       // arrayAccountDefuly.add(strIsdefult);

//
                    }
                }
            }
            else
            {

            }
            //===================================================

            //arrayAccountName.add("Profile Picture");
//            arrayAccountName.add("National ID");
//            arrayAccountName.add("Driving Licencse");
//            arrayAccountName.add("Passport");
//            arrayAccountName.add("Birth certificate");


            adapter = new MyCustomAdapterForIdentification(arrayAccountName, this);

            mProgressDialog = ProgressDialog.show(KycListOfIdentificationInfo.this, null, "Load data...", false, true);

            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {

                    mListMenuServices.setAdapter(adapter);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();

                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    });
                }
            });

            t.start();
            /////////////////////////////////

        }
        catch (Exception ex)
        {

        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void enableUiComponents() {
//        mEditTextIdentificationNumber.setEnabled(true);
//        mEditTextRemark.setEnabled(true);
//        mBtnSaveIdentificationInfo.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextIdentificationNumber.setEnabled(false);
//        mEditTextRemark.setEnabled(false);
//        mBtnSaveIdentificationInfo.setEnabled(false);
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
                startActivity(new Intent(KycListOfIdentificationInfo.this, Kyc.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycPreviewIdentificationInfo.this, Login.class)
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
            startActivity(new Intent(KycListOfIdentificationInfo.this, Kyc.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
