package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GenerateQrCodeNew;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class QrScan extends AppCompatActivity  implements View.OnClickListener {
    private ProgressDialog mProgressDialog = null;
    private TextView mTextViewAccountHolderFullName, mTextViewAccountNumber,
            mTextViewAccountHolderFullNameReverse, mTextViewAccountNumberReverse;
    private ImageView mImgView;
    private Bitmap mBitmapQrCode;
    private String mStrQrCodeContent,mStrEncryptAccountNumber,strName,strIsdefult,strAccountNo,strBankAccounStatus,
            strGETIsdefault,strGETbankStatus,strGetBankNAme;
    ImageButton btnShowOfflineQrCode;
    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();
    private EncryptionDecryption encryption = new EncryptionDecryption();

    private SharedPreferences msharePrefOfflinePin;
    private SharedPreferences.Editor mshareprefOfflinePINEditor;
    //#############################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);
        checkOs();
        initUI();
    }

    private void initUI() {
        mImgView = findViewById(R.id.imageViewPreviewQrCode);
        mTextViewAccountHolderFullName = findViewById(R.id.textViewAccountHolderFullName);
        mTextViewAccountNumber = findViewById(R.id.textViewAccountNumber);
        mTextViewAccountHolderFullNameReverse = findViewById(R.id.textViewAccountHolderFullNameReverse);
        mTextViewAccountNumberReverse = findViewById(R.id.textViewAccountNumberReverse);

        mStrQrCodeContent = GlobalData.getStrQrCodeContent();
        btnShowOfflineQrCode=findViewById(R.id.btnShowOfflineQrCode);
        btnShowOfflineQrCode.setOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try {
            mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrMasterKey());
            //===================================---------------------------------
            String strGetList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());

            if (strGetList.contains("&")) {

                StringTokenizer tokensTransaction = new StringTokenizer(strGetList, "&");
                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                    while (tokensTransaction.hasMoreElements()) {
                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                         strName = tokensAllTransactionFinal.nextToken();
                         strIsdefult = tokensAllTransactionFinal.nextToken();
                         strAccountNo = tokensAllTransactionFinal.nextToken();
                         strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                        String strIsVerified = tokensAllTransactionFinal.nextToken();
                        String strBankID = tokensAllTransactionFinal.nextToken();
                        String strBalance=tokensAllTransactionFinal.nextToken();
                        String strQRcode=tokensAllTransactionFinal.nextToken();

                        arrayAccountName.add(strName);
                        arrayAccountDefuly.add(strIsdefult);
                        arrayBankAccountNo.add(strAccountNo);
                        arrayBankAccountStatus.add(strBankAccounStatus);
                        arrayBankAccountIsVerified.add(strIsVerified);
                        arrayBankID.add(strBankID);
                        if(strAccountNo.equalsIgnoreCase(GlobalData.getStrWallet())) {
                            if (strIsdefult.equalsIgnoreCase("Y")) {
                                GlobalData.setStrWallet(strAccountNo);
                                String strSourceWallet = strAccountNo;
                                GlobalData.setStrAccountTypename(strName);
                                btnShowOfflineQrCode.setVisibility(View.VISIBLE);
                                strGETIsdefault=strIsdefult;
                                strGETbankStatus=strBankAccounStatus;
                                strGetBankNAme=strName;
                                // mBtnDefultAccount.setText("   Default A/C\n   (" + GlobalData.getStrAccountTypename() + ")");
                            } else {
                                btnShowOfflineQrCode.setVisibility(View.GONE);

                            }
                        }
                    }
                }
            } else {

            }
            //===================================================
            //============================================
        }
        catch (Exception ex)
        {

        }

        // Initialize progress dialog
        mProgressDialog = ProgressDialog.show(QrScan.this, null, "Loading...", false, true);
        // Cancel progress dialog on back key press
        mProgressDialog.setCancelable(true);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                mBitmapQrCode = GenerateQrCodeNew.generateQrCode(mStrQrCodeContent);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                                mImgView.setImageBitmap(mBitmapQrCode);
                                mTextViewAccountHolderFullName.setText(GlobalData.getStrAccountHolderName());
                                mTextViewAccountNumber.setText(GlobalData.getStrWallet());
                                mTextViewAccountNumberReverse.setText(GlobalData.getStrWallet());
                                mTextViewAccountHolderFullNameReverse.setText(GlobalData.getStrAccountHolderName());

                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        // update ui info ( show response message )
                    }
                });
            }
        });

        t.start();

    }

    public void onClick(View v)
    {
        if (v == btnShowOfflineQrCode)
        {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(QrScan.this);
            myAlert.setMessage("Do you want to save Offline QR?");
            myAlert.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            dialog.cancel();
                            //--------------save into share prefarence
                            msharePrefOfflinePin = getSharedPreferences("OfflinePINPrefs", MODE_PRIVATE);
                            mshareprefOfflinePINEditor = msharePrefOfflinePin.edit();
                            //SharedPreferences.Editor editor = myPrefrence.edit();
                            //  editor.putString("namePreferance", itemNAme);
                            mshareprefOfflinePINEditor.putString("offlineAccountName", mTextViewAccountHolderFullName.getText().toString());
                            mshareprefOfflinePINEditor.putString("offlineAccountNo", mTextViewAccountNumber.getText().toString());
                            mshareprefOfflinePINEditor.putString("offlineAccountStatus", strGETbankStatus);
                            mshareprefOfflinePINEditor.putString("offlineAccountQRCode", mStrQrCodeContent);
                            mshareprefOfflinePINEditor.putString("offlineAccountDefault", strGETIsdefault);
                            mshareprefOfflinePINEditor.putString("offlineAccountBankName", strGetBankNAme);

                            mshareprefOfflinePINEditor.commit();
                           // startActivity(new Intent(QrScan.this, GenerateOtp.class));
                        }
                    });
            myAlert.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = myAlert.create();
            alertDialog.show();


        }

    }

    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    //########################## Logout ############################
    //########################## Logout ############################
    //########################## Logout ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
        pinMenuItem.setTitle("Reward Point:678");
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(QrScan.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(QrScan.this, Login.class)
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
            //online
        } else {
            //offline
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(QrScan.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
