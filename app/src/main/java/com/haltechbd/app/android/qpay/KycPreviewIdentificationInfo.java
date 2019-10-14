package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;


public class KycPreviewIdentificationInfo extends AppCompatActivity implements View.OnClickListener {
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private TextView mTextViewDocumentType, mTextViewIdentificationNumber, mTextViewRemark;
    private Button mBtnUpdate;
    ImageView imgFront,imgBack;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrAccountNumber, mStrPin,
            mStrEncryptAccountNumber, mStrEncryptPin, mStrIdentificationId,
            mStrEncryptIdentificationId, mStrEncryptIdentificationNumber, mStrEncryptRemark,
            mStrServerResponse,mstrparameten,mstrPictureID,mstrPictureIDBack;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identificaiton_document_preview_new);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mTextViewDocumentType = findViewById(R.id.txtLabel);
        mTextViewIdentificationNumber = findViewById(R.id.textViewlabelId);
       // mTextViewRemark = findViewById(R.id.textViewRemark);/
        imgFront=findViewById(R.id.imgViewDocumentFront);
        imgBack=findViewById(R.id.imgViewDocumentBack);

//        mBtnUpdate = findViewById(R.id.btnKycUpdateIndentificationInfo);
//        mBtnUpdate.setOnClickListener(this);
       // mTextViewShowServerResponse = findViewById(R.id.textViewKycIndentificationInfoShowServerResponse);

     //   mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();
        mTextViewDocumentType.setText(GlobalData.getStrIdentificationname());
        mTextViewIdentificationNumber.setText(GlobalData.getStrIdentificationNumber());
        if(GlobalData.getStrIdentificationname().equalsIgnoreCase("NATIONAL ID"))
        {
            imgBack.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
//        if (v == mBtnUpdate) {
//            startActivity(new Intent(KycPreviewIdentificationInfo.this, KycUpdateIdentificationInfo.class));
//            finish();
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
//        }

    }


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            try {
//                mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, GlobalData.getStrSessionId());
//                mStrEncryptPin = encryption.Encrypt(mStrPin, GlobalData.getStrSessionId());
//                mStrEncryptIdentificationId = encryption.Encrypt("", GlobalData.getStrSessionId());
//                mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
//                mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
//                mstrparameten= encryption.Encrypt("G", GlobalData.getStrSessionId());
                mstrPictureID= GlobalData.getStrIdentificationPictureID();
                if(mstrPictureID!="") {
                    String imageEncoded = InsertKyc.KycGetDocument(mstrPictureID);
                   // String imageEncoded = Base64.encodeToString(strImage, Base64.DEFAULT);
                    Bitmap bitImage=  decodeBase64(imageEncoded);
                    imgFront.setImageBitmap(bitImage);
                }
                if(GlobalData.getStrIdentificationname().equalsIgnoreCase("NATIONAL ID"))
                {
                    mstrPictureIDBack=GlobalData.getStrIdentificationPicIDBack();
                    if(!mstrPictureIDBack.equalsIgnoreCase("No Data Found")) {
                        String imageEncoded = InsertKyc.KycGetDocument(mstrPictureIDBack);
                       // String imageEncoded = Base64.encodeToString(strImage, Base64.DEFAULT);
                        Bitmap bitImage=  decodeBase64(imageEncoded);
                        imgBack.setImageBitmap(bitImage);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycPreviewIdentificationInfo.this);
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


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
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
                startActivity(new Intent(KycPreviewIdentificationInfo.this, KycListOfIdentificationInfo.class));
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
            startActivity(new Intent(KycPreviewIdentificationInfo.this, KycListOfIdentificationInfo.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
