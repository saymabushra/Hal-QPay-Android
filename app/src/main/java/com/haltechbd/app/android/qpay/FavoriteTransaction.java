package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.CustomList;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetAllFavoriteList;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class FavoriteTransaction extends AppCompatActivity implements View.OnClickListener {
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    private ListView mListView;
    private Button mBtnDeleteAll;
    private TextView mTextViewCaption, mTextViewSourceWallet, mTextViewDestinationWallet,
            mTextViewDestinationAccountHolderName, mTextViewAmount, mTextViewFunctionType;
    private String mStrCaption, mStrSourceWallet, mStrDestinationWallet,
            mStrDestinationWalletHolderName, mStrAmount, mStrFunctionType,mStrEncryptAccountNumber;

    ArrayList<String> arrayListSourceAcc = new ArrayList<>();
    ArrayList<String> arrayListDestinationAcc = new ArrayList<>();
    ArrayList<String> arrayListName = new ArrayList<>();
    ArrayList<String> arrayListAmount = new ArrayList<>();
    ArrayList<String> arrayListCaption = new ArrayList<>();
    ArrayList<String> arrayListFunctionType = new ArrayList<>();
    private StringTokenizer tokensFavoriteList, tokensAllFavoriteList;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_transation);
        checkOs();
        initUI();
    }

    //########################## Initialize UI ############################
    //########################## Initialize UI ############################
    //########################## Initialize UI ############################
    private void initUI() {
        mListView = findViewById(R.id.listViewfav);
        mBtnDeleteAll = findViewById(R.id.btnDeleteAllFavList);
        mBtnDeleteAll.setOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkInternet();
    }

    //########################## Delete All ############################
    //########################## Delete All ############################
    //########################## Delete All ############################
    @Override
    public void onClick(View v) {
        if (v == mBtnDeleteAll) {
            Toast.makeText(FavoriteTransaction.this, "Clicked", Toast.LENGTH_LONG).show();
            try {
                String encryptCaption = encryptionDecryption.Encrypt("allfavfun", GlobalData.getStrSessionId());
                mStrEncryptAccountNumber = encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String deleteFavoriteItemResponse = GetAllFavoriteList.deleteFavoriteList(mStrEncryptAccountNumber, encryptCaption);
                if (deleteFavoriteItemResponse.equalsIgnoreCase("Update")) {
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(FavoriteTransaction.this);
                    myAlert.setMessage("Delete All Favorite Items Successfully.");
                    myAlert.setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();
                } else {
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(FavoriteTransaction.this);
                    myAlert.setMessage("Delete All Favorite Item Fail.");
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

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    //########################## All Fav Transaction List ############################
    //########################## All Fav Transaction List ############################
    //########################## All Fav Transaction List ############################
    private void loadAllFavoriteList() {
        arrayListSourceAcc.clear();
        arrayListDestinationAcc.clear();
        arrayListName.clear();
        arrayListAmount.clear();
        arrayListCaption.clear();
        arrayListFunctionType.clear();
        try {
            mStrEncryptAccountNumber = encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
        }
        catch (Exception ex)
        {}
        String strAllFavoriteList = GetAllFavoriteList.getAllFavoriteList(mStrEncryptAccountNumber);
        int intIndex = strAllFavoriteList.indexOf("*");
        if (intIndex == -1) {
            //####################### Show Dialog ####################
            //####################### Show Dialog ####################
            //####################### Show Dialog ####################
            AlertDialog.Builder myAlert = new AlertDialog.Builder(FavoriteTransaction.this);
            myAlert.setMessage("No Data Found.");
            myAlert.setNegativeButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                            startActivity(new Intent(FavoriteTransaction.this, MainActivity.class));
                            finish();

                        }
                    });
            AlertDialog alertDialog = myAlert.create();
            alertDialog.show();
        } else {
            //get individual record
            tokensAllFavoriteList = new StringTokenizer(strAllFavoriteList, "&");

            for (int j = 0; j <= tokensAllFavoriteList.countTokens(); j++) {
                while (tokensAllFavoriteList.hasMoreElements()) {
                    tokensFavoriteList = new StringTokenizer(tokensAllFavoriteList.nextToken(), "*");
                    String strSourceAcc = tokensFavoriteList.nextToken();
                    String strDestinationAcc = tokensFavoriteList.nextToken();
                    String strName = tokensFavoriteList.nextToken();
                    String strAmount = tokensFavoriteList.nextToken();
                    String strCaption = tokensFavoriteList.nextToken();
                    String strFunctionType = tokensFavoriteList.nextToken();

                    arrayListSourceAcc.add(strSourceAcc);
                    arrayListDestinationAcc.add(strDestinationAcc);
                    arrayListName.add(strName);
                    arrayListAmount.add(strAmount);
                    arrayListCaption.add(strCaption);
                    arrayListFunctionType.add(strFunctionType);

                    CustomList adapter = new CustomList(FavoriteTransaction.this,
                            arrayListSourceAcc,
                            arrayListDestinationAcc,
                            arrayListName,
                            arrayListAmount,
                            arrayListCaption,
                            arrayListFunctionType);
                    mListView.setAdapter(adapter);
                }

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> myAdapter, View view, final int position, long id) {
                        mTextViewCaption = view.findViewById(R.id.textViewCaption);
                        mTextViewSourceWallet = view.findViewById(R.id.textViewSourceAccount);
                        mTextViewDestinationWallet = view.findViewById(R.id.textViewDestinationAccount);
                        mTextViewDestinationAccountHolderName = view.findViewById(R.id.textViewName);
                        mTextViewAmount = view.findViewById(R.id.textViewAmount);
                        mTextViewFunctionType = view.findViewById(R.id.textViewFunctionType);

                        mStrCaption = mTextViewCaption.getText().toString();
                        mStrSourceWallet = mTextViewSourceWallet.getText().toString();
                        mStrDestinationWallet = mTextViewDestinationWallet.getText().toString();
                        mStrDestinationWalletHolderName = mTextViewDestinationAccountHolderName.getText().toString();
                        mStrAmount = mTextViewAmount.getText().toString();
                        mStrFunctionType = mTextViewFunctionType.getText().toString();

                        String strFinal = "Caption: " + mStrCaption + "\n" +
                                "Source Acc: " + mStrSourceWallet + "\n" +
                                "Destination Acc: " + mStrDestinationWallet + "\n" +
                                "Destination Acc Holder: " + mStrDestinationWalletHolderName + "\n" +
                                "Amount: " + mStrAmount+"\n"+"Function:"+mStrFunctionType;

                        AlertDialog.Builder a = new AlertDialog.Builder(FavoriteTransaction.this);
                        a.setMessage(strFinal);
                        a.setPositiveButton(
                                "Go",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {
                                            if (arrayListFunctionType.get(position).equalsIgnoreCase("CFT")) {
                                                GlobalData.setStrFavCaption(mStrCaption);
                                                GlobalData.setStrFavSourceWallet(mStrSourceWallet);
                                                GlobalData.setStrFavDestinationWallet(mStrDestinationWallet);
                                                GlobalData.setStrFavDestinationWalletAccountHolderName(mStrDestinationWalletHolderName);
                                                GlobalData.setStrFavAmount(mStrAmount);
                                                GlobalData.setStrFavFunctionType(arrayListFunctionType.get(position));
                                                GlobalData.setStrMenuFrom("Favorite Transaction");
                                               // startActivity(new Intent(FavoriteTransaction.this, FT_C2C_FromFav.class));
                                            } else if (arrayListFunctionType.get(position).equalsIgnoreCase("CMP")) {
                                                GlobalData.setStrFavCaption(mStrCaption);
                                                GlobalData.setStrFavSourceWallet(mStrSourceWallet);
                                                GlobalData.setStrFavDestinationWallet(mStrDestinationWallet);
                                                GlobalData.setStrFavDestinationWalletAccountHolderName(mStrDestinationWalletHolderName);
                                                GlobalData.setStrFavAmount(mStrAmount);
                                                GlobalData.setStrFavFunctionType(arrayListFunctionType.get(position));
                                                GlobalData.setStrMenuFrom("Favorite Transaction");
                                                startActivity(new Intent(FavoriteTransaction.this, MP_Through_C_C2M_FromFav.class));
                                                finish();
                                            } else if (arrayListFunctionType.get(position).equalsIgnoreCase("MFM")) {
                                                GlobalData.setStrFavCaption(mStrCaption);
                                                GlobalData.setStrFavSourceWallet(mStrSourceWallet);
                                                GlobalData.setStrFavDestinationWallet(mStrDestinationWallet);
                                                GlobalData.setStrFavDestinationWalletAccountHolderName(mStrDestinationWalletHolderName);
                                                GlobalData.setStrFavAmount(mStrAmount);
                                                GlobalData.setStrFavFunctionType(arrayListFunctionType.get(position));
                                                GlobalData.setStrMenuFrom("Favorite Transaction");
                                               /// startActivity(new Intent(FavoriteTransaction.this, FM_M2M_FromFav.class));
                                            } else {
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(FavoriteTransaction.this);
                                                myAlert.setMessage("Error in loading Favorite Transaction. Please try again.");
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
                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    }
                                });
                        a.setNeutralButton(
                                "Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        a.setNegativeButton(
                                "Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {
                                            String encryptCaption = encryptionDecryption.Encrypt(mStrCaption, GlobalData.getStrSessionId());
                                            mStrEncryptAccountNumber= encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                                            String deleteFavoriteItemResponse = GetAllFavoriteList.deleteFavoriteList(mStrEncryptAccountNumber, encryptCaption);
                                            if (deleteFavoriteItemResponse.equalsIgnoreCase("Update")) {
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(FavoriteTransaction.this);
                                                myAlert.setMessage("Delete Favorite Item Successfully.");
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                Intent intent = getIntent();
                                                                finish();
                                                                startActivity(intent);
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            } else {
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(FavoriteTransaction.this);
                                                myAlert.setMessage("Delete Favorite Item Fail.");
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

                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    }
                                });
                        AlertDialog a1 = a.create();
                        a1.show();
                    }
                });


            }
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
                startActivity(new Intent(FavoriteTransaction.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(FavoriteTransaction.this, Login.class)
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

    //########################## Check Internet ############################
    //########################## Check Internet ############################
    //########################## Check Internet ############################
    private void checkInternet() {
        if (isNetworkConnected()) {
            loadAllFavoriteList();
        } else {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(FavoriteTransaction.this);
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

    //########################## Check OS ############################
    //########################## Check OS ############################
    //########################## Check OS ############################
    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(FavoriteTransaction.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}