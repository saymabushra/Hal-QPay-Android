package com.haltechbd.app.android.qpay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerateOtp extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_LOCATION = 1;
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    //#############################################################
    private SharedPreferences mSharedPreferencsOtp;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor;
    //#############################################################

    // initialize all ui components
    private EditText mEditTextNumberOfTransaction, mEditTextOtpDuration, mEditTextOtp;
    private Button mBtnGenerateOtp, mBtnSaveOtp;
    private TextView mTextViewShowServerResponse;
    private String mStrMasterKey, mStrNumberOfTransaction, mStrOtpDuration,
            mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptNumberOfTransaction, mStrEncryptOtpDuration,
            mStrOtpFromSMS, mStrServerResponse, strOtpFromDevice;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_LOCATION);
        checkOs();
        initUI();
    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextNumberOfTransaction = findViewById(R.id.editTextNumberOfTransaction);
        mEditTextOtpDuration = findViewById(R.id.editTextDuration);
        mBtnGenerateOtp = findViewById(R.id.btnGenerateOtp);
        mBtnGenerateOtp.setOnClickListener(this);
        mEditTextOtp = findViewById(R.id.editTextGenerateOtpOtp);
        mEditTextOtp.requestFocus();
        mBtnSaveOtp = findViewById(R.id.btnGenerateOtpSaveOtp);
        mBtnSaveOtp.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewOtpShowServerResponse);
        mStrMasterKey = GlobalData.getStrMasterKey();

        //######################################################################################################
        mSharedPreferencsOtp = getSharedPreferences("otpPrefs", MODE_PRIVATE);
        mSharedPreferencsOtpEditor = mSharedPreferencsOtp.edit();
        //#######################################################################################################

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnGenerateOtp) {
            if (mEditTextNumberOfTransaction.getText().toString().length() == 0) {
                mEditTextNumberOfTransaction.setError("Field cannot be empty");
            } else if (mEditTextOtpDuration.getText().toString().length() == 0) {
                mEditTextOtpDuration.setError("Field cannot be empty");
            } else {
                try {

                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    mStrEncryptNumberOfTransaction = encryption.Encrypt(mEditTextNumberOfTransaction.getText().toString(), GlobalData.getStrSessionId());
                    if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("5 Min")) {
                        mStrOtpDuration = "5";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("30 Min")) {
                        mStrOtpDuration = "30";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("1 Week")) {
                        mStrOtpDuration = "10080";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("1 Month")) {
                        mStrOtpDuration = "43200";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("6 Month")) {
                        mStrOtpDuration = "262800";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("1 Year")) {
                        mStrOtpDuration = "525600";
                    } else {
                        mStrOtpDuration = mEditTextOtpDuration.getText().toString();
                    }
                    mStrEncryptOtpDuration = encryption.Encrypt(mStrOtpDuration, GlobalData.getStrSessionId());

                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(GenerateOtp.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            generateOtp(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptNumberOfTransaction,
                                    mStrEncryptOtpDuration, GlobalData.getStrMasterKey());
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                            //####################### Show Dialog ####################
                                            //####################### Show Dialog ####################
                                            //####################### Show Dialog ####################
                                            if(mStrServerResponse.equalsIgnoreCase(""))
                                            {
                                                mStrServerResponse="Request is in process";
                                            }

                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(GenerateOtp.this);
                                            myAlert.setMessage(mStrServerResponse);
                                            myAlert.setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            mEditTextOtp.setText(mStrOtpFromSMS);
                                                        }
                                                    });
                                            AlertDialog alertDialog = myAlert.create();
                                            alertDialog.show();
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
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        if (v == mBtnSaveOtp) {
            if (mEditTextNumberOfTransaction.getText().toString().length() == 0) {
                mEditTextNumberOfTransaction.setError("Field cannot be empty");
            } else if (mEditTextOtpDuration.getText().toString().length() == 0) {
                mEditTextOtpDuration.setError("Field cannot be empty");
            } else if (mEditTextOtp.getText().toString().length() == 0) {
                mEditTextOtp.setError("Field cannot be empty");
            } else if (mEditTextOtp.getText().toString().length() < 5) {
                mEditTextOtp.setError("Must be 5 characters in length");
            } else {
                try {
                    if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("5 Min")) {
                        mStrOtpDuration = "5";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("30 Min")) {
                        mStrOtpDuration = "30";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("1 Week")) {
                        mStrOtpDuration = "10080";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("1 Month")) {
                        mStrOtpDuration = "43200";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("6 Month")) {
                        mStrOtpDuration = "262800";
                    } else if (mEditTextOtpDuration.getText().toString().equalsIgnoreCase("1 Year")) {
                        mStrOtpDuration = "525600";
                    } else {
                        mStrOtpDuration = mEditTextOtpDuration.getText().toString();
                    }
                    int intOtpDurationInMin = Integer.parseInt(mStrOtpDuration);
                    String strOtp = mEditTextOtp.getText().toString();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    // Current Time
                    Date currentTime = Calendar.getInstance().getTime();
                    String strCurrentTime = df.format(currentTime);
                    //Expire Time
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(df.parse(strCurrentTime));
                    calendar.add(Calendar.MINUTE, intOtpDurationInMin);
                    String strExpireTime = df.format(calendar.getTime());
                    //##########################################################################################
                    //##########################################################################################
                    //##########################################################################################
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditTextNumberOfTransaction.getWindowToken(), 0);
                    mSharedPreferencsOtpEditor.putString("otp_expire_time", strExpireTime);
                    mSharedPreferencsOtpEditor.putString("generate_otp", strOtp);
                    mSharedPreferencsOtpEditor.commit();
                    //##########################################################################################
                    //##########################################################################################
                    //##########################################################################################

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mStrServerResponse = "OTP save successfully for transaction.";
                //####################### Show Dialog ####################
                //####################### Show Dialog ####################
                //####################### Show Dialog ####################
                AlertDialog.Builder myAlert = new AlertDialog.Builder(GenerateOtp.this);
                myAlert.setMessage(mStrServerResponse);
                myAlert.setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(GenerateOtp.this, MainActivity.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();

            }
        }

    }

    // method for get balance
    public void generateOtp(String strEncryptAccountNumber,
                            String strEncryptPin,
                            String strEncryptMaxNumberOfTransaction,
                            String strEncryptDurationInMin,
                            String strMasterKey) {
        METHOD_NAME = "QPAY_GenerateOTP_With_transaction_Duration";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GenerateOTP_With_transaction_Duration";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        PropertyInfo encryptAccountNumber = new PropertyInfo();
        encryptAccountNumber.setName("E_AccountNo");
        encryptAccountNumber.setValue(strEncryptAccountNumber);
        encryptAccountNumber.setType(String.class);
        request.addProperty(encryptAccountNumber);

        PropertyInfo encryptPin = new PropertyInfo();
        encryptPin.setName("E_PIN");
        encryptPin.setValue(strEncryptPin);
        encryptPin.setType(String.class);
        request.addProperty(encryptPin);

        PropertyInfo encryptMaxNumberOfTransaction = new PropertyInfo();
        encryptMaxNumberOfTransaction.setName("E_MaximiumTransactionNo");
        encryptMaxNumberOfTransaction.setValue(strEncryptMaxNumberOfTransaction);
        encryptMaxNumberOfTransaction.setType(String.class);
        request.addProperty(encryptMaxNumberOfTransaction);

        PropertyInfo encryptDurationInMin = new PropertyInfo();
        encryptDurationInMin.setName("E_TransactionDurationInMiniute");
        encryptDurationInMin.setValue(strEncryptDurationInMin);
        encryptDurationInMin.setType(String.class);
        request.addProperty(encryptDurationInMin);

        PropertyInfo encryptUserId = new PropertyInfo();
        encryptUserId.setName("UserID");
        encryptUserId.setValue(GlobalData.getStrUserId());
        encryptUserId.setType(String.class);
        request.addProperty(encryptUserId);

        PropertyInfo masterKey = new PropertyInfo();
        masterKey.setName("DeviceID");
        masterKey.setValue(GlobalData.getStrDeviceId());
        masterKey.setType(String.class);
        request.addProperty(masterKey);


        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        Log.v("myApp:", request.toString());
        envelope.implicitTypes = true;
        Object objGenerateOtp = null;
        String strOtpResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objGenerateOtp = envelope.getResponse();
            strOtpResponse = objGenerateOtp.toString();
            int intIndex = strOtpResponse.indexOf("Your OTP IS");
            if (intIndex == -1) {
                mStrServerResponse = strOtpResponse;
            } else {
                mStrServerResponse = "Check your SMS for OTP.";

//                //#############################################
//                //#############################################
//                //#############################################
//                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//
//                // Current Time
//                Date currentTime = Calendar.getInstance().getTime();
//                String strCurrentTime = df.format(currentTime);
//
//                //Expire Time
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(df.parse(strCurrentTime));
//                calendar.add(Calendar.MINUTE, mIntOtpDurationInMin);
//                String strExpireTime = df.format(calendar.getTime());
//
//                //##########################################################################################
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mEditTextMaxNumberOfTransaction.getWindowToken(), 0);
//                mSharedPreferencsOtpEditor.putString("otp_expire_time", strExpireTime);
//                mSharedPreferencsOtpEditor.commit();
//                //##########################################################################################
//
//                //#############################################
//                //#############################################
//                //#############################################

                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, "date DESC");
                String sms = "";
                while (cur.moveToNext()) {
                    if (cur.getString(2).equalsIgnoreCase("QPAY")) {
                        if (cur.getString(cur.getColumnIndex("read")).equalsIgnoreCase("0")) {
                            String strText = cur.getString(cur.getColumnIndexOrThrow("body"));
                            if (strText.startsWith("Your OTP IS ")) {
                                sms = cur.getString(cur.getColumnIndexOrThrow("body"));
                                //================
                                String date = cur.getString(cur.getColumnIndexOrThrow("date"));
                                Long timestamp = Long.parseLong(date);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(timestamp);
                                Date finaldate = calendar.getTime();
                                String smsDate = finaldate.toString();

                                //============
                                if (!sms.equalsIgnoreCase("")) {
                                    cur.close();
                                }
                                int intIndexSms = strOtpResponse.indexOf("Your OTP IS");
                                if (intIndexSms == -1) {

                                } else {
                                    String strNumbers = sms.replaceAll("\\D+", "");//90459
                                    String strOtp = strNumbers.substring(0, 5);//90459
                                    mStrOtpFromSMS = strOtp;
                                    mSharedPreferencsOtp = getSharedPreferences("otpPrefs", MODE_PRIVATE);
                                    strOtpFromDevice = mSharedPreferencsOtp.getString("generate_otp", "");
                                    if (strOtpFromDevice.equalsIgnoreCase(mStrOtpFromSMS)) {
                                        mStrOtpFromSMS = "";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            mStrServerResponse = "Check your SMS for OTP.";
        }
    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(GenerateOtp.this);
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

    private void enableUiComponents() {
        mEditTextNumberOfTransaction.setEnabled(true);
        mEditTextOtpDuration.setEnabled(true);
        mBtnGenerateOtp.setEnabled(true);
        mBtnSaveOtp.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextNumberOfTransaction.setEnabled(false);
        mEditTextOtpDuration.setEnabled(false);
        mBtnGenerateOtp.setEnabled(false);
        mBtnSaveOtp.setEnabled(false);
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
                startActivity(new Intent(GenerateOtp.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(GenerateOtp.this, Login.class)
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(GenerateOtp.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onRadioButtonNumberOfTransaction(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_no_of_tranaction_1:
                if (checked)
                    mStrNumberOfTransaction = "1";
                mEditTextNumberOfTransaction.setText(mStrNumberOfTransaction);
                break;
            case R.id.radio_no_of_tranaction_5:
                if (checked)
                    mStrNumberOfTransaction = "5";
                mEditTextNumberOfTransaction.setText(mStrNumberOfTransaction);
                break;
            case R.id.radio_no_of_tranaction_10:
                if (checked)
                    mStrNumberOfTransaction = "10";
                mEditTextNumberOfTransaction.setText(mStrNumberOfTransaction);
                break;
            case R.id.radio_no_of_tranaction_25:
                if (checked)
                    mStrNumberOfTransaction = "25";
                mEditTextNumberOfTransaction.setText(mStrNumberOfTransaction);
                break;
            case R.id.radio_no_of_tranaction_50:
                if (checked)
                    mStrNumberOfTransaction = "50";
                mEditTextNumberOfTransaction.setText(mStrNumberOfTransaction);
                break;
            case R.id.radio_no_of_tranaction_100:
                if (checked)
                    mStrNumberOfTransaction = "100";
                mEditTextNumberOfTransaction.setText(mStrNumberOfTransaction);
                break;
        }
    }

    public void onRadioButtonOtpDuraion(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_otp_duration_5_min:
                if (checked)
                    mEditTextOtpDuration.setText("5 Min");
                break;
            case R.id.radio_otp_duration_30_min:
                if (checked)
                    mEditTextOtpDuration.setText("30 Min");
                break;
            case R.id.radio_otp_duration_one_week:
                if (checked)
                    mEditTextOtpDuration.setText("1 Week");
                break;
            case R.id.radio_otp_duration_one_month:
                if (checked)
                    mEditTextOtpDuration.setText("1 Month");
                break;
            case R.id.radio_otp_duration_six_month:
                if (checked)
                    mEditTextOtpDuration.setText("6 Month");
                break;
            case R.id.radio_otp_duration_one_year:
                if (checked)
                    mEditTextOtpDuration.setText("1 Year");
                break;
        }
    }


    //####################### OTP Count Down Timer ########################
    //####################### OTP Count Down Timer ########################
    //####################### OTP Count Down Timer ########################
//                        int intOtpDurationInMinutes = Integer.parseInt(mEditTextDurationInMin.getText().toString());
//                        int intOtpDurationInMiliSeconds = intOtpDurationInMinutes * 60 * 1000;
//                        CountDownTimer timer = new CountDownTimer(intOtpDurationInMiliSeconds, 1000) {
//                            public void onTick(long millisUntilFinished) {
//                                int intSeconds = (int) (millisUntilFinished / 1000) % 60;
//                                int intMinutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
//                                int intHours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
//                                mTextViewCountDownTime.setText(String.format("%02d:%02d:%02d", intHours, intMinutes, intSeconds));
//
////                                mStrOtpCountDownTime=(String.format("%02d:%02d:%02d", intHours, intMinutes, intSeconds));
////                                mEditTextAccountNumber.setEnabled(false);
////                                mEditTextPin.setEnabled(false);
////                                mEditTextMaxNumberOfTransaction.setEnabled(false);
////                                mEditTextDurationInMin.setEnabled(false);
//                            }
//
//                            public void onFinish() {
//                                mTextViewCountDownTime.setText("OTP expired!");
////                                mEditTextAccountNumber.setEnabled(true);
////                                mEditTextPin.setEnabled(true);
////                                mEditTextMaxNumberOfTransaction.setEnabled(true);
////                                mEditTextDurationInMin.setEnabled(true);
//
//                            }
//                        };
//                        timer.start();
    //####################### OTP Count Down Timer ########################
    //####################### OTP Count Down Timer ########################
    //####################### OTP Count Down Timer ########################

    //##########################################################################################
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(mEditTextAccountNumber.getWindowToken(), 0);
//                        mSharedPreferencsOtpEditor.putString("time", "12:00");
//                        mSharedPreferencsOtpEditor.putString("durationInMinute", mEditTextDurationInMin.getText().toString());
//                        mSharedPreferencsOtpEditor.putString("generate_otp", mStrOtpCountDownTime);
//                        mSharedPreferencsOtpEditor.commit();
    //##########################################################################################
//                    } else {
//                        mTextViewShowServerResponse.setText("Session Expire, Please Login Again");
//                    }

}


