package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
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
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bushra on 6/4/2018.
 */

public class DESCO extends AppCompatActivity implements View.OnClickListener  {

    private Spinner mSpinnerDescoBillServiceType,mSpinnerWallet;
    TextView mTextViewShowServerResponse;
    private EditText mEditTextDescoBillNumber, mEditTextReTypeDescoBillNumber, mEditTextOtp, mEditTextPayerMobileNumber;
    private Button mBtnSubmit;
    private SharedPreferences mSharedPreferencsOtp;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor;
    private ProgressDialog mProgressDialog = null;
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    private String SOAP_ACTION, METHOD_NAME;

    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();
    int year;
    int month;
    int day;
    String strdate;
    String strBillVerification = "";
    String mStrServerResponse;
    String mStrSourceWallet,mStrBillNumber,msStrRetypeBillNumber,mStrPayerMobileNumber,mStrType,mStrOtp;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desco);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkOs();
        initUI();
    }

    private void initUI() {

        mEditTextDescoBillNumber = (EditText) findViewById(R.id.txtDescobillno);
        mEditTextReTypeDescoBillNumber = (EditText) findViewById(R.id.txtDescobillnoReType);
        mEditTextPayerMobileNumber = (EditText) findViewById(R.id.txtDescoPayerMobile);
        mEditTextOtp = findViewById(R.id.txtDescoOTP);
        mSpinnerWallet = findViewById(R.id.spinnerDESCOWallet);
        mTextViewShowServerResponse = (TextView) findViewById(R.id.txtViewBillPaymentDescoShowServerResponse);
        mSpinnerDescoBillServiceType = (Spinner) findViewById(R.id.spdesco);
        mBtnSubmit = (Button) findViewById(R.id.btnBillPaymentDescoSubmit);
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
                AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
                myAlert.setMessage("OTP is expired. Generate a new OTP?");
                myAlert.setPositiveButton(
                        "Generate OTP",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(DESCO.this, GenerateOtp.class));
                                finish();
                            }
                        });
                myAlert.setNegativeButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(DESCO.this, MainActivity.class));
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

        mBtnSubmit.setOnClickListener(this);


        addItemsOnSpinner2();

        checkInternet();
        mEditTextOtp.setVisibility(View.GONE);
        mSpinnerDescoBillServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String strType = String.valueOf(mSpinnerDescoBillServiceType.getSelectedItem());
                if (strType == "Bill Info") {
                    mEditTextOtp.setVisibility(View.GONE);

                } else if (strType == "Bill Payment") {
                    mEditTextOtp.setVisibility(View.VISIBLE);

                }
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


        public void addItemsOnSpinner2() {
            mSpinnerDescoBillServiceType = (Spinner) findViewById(R.id.spdesco);
            List<String> list = new ArrayList<String>();
            list.add("Bill Info");
            list.add("Bill Payment");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerDescoBillServiceType.setAdapter(dataAdapter);
        }


    public void onClick(View v)
    {

        if (v == mBtnSubmit) {
            mSpinnerDescoBillServiceType = (Spinner) findViewById(R.id.spdesco);
            mStrBillNumber = mEditTextDescoBillNumber.getText().toString();
            msStrRetypeBillNumber = mEditTextReTypeDescoBillNumber.getText().toString();
            mStrPayerMobileNumber = mEditTextPayerMobileNumber.getText().toString();
            mStrType = String.valueOf(mSpinnerDescoBillServiceType.getSelectedItem());

            if (mStrType == "Bill Info") {
                mEditTextOtp.setVisibility(View.GONE);
                v.setVisibility(View.GONE);
                mStrType = "UBI";
                // -------------------------------------------

                if (mStrBillNumber.equalsIgnoreCase(msStrRetypeBillNumber)) {
                    // --------------check field------------------
                    if (mEditTextDescoBillNumber.getText().toString().length() == 0) {
                        mEditTextDescoBillNumber.setError("This field cannot be empty.");

                    } else if (mEditTextReTypeDescoBillNumber.getText().toString().length() == 0) {
                        mEditTextReTypeDescoBillNumber.setError("This field cannot be empty.");

                    } else if (mEditTextPayerMobileNumber.getText().toString().length() == 0) {
                        mEditTextPayerMobileNumber.setError("This field cannot be empty.");

                    } else if (mEditTextPayerMobileNumber.getText().toString().length() < 11) {
                        mEditTextPayerMobileNumber.setError("Payer Mobile No should be 11 digits!!!");

                    } else {


                        mProgressDialog = ProgressDialog.show(DESCO.this, "Desco Bill info Request Processing",
                                "Please wait...");
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Submit_Bill_Info(mStrBillNumber, mStrType, mStrPayerMobileNumber, mStrSourceWallet, GlobalData.getStrPin());
                                }

                                catch (Exception ex) {
                                    mTextViewShowServerResponse.setText("Connection Error");
                                }

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                int intIndex = mStrServerResponse.indexOf("successful");
                                                if (intIndex == -1) {
                                                    if(mStrServerResponse.equalsIgnoreCase(""))
                                                    {
                                                        mStrServerResponse="Request is in process";
                                                    }
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
                                                    myAlert.setMessage(mStrServerResponse);
                                                    myAlert.setNegativeButton(
                                                            "Close",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                    startActivity(new Intent(DESCO.this, MainActivity.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();
                                                } else {
                                                    int intindex2= mStrServerResponse.indexOf("unsuccessful");
                                                    if (intindex2 == -1) {

                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
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
                                                                        startActivity(new Intent(DESCO.this, DESCO.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                        ////

                                                    }
                                                    else {
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
                                                        myAlert.setMessage(mStrServerResponse);
                                                        myAlert.setNegativeButton(
                                                                "Close",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                        startActivity(new Intent(DESCO.this, Topup.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            // TODO: handle
                                            mTextViewShowServerResponse.setText("Connection Error");
                                        }
                                        // update ui info ( show
                                        // response message )

                                    }
                                });

                            }
                        });
                        t.start();


                        ////////////////////////////////////
                    }
                    // --------- chech filed end-----------------------------

                } else {
                    // txtbillNo.setError("Bill no not match!!!");
                    // txtBillNoRetype.setError("Bill no not match!!!");

                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
                    // //////////////////
                   // mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                    mAlertDialogBuilder.setTitle("Please try again !!!");
                    mAlertDialogBuilder.setMessage("Sorry, Bill number does not match!!!");
                    mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // ----------------------------------------------------------

                        }
                    });

                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                    mAlertDialog.show();
                }

            } else if (mStrType == "Bill Payment") {
                mEditTextOtp.setVisibility(View.VISIBLE);
                v.setVisibility(View.VISIBLE);
                mStrType = "UBP";
                mStrOtp = mEditTextOtp.getText().toString();
              //  String strGetAmount = GetAmount(mStrBillNumber);
                strBillVerification = BillVerification(mStrBillNumber, "DESCO");

                if (mStrBillNumber.equalsIgnoreCase(msStrRetypeBillNumber)) {

                    // --------------check field------------------
                    if (mEditTextDescoBillNumber.getText().toString().length() == 0) {
                        mEditTextDescoBillNumber.setError("This field cannot be empty.");

                    }
                    else if (mEditTextDescoBillNumber.getText().toString().length() < 12) {
                        mEditTextDescoBillNumber.setError("SORRY, DESCO Bill Number must be 12 Digit");

                    }
                    else if (mEditTextReTypeDescoBillNumber.getText().toString().length() == 0) {
                        mEditTextReTypeDescoBillNumber.setError("This field cannot be empty.");

                    }
                    else if (mEditTextReTypeDescoBillNumber.getText().toString().length() < 12) {
                        mEditTextReTypeDescoBillNumber.setError("SORRY, DESCO Bill Number must be 12 Digit");

                    }
                    else if (mEditTextOtp.getText().toString().length() == 0) {
                        mEditTextOtp.setError("This field cannot be empty.");

                    } else if (mEditTextPayerMobileNumber.getText().toString().length() == 0) {
                        mEditTextPayerMobileNumber.setError("This field cannot be empty.");

                    } else if (mEditTextPayerMobileNumber.getText().toString().length() < 11) {
                        mEditTextPayerMobileNumber.setError("Payer Mobile No should be 11 digits!!!");

                    } else {

                        // -----------------------------------------

                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
                            // //////////////////
                            // mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                            // mAlertDialogBuilder.setTitle("Check your bill
                            // amount");
                            // mAlertDialogBuilder.setMessage(strGetAmount);
                            // -------------------------

                            mAlertDialogBuilder.setTitle("Do you want to submit?");
                            mAlertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // ----------------------------------

                                    if (strBillVerification.equalsIgnoreCase("unpaid")) {


                                        mProgressDialog = ProgressDialog.show(DESCO.this, "Desco Bill Payment Request Processing",
                                                "Please wait...");
                                        mProgressDialog.setCancelable(true);

                                        Thread t = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {

                                                    Submit_Bill_Payment(mStrBillNumber, mStrType, mStrPayerMobileNumber, mStrSourceWallet,
                                                            GlobalData.getStrPin(), mStrOtp);

                                                }

                                                catch (Exception ex) {
                                                    mTextViewShowServerResponse.setText("Connection Error");
                                                }

                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        try {
                                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                                mProgressDialog.dismiss();
                                                                int intIndex = mStrServerResponse.indexOf("successful");
                                                                if (intIndex == -1) {
                                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
                                                                    myAlert.setMessage(mStrServerResponse);
                                                                    myAlert.setNegativeButton(
                                                                            "Close",
                                                                            new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                    startActivity(new Intent(DESCO.this, MainActivity.class));
                                                                                    finish();
                                                                                }
                                                                            });
                                                                    AlertDialog alertDialog = myAlert.create();
                                                                    alertDialog.show();
                                                                } else {
                                                                    int intindex2= mStrServerResponse.indexOf("unsuccessful");
                                                                    if (intindex2 == -1) {

                                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
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
                                                                                        startActivity(new Intent(DESCO.this, MainActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog = myAlert.create();
                                                                        alertDialog.show();
                                                                        ////

                                                                    }
                                                                    else {
                                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(DESCO.this);
                                                                        myAlert.setMessage(mStrServerResponse);
                                                                        myAlert.setNegativeButton(
                                                                                "Close",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                        startActivity(new Intent(DESCO.this, Topup.class));
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog = myAlert.create();
                                                                        alertDialog.show();
                                                                    }
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            // TODO: handle
                                                            mTextViewShowServerResponse.setText("Connection Error");
                                                        }
                                                        // update ui info ( show
                                                        // response message )

                                                    }
                                                });

                                            }
                                        });
                                        t.start();
                                        // ----------------------------



                                        mEditTextDescoBillNumber.setText("");
                                        mEditTextReTypeDescoBillNumber.setText("");
//										mEditTextOtp.setText("");
//										mEditTextPayerMobileNumber.setText("");
                                        //mTextViewShowServerResponse.setText("");

                                    } else {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
                                        // //////////////////
                                       // mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                                        mAlertDialogBuilder.setTitle("Check your bill");
                                        mAlertDialogBuilder.setMessage(strBillVerification);
                                        mAlertDialogBuilder.setPositiveButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        // ----------------------------------------------------------


                                                    }
                                                });

                                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                                        mAlertDialog.show();

                                    }
                                    // -------------------------------------
                                    // -------------------------------
                                }
                            });

                            mAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // ----------------------------------------------------------

                                }
                            });
                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                            mAlertDialog.show();


                    }

                    // --- check field end----------------------------------
                } else {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
                    // //////////////////
                  //  mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                    mAlertDialogBuilder.setTitle("Please try again !!!");
                    mAlertDialogBuilder.setMessage("Sorry, Bill number does not match!!!");
                    mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // ----------------------------------------------------------

                        }
                    });

                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                    mAlertDialog.show();

                }
            }
        }

    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            mProgressDialog = ProgressDialog.show(DESCO.this, null, "Loading Bank...", false, true);
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
                                    ArrayAdapter<String> adapterWallet = new ArrayAdapter<String>(DESCO.this,
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
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
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
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
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
                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(DESCO.this);
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


    protected String BillVerification(String strbillno, String strOwner) {

        Object response1 = null;
        try {


            String strbillnoEncpyt = encryptionDecryption.Encrypt(strbillno,  GlobalData.getStrSessionId());
            String strOwnerEncpty = encryptionDecryption.Encrypt(strOwner,  GlobalData.getStrSessionId());

            METHOD_NAME = "QPAY_Bill_Verification";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Bill_Verification";

            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);

            // Declare the version of the SOAP request

            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_BillNo");
            Acc.setValue(strbillnoEncpyt);
            Acc.setType(String.class);
            request.addProperty(Acc);

            PropertyInfo ownercode = new PropertyInfo();
            ownercode.setName("E_Ownercode");
            ownercode.setValue(strOwnerEncpty);
            ownercode.setType(String.class);
            request.addProperty(ownercode);


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



            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 10000);
           // Log.v("url", "url is:" + URL.toString());
            androidHttpTransport.call(SOAP_ACTION, envelope);
            response1 = envelope.getResponse();
        } catch (Exception exception) {
            // ViewResult.setText(exception.toString()+" Or enter number is not
            // Available!");
            mTextViewShowServerResponse.setText("Please try again");
            Log.v("tracing  value ", "Get result is: " + mTextViewShowServerResponse.getText().toString());
        }
        return response1.toString();
    }

    protected void Submit_Bill_Info(String strbillno, String strType, String strPayerMobile, String walletid, String strPIN) {
        try {


            String strbillnoEncpyt = encryptionDecryption.Encrypt(strbillno,  GlobalData.getStrSessionId());
            String strTypeEncpty = encryptionDecryption.Encrypt(strType,  GlobalData.getStrSessionId());
            String strPayerMobileEncpty = encryptionDecryption.Encrypt(strPayerMobile,  GlobalData.getStrSessionId());
            String walletidEncpty = encryptionDecryption.Encrypt(walletid,  GlobalData.getStrSessionId());
            String strPINEncpty = encryptionDecryption.Encrypt(strPIN,  GlobalData.getStrSessionId());


            METHOD_NAME = "QPAY_DESCO_bilInfo";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_DESCO_bilInfo";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
            // Declare the version of the SOAP request
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_AccountNo");
            Acc.setValue(walletidEncpty);
            Acc.setType(String.class);
            request.addProperty(Acc);

            PropertyInfo sPIN = new PropertyInfo();
            sPIN.setName("E_PIN");
            sPIN.setValue(strPINEncpty);
            sPIN.setType(String.class);
            request.addProperty(sPIN);

            PropertyInfo billtype = new PropertyInfo();
            billtype.setName("E_billType");
            billtype.setValue(strTypeEncpty);
            billtype.setType(String.class);
            request.addProperty(billtype);

            PropertyInfo AccNo = new PropertyInfo();
            AccNo.setName("E_BillNo");
            AccNo.setValue(strbillnoEncpyt);
            AccNo.setType(String.class);
            request.addProperty(AccNo);

            PropertyInfo payer = new PropertyInfo();
            payer.setName("E_PayerMobileNo");
            payer.setValue(strPayerMobileEncpty);
            payer.setType(String.class);
            request.addProperty(payer);


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
            Object response1 = null;


            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 10000);
          //  Log.v("url", "url is:" + URL.toString());
            androidHttpTransport.call(SOAP_ACTION, envelope);
            response1 = envelope.getResponse();
            Log.v("response:", response1.toString());
            mStrServerResponse=response1.toString();
            // mTextViewShowServerResponse.setText(response1.toString());
           // Log.v("Result:", mTextViewShowServerResponse.getText().toString());
        } catch (Exception exception) {
            // ViewResult.setText(exception.toString()+" Or enter number is not
            // Available!");
            mTextViewShowServerResponse.setText("Please try again");
            Log.v("tracing  value ", "Get result is: " + mTextViewShowServerResponse.getText().toString());
        }

    }

    protected void Submit_Bill_Payment(String strbillno, String strType, String strPayerMobile, String walletid, String strPIN,
                             String strOTP) {

        try {


            String strbillnoEncpyt = encryptionDecryption.Encrypt(strbillno,  GlobalData.getStrSessionId());
            String strTypeEncpty = encryptionDecryption.Encrypt(strType,  GlobalData.getStrSessionId());
            String strPayerMobileEncpty = encryptionDecryption.Encrypt(strPayerMobile,  GlobalData.getStrSessionId());
            String walletidEncpty = encryptionDecryption.Encrypt(walletid,  GlobalData.getStrSessionId());
            String strPINEncpty = encryptionDecryption.Encrypt(strPIN,  GlobalData.getStrSessionId());
            String strOTPEncpty = encryptionDecryption.Encrypt(strOTP,  GlobalData.getStrSessionId());

            METHOD_NAME = "QPAY_DESCO_Bill_Payment";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_DESCO_Bill_Payment";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
            // Declare the version of the SOAP request
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_AccountNo");
            Acc.setValue(walletidEncpty);
            Acc.setType(String.class);
            request.addProperty(Acc);

            PropertyInfo sPIN = new PropertyInfo();
            sPIN.setName("E_PIN");
            sPIN.setValue(strPINEncpty);
            sPIN.setType(String.class);
            request.addProperty(sPIN);

            PropertyInfo billtype = new PropertyInfo();
            billtype.setName("E_billType");
            billtype.setValue(strTypeEncpty);
            billtype.setType(String.class);
            request.addProperty(billtype);

            PropertyInfo AccNo = new PropertyInfo();
            AccNo.setName("E_BillNo");
            AccNo.setValue(strbillnoEncpyt);
            AccNo.setType(String.class);
            request.addProperty(AccNo);

            PropertyInfo payer = new PropertyInfo();
            payer.setName("E_PayerMobileNo");
            payer.setValue(strPayerMobileEncpty);
            payer.setType(String.class);
            request.addProperty(payer);

            PropertyInfo otp = new PropertyInfo();
            otp.setName("E_OTP");
            otp.setValue(strOTPEncpty);
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
            Object response1 = null;


            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 10000);
            //Log.v("url", "url is:" + URL.toString());
            androidHttpTransport.call(SOAP_ACTION, envelope);
            response1 = envelope.getResponse();
            mStrServerResponse=response1.toString();
            Log.v("response:", response1.toString());
            mTextViewShowServerResponse.setText(response1.toString());
            Log.v("Result:", mTextViewShowServerResponse.getText().toString());
        } catch (Exception exception) {
            // ViewResult.setText(exception.toString()+" Or enter number is not
            // Available!");
            mTextViewShowServerResponse.setText("Please try again");
            Log.v("tracing  value ", "Get result is: " + mTextViewShowServerResponse.getText().toString());
        }

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
                startActivity(new Intent(DESCO.this, MainActivity.class));
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
            startActivity(new Intent(DESCO.this, Utility_Bill.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clearEditText() {

        mEditTextReTypeDescoBillNumber.setText("");
        mEditTextDescoBillNumber.setText("");
        mEditTextPayerMobileNumber.setText("");
    }

}
