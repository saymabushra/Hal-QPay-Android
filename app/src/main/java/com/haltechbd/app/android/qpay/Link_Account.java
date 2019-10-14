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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetBalance;
import com.haltechbd.app.android.qpay.utils.GetQrCodeContent;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.MyCustomAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by bushra on 5/15/2018.
 */

public class Link_Account extends AppCompatActivity implements View.OnClickListener {
    private ListView mListView;
    private TextView mTextViewlinkAccountlist;
    LinearLayout linlaHeaderProgress;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;
    String mStrMasterKey,mStrEncryptAccountNumber,mStrServerResponse;
    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountBalance = new ArrayList<String>();
    ArrayList<String> arrayQRCodelinkAccount = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    MyCustomAdapter adapter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        checkOs();
        initUI();

        //---------- Add link Account ------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Intent intent = new Intent(Link_Account.this, Add_Link_Account.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                startActivity(new Intent(Link_Account.this, Add_Link_Account.class));
                finish();
              //  startActivity(new Intent(Link_Account.this, Add_Link_Account.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                // your action code will be here  add link account here
//                Toast.makeText(Link_Account.this, "button clicked: " , Toast.LENGTH_SHORT).show();
            }
        });
        //----------------------


    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {

        mListView = findViewById(R.id.listLinkAccount);
        mStrMasterKey = GlobalData.getStrMasterKey();

        checkInternet();

//        mProgressDialog = ProgressDialog.show(Link_Account.this, null, "Load data...", false, true);
//        // Cancel progress dialog on back key press
//        mProgressDialog.setCancelable(true);
//
//        Thread t = new Thread(new Runnable() {
//
//            @Override
//            public void run() {

               LoadAccountList();

//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        try {
//                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                mProgressDialog.dismiss();
//
//                            }
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                        }
//                    }
//                });
//            }
//        });
//
//        t.start();

    }



    @Override
    public void onClick(View v) {


    }


    public void LoadAccountList()
    {
        try
        {
            //========================Get Account list========================
            mStrEncryptAccountNumber=encryption.Encrypt(GlobalData.getStrAccountNumber(),GlobalData.getStrSessionId());
            String strGetAccountList= CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber,GlobalData.getStrUserId(),GlobalData.getStrDeviceId());

            if(strGetAccountList.contains("&")) {
                StringTokenizer tokensTransaction = new StringTokenizer(strGetAccountList, "&");
                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                    while (tokensTransaction.hasMoreElements()) {


                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                        // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());
                        String strName = tokensAllTransactionFinal.nextToken();
                        String strIsdefult = tokensAllTransactionFinal.nextToken();
                        String strAccountNo = tokensAllTransactionFinal.nextToken();
                        String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                        String strIsVerified=tokensAllTransactionFinal.nextToken();

                        arrayAccountName.add(strName);
                        arrayAccountDefuly.add(strIsdefult);
                        arrayBankAccountNo.add(strAccountNo);
                        arrayBankAccountStatus.add(strBankAccounStatus);
                        arrayBankAccountIsVerified.add(strIsVerified);
                        //------------ get balance and QR code -------------------
//                        String strEncWallet = null;
//                        try {
//                            strEncWallet = encryption.Encrypt(strAccountNo, GlobalData.getStrMasterKey());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        String strVoucherBalance = GetBalance.getBalance(GlobalData.getStrUserId(), GlobalData.getStrEncryptPin(), strEncWallet, GlobalData.getStrDeviceId());
//                        String mStrQrCodeContent = GetQrCodeContent.getQrCode(strEncWallet, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
//                        if (strVoucherBalance.equalsIgnoreCase("")) {
//                            strVoucherBalance = "0";
//                        }
//                        if (mStrQrCodeContent.equalsIgnoreCase("")) {
//                            mStrQrCodeContent = "data not found";
//                        }
//                        arrayBankAccountBalance.add(strVoucherBalance);
//                        arrayQRCodelinkAccount.add(mStrQrCodeContent);
//
                    }
                }
            }
            else
            {

            }
            //===================================================

            adapter = new MyCustomAdapter(arrayAccountName, this);

            mProgressDialog = ProgressDialog.show(Link_Account.this, null, "Load data...", false, true);

        mProgressDialog.setCancelable(true);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                mListView.setAdapter(adapter);
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
//                onBackPressed();
                startActivity(new Intent(Link_Account.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(Link_Account.this, Login.class)
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
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Link_Account.this);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Link_Account.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
