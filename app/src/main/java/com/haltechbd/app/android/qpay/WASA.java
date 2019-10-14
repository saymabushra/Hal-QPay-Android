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

public class WASA extends AppCompatActivity implements View.OnClickListener  {

    private Spinner mSpinnerSelectWasaBillType,mSpinnerWallet;


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
    private EditText mEditTextWasaBillNumber, mEditTextReTypeWasaBillNumber, mEditTextOtp, mEditTextPayerMobileNumber;
    private Button mBtnSubmit, mBtnClear;
    private TextView mTextViewShowServerResponse;
    private String strbillno, strRetypeBillNo, strPayerMobile, strType, strOTP, mStrSourceWallet, mStrPin,mStrServerResponse;
    int year;
    int month;
    int day;
    String strdate;
    String strBillVerification="";
    String strResonseOtp;
    String strResponseLast;
    String strResonse;
    String strRequestLog;
    String strResponseLog;
    String strResponse;
    String strResonse2;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wasa);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkOs();
        initUI();
    }

    private void initUI() {

        mEditTextWasaBillNumber = (EditText) findViewById(R.id.txtwasabillno);
        mEditTextPayerMobileNumber = (EditText) findViewById(R.id.txtwasaPayerMobile);

        mEditTextReTypeWasaBillNumber = (EditText) findViewById(R.id.txtwasabillnoReType);
        mTextViewShowServerResponse = (TextView) findViewById(R.id.txtViewBillPaymentWasaShowServerResponse);
        mEditTextOtp = findViewById(R.id.txtwasaOTP);
        mSpinnerWallet = findViewById(R.id.spinnerWASAWallet);
        mBtnSubmit = (Button) findViewById(R.id.btnBillPaymentWasaSubmit);
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
                AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
                myAlert.setMessage("OTP is expired. Generate a new OTP?");
                myAlert.setPositiveButton(
                        "Generate OTP",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(WASA.this, GenerateOtp.class));
                                finish();
                            }
                        });
                myAlert.setNegativeButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(WASA.this, MainActivity.class));
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

        addItemsOnSpinner2();

        checkInternet();
        mBtnSubmit.setOnClickListener(this);
        mEditTextOtp.setVisibility(View.GONE);
        mSpinnerSelectWasaBillType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String strType = String.valueOf(mSpinnerSelectWasaBillType.getSelectedItem());
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
            mSpinnerSelectWasaBillType = (Spinner) findViewById(R.id.spwasa);
            List<String> list = new ArrayList<String>();
            list.add("Bill Info");
            list.add("Bill Payment");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerSelectWasaBillType.setAdapter(dataAdapter);
        }


    public void onClick(View v)
    {
        if (v == mBtnSubmit) {

            mSpinnerSelectWasaBillType = (Spinner) findViewById(R.id.spwasa);
            strbillno = mEditTextWasaBillNumber.getText().toString();
            strRetypeBillNo = mEditTextReTypeWasaBillNumber.getText().toString();
            strPayerMobile = mEditTextPayerMobileNumber.getText().toString();
            strType = String.valueOf(mSpinnerSelectWasaBillType.getSelectedItem());

            if (strType == "Bill Info") {
                mEditTextOtp.setVisibility(View.GONE);
                v.setVisibility(View.GONE);
                strType = "UBI";
                // -------------------------------------------
                if (strbillno.equalsIgnoreCase(strRetypeBillNo)) {
                    // --------------check field------------------
                    if (mEditTextWasaBillNumber.getText().toString().length() == 0) {
                        mEditTextWasaBillNumber.setError("This field cannot be empty.");
                    } else if (mEditTextWasaBillNumber.getText().toString().length() < 11) {
                        mEditTextWasaBillNumber.setError("Sorry, WASA Bill Number must be 12 Digit");
                    }
                    else if (mEditTextReTypeWasaBillNumber.getText().toString().length() == 0) {
                        mEditTextReTypeWasaBillNumber.setError("This field cannot be empty.");
                    }
                    else if (mEditTextReTypeWasaBillNumber.getText().toString().length() < 11) {
                        mEditTextReTypeWasaBillNumber.setError("Sorry, WASA Bill Number must be 12 Digit");
                    }
                    else if (mEditTextPayerMobileNumber.getText().toString().length() == 0) {
                        mEditTextPayerMobileNumber.setError("This field cannot be empty.");
                    } else if (mEditTextPayerMobileNumber.getText().toString().length() < 11) {
                        mEditTextPayerMobileNumber.setError("Payer Mobile No should be 11 digits!!!");
                    } else {
                        // -------------------------
                        // final Calendar c = Calendar.getInstance();
                        // year = c.get(Calendar.YEAR);
                        // month = c.get(Calendar.MONTH);
                        // c.add(Calendar.MONTH, -1);
                        // day = c.get(Calendar.DAY_OF_MONTH);
                        // SimpleDateFormat df1 = new SimpleDateFormat("MM");
                        // //called without pattern
                        // strdate=df1.format(c.getTime());
                        // String strMonth=strbillno.substring(0, 2);
                        // -----------------------------
                        // if(strdate.equalsIgnoreCase(strMonth))
                        // {


                        // ----------------------------------------
                        mProgressDialog = ProgressDialog.show(WASA.this, "Wasa Bill info Processing", "Please wait...");
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {

                                    Submit_bill_Info(strbillno, strType, strPayerMobile, mStrSourceWallet, GlobalData.getStrPin());

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
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
                                                    myAlert.setMessage(mStrServerResponse);
                                                    myAlert.setNegativeButton(
                                                            "Close",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                    startActivity(new Intent(WASA.this, MainActivity.class));
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = myAlert.create();
                                                    alertDialog.show();
                                                } else {
                                                    int intindex2= mStrServerResponse.indexOf("unsuccessful");
                                                    if (intindex2 == -1) {

                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
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
                                                                        startActivity(new Intent(WASA.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                        ////

                                                    }
                                                    else {
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
                                                        myAlert.setMessage(mStrServerResponse);
                                                        myAlert.setNegativeButton(
                                                                "Close",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                        startActivity(new Intent(WASA.this, WASA.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                    }
                                                }

                                                // mEditTextOldPin.setText("");
                                                // mEditTextNewPin.setText("");
                                                // mTextViewShowServerResponse.setText("");
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

                    }
                    // --------- chech filed end-----------------------------

                } else {
                    // txtbillNo.setError("Bill no not match!!!");
                    // txtBillNoRetype.setError("Bill no not match!!!");
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
                    // //////////////////
                   // mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                    mAlertDialogBuilder.setTitle("Please try again !!!");
                    mAlertDialogBuilder.setMessage("Sorry,Bill number does not match!!!");
                    mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // ----------------------------------------------------------
                        }
                    });

                    AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                    mAlertDialog.show();
                }

            } else if (strType == "Bill Payment")  {
                mEditTextOtp.setVisibility(View.VISIBLE);
                v.setVisibility(View.VISIBLE);
                strType = "UBP";
                strOTP = mEditTextOtp.getText().toString();
                String strGetAmount = GetAmount(strbillno);

                strBillVerification=BillVerification(strbillno,"WASA");

                if (strbillno.equalsIgnoreCase(strRetypeBillNo)) {
                    // --------------check field------------------
                    if (mEditTextWasaBillNumber.getText().toString().length() == 0) {
                        mEditTextWasaBillNumber.setError("This field cannot be empty.");

                    }
                    else if (mEditTextWasaBillNumber.getText().toString().length() < 12) {
                        mEditTextWasaBillNumber.setError("SORRY, WASA Bill Number must be 12 Digit");

                    }
                    else if (mEditTextReTypeWasaBillNumber.getText().toString().length() == 0) {
                        mEditTextReTypeWasaBillNumber.setError("This field cannot be empty.");

                    }
                    else if (mEditTextReTypeWasaBillNumber.getText().toString().length() < 12) {
                        mEditTextReTypeWasaBillNumber.setError("SORRY, WASA Bill Number must be 12 Digit");

                    }
                    else if (mEditTextOtp.getText().toString().length() == 0) {
                        mEditTextOtp.setError("This field cannot be empty.");

                    } else if (mEditTextPayerMobileNumber.getText().toString().length() == 0) {
                        mEditTextPayerMobileNumber.setError("This field cannot be empty.");

                    } else if (mEditTextPayerMobileNumber.getText().toString().length() < 11) {
                        mEditTextPayerMobileNumber.setError("Payer Mobile No should be 11 digits!!!");

                    } else {

                        if (strGetAmount.contains("Your Account No:")) {
                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
                            // //////////////////
//							mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
//							mAlertDialogBuilder.setTitle("Check your bill amount");
//							mAlertDialogBuilder.setMessage(strGetAmount);

                            //-------------------------
                            StringTokenizer getTotleToken=new StringTokenizer(strGetAmount.toString(),"#");
                            ArrayList<String> strArryFist=new ArrayList<String>();
                            for (int i = 0; i <= getTotleToken.countTokens(); i++) {
                                while (getTotleToken.hasMoreElements()) {
                                    // list.add(tokens.nextToken());
                                    strArryFist.add(getTotleToken.nextToken());
                                }
                            }



                            StringTokenizer get2ndtokens = new StringTokenizer(strArryFist.get(1).toString(), ";");
                            ArrayList<String> stringArrayList2nd = new ArrayList<String>();
                            for (int j = 0; j <= get2ndtokens.countTokens(); j++) {
                                while (get2ndtokens.hasMoreElements()) {
                                    // list.add(tokens.nextToken());
                                    stringArrayList2nd.add(get2ndtokens.nextToken());
                                }
                            }

                            strResonse2=	stringArrayList2nd.get(0).toString();
                            strRequestLog=stringArrayList2nd.get(1).toString();
                            strResponseLog=stringArrayList2nd.get(2).toString();

//                            StringTokenizer tokens = new StringTokenizer(strArryFist.get(0).toString(), ";");
//                            ArrayList<String> stringArrayList = new ArrayList<String>();
//                            for (int j = 0; j <= tokens.countTokens(); j++) {
//                                while (tokens.hasMoreElements()) {
//                                    // list.add(tokens.nextToken());
//                                    stringArrayList.add(tokens.nextToken());
//                                }
//                            }





                            mAlertDialogBuilder.setTitle("Do you want to submit?");

                            mAlertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener()  {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // ----------------------------------
                                    if(strBillVerification.equalsIgnoreCase("unpaid"))
                                    {

                                        // ----------------------------------------
                                        mProgressDialog = ProgressDialog.show(WASA.this, "Wasa Bill Payment Processing", "Please wait...");
                                        mProgressDialog.setCancelable(true);

                                        Thread t = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try
                                                {

                                                    InsertAmount(strbillno,strResonse2,strRequestLog,strResponseLog);
                                                    Submit_Bill_payment(strbillno, strType, strPayerMobile, mStrSourceWallet, GlobalData.getStrPin(), strOTP);
                                                    //---------------------------

                                                    //---------------------------

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
                                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
                                                                    myAlert.setMessage(mStrServerResponse);
                                                                    myAlert.setNegativeButton(
                                                                            "Close",
                                                                            new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                    startActivity(new Intent(WASA.this, MainActivity.class));
                                                                                    finish();
                                                                                }
                                                                            });
                                                                    AlertDialog alertDialog = myAlert.create();
                                                                    alertDialog.show();
                                                                } else {
                                                                    int intindex2= mStrServerResponse.indexOf("unsuccessful");
                                                                    if (intindex2 == -1) {

                                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
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
                                                                                        startActivity(new Intent(WASA.this, MainActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog = myAlert.create();
                                                                        alertDialog.show();
                                                                        ////

                                                                    }
                                                                    else {
                                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(WASA.this);
                                                                        myAlert.setMessage(mStrServerResponse);
                                                                        myAlert.setNegativeButton(
                                                                                "Close",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.cancel();
//                                                            enableUiComponentAfterClick();
                                                                                        startActivity(new Intent(WASA.this, Topup.class));
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog = myAlert.create();
                                                                        alertDialog.show();
                                                                    }
                                                                }

                                                                // mEditTextOldPin.setText("");
                                                                // mEditTextNewPin.setText("");
                                                                // mTextViewShowServerResponse.setText("");
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

                                       // initBtnState();
                                        mEditTextWasaBillNumber.setText("");
                                        mEditTextReTypeWasaBillNumber.setText("");
//									mEditTextOtp.setText("");
//									mEditTextPayerMobileNumber.setText("");

                                    }
                                    else
                                    {
                                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
                                        // //////////////////
                                       // mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                                        mAlertDialogBuilder.setTitle("Check your bill");
                                        mAlertDialogBuilder.setMessage(strBillVerification);
                                        mAlertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                // ----------------------------------------------------------
                                              //  initBtnState();

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
                                   // initBtnState();
                                }
                            });
                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                            mAlertDialog.show();
                        } else {
                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
                            // //////////////////
                           // mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                            mAlertDialogBuilder.setTitle("Check your bill amount");
                            mAlertDialogBuilder.setMessage(strGetAmount);
                            mAlertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // ----------------------------------------------------------
                                   // initBtnState();

                                }
                            });

                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                            mAlertDialog.show();

                        }

                    }

                    // --- check field end----------------------------------
                } else {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
                    // //////////////////
                    //mAlertDialogBuilder.setIcon(R.drawable.icon_alert_dialog);
                    mAlertDialogBuilder.setTitle("Sorry,Bill no does not match!!!");
                    mAlertDialogBuilder.setMessage("Please try again !!!");
                    mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // ----------------------------------------------------------
                           // initBtnState();

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
            mProgressDialog = ProgressDialog.show(WASA.this, null, "Loading Bank...", false, true);
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
                                    ArrayAdapter<String> adapterWallet = new ArrayAdapter<String>(WASA.this,
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
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
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
                            }
                        }
                        //arrayListWalletType.add(tokenWalletTypeAndAccount.nextToken());
                        //arrayListWallet.add(tokenWalletTypeAndAccount.nextToken());
                    }
                }
                else
                {
                    AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
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
                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(WASA.this);
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



    protected String GetAmount(String strAccount) {

        Object response1 = null;
        try {


            String strAccountEncpyt = encryptionDecryption.Encrypt(strAccount,  GlobalData.getStrSessionId());
            METHOD_NAME = "QPAY_WASA_GetAmount";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_WASA_GetAmount";

            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);

            // Declare the version of the SOAP request

            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_BillNo");
            Acc.setValue(strAccountEncpyt);
            Acc.setType(String.class);
            request.addProperty(Acc);

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
            //Log.v("url", "url is:" + URL.toString());
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

    protected void  Submit_bill_Info(String strbillno, String strType, String strPayerMobile, String walletid, String strPIN) {
        try {


            String strbillnoEncpyt = encryptionDecryption.Encrypt(strbillno,  GlobalData.getStrSessionId());
            String strTypeEncpyt = encryptionDecryption.Encrypt(strType,  GlobalData.getStrSessionId());
            String strPayerMobileEncpyt = encryptionDecryption.Encrypt(strPayerMobile,  GlobalData.getStrSessionId());
            String walletidEncpyt = encryptionDecryption.Encrypt(walletid,  GlobalData.getStrSessionId());
            String strPINEncpyt = encryptionDecryption.Encrypt(strPIN,  GlobalData.getStrSessionId());
            METHOD_NAME = "QPAY_Wasa_bilInfo";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_Wasa_bilInfo";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
            // Declare the version of the SOAP request
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_AccountNo");
            Acc.setValue(walletidEncpyt);
            Acc.setType(String.class);
            request.addProperty(Acc);

            PropertyInfo sPIN = new PropertyInfo();
            sPIN.setName("E_PIN");
            sPIN.setValue(strPINEncpyt);
            sPIN.setType(String.class);
            request.addProperty(sPIN);

            PropertyInfo billtype = new PropertyInfo();
            billtype.setName("E_billType");
            billtype.setValue(strTypeEncpyt);
            billtype.setType(String.class);
            request.addProperty(billtype);

            PropertyInfo AccNo = new PropertyInfo();
            AccNo.setName("E_BillNo");
            AccNo.setValue(strbillnoEncpyt);
            AccNo.setType(String.class);
            request.addProperty(AccNo);

            PropertyInfo payer = new PropertyInfo();
            payer.setName("E_PayerMobileNo");
            payer.setValue(strPayerMobileEncpyt);
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
           // Log.v("url", "url is:" + URL.toString());
            androidHttpTransport.call(SOAP_ACTION, envelope);
            response1 = envelope.getResponse();
            strResonse=response1.toString();
            mStrServerResponse=strResponse;
           // Log.v("response:", response1.toString());
            //mTextViewShowServerResponse.setText(response1.toString());
           // Log.v("Result:", mTextViewShowServerResponse.getText().toString());
        } catch (Exception exception) {
            // ViewResult.setText(exception.toString()+" Or enter number is not
            // Available!");
            mTextViewShowServerResponse.setText("Please try again");
            Log.v("tracing  value ", "Get result is: " + mTextViewShowServerResponse.getText().toString());
        }
    }

    protected void Submit_Bill_payment(String strbillno, String strType, String strPayerMobile, String walletid, String strPIN,
                             String strOTP) {
        try {


            String strbillnoEncpyt = encryptionDecryption.Encrypt(strbillno,  GlobalData.getStrSessionId());
            String strTypeEncpyt = encryptionDecryption.Encrypt(strType,  GlobalData.getStrSessionId());
            String strPayerMobileEncpyt = encryptionDecryption.Encrypt(strPayerMobile,  GlobalData.getStrSessionId());
            String walletidEncpyt = encryptionDecryption.Encrypt(walletid,  GlobalData.getStrSessionId());
            String strPINEncpyt = encryptionDecryption.Encrypt(strPIN,  GlobalData.getStrSessionId());
            String strOTPEncpyt = encryptionDecryption.Encrypt(strOTP,  GlobalData.getStrSessionId());

            METHOD_NAME = "QPAY_WASA_BillPay";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_WASA_BillPay";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
            // Declare the version of the SOAP request
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_AccountNo");
            Acc.setValue(walletidEncpyt);
            Acc.setType(String.class);
            request.addProperty(Acc);

            PropertyInfo sPIN = new PropertyInfo();
            sPIN.setName("E_PIN");
            sPIN.setValue(strPINEncpyt);
            sPIN.setType(String.class);
            request.addProperty(sPIN);

            PropertyInfo billtype = new PropertyInfo();
            billtype.setName("E_billType");
            billtype.setValue(strTypeEncpyt);
            billtype.setType(String.class);
            request.addProperty(billtype);

            PropertyInfo AccNo = new PropertyInfo();
            AccNo.setName("E_BillNo");
            AccNo.setValue(strbillnoEncpyt);
            AccNo.setType(String.class);
            request.addProperty(AccNo);

            PropertyInfo payer = new PropertyInfo();
            payer.setName("E_PayerMobileNo");
            payer.setValue(strPayerMobileEncpyt);
            payer.setType(String.class);
            request.addProperty(payer);

            PropertyInfo otp = new PropertyInfo();
            otp.setName("E_OTP");
            otp.setValue(strOTPEncpyt);
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
            strResonseOtp=response1.toString();
            mStrServerResponse=strResonseOtp;
            //Log.v("response:", response1.toString());
            //mTextViewShowServerResponse.setText(response1.toString());
           // Log.v("Result:", mTextViewShowServerResponse.getText().toString());
        } catch (Exception exception) {
            // ViewResult.setText(exception.toString()+" Or enter number is not
            // Available!");
            mTextViewShowServerResponse.setText("Please try again");
            Log.v("tracing  value ", "Get result is: " + mTextViewShowServerResponse.getText().toString());
        }

    }

    protected  void InsertAmount(String strbillno,String strResonse,String strRequestLog,String strResponseLog)
    {
        try {


            String strbillnoEncpyt = encryptionDecryption.Encrypt(strbillno,  GlobalData.getStrSessionId());
            String strResonseEncpyt = encryptionDecryption.Encrypt(strResonse,  GlobalData.getStrSessionId());
            String strRequestLogEncpyt = encryptionDecryption.Encrypt(strRequestLog,  GlobalData.getStrSessionId());
            String strResponseLogEncpyt = encryptionDecryption.Encrypt(strResponseLog,  GlobalData.getStrSessionId());


            METHOD_NAME = "QPAY_InsertAmountWasa";
            SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_InsertAmountWasa";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
            // Declare the version of the SOAP request
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo Acc = new PropertyInfo();
            Acc.setName("E_strBillNo");
            Acc.setValue(strbillnoEncpyt);
            Acc.setType(String.class);
            request.addProperty(Acc);

            PropertyInfo sPIN = new PropertyInfo();
            sPIN.setName("E_strResponse");
            sPIN.setValue(strResonseEncpyt);
            sPIN.setType(String.class);
            request.addProperty(sPIN);

            PropertyInfo billtype = new PropertyInfo();
            billtype.setName("E_strRequestLog");
            billtype.setValue(strRequestLogEncpyt);
            billtype.setType(String.class);
            request.addProperty(billtype);

            PropertyInfo AccNo = new PropertyInfo();
            AccNo.setName("E_strResponseLog");
            AccNo.setValue(strResponseLogEncpyt);
            AccNo.setType(String.class);
            request.addProperty(AccNo);


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
            strResponseLast=response1.toString();
            //Log.v("response:", response1.toString());
            //mTextViewShowServerResponse.setText(response1.toString());
            //Log.v("Result:", mTextViewShowServerResponse.getText().toString());
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
                startActivity(new Intent(WASA.this, MainActivity.class));
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
            startActivity(new Intent(WASA.this, Utility_Bill.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clearEditText() {

        mEditTextReTypeWasaBillNumber.setText("");
        mEditTextWasaBillNumber.setText("");
        mEditTextPayerMobileNumber.setText("");
    }
}
