package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetDistrict;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by bushra on 5/17/2018.
 */

public class Add_Link_Account extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mProgressDialog = null;
    private EncryptionDecryption encryptDecrypt = new EncryptionDecryption();
    String mStrMasterKey, mStrEncryptAccountNumber, mStrServerResponse, mStrAccountNumber, mStrPin, mStrEncryptPin, mStrDistirctId, mStrBankId, StrSelcetDistrictID, StrSelectBankID, mStrBankName;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private TextView txtViewSalary, txtViewCredit, txtviewAddLinkAccount, mTextViewShowServerResponse;
    CardView cardViewSalary, CardViewCredit;
    ImageButton imgbtnAddSalay, imgBtnAddCredit;
    Button mBtnSubmit;
    LinearLayout linerlayoutlAddOwnAcc, linerlayoutHeadtext, linerlayoutSalay, linerlayoutCredit;
    ArrayList<String> arrayListDistrictId = new ArrayList<String>();
    ArrayList<String> arrayListDistrictName = new ArrayList<String>();
    ArrayList<String> arrayListBankId = new ArrayList<String>();
    ArrayList<String> arrayListBankCBCode = new ArrayList<String>();
    ArrayList<String> arrayListBankName = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();
    private Spinner mSpinnerDistrict, mSpinnerBank;
    private EditText mEditTextBranch, mEdittxtOtherAccountNo;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_link_account_new);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkOs();
        initUI();
    }

    private void initUI() {
        // txtViewSalary = (TextView) findViewById(R.id.txtviewaddSalary);
        // txtViewCredit = (TextView) findViewById(R.id.txtViewAddCredit);
        txtviewAddLinkAccount = (TextView) findViewById(R.id.txtviewAddLinkAccount);
        imgbtnAddSalay = (ImageButton) findViewById(R.id.imgBtnSalaryWalletAddlink);
        imgBtnAddCredit = (ImageButton) findViewById(R.id.imgBtnCreditWalletAddlink);
        // cardViewSalary = (CardView) findViewById(R.id.cardViewAddSalary);
        // CardViewCredit = (CardView) findViewById(R.id.cardViewAddCredit);
        linerlayoutlAddOwnAcc = (LinearLayout) findViewById(R.id.LinerActiveOnwAccountAddlinkAcc);
        linerlayoutHeadtext = (LinearLayout) findViewById(R.id.linerlayoutHeadtext);

        linerlayoutSalay = (LinearLayout) findViewById(R.id.linerSalary);
        linerlayoutCredit = (LinearLayout) findViewById(R.id.linerCredit);

        mSpinnerDistrict = findViewById(R.id.spinnerDistrictAddlinkAccount);
        mSpinnerBank = findViewById(R.id.spinnerBankAddLinkAccount);
        mEditTextBranch = findViewById(R.id.editTextAddLinkAccountBranchName);
        mEdittxtOtherAccountNo = findViewById(R.id.editTextAddLinkOtherAccAccountNo);

        mBtnSubmit = findViewById(R.id.btnAddlinkAccount);

        mTextViewShowServerResponse = findViewById(R.id.txtViewAddLinkAccountShowServerResponse);

        mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();

        mBtnSubmit.setOnClickListener(this);
        imgbtnAddSalay.setOnClickListener(this);
        imgBtnAddCredit.setOnClickListener(this);

        checkInternet();
        LoadAccountList();
//Load district
        /// load bank

    }

    public void LoadAccountList() {


        try {
            if (GlobalData.getStrPackage().equalsIgnoreCase("1205190003")) { // customer
                ////////////////////////////////////////////////////////
                ArrayList<String> arrayAccountName = new ArrayList<String>();
                ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
                ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
//            ArrayList<String> arrayBankAccountBalance = new ArrayList<String>();
//            ArrayList<String> arrayQRCodelinkAccount = new ArrayList<String>();
                ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
                ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
                //========================Get Account list========================
                mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String strGetAccountList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
                // String strGetAccountList =GlobalData.getStrGetAccountList();
                // GlobalData.setStrGetAccountList(strGetAccountList);
                GlobalData.setStrGetAccountList(strGetAccountList);
                if (strGetAccountList.contains("*")) {
                    StringTokenizer tokensTransaction = new StringTokenizer(strGetAccountList, "*");
                    for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                        while (tokensTransaction.hasMoreElements()) {


                            StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                            // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());
                            String strName = tokensAllTransactionFinal.nextToken();
                            String strIsdefult = tokensAllTransactionFinal.nextToken();
                            String strAccountNo = tokensAllTransactionFinal.nextToken();
                            String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                            String strIsVerified = tokensAllTransactionFinal.nextToken();
                            String strBankID = tokensAllTransactionFinal.nextToken();
                            String strBalance = tokensAllTransactionFinal.nextToken();
                            String strQRcode = tokensAllTransactionFinal.nextToken();

                            arrayAccountName.add(strName);
                            arrayAccountDefuly.add(strIsdefult);
                            arrayBankAccountNo.add(strAccountNo);
                            arrayBankAccountStatus.add(strBankAccounStatus);
                            arrayBankAccountIsVerified.add(strIsVerified);
                            arrayBankID.add(strBankID);
                            if (strAccountNo.equalsIgnoreCase(GlobalData.getStrAccountNumber() + "2")) {
                                linerlayoutSalay.setVisibility(View.GONE);
                                imgbtnAddSalay.setVisibility(View.GONE);
                            } else if (strAccountNo.equalsIgnoreCase(GlobalData.getStrAccountNumber() + "3")) {
                                linerlayoutCredit.setVisibility(View.GONE);
                                imgBtnAddCredit.setVisibility(View.GONE);
                            }


                        }
                    }
                    int intAccLsitsize = arrayBankAccountNo.size();
                    if (intAccLsitsize == 1) {
                        String strVale = arrayBankAccountNo.get(0);
                        if (strVale.equalsIgnoreCase(GlobalData.getStrWallet())) {
                            linerlayoutSalay.setVisibility(View.VISIBLE);
                            linerlayoutCredit.setVisibility(View.VISIBLE);
                            imgbtnAddSalay.setVisibility(View.VISIBLE);
                            imgBtnAddCredit.setVisibility(View.VISIBLE);
                            linerlayoutHeadtext.setVisibility(View.VISIBLE);
                            // txtviewAddLinkAccount.setVisibility(View.VISIBLE);
                            //txtviewAddLinkAccount.setText("Please add a bank Account to make money in Your QPAY Account.");
                        } else {
                            linerlayoutHeadtext.setVisibility(View.GONE);

                            //txtviewAddLinkAccount.setVisibility(View.GONE);
                        }
                    } else {
                        String strSalary = "";
                        String strCredit = "";
                        for (int i = 0; i <= intAccLsitsize - 1; i++) {
                            if (arrayBankAccountNo.get(i).equalsIgnoreCase(GlobalData.getStrAccountNumber() + "2")) {
                                strSalary = arrayBankAccountNo.get(i);
                            } else if (arrayBankAccountNo.get(i).equalsIgnoreCase(GlobalData.getStrAccountNumber() + "3")) {
                                strCredit = arrayBankAccountNo.get(i);
                            }
                        }
                        if (strSalary != "" && strCredit != "") {
                            linerlayoutlAddOwnAcc.setVisibility(View.GONE);
                        }
                        linerlayoutHeadtext.setVisibility(View.GONE);
                    }
                } else {

                    linerlayoutSalay.setVisibility(View.VISIBLE);
                    linerlayoutCredit.setVisibility(View.VISIBLE);
                    imgbtnAddSalay.setVisibility(View.VISIBLE);
                    imgBtnAddCredit.setVisibility(View.VISIBLE);
                    linerlayoutHeadtext.setVisibility(View.VISIBLE);
//                txtviewAddLinkAccount.setVisibility(View.VISIBLE);
//                txtviewAddLinkAccount.setText("Please add a bank Account to make money in your QPAY Account.");
                }
//////////////////////////////////////////////////////////////////////////////////////
            }
            else
            {
                linerlayoutSalay.setVisibility(View.GONE);
                linerlayoutCredit.setVisibility(View.GONE);
                imgbtnAddSalay.setVisibility(View.GONE);
                imgBtnAddCredit.setVisibility(View.GONE);
                linerlayoutHeadtext.setVisibility(View.GONE);
                linerlayoutlAddOwnAcc.setVisibility(View.GONE);
            }

        } catch (Exception ex) {

            ex.getMessage();
        }
    }


    private void loadSpinnerBank() {
        arrayListBankId.clear();
        arrayListBankName.clear();
        arrayListBankCBCode.clear();
        String str = GetDistrict.getBank(mStrEncryptAccountNumber);
        if (str != null && !str.isEmpty()) {
            StringTokenizer strToken = new StringTokenizer(str, "&");
            ArrayList<String> arrayListBankIdAndName = new ArrayList<String>();
            for (int j = 0; j <= strToken.countTokens(); j++) {
                while (strToken.hasMoreElements()) {
                    arrayListBankIdAndName.add(strToken.nextToken());
                }
            }
            for (int i = 0; i <= arrayListBankIdAndName.size() - 1; i++) {
                StringTokenizer tokenDistrictIdAndName = new StringTokenizer(arrayListBankIdAndName.get(i), "*");
                arrayListBankName.add(tokenDistrictIdAndName.nextToken());
                arrayListBankCBCode.add(tokenDistrictIdAndName.nextToken());
                arrayListBankId.add(tokenDistrictIdAndName.nextToken());
                if (arrayListBankName.get(i).equalsIgnoreCase("QPAY")) {
                    StrSelectBankID = arrayListBankId.get(i);
                }
            }
        } else {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Add_Link_Account.this);
            mAlertDialogBuilder.setMessage("No Bank Found.");
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

    private void loadSpinnerDistrict() {
        arrayListDistrictName.clear();
        arrayListDistrictId.clear();
        String str = GetDistrict.getDistrict(mStrEncryptAccountNumber, mStrEncryptPin);
        if (str != null && !str.isEmpty()) {
            StringTokenizer strToken = new StringTokenizer(str, "&");
            ArrayList<String> arrayListDistrictIdAndName = new ArrayList<String>();
            for (int j = 0; j <= strToken.countTokens(); j++) {
                while (strToken.hasMoreElements()) {
                    arrayListDistrictIdAndName.add(strToken.nextToken());
                }
            }
            for (int i = 0; i <= arrayListDistrictIdAndName.size() - 1; i++) {
                StringTokenizer tokenDistrictIdAndName = new StringTokenizer(arrayListDistrictIdAndName.get(i), "*");
                arrayListDistrictId.add(tokenDistrictIdAndName.nextToken());
                arrayListDistrictName.add(tokenDistrictIdAndName.nextToken());
                if (arrayListDistrictName.get(i).equalsIgnoreCase("Dhaka")) {
                    StrSelcetDistrictID = arrayListDistrictId.get(i);
                }
            }
        } else {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Add_Link_Account.this);
            mAlertDialogBuilder.setMessage("No District Found.");
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

    public void onClick(View v) {


        if (v == imgbtnAddSalay) {


            mProgressDialog = ProgressDialog.show(Add_Link_Account.this, null, "Active Salary Account...", false, true);
            // Cancel progress dialog on back key press
            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {


                    mStrServerResponse = CheckDeviceActiveStatus.AddLinkAccount(GlobalData.getStrAccountNumber(), StrSelectBankID, "S", GlobalData.getStrAccountNumber(), "Salary Wallet", StrSelcetDistrictID, "QPAY");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                    int intIndex = mStrServerResponse.indexOf("successfully");
                                    if (intIndex == -1) {
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Add_Link_Account.this);
                                       if(mStrServerResponse.equalsIgnoreCase(""))
                                       {
                                           mStrServerResponse="Request on Process.Please wait";


                                       }
                                        myAlert.setMessage(mStrServerResponse);
                                        myAlert.setNegativeButton(
                                                "Close",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();

                                                    }
                                                });
                                        AlertDialog alertDialog = myAlert.create();
                                        alertDialog.show();
                                    } else {
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Add_Link_Account.this);
                                        myAlert.setMessage(mStrServerResponse);
                                        myAlert.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
//                                                        Intent intent = new Intent(Add_Link_Account.this, Link_Account.class);
//                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                        startActivity(intent);
//                                                       try {
//                                                           mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
//                                                           String strGetAccountList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
//                                                           GlobalData.setStrGetAccountList(strGetAccountList);
//                                                       }
//                                                       catch (Exception ex)
//                                                       {
//
//                                                       }
                                                        startActivity(new Intent(Add_Link_Account.this, Link_Account_New_V2.class));
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
//---------------------------------------
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
            LoadAccountList();

        }
        if (v == imgBtnAddCredit) {

            mProgressDialog = ProgressDialog.show(Add_Link_Account.this, null, "Active Credit Account...", false, true);
            // Cancel progress dialog on back key press
            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {


                    mStrServerResponse = CheckDeviceActiveStatus.AddLinkAccount(GlobalData.getStrAccountNumber(), StrSelectBankID, "C", GlobalData.getStrAccountNumber(), "Credit Wallet", StrSelcetDistrictID, "QPAY");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                    int intIndex = mStrServerResponse.indexOf("successfully");
                                    if (intIndex == -1) {
                                        if(mStrServerResponse.equalsIgnoreCase(""))
                                        {
                                            mStrServerResponse="Request on Process.Please wait";


                                        }
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Add_Link_Account.this);
                                        myAlert.setMessage(mStrServerResponse);
                                        myAlert.setNegativeButton(
                                                "Close",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();


                                                    }
                                                });
                                        AlertDialog alertDialog = myAlert.create();
                                        alertDialog.show();
                                    } else {
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Add_Link_Account.this);
                                        myAlert.setMessage(mStrServerResponse);
                                        myAlert.setNegativeButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
//                                                        Intent intent = new Intent(Add_Link_Account.this, Link_Account.class);
//                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                        startActivity(intent);

                                                        startActivity(new Intent(Add_Link_Account.this, Link_Account_New_V2.class));
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

            ///////////////////////////
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
            LoadAccountList();
        }


        if (v == mBtnSubmit) {

            if (mEdittxtOtherAccountNo.getText().toString().length() == 0) {
                mEdittxtOtherAccountNo.setError("Field cannot be empty");
            } else if (mEditTextBranch.getText().toString().length() == 0) {
                mEditTextBranch.setError("Field cannot be empty");
            } else {

                mProgressDialog = ProgressDialog.show(Add_Link_Account.this, null, "Add Bank Account...", false, true);
                // Cancel progress dialog on back key press
                mProgressDialog.setCancelable(true);

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {


                        mStrServerResponse = CheckDeviceActiveStatus.AddLinkAccount(GlobalData.getStrAccountNumber(), mStrBankId, "O", mEdittxtOtherAccountNo.getText().toString(), mStrBankName, mStrDistirctId, mEditTextBranch.getText().toString());

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                        int intIndex = mStrServerResponse.indexOf("successfully");
                                        if (intIndex == -1) {
                                            if(mStrServerResponse.equalsIgnoreCase(""))
                                            {
                                                mStrServerResponse="Request on Process.Please wait";


                                            }
                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Add_Link_Account.this);
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
                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Add_Link_Account.this);
                                            myAlert.setMessage(mStrServerResponse);
                                            myAlert.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            mEdittxtOtherAccountNo.setText("");
                                                            mEditTextBranch.setText("");
//                                                            Intent intent = new Intent(Add_Link_Account.this, Link_Account.class);
//                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                            startActivity(intent);

                                                            startActivity(new Intent(Add_Link_Account.this, Link_Account_New_V2.class));
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

//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
                LoadAccountList();
            }
//---------------------------------------


        }

        LoadAccountList();
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);

    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    //########################## Back ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:678");
        return true;
    }

    //########################## Logout ############################
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Add_Link_Account.this, Link_Account_New_V2.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(Add_Link_Account.this, Login.class)
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

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            try {
                mStrEncryptAccountNumber = encryptDecrypt.Encrypt(mStrAccountNumber, GlobalData.getStrSessionId());
                mStrEncryptPin = encryptDecrypt.Encrypt(mStrPin, GlobalData.getStrSessionId());

                // load bank -----------------
                loadSpinnerBank();
                ArrayAdapter<String> adapterBankName = new ArrayAdapter<String>(Add_Link_Account.this,
                        android.R.layout.simple_spinner_item, arrayListBankName);
                adapterBankName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerBank.setAdapter(adapterBankName);
                mSpinnerBank.setOnItemSelectedListener(onItemSelectedListenerForBank);

                //--------------- load distrcit------------
                loadSpinnerDistrict();
                ArrayAdapter<String> adapterDistrictName = new ArrayAdapter<String>(Add_Link_Account.this,
                        android.R.layout.simple_spinner_item, arrayListDistrictName);
                adapterDistrictName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerDistrict.setAdapter(adapterDistrictName);
                mSpinnerDistrict.setOnItemSelectedListener(onItemSelectedListenerForDistrict);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Add_Link_Account.this);
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
//        mEditTextNumberOfTransaction.setEnabled(true);
//        mEditTextOtpDuration.setEnabled(true);
//        mBtnGenerateOtp.setEnabled(true);
//        mBtnSaveOtp.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextNumberOfTransaction.setEnabled(false);
//        mEditTextOtpDuration.setEnabled(false);
//        mBtnGenerateOtp.setEnabled(false);
//        mBtnSaveOtp.setEnabled(false);
    }

    AdapterView.OnItemSelectedListener onItemSelectedListenerForDistrict = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mStrDistirctId = String.valueOf(arrayListDistrictId.get(position));
//            if (mStrDistirctId != null && !mStrDistirctId.isEmpty()) {
//                try {
//                    mStrEncryptDistrictId = encryption.Encrypt(mStrDistirctId, mStrMasterKey);
//                    loadSpinnerThana();
//                    ArrayAdapter<String> adapterThanaName = new ArrayAdapter<String>(Add_Link_Account.this,
//                            android.R.layout.simple_spinner_item, arrayListThana);
//                    adapterThanaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    mSpinnerThana.setAdapter(adapterThanaName);
//                    mSpinnerThana.setOnItemSelectedListener(onItemSelectedListenerForThana);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedListenerForBank = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mStrBankId = String.valueOf(arrayListBankId.get(position));
            mStrBankName = String.valueOf(arrayListBankName.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Add_Link_Account.this, Link_Account_New_V2.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
