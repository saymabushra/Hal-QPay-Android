package com.haltechbd.app.android.qpay;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bushra on 6/4/2018.
 */

public class Topup  extends AppCompatActivity implements View.OnClickListener  {

    private Spinner mSpinnerSubscriberType,mSpinnerWallet;
    EditText mEditTextOtp,mEditTextDestinationMobileNumber,mEditTextAmount;
    TextView mTextViewShowServerResponse;
    Button mBtnSubmit;
    ImageView imgViewContachlistMP;
    private SharedPreferences mSharedPreferencsOtp;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor;
    private ProgressDialog mProgressDialog = null;
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    private String SOAP_ACTION, METHOD_NAME;
    String strOTP,strAmount,strDesMbl,strSubscriberType,mStrSourceWallet,mStrServerResponse,mStrEncryptAccountNumber,mStrEncryptPin,
    mStrEncryptSubscriberAccountNumber,mStrEncryptAmount,mStrEncryptOtp,mstrEncryptSubscriberType,strPhnoeNumber;
    private int PICK_CONTACT=1;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // btn.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // btn.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        checkOs();
        initUI();
    }

    private void initUI() {

        mEditTextDestinationMobileNumber = (EditText) findViewById(R.id.editTextTopUpMobileNumber);
        mEditTextDestinationMobileNumber.requestFocus();
        mEditTextAmount = (EditText) findViewById(R.id.editTextTopUpAmount);
        mBtnSubmit = (Button) findViewById(R.id.btnTopUpSubmit);
        mTextViewShowServerResponse = (TextView) findViewById(R.id.txtViewOtpShowServerResponse);
        mEditTextOtp = findViewById(R.id.editTextTopUpOtp);
        mSpinnerWallet = findViewById(R.id.spinnerTopupWallet);
        imgViewContachlistMP=findViewById(R.id.imgViewContachlistMP);
        imgViewContachlistMP.setOnClickListener(this);
        try {
            //############################################### OTP ###############################################
            //############################################### OTP ###############################################
            //############################################### OTP ###############################################
            mSharedPreferencsOtp = getSharedPreferences("otpPrefs", MODE_PRIVATE);
            mSharedPreferencsOtpEditor = mSharedPreferencsOtp.edit();
            String strExpireTime = mSharedPreferencsOtp.getString("otp_expire_time", "");
            String strOtp = mSharedPreferencsOtp.getString("generate_otp", "");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            // Current Time
            Date currentTime = Calendar.getInstance().getTime();
            String strCurrentTime = df.format(currentTime);
            // Expire Time
            Date timeCurrent = df.parse(strCurrentTime);
            Date timeExpire = df.parse(strExpireTime);
            if (timeCurrent.before(timeExpire)) {
                // if valid
                mEditTextOtp.setText(strOtp);
            } else {
                // if expire
                AlertDialog.Builder myAlert = new AlertDialog.Builder(Topup.this);
                myAlert.setMessage("OTP is expired. Generate a new OTP?");
                myAlert.setPositiveButton(
                        "Generate OTP",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(Topup.this, GenerateOtp.class));
                                finish();
                            }
                        });
                myAlert.setNegativeButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(Topup.this, MainActivity.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();
            }
            //#######################################################################################################
            //#######################################################################################################
            //#######################################################################################################
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        loadSpinnerSubscriberType();

        checkInternet();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mBtnSubmit.setOnClickListener(this);

    }

    public void loadSpinnerSubscriberType() {
        mSpinnerSubscriberType = (Spinner) findViewById(R.id.spinnerTopUpSubscriberType);
        List<String> list = new ArrayList<String>();
        list.add("Pre-Paid");
        list.add("Post-Paid");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSubscriberType.setAdapter(dataAdapter);
    }

    public void onClick(View v) {

        if (v == mBtnSubmit) {

            strOTP = mEditTextOtp.getText().toString();
            strAmount = mEditTextAmount.getText().toString();
            strDesMbl = mEditTextDestinationMobileNumber.getText().toString();
            strSubscriberType = String.valueOf(mSpinnerSubscriberType.getSelectedItem());
            if (strSubscriberType == "Pre-Paid") {
                strSubscriberType = "0";
            } else if (strSubscriberType == "Post-Paid") {
                strSubscriberType = "1";
            }

            //-------------------------------------------------
            try {
                mSharedPreferencsOtp = getSharedPreferences("otpPrefs", MODE_PRIVATE);
                mSharedPreferencsOtpEditor = mSharedPreferencsOtp.edit();
                String strExpireTime = mSharedPreferencsOtp.getString("otp_expire_time", "");
                String strOtp = mSharedPreferencsOtp.getString("generate_otp", "");

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                // Current Time
                Date currentTime = Calendar.getInstance().getTime();
                String strCurrentTime = df.format(currentTime);
                // Expire Time
                Date timeCurrent = df.parse(strCurrentTime);
                Date timeExpire = df.parse(strExpireTime);

                if (timeCurrent.before(timeExpire)) {
                    //#############################
                    if (mEditTextDestinationMobileNumber.getText().toString().length() == 0) {
                        mEditTextDestinationMobileNumber.setError("Field cannot be empty");
                    } else if (mEditTextAmount.getText().toString().length() == 0) {
                        mEditTextAmount.setError("Field cannot be empty");
                    } else if (mEditTextOtp.getText().toString().length() == 0) {
                        mEditTextOtp.setError("Field cannot be empty");
                    } else if (mEditTextOtp.getText().toString().length() < 5) {
                        mEditTextOtp.setError("Must be 5 characters in length");
                    }
                    else if (mEditTextDestinationMobileNumber.getText().toString().length() < 11) {
                        mEditTextDestinationMobileNumber.setError("Must be 11 characters in length");
                    }
                    else
                        {
                            try
                            {
                             mStrEncryptAccountNumber = encryptionDecryption.Encrypt(mStrSourceWallet,GlobalData.getStrSessionId());
                             mStrEncryptPin = encryptionDecryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                             mStrEncryptSubscriberAccountNumber = encryptionDecryption.Encrypt(strDesMbl, GlobalData.getStrSessionId());
                             mStrEncryptAmount = encryptionDecryption.Encrypt(strAmount, GlobalData.getStrSessionId());
                             mStrEncryptOtp = encryptionDecryption.Encrypt(strOTP, GlobalData.getStrSessionId());
                             mstrEncryptSubscriberType=encryptionDecryption.Encrypt(strSubscriberType, GlobalData.getStrSessionId());

                            // Initialize progress dialog
                            mProgressDialog = ProgressDialog.show(Topup.this, null, "Processing request...", false, true);
                            // Cancel progress dialog on back key press
                            mProgressDialog.setCancelable(true);

                            Thread t = new Thread(new Runnable() {

                                @Override
                                public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                                    doTopUp(mStrEncryptOtp,mStrEncryptAmount,mStrEncryptSubscriberAccountNumber,mstrEncryptSubscriberType,mStrEncryptAccountNumber,mStrEncryptPin);
//                            SystemClock.sleep(10000);
//                    } else {
//                        mTextViewShowServerResponse.setText("Session Expire, Please Login Again");
//                    }
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                    mProgressDialog.dismiss();
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    //####################### Show Dialog ####################
                                                    int intIndex = mStrServerResponse.indexOf("successful");
                                                    if (intIndex == -1) {
                                                        if(mStrServerResponse.equalsIgnoreCase(""))
                                                        {
                                                            mStrServerResponse="Request is in process";
                                                        }
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Topup.this);
                                                        myAlert.setMessage(mStrServerResponse);
                                                        myAlert.setNegativeButton(
                                                                "Close",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                        startActivity(new Intent(Topup.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                    } else {
                                                        int intindex2= mStrServerResponse.indexOf("unsuccessful");
                                                        if (intindex2 == -1) {

                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Topup.this);
                                                            myAlert.setMessage(mStrServerResponse);
                                                            myAlert.setNeutralButton(
                                                                    "Continue",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                            clearEditText();
                                                                        }
                                                                    });
//                                                            myAlert.setPositiveButton(
//                                                                    "Add Favorite",
//                                                                    new DialogInterface.OnClickListener() {
//                                                                        public void onClick(DialogInterface dialog, int id) {
//                                                                            dialog.cancel();
////                                                            enableUiComponentAfterClick();
//                                                                            GlobalData.setStrFavSourceWallet(mStrSourceWallet);
//                                                                            GlobalData.setStrFavSourcePin(mStrSourcePin);
//                                                                            GlobalData.setStrFavAmount(strAmount);
//                                                                            GlobalData.setStrFavDestinationWallet(strDestinationWallet1);
//                                                                            GlobalData.setStrFavDestinationWalletAccountHolderName(mStrDestinationAccountHolderName);
//                                                                            GlobalData.setStrFavReference(strReference);
//                                                                            GlobalData.setStrFavFunctionType(strFunctionType);
//                                                                            startActivity(new Intent(MP_Through_C_C2M.this, MP_Through_C_C2M_AddToFav.class));
//                                                                        }
//                                                                    });
                                                            myAlert.setNegativeButton(
                                                                    "Close",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                            startActivity(new Intent(Topup.this, MainActivity.class));
                                                                            finish();
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog = myAlert.create();
                                                            alertDialog.show();
                                                            ////

                                                        }
                                                        else {
                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Topup.this);
                                                            myAlert.setMessage(mStrServerResponse);
                                                            myAlert.setNegativeButton(
                                                                    "Close",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                            startActivity(new Intent(Topup.this, Topup.class));
                                                                            finish();
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog = myAlert.create();
                                                            alertDialog.show();
                                                        }
                                                    }

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


                        } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                    }

                } else {
                    // if expire
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(Topup.this);
                    myAlert.setMessage("OTP is expired. Generate a new OTP?");
                    myAlert.setPositiveButton(
                            "Generate OTP",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(Topup.this, GenerateOtp.class));
                                    finish();
                                }
                            });
                    myAlert.setNegativeButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(Topup.this, MainActivity.class));
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();
                }
                //####
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==imgViewContachlistMP)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
         if(requestCode == PICK_CONTACT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst())
                {
                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1"))
                    {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                        phones.moveToFirst();
                        String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        if(cNumber.length()==11)
                        {
                            strPhnoeNumber=cNumber;
                        }
                        else if(cNumber.length()==14)
                        {
                            strPhnoeNumber=cNumber.substring(3,14);

                        }
                        else if(cNumber.length()==13)
                        {
                            strPhnoeNumber=cNumber.substring(2,13);

                        }
                        //setCn(cNumber);
                        mEditTextDestinationMobileNumber.setText(strPhnoeNumber);
                        Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private String  getContactList() {
       String strPhnoeNumber="";

        try {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //Log.i(TAG, "Name: " + name);
                            // Log.i(TAG, "Phone Number: " + phoneNo);
                            String strname = name;
                            String strNo = phoneNo;
                            if(strNo.length()==11)
                            {
                                strPhnoeNumber=strNo;
                            }
                            else if(strNo.length()==14)
                            {
                                strPhnoeNumber=strNo.substring(3,14);

                            }
                            else if(strNo.length()==13)
                            {
                                strPhnoeNumber=strNo.substring(2,13);

                            }
                            // Toast.makeText(parent.getContext(), "view clicked: " + arrayIdentifocationName.get(position), Toast.LENGTH_SHORT).show();
                        }
                        pCur.close();
                    }
                }
            }
            if (cur != null) {
                cur.close();
            }
        }
        catch (Exception ex)
        {}
        return  strPhnoeNumber;
    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            mProgressDialog = ProgressDialog.show(Topup.this, null, "Loading Bank...", false, true);
            // Cancel progress dialog on back key press
            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    // Background code should be in here
                    loadSpinnerWallet();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                    ArrayAdapter<String> adapterWallet = new ArrayAdapter<String>(Topup.this,
                                            android.R.layout.simple_spinner_item, arrayAccountName);
                                    adapterWallet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    mSpinnerWallet.setAdapter(adapterWallet);
                                    mSpinnerWallet.setOnItemSelectedListener(onItemSelectedListenerForWallet);
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


        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Topup.this);
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


    private void loadSpinnerWallet() {
        try {
           String mStrEncryptAccountNumber = encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
            String strGetAccountList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
            // String strAllWalletTypeAndNumber = GetAllCustomerWallet.getAllWallets();
            if (strGetAccountList != null && !strGetAccountList.isEmpty()) {
                if(strGetAccountList.contains("*"))
                {
                    StringTokenizer strToken = new StringTokenizer(strGetAccountList, "*");
                    ArrayList<String> arrayListWalletTypeAndNumber = new ArrayList<String>();
                    for (int j = 0; j <= strToken.countTokens(); j++) {
                        while (strToken.hasMoreElements()) {
                            arrayListWalletTypeAndNumber.add(strToken.nextToken());
                        }
                    }
                    for (int i = 0; i <= arrayListWalletTypeAndNumber.size() - 1; i++) {
                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(arrayListWalletTypeAndNumber.get(i), ";");

                        String strName = tokensAllTransactionFinal.nextToken();
                        String strIsdefult = tokensAllTransactionFinal.nextToken();
                        String strAccountNo = tokensAllTransactionFinal.nextToken();
                        String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                        String strIsVerified=tokensAllTransactionFinal.nextToken();

                        if(strBankAccounStatus.equalsIgnoreCase("A")) {
                            if(strIsVerified.equalsIgnoreCase("Y")) {
                                if(strIsdefult.equalsIgnoreCase("Y"))
                                {
                                    arrayAccountName.add(strName+"(Default)");
                                }
                                else
                                {
                                    arrayAccountName.add(strName);

                                }
                                arrayAccountDefuly.add(strIsdefult);
                                arrayBankAccountNo.add(strAccountNo);
                                arrayBankAccountStatus.add(strBankAccounStatus);
                                arrayBankAccountIsVerified.add(strIsVerified);
                            }
                        }
                        //arrayListWalletType.add(tokenWalletTypeAndAccount.nextToken());
                        //arrayListWallet.add(tokenWalletTypeAndAccount.nextToken());
                    }
                }
                else
                {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Topup.this);
                    mAlertDialogBuilder.setMessage(strGetAccountList);
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
            else {
                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Topup.this);
                mAlertDialogBuilder.setMessage("No Account Found.");
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
        catch (Exception ex)
        {

        }
    }

    AdapterView.OnItemSelectedListener onItemSelectedListenerForWallet = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           mStrSourceWallet = String.valueOf(arrayBankAccountNo.get(position));
            //GlobalData.setStrCustomerAccountL14(mStrSourceWallet);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:0");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Topup.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(ChangePin.this, Login.class)
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
            startActivity(new Intent(Topup.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doTopUp(String strOTP, String strAmount, String strDesMbl, String strSubscriberType, String Wallet,String PIN) {

        METHOD_NAME = "QPAY_Topup";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Topup";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //QPAY_Merchant_Payment_Customer
       // string E_AccountNo, string E_PIN, string E_OTP, string E_Amount, string E_Destination_Mobile, string E_Subscriber_Type, string UserID, string DeviceID

        PropertyInfo customerAccountNumber = new PropertyInfo();
        customerAccountNumber.setName("E_AccountNo");
        customerAccountNumber.setValue(Wallet);
        customerAccountNumber.setType(String.class);
        request.addProperty(customerAccountNumber);

        PropertyInfo customerPin = new PropertyInfo();
        customerPin.setName("E_PIN");
        customerPin.setValue(PIN);
        customerPin.setType(String.class);
        request.addProperty(customerPin);

        PropertyInfo OTP = new PropertyInfo();
        OTP.setName("E_OTP");
        OTP.setValue(strOTP);
        OTP.setType(String.class);
        request.addProperty(OTP);

        PropertyInfo merchantAccountNumber = new PropertyInfo();
        merchantAccountNumber.setName("E_Amount");
        merchantAccountNumber.setValue(strAmount);
        merchantAccountNumber.setType(String.class);
        request.addProperty(merchantAccountNumber);

        PropertyInfo amount = new PropertyInfo();
        amount.setName("E_Destination_Mobile");
        amount.setValue(strDesMbl);
        amount.setType(String.class);
        request.addProperty(amount);

        PropertyInfo otp = new PropertyInfo();
        otp.setName("E_Subscriber_Type");
        otp.setValue(strSubscriberType);
        otp.setType(String.class);
        request.addProperty(otp);


        PropertyInfo encryptMasterKey = new PropertyInfo();
        encryptMasterKey.setName("UserID");
        encryptMasterKey.setValue(GlobalData.getStrUserId());
        encryptMasterKey.setType(String.class);
        request.addProperty(encryptMasterKey);


        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(GlobalData.getStrDeviceId());
        masterKey.setType(String.class);
        request.addProperty(masterKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objMakePayment = null;
        String strMakePaymentResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 10000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objMakePayment = envelope.getResponse();
            strMakePaymentResponse = objMakePayment.toString();
            mStrServerResponse = strMakePaymentResponse;

//            Merchant Payment successful on 20-Mar-18 04:06:03Sent to: 00611000000561 Amount Tk.5,320.00
//            Balance: Tk.92,480.00
//            TXN ID: 18032000000086
//            QPAY//Merchant Payment Received From: 00612000000532 on 20-Mar-18 04:06:03
//            Amount: Tk.5,320.00
//            Balance: Tk.7,520.00
//            TXN ID: 18032000000086
//            QPAY,*AP180320160903

//            Merchant are not allow for this transactionQPAY,*AP180320160638

//            The customer OTP is wrong please insert correct OTP.,*AP180320161231

            int intIndex = mStrServerResponse.indexOf("successful");
            if (intIndex == -1) {
                int intIndex1 = mStrServerResponse.indexOf("not allow");
                if (intIndex1 == -1) {

                } else {
                    String[] parts = mStrServerResponse.split(",");
                    String strResponse = parts[0];
                    String strExtra = parts[1];
                    mStrServerResponse = strResponse;
                }
                int intIndex2 = mStrServerResponse.indexOf("wrong");
                if (intIndex2 == -1) {

                } else {
                    String[] parts = mStrServerResponse.split(",");
                    String strResponse = parts[0];
                    String strExtra = parts[1];
                    mStrServerResponse = strResponse;
                }
            } else {
                String[] parts = mStrServerResponse.split("//");
                String strResponse = parts[0];//
                String strExtra = parts[1];
                mStrServerResponse = strResponse;
            }

        } catch (Exception exception) {
            mStrServerResponse = strMakePaymentResponse;
        }
    }

    private void clearEditText() {
        mEditTextAmount.setText("");
        mEditTextDestinationMobileNumber.setText("");
    }



}
