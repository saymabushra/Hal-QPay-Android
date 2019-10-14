package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class KycUpdatePersonalInfo extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    private Calendar cal;
    private int day;
    private int month;
    private int year;


    // initialize all ui components
    private EditText mEditTextDateOfBirth, mEditTextOccupation, mEditTextOrganizationName;
    private Button mBtnSubmit;
    private TextView mTextViewShowServerResponse;
    private String mStrGender, mStrMasterKey, mStrEncryptAccountNumber, mStrEncryptPin,
            mStrEncryptDateOfBirth, mStrEncryptOccupation, mStrEncryptOrganizationName,
            mStrEncryptGender, mStrServerResponse, mStrCurrentDate,strParamete;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_update_personal_info);
        checkOs();
        // initialize all ui components
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mEditTextDateOfBirth = findViewById(R.id.editTextKycPersonalInfoDateOfBirth);
        mEditTextDateOfBirth.requestFocus();
        mEditTextOccupation = findViewById(R.id.editTextKycPersonalInfoOccupation);
        mEditTextOrganizationName = findViewById(R.id.editTextKycPersonalInfoOrganizationName);
        mBtnSubmit = findViewById(R.id.btnKycPersonalInfoSubmit);
        mBtnSubmit.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.textViewKycPersonalInfoServerResponse);
        mStrMasterKey = GlobalData.getStrMasterKey();


        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mStrCurrentDate = sdf.format(new Date());

//        mEditTextDateOfBirth.setText(mStrCurrentDate);
        mEditTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogFromDate();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();

        mEditTextDateOfBirth.setText(GlobalData.getStrKYCPersonalInfoDOB());
        mEditTextOrganizationName.setText(GlobalData.getStrKYCPersonalInfoOrg());
        String Gender= GlobalData.getStrKYCPersonalInfoGender();
        mEditTextOccupation.setText( GlobalData.getStrKYCPersonalInfoOccupation());

        try {
            if (Gender.equalsIgnoreCase("M") || Gender.equalsIgnoreCase("F")) {
            RadioGroup rb1 = (RadioGroup) findViewById(R.id.radiogroup);
            RadioButton rbmale = (RadioButton) findViewById(R.id.radio_male);
            RadioButton rbFemale = (RadioButton) findViewById(R.id.radio_female);

                if (Gender.equalsIgnoreCase("M")) {
                    rbmale.setChecked(true);
                    mStrGender = "M";
                } else {

                    rbFemale.setChecked(true);
                    mStrGender = "F";
                }
            }
        }
        catch(Exception ex)
        {}

    }

    public void datePickerDialogFromDate() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String strDay = Integer.toString(dayOfMonth);
                String strMonth = Integer.toString(monthOfYear + 1);

                if (strDay.length() == 1) {
                    strDay = "0" + strDay;
                }
                if (strMonth.length() == 1) {
                    strMonth = "0" + strMonth;
                }
                mEditTextDateOfBirth.setText(strDay + "/" + strMonth + "/" + year);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();

    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mEditTextDateOfBirth.getText().toString().length() == 0) {
                mEditTextDateOfBirth.setError("Field cannot be empty");
            } else if (mEditTextOccupation.getText().toString().length() == 0) {
                mEditTextOccupation.setError("Field cannot be empty");
            } else if (mEditTextOrganizationName.getText().toString().length() == 0) {
                mEditTextOrganizationName.setError("Field cannot be empty");
            } else {
//                disableUiComponentAfterClick();
                try {
                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(),  GlobalData.getStrSessionId());
                    mStrEncryptDateOfBirth = encryption.Encrypt(mEditTextDateOfBirth.getText().toString(),  GlobalData.getStrSessionId());
                    mStrEncryptOccupation = encryption.Encrypt(mEditTextOccupation.getText().toString(),  GlobalData.getStrSessionId());
                    mStrEncryptOrganizationName = encryption.Encrypt(mEditTextOrganizationName.getText().toString(),  GlobalData.getStrSessionId());
                    mStrEncryptGender = encryption.Encrypt(mStrGender,  GlobalData.getStrSessionId());

                    strParamete=encryption.Encrypt("U", GlobalData.getStrSessionId());
                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(KycUpdatePersonalInfo.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
//                    if (CheckMasterKeyAndSessionId.checkMasterKeyAndSessionId() == true) {
                          String strGet= InsertKyc.insertPersonalInfo(
                                    mStrEncryptAccountNumber,
                                    mStrEncryptPin,
                                    mStrEncryptDateOfBirth,
                                    mStrEncryptOccupation,
                                    mStrEncryptOrganizationName,
                                    mStrEncryptGender,
                                    strParamete
                            );
                            mStrServerResponse = strGet;
                            if (mStrServerResponse.equalsIgnoreCase("Update")) {
                                mStrServerResponse = "Personal Info update succesfully.";
                            } else {
                                mStrServerResponse = strGet;
                            }

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
                                            int intIndex = mStrServerResponse.indexOf("Personal Info update succesfully.");
                                            if (intIndex == -1) {
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdatePersonalInfo.this);
                                                myAlert.setMessage(mStrServerResponse);
                                                myAlert.setNegativeButton(
                                                        "Close",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
//                                                            enableUiComponentAfterClick();

                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            } else {
                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                {
                                                    mStrServerResponse="Request is in process";
                                                }
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdatePersonalInfo.this);
                                                myAlert.setMessage(mStrServerResponse);
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
//                                                                Intent intent = new Intent(KycUpdatePersonalInfo.this, KycPreviewPersonalInfo.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                startActivity(intent);
                                                               startActivity(new Intent(KycUpdatePersonalInfo.this, KycPreviewPersonalInfo.class));
                                                               finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
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
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

    }



    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdatePersonalInfo.this);
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

    private void enableUiComponents() {
        mEditTextDateOfBirth.setEnabled(true);
        mEditTextOccupation.setEnabled(true);
        mEditTextOrganizationName.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextDateOfBirth.setEnabled(false);
        mEditTextOccupation.setEnabled(false);
        mEditTextOrganizationName.setEnabled(false);
        mBtnSubmit.setEnabled(false);
        mBtnSubmit.setEnabled(false);
    }

    //########################## Logout ############################
    //########################## Logout ############################
    //########################## Logout ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:0");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(KycUpdatePersonalInfo.this, KycPreviewPersonalInfo.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycUpdatePersonalInfo.this, Login.class)
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_male:
                if (checked)
                    mStrGender = "M";
                Toast.makeText(KycUpdatePersonalInfo.this, "Male", Toast.LENGTH_LONG).show();
                break;
            case R.id.radio_female:
                if (checked)
                    mStrGender = "F";
                Toast.makeText(KycUpdatePersonalInfo.this, "Female", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(KycUpdatePersonalInfo.this, KycPreviewPersonalInfo.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}

