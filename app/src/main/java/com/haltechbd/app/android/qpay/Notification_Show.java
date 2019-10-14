package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CustomListForNotiifcation;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetAllFavoriteList;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Notification_Show extends AppCompatActivity {
    private ListView mListView;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    ArrayList<String> arrayListName = new ArrayList<>();
    ArrayList<String> arrayListAcc = new ArrayList<>();
    ArrayList<String> arrayListValidity = new ArrayList<>();
//--------------------
private ProgressDialog mProgressDialog = null;
ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountBalance = new ArrayList<String>();
    ArrayList<String> arrayQRCodelinkAccount = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();
    ArrayList<Bitmap>arrayimage=new ArrayList<Bitmap>();
    ImageView imgViewQR,imgprofile;

   // Bitmap bitImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        mListView = findViewById(R.id.listViewNotification);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        checkOs();
        checkInternet();

    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            LoadAccountList();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Notification_Show.this);
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
    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }
    public void LoadAccountList()
    {
        try
        {


            //========================Get Account list========================

              String  mStrEncryptAccountNumber=encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
              final String  mStrEncryptPin=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
              String strGetNotification= GetAllFavoriteList.GetNotification(mStrEncryptAccountNumber,mStrEncryptPin,GlobalData.getStrUserId(),GlobalData.getStrDeviceId());

            if (strGetNotification.equalsIgnoreCase("No Data Found"))
            {

            }
            else if(strGetNotification.equalsIgnoreCase("anyType{}"))
            {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(Notification_Show.this);
                myAlert.setMessage("No notification available" );
                myAlert.setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();
            }
            else
            {
                StringTokenizer tokensTransaction = new StringTokenizer(strGetNotification, "&");
                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                    while (tokensTransaction.hasMoreElements()) {


                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                        // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());
                        String strDate = tokensAllTransactionFinal.nextToken();
                        String strCatagories = tokensAllTransactionFinal.nextToken();
                        String strText = tokensAllTransactionFinal.nextToken();
                        String strNotifyID=tokensAllTransactionFinal.nextToken();
                        String strViewStatus=tokensAllTransactionFinal.nextToken();
                        arrayAccountName.add(strDate);
                        arrayAccountDefuly.add(strCatagories);
                        arrayBankAccountNo.add(strText);
                        arrayBankAccountBalance.add(strNotifyID);
                        arrayBankAccountStatus.add(strViewStatus);


                        //------------------------------------------
                        final CustomListForNotiifcation adapter = new CustomListForNotiifcation(Notification_Show.this,
                                arrayAccountName,
                                arrayAccountDefuly,
                                arrayBankAccountNo,arrayBankAccountBalance,arrayBankAccountStatus);

                        mListView.setAdapter(adapter);
//                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            public void onItemClick(AdapterView<?> parent, final View view,
//                                                    int position, long id) {
//
//                                try {
//                                    final String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
//                                    final String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//                                    final String strEncrpytedNotifyID=encryption.Encrypt(arrayBankAccountBalance.get(position),GlobalData.getStrSessionId());
//                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(view.getRootView().getContext());
//                                    myAlert.setMessage(arrayAccountDefuly.get(position) + "\n" + arrayBankAccountNo.get(position) + "\n" + arrayAccountName.get(position));
//                                    myAlert.setNegativeButton(
//                                            "OK",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                    String strUpdate = GetAllFavoriteList.UpdateNotification(mStrEncryptAccountNumber, mStrEncryptPin,strEncrpytedNotifyID , GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
//                                                    if(strUpdate.equalsIgnoreCase("Update"))
//                                                    {
//                                                       CardView crview=view.findViewById(R.id.card_root);
//                                                        crview.setCardBackgroundColor(Color.WHITE);
//
//
//                                                    }
//
//                                                }
//                                            });
//                                    AlertDialog alertDialog = myAlert.create();
//                                    alertDialog.show();
//
//                                }
//                                catch (Exception ex)
//                                {
//
//                                }
//
//                            }
//                        });

                    }

                }

            }
            //===================================================

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
                startActivity(new Intent(Notification_Show.this, MainActivity.class));
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Notification_Show.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
