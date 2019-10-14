package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.CustomListForLinkedAccout;
import com.haltechbd.app.android.qpay.utils.CustomListForLinkedAccout_V2;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetBalance;
import com.haltechbd.app.android.qpay.utils.GetQrCodeContent;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;
import com.haltechbd.app.android.qpay.utils.MyCustomAdapterBusinesscard;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.haltechbd.app.android.qpay.MainActivity.decodeBase64;

public class Link_Account_New_V2 extends AppCompatActivity {
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
        setContentView(R.layout.link_account_new);
        mListView = findViewById(R.id.listViewlinkacconut);
        mListView.setLongClickable(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        checkOs();
        checkInternet();

        //---------- Add link Account ------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Intent intent = new Intent(Link_Account.this, Add_Link_Account.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                startActivity(new Intent(Link_Account_New_V2.this, Add_Link_Account.class));
                finish();
                //  startActivity(new Intent(Link_Account.this, Add_Link_Account.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                // your action code will be here  add link account here
//                Toast.makeText(Link_Account.this, "button clicked: " , Toast.LENGTH_SHORT).show();
            }
        });
        //----------------------



//        StringTokenizer tokenAll = new StringTokenizer("Name1*Acc1*Valid1&Name2*Acc2*Valid2&Name3*Acc3*Valid3", "&");
//        for (int j = 0; j <= tokenAll.countTokens(); j++) {
//            while (tokenAll.hasMoreElements()) {
//                StringTokenizer tokenSingle = new StringTokenizer(tokenAll.nextToken(), "*");
//                String strName = tokenSingle.nextToken();
//                String strAcc = tokenSingle.nextToken();
//                String strValidity = tokenSingle.nextToken();
//                arrayListName.add(strName);
//                arrayListAcc.add(strAcc);
//                arrayListValidity.add(strValidity);
//                CustomListForLinkedAccout_V2 adapter = new CustomListForLinkedAccout_V2(Link_Account_New_V2.this,
//                        arrayListName,
//                        arrayListAcc,
//                        arrayListValidity);
//                mListView.setAdapter(adapter);
//                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        TextView textView01 = view.findViewById(R.id.txt01);
//                        TextView textView02 = view.findViewById(R.id.txt02);
//                        TextView textView03 = view.findViewById(R.id.txt03);
//
//                        String str01 = textView01.getText().toString();
//                        String str02 = textView02.getText().toString();
//                        String str03 = textView03.getText().toString();
//
//                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Link_Account_New_V2.this);
//                        myAlert.setMessage("Name: " + str01 + " Acc: " + str02 + " Validity: " + str03);
//                        myAlert.setNegativeButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alertDialog = myAlert.create();
//                        alertDialog.show();
//                    }
//                });
//            }
//        }
    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            LoadAccountList();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Link_Account_New_V2.this);
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
           String mStrEncryptAccountNumber=encryption.Encrypt(GlobalData.getStrAccountNumber(),GlobalData.getStrSessionId());
            String strGetAccountList= CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber,GlobalData.getStrUserId(),GlobalData.getStrDeviceId());
            //String strGetAccountList= GlobalData.getStrGetAccountList();
            GlobalData.setStrGetAccountList(strGetAccountList);
            if(strGetAccountList.contains("*")) {
                StringTokenizer tokensTransaction = new StringTokenizer(strGetAccountList, "*");
                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                    while (tokensTransaction.hasMoreElements()) {


                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                        // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());
                        String strName = tokensAllTransactionFinal.nextToken();
                        String strIsdefult = tokensAllTransactionFinal.nextToken();
                        String strAccountNo = tokensAllTransactionFinal.nextToken();
                        String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                        String strIsVerified=tokensAllTransactionFinal.nextToken();
                        String strBankID=tokensAllTransactionFinal.nextToken();

                        arrayAccountName.add(strName);
                        arrayAccountDefuly.add(strIsdefult);
                        arrayBankAccountNo.add(strAccountNo);
                        arrayBankAccountStatus.add(strBankAccounStatus);
                        arrayBankAccountIsVerified.add(strIsVerified);
                        arrayBankID.add(strBankID);
                        //------------ get balance and QR code -------------------
                        String strEncWallet = null;
                        String strEncryptedPIN="";
                        String strEncryptBankID="";
                        String strVoucherBalance="";
                        try {
                            strEncWallet = encryption.Encrypt(strAccountNo, GlobalData.getStrSessionId());
                            strEncryptedPIN= encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                            strEncryptBankID=encryption.Encrypt(strBankID, GlobalData.getStrSessionId());;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(strBankID.equalsIgnoreCase("170402004"))  // For mYcash
                        {
                            strVoucherBalance = GetBalance.getBalance_mycash(GlobalData.getStrUserId(), strEncryptedPIN, strEncWallet,strEncryptBankID, GlobalData.getStrDeviceId());
                        }
                        else
                            {
                            strVoucherBalance = GetBalance.getBalance(GlobalData.getStrUserId(), strEncryptedPIN, strEncWallet, GlobalData.getStrDeviceId());
                        }
                        String mStrQrCodeContent = GetQrCodeContent.getQrCode(strEncWallet, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
                        if (strVoucherBalance.equalsIgnoreCase("")) {
                            strVoucherBalance = "0";
                        }
                        if (mStrQrCodeContent.equalsIgnoreCase("")) {
                            mStrQrCodeContent = "data not found";
                        }
                        arrayBankAccountBalance.add(strVoucherBalance);
                        arrayQRCodelinkAccount.add(mStrQrCodeContent);
                        //=============================================

  //------------------------------------------
                                CustomListForLinkedAccout_V2 adapter = new CustomListForLinkedAccout_V2(Link_Account_New_V2.this,
                                        arrayAccountName,
                                        arrayAccountDefuly,
                                        arrayBankAccountNo,
                                        arrayBankAccountStatus,
                                        arrayBankAccountIsVerified,
                                        arrayBankAccountBalance,
                                        arrayQRCodelinkAccount,arrayBankID);
                                mListView.setAdapter(adapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        TextView textView01 = view.findViewById(R.id.txt01);
                                        TextView textView02 = view.findViewById(R.id.txt02);
                                        TextView textView03 = view.findViewById(R.id.txt03);

                                        String str01 = textView01.getText().toString();
                                        String str02 = textView02.getText().toString();
                                        String str03 = textView03.getText().toString();

                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Link_Account_New_V2.this);
                                        myAlert.setMessage("Name: " + str01 + " Acc: " + str02 + str03);
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
                                });

                        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                           final int pos, long id) {
                                // TODO Auto-generated method stub

                                AlertDialog.Builder myAlert = new AlertDialog.Builder(Link_Account_New_V2.this);
                                myAlert.setMessage("Do you want to delete this account??");
                                myAlert.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                if(arrayAccountDefuly.get(pos).equalsIgnoreCase("Y"))
                                                {
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(Link_Account_New_V2.this);
                                                    myAlert.setMessage("You don't Delete Your Primary Wallet");
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
                                                                                                                    // (GlobalData.getStrAccountNumber(), StrSelectBankID, "S", GlobalData.getStrAccountNumber(), "Salary Wallet", StrSelcetDistrictID, "QPAY");
                                                    String strDeleteAccount=CheckDeviceActiveStatus.DeleteLinkAccount(GlobalData.getStrAccountNumber(),arrayBankID.get(pos),"",arrayBankAccountNo.get(pos),"","","");
                                                    if(strDeleteAccount.equalsIgnoreCase("Delete"))
                                                    {
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Link_Account_New_V2.this);
                                                        myAlert.setMessage("You Account is deleted successfully");
                                                        myAlert.setNegativeButton(
                                                                "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                        LoadAccountList();
                                                                        startActivity(new Intent(Link_Account_New_V2.this, MainActivity.class));
                                                                        finish();
                                                                       // LoadAccountList();
                                                                    }
                                                                });

                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();



                                                    }
                                                    else
                                                    {
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Link_Account_New_V2.this);
                                                        myAlert.setMessage(strDeleteAccount);
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

                                                }

                                            }
                                        });

                                myAlert.setNeutralButton(
                                        "Close",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                AlertDialog alertDialog = myAlert.create();
                                alertDialog.show();

                                Log.v("long clicked","pos: " + pos);

                                return true;
                            }
                        });


                        //------------------------------------------------------------

                                //-----------------------------------

//                        mProgressDialog = ProgressDialog.show(Link_Account_New_V2.this, null, "Load data...", false, true);
//
//                        mProgressDialog.setCancelable(true);
//
//                        Thread t = new Thread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                                mProgressDialog.dismiss();
//
//                                            }
//                                        } catch (Exception e) {
//                                            // TODO: handle exception
//                                        }
//                                    }
//                                });
//                            }
//                        });
//
//                        t.start();
                        /////////////////////////////////

//
                    }

                }
            }
            else
            {

            }
            //===================================================

        }
        catch (Exception ex)
        {

        }

    }

    public static Bitmap decodeBase64(String input)
    {
        Bitmap bm=null;
        try {
            byte[] decodedByte = Base64.decode(input, 0);
             bm= BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }
        catch (Exception ex)
        {

        }
        return bm;
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
                startActivity(new Intent(Link_Account_New_V2.this, MainActivity.class));
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
            startActivity(new Intent(Link_Account_New_V2.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
