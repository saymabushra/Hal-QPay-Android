package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.CheckMasterKeyAndSessionId;
import com.haltechbd.app.android.qpay.utils.DeviceRegistration;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetBalance;
import com.haltechbd.app.android.qpay.utils.GetLastTransaction;
import com.haltechbd.app.android.qpay.utils.GetMasterKeyAndAccountNumber;
import com.haltechbd.app.android.qpay.utils.GetQrCodeContent;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Login extends Activity implements View.OnClickListener {
    private ProgressDialog mProgressDialog = null;

    //#############################################################
    private SharedPreferences mSharedPreferencsLogin, msharePrefOfflinePin;
    private SharedPreferences.Editor mSharedPreferencsLoginEditor, mshareprefOfflinePINEditor;
    // private String isRememberMe;
    //#############################################################

    private EncryptionDecryption encryptDecrypt = new EncryptionDecryption();
    //  private Encryption encryption = new Encryption();

    private ImageButton mImgBtnInfo;
    private EditText mEditTextUserId, mEditTextPin;
    private CheckBox mCheckBoxRememberMe;
    private Button mBtnLogin;
    private TextView mTextViewForgetPin, mTextViewRegistration, mTextViewShowServerResponse, mTextViewOfflineLogin;
    private String mStrUserId, mStrPin, mStrMasterKey,
            mStrEncryptUserId, mStrEnycryptPin, mStrEncryptPackage,
            mStrEncryptAccountNumber, mStrEncryptAccountHolderName,
            mStrPackage, mStrAccountNumber, mStrAccountHolderName,
            mStrSessionId, mStrServerResponse, mStrTerminalName,
            mStrQrCodeContent, mStrDeviceId, mStrEnycryptDeviceId,
            mStrWallet, mStrDeviceRegistrationStatus, mStrEncryptAccountRank,
            mStrAccountRank, mStrDecryptSessionId, mStrAccountListString, strGetofflineuser, strGetOflinePIN,mstrAccountStatus,mstrEncryptedAccountStatus,
            strCallNumber,mStrLastTransaction,mstrEncryptedIdentificationinfo,mStrIdentificationinfo,mStrEncryptAllAccountlist,mStrEncryptlastTransation;

    @SuppressLint({"NewApi", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        checkOS();
        initUI();
    }

    private void checkOS() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void initUI() {
        mEditTextUserId = findViewById(R.id.editTextLoginUserId);
        mImgBtnInfo = findViewById(R.id.imageBtnInfo);
        mImgBtnInfo.setOnClickListener(this);
        mEditTextPin = findViewById(R.id.editTextLoginPin);
        mCheckBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        mTextViewForgetPin = findViewById(R.id.txtViewForgetPin);
        mTextViewOfflineLogin = findViewById(R.id.txtViewOfflineLogin);
        mTextViewForgetPin.setOnClickListener(this);
        mTextViewOfflineLogin.setOnClickListener(this);
        mBtnLogin = findViewById(R.id.btnLoginLogin);
        mBtnLogin.setOnClickListener(this);
        mTextViewRegistration = findViewById(R.id.txtViewRegistration);
        mTextViewRegistration.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewLoginShowServerResponse);
        //######## Device Model #########
        //######## Device Model #########
        //######## Device Model #########
        mStrTerminalName = Build.MODEL;
        //######## Device ID #########
        //######## Device ID #########
        //######## Device ID #########
        mStrDeviceId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);



//        String serialnum = null;
//
//        try {
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class, String.class );
//            mStrDeviceId = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );
//        }
//        catch (Exception ignored)
//        {
//        }


        //############# Check Internet ##############
        //############# Check Internet ##############
        //############# Check Internet ##############
                   mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
                    String strAutoCheck = mSharedPreferencsLogin.getString("rememberCredentials", "");
                    if(strAutoCheck.equalsIgnoreCase("check"))
                    { mCheckBoxRememberMe.setChecked(true);
                    }
                    else
                        {
                        mCheckBoxRememberMe.setChecked(false);
                    }

        mCheckBoxRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
                    // String strLogout = mSharedPreferencsLogin.getString("loguotcredentials", "");

//                        if(strLogout.isEmpty() || strLogout.equalsIgnoreCase("")) {
                    mSharedPreferencsLoginEditor.putString("rememberCredentials", "check");
                    mSharedPreferencsLoginEditor.commit();
                } else {
                    mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
                    // String strLogout = mSharedPreferencsLogin.getString("loguotcredentials", "");

//                        if(strLogout.isEmpty() || strLogout.equalsIgnoreCase("")) {
                    mSharedPreferencsLoginEditor.putString("rememberCredentials", "");
                    mSharedPreferencsLoginEditor.commit();
                }

            }
        });


        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mImgBtnInfo) {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Login.this);
            mAlertDialogBuilder.setMessage("You can only use one account per device. If you want to use another account from this device, please tag your account.");
            mAlertDialogBuilder.setNegativeButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            GlobalData.setStrUserId(mEditTextUserId.getText().toString());
                            startActivity(new Intent(Login.this, TagDevice.class));
                            finish();
                        }
                    });
            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
            mAlertDialog.show();
        }
        if (v == mBtnLogin) {

            if (mEditTextUserId.getText().toString().length() == 0) {
                mEditTextUserId.setError("Field cannot be empty");
            } else if (mEditTextPin.getText().toString().length() == 0) {
                mEditTextPin.setError("Field cannot be empty");
            } else if (mEditTextPin.getText().toString().length() < 4) {
                mEditTextPin.setError("Must be 4 characters in length");
            } else {
               // checkInternet();
                login();
            }

        } else if (v == mTextViewForgetPin) {

            startActivity(new Intent(Login.this, ForgetPin.class));
            finish();
        } else if (v == mTextViewRegistration) {
            startActivity(new Intent(Login.this, SignUp.class));
            finish();
        }
        else if (v == mTextViewOfflineLogin)
        {

            //---------get offline pin user fron share prefes--------------
            msharePrefOfflinePin = getSharedPreferences("OfflinePINPrefs", MODE_PRIVATE);
            mshareprefOfflinePINEditor = msharePrefOfflinePin.edit();
            strGetofflineuser = msharePrefOfflinePin.getString("offlineuser", "");
            strGetOflinePIN = msharePrefOfflinePin.getString("offlinepin", "");
            //---------------------------------------------------------------
            if (mEditTextUserId.getText().toString().length() == 0) {
                mEditTextUserId.setError("Field cannot be empty");
            } else if (mEditTextPin.getText().toString().length() == 0) {
                mEditTextPin.setError("Field cannot be empty");
            } else if (mEditTextPin.getText().toString().length() < 4) {
                mEditTextPin.setError("Must be 4 characters in length");
            } else {
                if (mEditTextUserId.getText().toString().equalsIgnoreCase(strGetofflineuser)) {
                    if (mEditTextPin.getText().toString().equalsIgnoreCase(strGetOflinePIN)) {
                        startActivity(new Intent(Login.this, Offline_Login.class));
                        finish();
                    } else {
                        mTextViewShowServerResponse.setText("Wrong PIN!!");

                    }
                } else {
                    mTextViewShowServerResponse.setText("User id not match!!");
                }
            }
        }
    }

    private void login() {

            if (mStrDeviceRegistrationStatus.equalsIgnoreCase("Device is Registered")) {
                // ########## Device Register #############
                // ########## Device Register #############
                // ########## Device Register #############
                String strDeviceActiveStatus = CheckDeviceActiveStatus.checkDeviceActiveStatus(mStrDeviceId);
                if (strDeviceActiveStatus.equals("A")) {
                    // ############################ Active ############################
                    // ############################ Active ############################
                    // ############################ Active ############################
                    mStrUserId = mEditTextUserId.getText().toString();
                    mStrPin = mEditTextPin.getText().toString();

                    //##########################################################################################
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditTextUserId.getWindowToken(), 0);
                    //##########################################################################################

                    //#############################################################################################
//                    if (mCheckBoxRememberMe.isChecked())
//                    {
//                        mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                        mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
//                       // String strLogout = mSharedPreferencsLogin.getString("loguotcredentials", "");
//
////                        if(strLogout.isEmpty() || strLogout.equalsIgnoreCase("")) {
//                           // mSharedPreferencsLoginEditor.putBoolean("rememberCredentials", true);
//                            mSharedPreferencsLoginEditor.putString("userid", mStrUserId);
//                            mSharedPreferencsLoginEditor.putString("pin_submission", mStrPin);
//                            mSharedPreferencsLoginEditor.commit();
//
//                    } else {
//                        mSharedPreferencsLoginEditor.clear();
//                        mSharedPreferencsLoginEditor.commit();
//                    }
                    //##############################################################################################

                    String strMasterKeyAndAccountNumber = GetMasterKeyAndAccountNumber.getMasterKeyByUserId(mStrUserId, GlobalData.getStrRegistrationSecurityKey());
                    int intIndexOtp = strMasterKeyAndAccountNumber.indexOf("\\*");
                    if (intIndexOtp == -1) {
                        if (strMasterKeyAndAccountNumber.equalsIgnoreCase("Master key is null")) {
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Login.this);
                            myAlert.setMessage("Invalid User ID or PIN");
                            myAlert.setNegativeButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            //mSharedPreferencsLoginEditor.clear();
                                          //  mSharedPreferencsLoginEditor.commit();

                                        }
                                    });
                            AlertDialog alertDialog = myAlert.create();
                            alertDialog.show();
                        } else if (strMasterKeyAndAccountNumber.equalsIgnoreCase("Invalid Key")) {
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Login.this);
                            myAlert.setMessage("Invalid User ID or PIN");
                            myAlert.setNegativeButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                          //  mSharedPreferencsLoginEditor.clear();
                                           // mSharedPreferencsLoginEditor.commit();
                                        }
                                    });
                            AlertDialog alertDialog = myAlert.create();
                            alertDialog.show();
                        } else {
                            String[] parts = strMasterKeyAndAccountNumber.split("\\*");
                            mStrMasterKey = parts[0];//96745482897185504726639965371045
                            mStrEncryptAccountNumber = parts[1];//0021200000007

                            try {
                                //mStrEncryptUserId = encryptDecrypt.Encrypt(mStrUserId, mStrMasterKey);
                                mStrEnycryptPin = encryptDecrypt.Encrypt(mStrPin, mStrMasterKey);
                                // mStrEnycryptDeviceId = encryptDecrypt.Encrypt(mStrDeviceId, mStrMasterKey);
                                String strCheckDeviceId = CheckDeviceActiveStatus.checkDeviceIdByAccountNumber(mStrUserId, mStrDeviceId, mStrEncryptAccountNumber);
                                if (strCheckDeviceId.equalsIgnoreCase("Match")) {

                                    String strCheckMasterKey = CheckMasterKeyAndSessionId.checkMasterKey(mStrMasterKey, mStrEncryptAccountNumber);
                                    if (strCheckMasterKey.equals("Right key")) {
                                        // Initialize progress dialog
                                        mProgressDialog = ProgressDialog.show(Login.this, null, "Login...", false, true);
                                        // Cancel progress dialog on back key press
                                        mProgressDialog.setCancelable(true);

                                        Thread t = new Thread(new Runnable() {

                                            @Override
                                            public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                                                doLogin(mStrUserId, mStrEnycryptPin, mStrDeviceId);//QPAY_Login(UserID,PIN,strMasterKey)
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
                                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                                {
                                                                    mStrServerResponse="Try Again";
                                                                }
                                                                mTextViewShowServerResponse.setText(mStrServerResponse);
                                                                if(mStrServerResponse.equalsIgnoreCase("Login Successfully"))
                                                                {
                                                                    startActivity(new Intent(Login.this, MainActivity.class));
                                                                    mEditTextUserId.setText("");
                                                                    mEditTextPin.setText("");
                                                                    mTextViewShowServerResponse.setText("");
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

                                    } else {
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Login.this);
                                        myAlert.setMessage(strCheckMasterKey);
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
                                } else {
                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(Login.this);
                                    myAlert.setMessage(strCheckDeviceId);
                                    myAlert.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    startActivity(new Intent(Login.this, TagDevice.class));
                                                    finish();
                                                }
                                            });
                                    myAlert.setNegativeButton(
                                            "Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = myAlert.create();
                                    alertDialog.show();
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } else {
                    // ############################ Inactive ############################
                    // ############################ Inactive ############################
                    // ############################ Inactive ############################
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Login.this);
                    mAlertDialogBuilder.setMessage("Device is not Active. Please register to use this app.");
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
            } else {
                // ########## Device Not Register #############
                // ########## Device Not Register #############
                // ########## Device Not Register #############
                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Login.this);
                mAlertDialogBuilder.setMessage(mStrDeviceRegistrationStatus);
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

    // Login Button
    public void doLogin(String strUserId,
                        String strEncryptPin,
                        String strDeviceID) {
        String METHOD_NAME = "QPAY_Login";
        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Login";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //QPAY_Login(UserID,PIN,strMasterKey)
        PropertyInfo encryptUserId = new PropertyInfo();
        encryptUserId.setName("UserID");
        encryptUserId.setValue(strUserId);
        encryptUserId.setType(String.class);
        request.addProperty(encryptUserId);

        PropertyInfo encryptPin = new PropertyInfo();
        encryptPin.setName("E_PIN");
        encryptPin.setValue(strEncryptPin);
        encryptPin.setType(String.class);
        request.addProperty(encryptPin);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(strDeviceID);
        masterKey.setType(String.class);
        request.addProperty(masterKey);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.implicitTypes = true;
        Object objLoginResponse = null;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 200000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objLoginResponse = envelope.getResponse();
            String strLoginResponse = objLoginResponse.toString();// Login Response
            int intIndexOtpResponse = strLoginResponse.indexOf("Login Successfully");
            if (intIndexOtpResponse == -1) {
                mStrServerResponse = "Login failed. Please insert correct User ID and PIN.";
                mSharedPreferencsLoginEditor.clear();
                mSharedPreferencsLoginEditor.commit();
            } else {

                //------------------------------
                if (mCheckBoxRememberMe.isChecked()) {
                    mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
                    // String strLogout = mSharedPreferencsLogin.getString("loguotcredentials", "");

//                        if(strLogout.isEmpty() || strLogout.equalsIgnoreCase("")) {
                    mSharedPreferencsLoginEditor.putString("rememberCredentials", "check");
                    mSharedPreferencsLoginEditor.putString("userid", mStrUserId);
                    mSharedPreferencsLoginEditor.putString("pin_submission", mStrPin);
                    mSharedPreferencsLoginEditor.commit();
//                        }
//                        else
//                        {
//
//                        }
                }
//                    else {
//                        mSharedPreferencsLoginEditor.putString("rememberCredentials","");
//                        mSharedPreferencsLoginEditor.putString("userid", "");
//                        mSharedPreferencsLoginEditor.putString("pin_submission", "");
//                        mSharedPreferencsLoginEditor.commit();
//                    }
                //-------------------------------
                String[] parts = strLoginResponse.split("\\&");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                String strLoginStatus = parts[0];//Login Successfully
                mStrEncryptPackage = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                mStrEncryptAccountNumber = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                mStrEncryptAccountHolderName = parts[3];
                mStrEncryptAccountRank = parts[4];
                mstrEncryptedAccountStatus = parts[5];
                String Accountid = parts[6];
                 mstrEncryptedIdentificationinfo=parts[7];
                 mStrEncryptAllAccountlist=parts[8];
                 mStrEncryptlastTransation=parts[9];
                //Decrypt Values
                //Decrypt Values
                //Decrypt Values
                decryptionValues();
                // All Wallet
                // All Wallet
                // All Wallet
                // mStrVoucherWallet = mStrAccountNumber + 1;
//                mStrCanteenWallet = mStrAccountNumber + 1;
//                mStrSimBillWallet = mStrAccountNumber + 1;

                //Get Session ID
                //Get Session ID
                //Get Session ID
                mStrSessionId = CheckMasterKeyAndSessionId.getSessionId(mStrEncryptAccountNumber, mStrUserId, mStrDeviceId);
                if (mStrSessionId.toString().equalsIgnoreCase("Session key not update")) {
                    mStrServerResponse = "Session key not update";
                } else if (mStrSessionId.toString().equalsIgnoreCase("Session key null")) {
                    mStrServerResponse = "Session key null";
                } else if (mStrSessionId.toString().equalsIgnoreCase("Account null")) {
                    mStrServerResponse = "Account null";
                } else if (mStrSessionId.toString().equalsIgnoreCase("Invalid Key")) {
                    mStrServerResponse = "Invalid Key";
                } else {

                    // Check Session ID

                    String strCheckSessionID = CheckMasterKeyAndSessionId.checkSessionId(mStrSessionId, mStrUserId, mStrDeviceId);
                    if (strCheckSessionID.equalsIgnoreCase("Match")) {


//                                   String strGetList= CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber,mStrUserId,mStrDeviceId);
//
//                                   if(strGetList.contains("&"))
//                                   {
                        // mStrAccountListString=strGetList;
                        // Decrypt Session ID
                        mStrDecryptSessionId = encryptDecrypt.Decrypt(mStrSessionId, mStrMasterKey);

                        //==================== get identification info for profile picture============================
                        try {
//                            mStrEncryptAccountNumber=encryptDecrypt.Encrypt(mStrAccountNumber,mStrDecryptSessionId);
//                            String mStrEncryptPin=encryptDecrypt.Encrypt(mStrPin, mStrDecryptSessionId);
//                            String mStrEncryptIdentificationId = encryptDecrypt.Encrypt("140824000000000001", mStrDecryptSessionId);
//                            String  mStrEncryptIdentificationNumber = encryptDecrypt.Encrypt("", mStrDecryptSessionId);
//                            String mStrEncryptRemark = encryptDecrypt.Encrypt("", mStrDecryptSessionId);
//                            String mstrparameten = encryptDecrypt.Encrypt("G", mStrDecryptSessionId);
//                            String mstrPictureID = encryptDecrypt.Encrypt("", mStrDecryptSessionId);
//                            String mstrPictureIDback = encryptDecrypt.Encrypt("", mStrDecryptSessionId);
//                            String  strgetIdentificationInfo = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
//                                    mStrEncryptRemark, mstrparameten, mstrPictureID,mstrPictureIDback);
//                          GlobalData.setStrIdentificationInfo(strgetIdentificationInfo);
//
//                            if (strgetIdentificationInfo.equalsIgnoreCase("No Data Found")) {
//
//                            } else {
//
//                                String[] partsPic = strgetIdentificationInfo.split("\\*");
//                                String strIdentficationNo = partsPic[0];
//                                String strRemarks = partsPic[1];
//                                String strIPictureId = partsPic[2];
//                                String strIdentificationiDget = partsPic[3];
//                                String strPictureIDBack = partsPic[4];
//
////                                if(!strIPictureId.equalsIgnoreCase("No Data Found"))
////                                {
////                                    String imageEncoded = InsertKyc.KycGetDocument(strIPictureId);
////                                    // String imageEncoded = Base64.encodeToString(strImage, Base64.DEFAULT);
////                                    Bitmap bitImage=  decodeBase64(imageEncoded);
////                                    GlobalData.setStrProPicBitmap(bitImage);
////                                   // mImgViewProfilePic.setImageBitmap(bitImage);
////
////
////
////
////
////                                }
////                                else
////                                {
////
////                                }
//                            }
                            //===============================================
//                            String strGetList= CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber,mStrUserId,mStrDeviceId);
//                            mStrAccountListString=strGetList;
                            // Account list

//                                    String strVoucherBalance = GetBalance.getBalance(mStrUserId,strEncryptedPIN, strEncWallet, mStrDeviceId); // balance
//                                    mStrQrCodeContent = GetQrCodeContent.getQrCode(strEncWallet,mStrUserId, mStrDeviceId); // QR code


                            //=======================get last transaction=======================
//                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                            String strCurrentDate = sdf.format(new Date());

//                            String  mStrEncryptFromDate = encryptDecrypt.Encrypt(strCurrentDate,  mStrDecryptSessionId);
//                            String mStrEncryptToDate = encryptDecrypt.Encrypt(strCurrentDate,  mStrDecryptSessionId);
//                            String  mStrEncryptAccessCode = encryptDecrypt.Encrypt("all",  mStrDecryptSessionId);
//                            String  mStrEncryptParameter = encryptDecrypt.Encrypt("1",  mStrDecryptSessionId);

//                            String strLastTransaction = GetLastTransaction.getLastTransaction(
//                                    mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptFromDate,
//                                    mStrEncryptToDate, mStrEncryptAccessCode, mStrEncryptParameter, mStrUserId,mStrDeviceId);
//
//                            if (strLastTransaction != null && !strLastTransaction.isEmpty()) {
//                                if (strLastTransaction.equalsIgnoreCase("No data found"))
//                                {
//                                    mStrLastTransaction = "No Transaction Info Found.";
//                                }
//                                else if (strLastTransaction.equalsIgnoreCase("anyType{}"))
//                                {
//                                    mStrLastTransaction = "No Transaction Info Found.";
//                                }
//                                else
//                                {
//                                    mStrLastTransaction = strLastTransaction;
//                                }
//                            }
//                            else
//                            {
//                                mStrLastTransaction = "No Transaction Info Found.";
//                            }
                            //===============================================

                           // get call center number
                            strCallNumber=CheckDeviceActiveStatus.GetHelpLinePhnoeNo(mStrUserId, mStrDeviceId);

                            //===================================================
                            if(!strCallNumber.equalsIgnoreCase("")) {
                                setGlobalValues();

                                mStrServerResponse = "Login Successfully";
                            }
                        }
                        catch (Exception ex)
                        {

                        }
                        //============== profile picture end =============================
                        //Set All Values in GlobalData

                        //startActivity(new Intent(Login.this, MainActivity.class));


//                                   }
//                                   else
//                                   {
//                                       mStrServerResponse =strGetList;
//                                   }
                    }
                    else
                        {
                        mStrServerResponse = strCheckSessionID.toString();
                    }

                }

            }

        } catch (Exception exception) {
            mStrServerResponse = "Login failed. Please insert correct User ID and PIN.";
            mSharedPreferencsLoginEditor.clear();
            mSharedPreferencsLoginEditor.commit();
        }
    }


    private void decryptionValues() {
        try {
            mStrPackage = encryptDecrypt.Decrypt(mStrEncryptPackage, mStrMasterKey);
            mStrAccountNumber = encryptDecrypt.Decrypt(mStrEncryptAccountNumber, mStrMasterKey);
            mStrAccountHolderName = encryptDecrypt.Decrypt(mStrEncryptAccountHolderName, mStrMasterKey);
            mStrAccountRank = encryptDecrypt.Decrypt(mStrEncryptAccountRank, mStrMasterKey);
            mstrAccountStatus= encryptDecrypt.Decrypt(mstrEncryptedAccountStatus, mStrMasterKey);
            mStrIdentificationinfo=encryptDecrypt.Decrypt(mstrEncryptedIdentificationinfo,mStrMasterKey);
            mStrLastTransaction=encryptDecrypt.Decrypt(mStrEncryptlastTransation,mStrMasterKey);
            mStrAccountListString=encryptDecrypt.Decrypt(mStrEncryptAllAccountlist,mStrMasterKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private void setGlobalValues() {
        GlobalData.setStrDeviceId(mStrDeviceId);
        GlobalData.setStrDeviceName(mStrTerminalName);
        GlobalData.setStrUserId(mStrUserId);

        GlobalData.setStrEncryptPin(mStrEnycryptPin);
        GlobalData.setStrMasterKey(mStrMasterKey);
        GlobalData.setStrPin(mStrPin);
        GlobalData.setStrAccountNumber(mStrAccountNumber);
        // GlobalData.setStrEncryptAccountNumber(mStrEncryptAccountNumber);
        //GlobalData.setStrCustomerAccountL14(mStrWallet);
        GlobalData.setStrAccountHolderName(mStrAccountHolderName);
        //  GlobalData.setStrQrCodeContent(mStrQrCodeContent);
        GlobalData.setStrPackage(mStrPackage);
        //  GlobalData.setStrAccountRank(mStrAccountRank);
        GlobalData.setStrSessionId(mStrDecryptSessionId);
        GlobalData.setStrAccountStatus(mstrAccountStatus);
        GlobalData.setStrGetAccountList(mStrAccountListString);
        GlobalData.setStrCallCenterPhoneNumber(strCallNumber);
        GlobalData.setStrlastTransaction(mStrLastTransaction);
        GlobalData.setStrIndetificationInfoPicture(mStrIdentificationinfo);
        //GlobalData.setStrWallet(mStrWallet);
       //  GlobalData.setAccountListString(mStrAccountListString);

//-------------------------------------------------------

        //  GlobalData.setStrEncryptPackage(mStrEncryptPackage);
        //  GlobalData.setStrEncryptAccountRank(mStrEncryptAccountRank);


        // GlobalData.setStrWallet(mStrVoucherWallet);
        // GlobalData.setStrEncryptUserId(mStrEncryptUserId);
        //

//---------------------------------------------------

    }

    private void checkInternet() {
        if (isNetworkConnected()) {

            try {
                //########################## Online ###########################
                //########################## Online ###########################
                //########################## Online ###########################

                // ########## Device Registration #############
                // ########## Device Registration #############
                // ########## Device Registration #############
                mStrDeviceRegistrationStatus = DeviceRegistration.doDeviceRegistration(mStrTerminalName, mStrDeviceId, GlobalData.getStrChannelType(), GlobalData.getStrSecurityKey());
                if (mStrDeviceRegistrationStatus == "") {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Login.this);
                    mAlertDialogBuilder.setTitle("Network Error");
                    mAlertDialogBuilder.setMessage("Try Again");
                    mAlertDialogBuilder.setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                    mAlertDialog.show();
//                    mStrDeviceRegistrationStatus = "Try Again";
                } else {
                    GlobalData.setStrDeviceId(mStrDeviceId);


                    //############## Auto Login Check ##############
                    //############## Auto Login Check ##############
                    //############## Auto Login Check ##############


                    mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
                    String strAutoCheck = mSharedPreferencsLogin.getString("rememberCredentials", "");
                    // String strLogout=mSharedPreferencsLogin.getString("loguotcredentials", "");
                    //############# Login and Set Login Credentials ##############
                    //############# Login and Set Login Credentials ##############

                    if (strAutoCheck.equalsIgnoreCase("check")) {
                        mCheckBoxRememberMe.setChecked(true);
                        String strUser = mSharedPreferencsLogin.getString("userid", "");
                        String strPassword = mSharedPreferencsLogin.getString("pin_submission", "");
                        if (strUser.isEmpty() && strUser.equalsIgnoreCase("")) {
                           String strGetUser = mEditTextUserId.getText().toString();
                            mSharedPreferencsLoginEditor.putString("userid", strGetUser);
                            mSharedPreferencsLoginEditor.commit();
                        } else {
                            mEditTextUserId.setText(mSharedPreferencsLogin.getString("userid", ""));
                        }
                        if (strPassword.isEmpty() && strPassword.equalsIgnoreCase("")) {
                           String strGetPassword = mEditTextPin.getText().toString();
                            mSharedPreferencsLoginEditor.putString("pin_submission", strGetPassword);
                            mSharedPreferencsLoginEditor.commit();
                        } else {
                            mEditTextPin.setText(mSharedPreferencsLogin.getString("pin_submission", ""));
                        }

                        if(! strUser.equalsIgnoreCase("") &&  !strPassword.equalsIgnoreCase(""))
                        {
                            login();
                        }
                    }
                    else
                        {
                        mCheckBoxRememberMe.setChecked(false);
                    }

//                    String strGetUser, strGetPassword;
//                    if (mCheckBoxRememberMe.isChecked()) {
//
//                        String strUser = mSharedPreferencsLogin.getString("userid", "");
//                        String strPassword = mSharedPreferencsLogin.getString("pin_submission", "");
//                        if (strUser.isEmpty() && strUser.equalsIgnoreCase("")) {
//                            strGetUser = mEditTextUserId.getText().toString();
//                            mSharedPreferencsLoginEditor.putString("userid", strGetUser);
//                            mSharedPreferencsLoginEditor.commit();
//                        } else {
//                            mEditTextUserId.setText(mSharedPreferencsLogin.getString("userid", ""));
//                        }
//                        if (strPassword.isEmpty() && strPassword.equalsIgnoreCase("")) {
//                            strGetPassword = mEditTextPin.getText().toString();
//                            mSharedPreferencsLoginEditor.putString("pin_submission", strGetPassword);
//                            mSharedPreferencsLoginEditor.commit();
//                        } else {
//                            mEditTextPin.setText(mSharedPreferencsLogin.getString("pin_submission", ""));
//                        }
//
//
//                        // mCheckBoxRememberMe.setChecked(true);
//                        login();
//                    } else {
//                        mCheckBoxRememberMe.setChecked(false);
//                        login();
//                    }
                }
//            }
//            else
//            {
//                mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
//                mSharedPreferencsLoginEditor.putString("loguotcredentials", "");
//
//                mSharedPreferencsLoginEditor.commit();
//
//            }
                }catch(Exception e){
                    e.fillInStackTrace();
                }


        } else {
            //########################## Offline ###########################
            //########################## Offline ###########################
            //########################## Offline ###########################
            //disableUiComponents();

            //########################## Dialog ###########################
            //########################## Dialog ###########################
            //########################## Dialog ###########################
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Login.this);
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

//            final Dialog dialog = new Dialog(Login.this);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setContentView(R.layout.dialog_layout_one_buttons);
//            TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
//            TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessage);
//            TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperation);
//            Button btnYes = (Button) dialog.findViewById(R.id.btnOK);
//            //Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
//            textViewTitle.setText("No Internet Connection");
//            textViewMessage.setText("It looks like your internet connection is off. Please turn it on and try again.");
//            textViewOperation.setText("Action");
//            dialog.show();
//
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.cancel();
//
//                  //  Toast.makeText(getApplicationContext(), "Your Choose YES", Toast.LENGTH_LONG).show();
//                }
//            });
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), "Your Choose NO", Toast.LENGTH_LONG).show();
//                    dialog.cancel();
//                }
//            });

        }
    }

//    public static Bitmap decodeBase64(String input) {
//        byte[] decodedByte = Base64.decode(input, 0);
//        return BitmapFactory
//                .decodeByteArray(decodedByte, 0, decodedByte.length);
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void enableUiComponents() {
        mEditTextUserId.setEnabled(true);
        mEditTextPin.setEnabled(true);
        mCheckBoxRememberMe.setEnabled(true);
        mTextViewRegistration.setEnabled(true);
        mBtnLogin.setEnabled(true);
        mTextViewForgetPin.setEnabled(true);
        mImgBtnInfo.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextUserId.setEnabled(false);
        mEditTextPin.setEnabled(false);
        mCheckBoxRememberMe.setEnabled(false);
        mTextViewRegistration.setEnabled(false);
        mBtnLogin.setEnabled(false);
        mTextViewForgetPin.setEnabled(false);
        mImgBtnInfo.setEnabled(false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // startActivity(new Intent(Login.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}