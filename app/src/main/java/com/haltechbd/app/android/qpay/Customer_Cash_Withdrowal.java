package com.haltechbd.app.android.qpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.CheckMasterKeyAndSessionId;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetAllMerchant;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Customer_Cash_Withdrowal extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;
    private StringTokenizer tokensMerchantList;
    private Toolbar toolbar;
    //#############################################################
    private SharedPreferences mSharedPreferencsOtp;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor;

    private Spinner mSpinnerWallet;
    private EditText mEditTextMerchantAccount,
            mEditTextAmount, mEditTextOtp;
    private CheckBox mCheckBoxQrScan, mCheckBoxGetAllMerchant;
    private ImageButton mImgBtnScanQr, mImgBtnMerchantList;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String strMerchantAccountNumber, strMerchantMasterKey,
            strUrlForQrCode, strEncryptMerchantAccountNumber,
            strMethodName, strBankBin, mStrServerResponse,
            mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptMerchantAccountNumber,
            mStrEncryptAmount, mStrEncryptOtp, mStrEncryptReference,
            mStrEncryptMerchantName, mStrEncryptCaption, mStrEncryptFunctionType,
            mStrSourceWallet, mStrDestinationAccountHolderName, SourceRank, strName,
            strReference, strFunctionType, strDestinationWallet1, strAmount, mStrSourcePin;

    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_cash_withdrawal);
        checkOs();
        initUI();

    }

    private void initUI() {
        mSpinnerWallet = findViewById(R.id.spinnerCustomerNoCCT);
        mEditTextMerchantAccount = findViewById(R.id.editTextCCTMerchantAccountNumber);
        mImgBtnScanQr = findViewById(R.id.imgBtnScanQrCCT);
        mImgBtnScanQr.setOnClickListener(this);
        mImgBtnMerchantList = findViewById(R.id.imgBtnListCCT);
        mImgBtnMerchantList.setOnClickListener(this);
        mEditTextAmount = findViewById(R.id.editTextCCTAmount);
        mEditTextAmount.requestFocus();
        mEditTextOtp = findViewById(R.id.editTextCCTCustomerOtp);
      //  mEditTextReference = findViewById(R.id.editTextMerchantPaymentReference);
        mBtnSubmit = findViewById(R.id.btnCCTSubmit);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewCCTServerResponse);
        mStrSourcePin = GlobalData.getStrPin();
        //mStrMerchantRank=GlobalData.getStrAccountRank();
        mEditTextMerchantAccount.setText(GlobalData.getStrCCTFORTextAgentNO());

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
                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                myAlert.setMessage("OTP is expired. Generate a new OTP?");
                myAlert.setPositiveButton(
                        "Generate OTP",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(Customer_Cash_Withdrowal.this, GenerateOtp.class));
                                finish();
                            }
                        });
                myAlert.setNegativeButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
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


//        initSourceWallet();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mImgBtnScanQr) {
//            mEditTextMerchantAccount.setFocusable(false);
//            mEditTextMerchantAccount.setEnabled(false);
            try {
//                Intent intent = new Intent(ACTION_SCAN);
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                startActivityForResult(intent, 0);
                startActivity(new Intent(Customer_Cash_Withdrowal.this, QrCodeScanActivity.class));
                finish();
                GlobalData.setStrPageRecord("Customer_Cash_Withdrowal");

            } catch (ActivityNotFoundException anfe) {
//                showDialog(MP_Through_C_C2M.this, "No Scanner Found", "Download a QR Scanner App?", "Yes",
//                        "No").show();

                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                myAlert.setMessage("No Scanner Found");
                myAlert.setPositiveButton(
                        "Scan Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();
            }
        }
        if (v == mImgBtnMerchantList) {
            try {

                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                myAlert.setMessage("Search Merchant by Name");
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                myAlert.setView(input);
                myAlert.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                              String  m_TextSearch= input.getText().toString();
                              if(!m_TextSearch.equalsIgnoreCase("")) {
                                  //=============================================
                                  String strEncryptAccountNumber = "";
                                  try {
                                      strEncryptAccountNumber = encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                                      String Encrypted_TextSearch=encryptionDecryption.Encrypt(m_TextSearch, GlobalData.getStrSessionId());
                                      String strAllMerchant = GetAllMerchant.getAllMerchant(strEncryptAccountNumber,Encrypted_TextSearch);


//                                String strAllMerchant = "Merchant 01>>12345678900001&Merchant 02>>12345678900002&Merchant 03>>12345678900003&Merchant 01>>12345678900001&Merchant 02>>12345678900002&Merchant 03>>12345678900003&Merchant 01>>12345678900001&Merchant 02>>12345678900002&Merchant 03>>12345678900003";
                                      ///============================================================
                                      int intIndex = strAllMerchant.indexOf(">>");
                                      if (intIndex == -1) {
                                          AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                                          mAlertDialogBuilder.setMessage(strAllMerchant);
                                          mAlertDialogBuilder.setNegativeButton(
                                                  "OK",
                                                  new DialogInterface.OnClickListener() {
                                                      public void onClick(DialogInterface dialog, int id) {
                                                          dialog.cancel();
                                                      }
                                                  });
                                          AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                          mAlertDialog.show();
                                      } else {
                                          final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Customer_Cash_Withdrowal.this, android.R.layout.simple_list_item_1);
                                          tokensMerchantList = new StringTokenizer(strAllMerchant, "&");
                                          for (int j = 0; j <= tokensMerchantList.countTokens(); j++) {
                                              while (tokensMerchantList.hasMoreElements()) {
                                                  String strMerchantNameAndWallet = tokensMerchantList.nextToken();
                                                  arrayAdapter.add(strMerchantNameAndWallet);
                                              }
                                          }

                                          // ############################# Alert Dialog ###########################
                                          // ############################# Alert Dialog ###########################
                                          // ############################# Alert Dialog ###########################
                                          AlertDialog.Builder alertDialog = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                                          alertDialog.setTitle("Choose Agent");
                                          alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int position) {
                                                  String strMerchantNameAndWallet = arrayAdapter.getItem(position);
                                                  String[] parts = strMerchantNameAndWallet.split(">>");
                                                  String strMerchantName = parts[0];
                                                  String strMerchantWallet = parts[1];
                                                  mStrDestinationAccountHolderName = strMerchantName;
                                                  mEditTextMerchantAccount.setText(strMerchantWallet);
                                              }
                                          });
                                          alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                                  dialog.dismiss();
                                              }
                                          });
                                          alertDialog.show();
                                      }
                                  } catch (Exception e) {
                                      e.printStackTrace();
                                  }
                                  //====================================================================
                              }
                              else
                              {
                                  AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                                  myAlert.setMessage("No Search Found");
                                  myAlert.setPositiveButton(
                                          "Search Again",
                                          new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog, int id) {
                                                  dialog.cancel();

                                              }
                                          });

                                  AlertDialog alertDialog = myAlert.create();
                                  alertDialog.show();

                              }

                            }
                        });
                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();


            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (v == mBtnSubmit) {
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
                    if (mEditTextMerchantAccount.getText().toString().length() == 0) {
                        mEditTextMerchantAccount.setError("Field cannot be empty");
                    } else if (mEditTextAmount.getText().toString().length() == 0) {
                        mEditTextAmount.setError("Field cannot be empty");
                    } else if (mEditTextOtp.getText().toString().length() == 0) {
                        mEditTextOtp.setError("Field cannot be empty");
                    } else if (mEditTextOtp.getText().toString().length() < 5) {
                        mEditTextOtp.setError("Must be 5 characters in length");
                    } else {

                        String  mStrENcryptSessionId = encryptionDecryption.Encrypt(GlobalData.getStrSessionId(), GlobalData.getStrMasterKey());
                        String strCheckSessionID = CheckMasterKeyAndSessionId.checkSessionId(mStrENcryptSessionId, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
                        if (strCheckSessionID.equalsIgnoreCase("Match"))
                        {

                            String strSourcePin = GlobalData.getStrPin();
                            strAmount = mEditTextAmount.getText().toString();
                            strDestinationWallet1 = mEditTextMerchantAccount.getText().toString();


                           // strReference = mEditTextReference.getText().toString();
                            strFunctionType = "CMP";

                            try {

                                mStrEncryptAccountNumber = encryptionDecryption.Encrypt(mStrSourceWallet,GlobalData.getStrSessionId());
                                mStrEncryptPin = encryptionDecryption.Encrypt(strSourcePin, GlobalData.getStrSessionId());
                                mStrEncryptMerchantAccountNumber = encryptionDecryption.Encrypt(strDestinationWallet1, GlobalData.getStrSessionId());
                                mStrEncryptAmount = encryptionDecryption.Encrypt(strAmount, GlobalData.getStrSessionId());
                                mStrEncryptOtp = encryptionDecryption.Encrypt(mEditTextOtp.getText().toString(), GlobalData.getStrSessionId());
                              //  mStrEncryptReference = encryptionDecryption.Encrypt(strReference, GlobalData.getStrSessionId());

                                // Initialize progress dialog
                                mProgressDialog = ProgressDialog.show(Customer_Cash_Withdrowal.this, null, "Processing request...", false, true);
                                // Cancel progress dialog on back key press
                                mProgressDialog.setCancelable(true);

                                Thread t = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                                        doCashWithdrawal(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptMerchantAccountNumber,
                                                mStrEncryptAmount, mStrEncryptOtp);
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
                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                                                            myAlert.setMessage(mStrServerResponse);
                                                            myAlert.setNegativeButton(
                                                                    "Close",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                            startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
                                                                            finish();
                                                                            clearDataFromGlobal();
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog = myAlert.create();
                                                            alertDialog.show();
                                                        } else {
                                                            int intindex2= mStrServerResponse.indexOf("unsuccessful");
                                                            if (intindex2 == -1) {

                                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
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
//                                                                myAlert.setPositiveButton(
//                                                                        "Add Favorite",
//                                                                        new DialogInterface.OnClickListener() {
//                                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                                dialog.cancel();
////                                                            enableUiComponentAfterClick();
//                                                                                GlobalData.setStrFavSourceWallet(mStrSourceWallet);
//                                                                                GlobalData.setStrFavSourcePin(mStrSourcePin);
//                                                                                GlobalData.setStrFavAmount(strAmount);
//                                                                                GlobalData.setStrFavDestinationWallet(strDestinationWallet1);
//                                                                                GlobalData.setStrFavDestinationWalletAccountHolderName(mStrDestinationAccountHolderName);
//                                                                                GlobalData.setStrFavReference(strReference);
//                                                                                GlobalData.setStrFavFunctionType(strFunctionType);
//                                                                                startActivity(new Intent(Customer_Cash_Withdrowal.this, MP_Through_C_C2M_AddToFav.class));
//                                                                                finish();
//                                                                                clearDataFromGlobal();
//                                                                            }
//                                                                        });
                                                                myAlert.setNegativeButton(
                                                                        "Close",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
                                                                                finish();
                                                                            }
                                                                        });
                                                                AlertDialog alertDialog = myAlert.create();
                                                                alertDialog.show();
                                                                ////

                                                            }
                                                            else {
                                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                                                                myAlert.setMessage(mStrServerResponse);
                                                                myAlert.setNegativeButton(
                                                                        "Close",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
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
                        else
                        {
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                            myAlert.setMessage(strCheckSessionID);
                            myAlert.setNegativeButton(
                                    "Close",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                            startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
                                            finish();
                                            clearDataFromGlobal();
                                        }
                                    });
                            AlertDialog alertDialog = myAlert.create();
                            alertDialog.show();

                        }

                    }

                    //##############################
                } else {
                    // if expire
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
                    myAlert.setMessage("OTP is expired. Generate a new OTP?");
                    myAlert.setPositiveButton(
                            "Generate OTP",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(Customer_Cash_Withdrowal.this, GenerateOtp.class));
                                    finish();
                                    clearDataFromGlobal();
                                }
                            });
                    myAlert.setNegativeButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
                                    finish();
                                    clearDataFromGlobal();
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
//        if (v == mBtnAddToFav) {
//            if (mEditTextMerchantAccount.getText().toString().length() == 0) {
//                mEditTextMerchantAccount.setError("Field cannot be empty");
//            } else if (mEditTextAmount.getText().toString().length() == 0) {
//                mEditTextAmount.setError("Field cannot be empty");
//            } else if (mEditTextReference.getText().toString().length() == 0) {
//                mEditTextReference.setError("Field cannot be empty");
//            } else {
//                String strSourceAllWallet = mSpinnerWallet.getSelectedItem().toString();
//                String[] parts = strSourceAllWallet.split("\\-");
//                String strSourceWalletType = parts[0];
//                String strSourceWallet = parts[1];
//                String strSourcePin = GlobalData.getStrPin();
//                String strAmount = mEditTextAmount.getText().toString();
//                String strDestinationWallet = mEditTextMerchantAccount.getText().toString();
//                String strDestinationAccountName = mStrDestinationAccountHolderName;
//                String strReference = mEditTextReference.getText().toString();
//                String strFunctionType = "CMP";
//
//                GlobalData.setStrFavSourceWallet(strSourceWallet);
//                GlobalData.setStrFavSourcePin(strSourcePin);
//                GlobalData.setStrFavAmount(strAmount);
//                GlobalData.setStrFavDestinationWallet(strDestinationWallet);
//                GlobalData.setStrFavDestinationWalletAccountHolderName(strDestinationAccountName);
//                GlobalData.setStrFavReference(strReference);
//                GlobalData.setStrFavFunctionType(strFunctionType);
//
//                startActivity(new Intent(MP_Through_C_C2M.this, MP_Through_C_C2M_FromFav.class));
//
//
//            }
//
//
//        }
    }

    public void doCashWithdrawal(String strEncryptAccountNumber,
                              String strEncryptPin,
                              String strEncryptMerchantAccountNumber,
                              String strEncryptAmount,
                              String strEncryptOtp) {
        METHOD_NAME = "QPAY_Customer_Cash_Withdrawal";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Customer_Cash_Withdrawal";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
        // Declare the version of the SOAP request
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //QPAY_Merchant_Payment_Customer
        // CustomerAccount
        // CustomerPIN
        // MerchantAccountno
        // Amount
        // CustomerOTP
        // CustomerRefID
        // strMasterKey
        PropertyInfo customerAccountNumber = new PropertyInfo();
        customerAccountNumber.setName("E_AccountNo");
        customerAccountNumber.setValue(strEncryptAccountNumber);
        customerAccountNumber.setType(String.class);
        request.addProperty(customerAccountNumber);

        PropertyInfo customerPin = new PropertyInfo();
        customerPin.setName("E_PIN");
        customerPin.setValue(strEncryptPin);
        customerPin.setType(String.class);
        request.addProperty(customerPin);

        PropertyInfo otp = new PropertyInfo();
        otp.setName("E_OTP");
        otp.setValue(strEncryptOtp);
        otp.setType(String.class);
        request.addProperty(otp);


        PropertyInfo amount = new PropertyInfo();
        amount.setName("E_Amount");
        amount.setValue(strEncryptAmount);
        amount.setType(String.class);
        request.addProperty(amount);

        PropertyInfo merchantAccountNumber = new PropertyInfo();
        merchantAccountNumber.setName("E_AgentWallet");
        merchantAccountNumber.setValue(strEncryptMerchantAccountNumber);
        merchantAccountNumber.setType(String.class);
        request.addProperty(merchantAccountNumber);

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
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 20000);
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
//                onBackPressed();
                startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
                finish();
                clearDataFromGlobal();

                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(MP_Through_C_C2M.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearDataFromGlobal() {
//        GlobalData.setStrDeviceId("");
//        GlobalData.setStrDeviceName("");
//        GlobalData.setStrUserId("");
//        GlobalData.setStrEncryptUserId("");
//        GlobalData.setStrPin("");
//        GlobalData.setStrEncryptPin("");
//        GlobalData.setStrMasterKey("");
//        GlobalData.setStrPackage("");
//        GlobalData.setStrEncryptPackage("");
//        GlobalData.setStrAccountNumber("");
//       // GlobalData.setStrEncryptAccountNumber("");
//        GlobalData.setStrWallet("");
//        GlobalData.setStrAccountHolderName("");
//        GlobalData.setStrSessionId("");
//        GlobalData.setStrQrCodeContent("");
        GlobalData.setStrPageRecord("'");
        GlobalData.setStrCCTFORTextAgentNO("");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message,
                                          CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    Log.v("Tracing  Value: ", "Error!!!");
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                //Encrypt QR Code
//                String mStrQrCodeContents = intent.getStringExtra("SCAN_RESULT");
//                int intIndex01 = mStrQrCodeContents.indexOf(":");
//                if (intIndex01 == -1) {
//                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
//                    mAlertDialogBuilder.setNegativeButton(
//                            "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                    mAlertDialog.show();
//                } else {
//
//                }
//                String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
//                strBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
//                String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==
//
//                if (strBankBin.equalsIgnoreCase("006")) {//For Huawei
//                    strUrlForQrCode = GlobalData.getStrUrl();
//                    strMethodName = "QPAY_Get_Account_BY_QR_CARD";
//                } else if (strBankBin.equalsIgnoreCase("002")) {//For QPay
//                    strUrlForQrCode = GlobalData.getStrUrl();
//                    strMethodName = "QPAY_Get_Account_BY_QR_CARD";
//                }
//
//                String strEncryptDestinationAccountNumberAndMasterKey = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
//                if (!strEncryptDestinationAccountNumberAndMasterKey.equalsIgnoreCase("")) {
//                    int intIndex02 = strEncryptDestinationAccountNumberAndMasterKey.indexOf("*");
//                    if (intIndex02 == -1) {
//                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
//                        mAlertDialogBuilder.setNegativeButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                        mAlertDialog.show();
//                    } else {
//                        String[] parts = strEncryptDestinationAccountNumberAndMasterKey.split("\\*");
//                        strEncryptMerchantAccountNumber = parts[0];//96745482897185504726639965371045
//                       // strMerchantMasterKey = parts[1];//0021200000007
//                       String strMerchantEncryptName= parts[1];
//
//                        try {
//                            //################ Merchant Account Number ####################
//                            //################ Merchant Account Number ####################
//                            //################ Merchant Account Number ####################
//                            strMerchantAccountNumber = encryptionDecryption.Decrypt(strEncryptMerchantAccountNumber, GlobalData.getStrSessionId());
//                            String strAccountTypeCode = strMerchantAccountNumber.substring(3, 5);
//                            if (strAccountTypeCode.equalsIgnoreCase("11"))
//                            {
//                                //################ Merchant Name ####################
//                                //################ Merchant Name ####################
//                                //################ Merchant Name ####################
//                               // mStrDestinationAccountHolderName = GetMerchantName.getMerchantName(strEncryptMerchantAccountNumber,strMerchantMasterKey);
//                                mStrDestinationAccountHolderName =encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
////                                String[] parts01 = mStrDestinationAccountHolderName.split("\\*");
////                                String strName = parts01[0];
////                                final String strType = parts01[1];
//
////                                String strDestinationRank = mStrSourceWallet.substring(13, 14);
////                                if (strDestinationRank.equalsIgnoreCase("1")) {
////                                    SourceRank = "VOUPAY";
////                                } else if (strDestinationRank.equalsIgnoreCase("2")) {
////                                    SourceRank = "CANPAY";
////                                } else if (strDestinationRank.equalsIgnoreCase("3")) {
////                                    SourceRank = "SIMBIL";
////                                }
//                                //################ Show Dialog ####################
//                                //################ Show Dialog ####################
//                                //################ Show Dialog ####################
//                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                                myAlert.setTitle("MERCHANT INFO");
//                                myAlert.setMessage("MERCHANT NAME" + "\n" + mStrDestinationAccountHolderName + "\n" + "MERCHANT WALLET" + "\n" + strMerchantAccountNumber);
//                                myAlert.setPositiveButton(
//                                        "OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//
//                                                mEditTextMerchantAccount.setText(strMerchantAccountNumber);
////                                                if (strType.equalsIgnoreCase(SourceRank)) {
////                                                    mEditTextMerchantAccount.setText(strMerchantAccountNumber);
////                                                } else {
////                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_C_C2M.this);
////                                                    myAlert.setMessage("Account Type not match. Please scan correct account type.");
////                                                    myAlert.setNegativeButton(
////                                                            "OK",
////                                                            new DialogInterface.OnClickListener() {
////                                                                public void onClick(DialogInterface dialog, int id) {
////                                                                    dialog.cancel();
////                                                                }
////                                                            });
////                                                    AlertDialog alertDialog = myAlert.create();
////                                                    alertDialog.show();
////                                                }
//                                            }
//                                        });
//                                myAlert.setNegativeButton(
//                                        "CANCEL",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alertDialog = myAlert.create();
//                                alertDialog.show();
//                            } else {
//                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                                myAlert.setMessage("Please Scan Merchant QR");
//                                myAlert.setNegativeButton(
//                                        "OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alertDialog = myAlert.create();
//                                alertDialog.show();
//                            }
//
//
//                        } catch (Exception exception) {
//                            exception.printStackTrace();
//                        }
//                    }
//
//                } else {
//                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MP_Through_C_C2M.this);
//                    mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
//                    mAlertDialogBuilder.setNegativeButton(
//                            "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                    mAlertDialog.show();
//                }
//            }
//        }
//    }

    private void checkInternet() {

        if (isNetworkConnected()) {
            enableUiComponents();
            //######################### Spinner Account Type#########################
            //######################### Spinner Account Type#########################
            //######################### Spinner Account Type#########################
            // Initialize progress dialog
            mProgressDialog = ProgressDialog.show(Customer_Cash_Withdrowal.this, null, "Loading Bank...", false, true);
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
                                    ArrayAdapter<String> adapterWallet = new ArrayAdapter<String>(Customer_Cash_Withdrowal.this,
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
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
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

    //######################### Show Account Type #########################
    //######################### Show Account Type #########################
    //######################### Show Account Type #########################
    private void loadSpinnerWallet() {
        try {
            mStrEncryptAccountNumber = encryptionDecryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
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
                        String strBankID=tokensAllTransactionFinal.nextToken();
                        String strBalance=tokensAllTransactionFinal.nextToken();
                        String strQRcode=tokensAllTransactionFinal.nextToken();

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
                                arrayBankID.add(strBankID);
                            }
                        }
                        //arrayListWalletType.add(tokenWalletTypeAndAccount.nextToken());
                        //arrayListWallet.add(tokenWalletTypeAndAccount.nextToken());
                    }
                }
                else
                {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
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
                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Customer_Cash_Withdrowal.this);
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

    //######################### Account Number #########################
    //######################### Account Number #########################
    //######################### Account Number #########################
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

    private void enableUiComponents() {
        mSpinnerWallet.setEnabled(true);
        mEditTextMerchantAccount.setEnabled(true);
        mEditTextAmount.setEnabled(true);
        mEditTextOtp.setEnabled(true);

        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mSpinnerWallet.setEnabled(false);
        mEditTextMerchantAccount.setEnabled(false);
        mEditTextAmount.setEnabled(false);
        mEditTextOtp.setEnabled(false);

        mBtnSubmit.setEnabled(false);
    }

    private void clearEditText() {
        mEditTextAmount.setText("");
//        mEditTextReference.setText("");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Customer_Cash_Withdrowal.this, MainActivity.class));
            finish();
            clearDataFromGlobal();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

}
