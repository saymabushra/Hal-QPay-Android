package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
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
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.GenerateQrCodeNew;
import com.haltechbd.app.android.qpay.utils.GlobalData;


public class Offline_Login extends AppCompatActivity   {
    private ProgressDialog mProgressDialog = null;
    private TextView mTextViewAccountHolderFullName, mTextViewAccountNumber,
            mTextViewAccountHolderFullNameReverse, mTextViewAccountNumberReverse,mTextViewAccountDetailes;

    private SharedPreferences mSharedPreferencsOtp,msharePrefImage,msharePrefOfflinePin;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor,mshareprefImageEditor,mshareprefOfflinePINEditor;
    //#############################################################
    private ImageView mImgView,mImgProfile;
    private Bitmap mBitmapQrCode;
    private String mStrQrCodeContent,strAccountName,strAccountNo,strAccountStatus,strAccountQR,strAccountDefault,strAccountBankNAme,strStatus;
    ImageButton btnShowOfflineQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_login);
        checkOs();
        initUI();
    }

    private void initUI() {
        mImgView = findViewById(R.id.imageViewPreviewQrCodeOfflineLogin);
        mImgProfile = findViewById(R.id.imgViewProfilePicOfflineLogin);
        mTextViewAccountDetailes = findViewById(R.id.textViewAccountDetailesOfflineLogin);
        mTextViewAccountNumber = findViewById(R.id.textViewAccountNumberOfflineLogin);
        mTextViewAccountHolderFullName=findViewById(R.id.txtviewAccountNameOfflineLogin);
   //--------------get share prefenece data--------------------------------
        msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
        mshareprefImageEditor = msharePrefImage.edit();
        String strBitMap = msharePrefImage.getString("imagePreferance", "");
        // decodeBase64(strBitMap);
        if(strBitMap!=null && !strBitMap.isEmpty())
        {
            final Bitmap selectedImage = decodeBase64(strBitMap);
            //  BitmapFactory.decodeStream(imageStream);
            mImgProfile.setImageBitmap(selectedImage);
        }
       //---------------------------------------------------------------------------
        msharePrefOfflinePin = getSharedPreferences("OfflinePINPrefs", MODE_PRIVATE);
        mshareprefOfflinePINEditor = msharePrefOfflinePin.edit();
        strAccountName = msharePrefOfflinePin.getString("offlineAccountName", "");
        strAccountNo = msharePrefOfflinePin.getString("offlineAccountNo", "");
        strAccountStatus = msharePrefOfflinePin.getString("offlineAccountStatus", "");
        strAccountQR = msharePrefOfflinePin.getString("offlineAccountQRCode", "");
        strAccountDefault = msharePrefOfflinePin.getString("offlineAccountDefault", "");
        strAccountBankNAme=msharePrefOfflinePin.getString("offlineAccountBankName", "");
        //----------get data end ------------------------

        mStrQrCodeContent = strAccountQR;

if(strAccountName!=null && !strAccountName.isEmpty()) {
    // Initialize progress dialog
    mProgressDialog = ProgressDialog.show(Offline_Login.this, null, "Loading offline mood...", false, true);
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
                            if (strAccountDefault.equalsIgnoreCase("Y")) {
                                mTextViewAccountDetailes.setText( strAccountBankNAme +"(Default A/C)");
                            } else {
                                mTextViewAccountDetailes.setText(strAccountBankNAme);
                            }
                            mTextViewAccountHolderFullName.setText(strAccountName);
                            if (strAccountStatus.equalsIgnoreCase("A")) {
                                strStatus = "Active";
                            } else {
                                strStatus = "In Active";
                            }
                            mTextViewAccountNumber.setText(strAccountNo + "|" + strStatus);


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
else
{
    AlertDialog.Builder myAlert = new AlertDialog.Builder(Offline_Login.this);
    myAlert.setMessage("No data was saved by Offline");
    myAlert.setNegativeButton(
            "Ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
    AlertDialog alertDialog = myAlert.create();
    alertDialog.show();

//    final Dialog dialog = new Dialog(Offline_Login.this);
//    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//    dialog.setContentView(R.layout.dialog_layout);
//    TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
//    TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessage);
//    TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperation);
//    Button btnYes = (Button) dialog.findViewById(R.id.btnClose);
//    Button btnNo = (Button) dialog.findViewById(R.id.btnOK);
//    textViewTitle.setText("Title");
//    textViewMessage.setText("No data was saved by Offline");
//    textViewOperation.setText("Action");
//    dialog.show();
//
//    btnYes.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "Your Choose YES", Toast.LENGTH_LONG).show();
//            dialog.cancel();
//        }
//    });
//    btnNo.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "Your Choose NO", Toast.LENGTH_LONG).show();
//            dialog.cancel();
//        }
//    });


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
        pinMenuItem.setTitle("Reward Point:0");
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Offline_Login.this, Login.class));
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
            startActivity(new Intent(Offline_Login.this, Login.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
