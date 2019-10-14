package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetDistrict;
import com.haltechbd.app.android.qpay.utils.GetThana;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class KycUpdateAddressInfo extends AppCompatActivity implements View.OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    ArrayList<String> arrayListDistrictId = new ArrayList<String>();
    ArrayList<String> arrayListDistrict = new ArrayList<String>();
    ArrayList<String> arrayListThanaId = new ArrayList<String>();
    ArrayList<String> arrayListThana = new ArrayList<String>();

    private Spinner mSpinnerDistrict, mSpinnerThana;
    private EditText mEditTextPresentAddress, mEditTextPermanentAddress, mEditTextOfficeAddress;
    private Button mBtnSaveAddress;
    private TextView mTextViewShowServerResponse;
    private String mStrAccountNumber, mStrPin, mStrMasterKey,
            mStrEncryptAccountNumber, mStrEncryptPin,
            mStrDistirctId, mStrEncryptDistrictId, mStrThanaId, mStrEncryptThanaId,
            mStrPresentAddress, mStrPermanentAddress, mStrOfficeAddress,mStrServerResponse,strParamete;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_update_address_info);
        checkOs();
        // initialize all ui components
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {
        mSpinnerDistrict = findViewById(R.id.spinnerDistrictAddressinfo);

        mSpinnerDistrict.setFocusable(true);
        mSpinnerDistrict.setFocusableInTouchMode(true);
        mSpinnerThana = findViewById(R.id.spinnerThana);
        mEditTextPresentAddress = findViewById(R.id.editTextKycAddressInfoPresentAddress);
        mEditTextPermanentAddress = findViewById(R.id.editTextKycAddressInfoPermanentAddress);
        mEditTextOfficeAddress = findViewById(R.id.editTextKycAddressInfoOfficeAddress);
        mBtnSaveAddress = findViewById(R.id.btnKycAddressInfoSaveAddress);
        mBtnSaveAddress.setOnClickListener(this);
        mTextViewShowServerResponse = findViewById(R.id.txtViewKycAddressInfoShowServerResponse);

        mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();

        mEditTextPresentAddress.setText(GlobalData.getStrKYCAddinfoPresent());
        mEditTextPermanentAddress.setText(GlobalData.getStrKYCAddinfoParmanent());
        mEditTextOfficeAddress.setText(GlobalData.getStrKYCAddinfoOffice());


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSaveAddress) {
            if (mEditTextPresentAddress.getText().toString().length() == 0) {
                mEditTextPresentAddress.setError("Field cannot be empty");
            } else if (mEditTextPermanentAddress.getText().toString().length() == 0) {
                mEditTextPermanentAddress.setError("Field cannot be empty");
            } else if (mEditTextOfficeAddress.getText().toString().length() == 0) {
                mEditTextOfficeAddress.setError("Field cannot be empty");
            } else {
//                disableUiComponentAfterClick();
                try {
                    mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, GlobalData.getStrSessionId());
                    mStrEncryptPin = encryption.Encrypt(mStrPin, GlobalData.getStrSessionId());
                    mStrPresentAddress = encryption.Encrypt(mEditTextPresentAddress.getText().toString(), GlobalData.getStrSessionId());
                    mStrPermanentAddress = encryption.Encrypt(mEditTextPermanentAddress.getText().toString(), GlobalData.getStrSessionId());
                    mStrOfficeAddress = encryption.Encrypt(mEditTextOfficeAddress.getText().toString(), GlobalData.getStrSessionId());
                    //mStrEncryptThanaId= encryption.Encrypt(mEditTextOfficeAddress.getText().toString(), GlobalData.getStrSessionId());
                    strParamete=encryption.Encrypt("U", GlobalData.getStrSessionId());

                    // Initialize progress dialog
                    mProgressDialog = ProgressDialog.show(KycUpdateAddressInfo.this, null, "Processing request...", false, true);
                    // Cancel progress dialog on back key press
                    mProgressDialog.setCancelable(true);

                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            mStrServerResponse = InsertKyc.insertAddressInfo(
                                    mStrEncryptAccountNumber,
                                    mStrEncryptPin,
                                    mStrEncryptThanaId,
                                    mStrPresentAddress,
                                    mStrPermanentAddress,
                                    mStrOfficeAddress,
                                    strParamete);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                            if(mStrServerResponse.equalsIgnoreCase("Update")){
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateAddressInfo.this);
                                                myAlert.setMessage("Successfully save address info.");
                                                myAlert.setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
//                                                                Intent intent = new Intent(KycUpdateAddressInfo.this, KycPreviewAddressInfo.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                startActivity(intent);
                                                                startActivity(new Intent(KycUpdateAddressInfo.this, KycPreviewAddressInfo.class));
                                                                finish();
                                                            }
                                                        });
                                                AlertDialog alertDialog = myAlert.create();
                                                alertDialog.show();
                                            }else{
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                //####################### Show Dialog ####################
                                                if(mStrServerResponse.equalsIgnoreCase(""))
                                                {
                                                    mStrServerResponse="Request is in process";
                                                }
                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateAddressInfo.this);
                                                myAlert.setMessage(mStrServerResponse);
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
            try {
                mStrAccountNumber = GlobalData.getStrAccountNumber();
                mStrPin = GlobalData.getStrPin();
                mStrMasterKey = GlobalData.getStrMasterKey();
                mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, GlobalData.getStrSessionId());
                mStrEncryptPin = encryption.Encrypt(mStrPin, GlobalData.getStrSessionId());
                loadSpinnerDistrict();
                ArrayAdapter<String> adapterDistrictName = new ArrayAdapter<String>(KycUpdateAddressInfo.this,
                        android.R.layout.simple_spinner_item, arrayListDistrict);
                adapterDistrictName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerDistrict.setAdapter(adapterDistrictName);
                mSpinnerDistrict.setOnItemSelectedListener(onItemSelectedListenerForDistrict);
                if(GlobalData.getStrKYCAddInfoDistrictID()!=null && !GlobalData.getStrKYCAddInfoDistrictID().isEmpty())
                {
                    //arrayListDistrictId.add( GlobalData.getStrKYCAddInfoDistrictID());
                    //arrayListDistrict.add( GlobalData.getStrKYCAddInfoDistrictName());
                    int spinnerPosition = adapterDistrictName.getPosition( GlobalData.getStrKYCAddInfoDistrictName());
                    mSpinnerDistrict.setSelection(spinnerPosition);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdateAddressInfo.this);
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

    //######################### Show District #########################
    //######################### Show District #########################
    //######################### Show District #########################
    private void loadSpinnerDistrict() {
        arrayListDistrict.clear();
        arrayListDistrictId.clear();
        String str = GetDistrict.getDistrict(mStrEncryptAccountNumber, mStrEncryptPin);
        if (str != null && !str.isEmpty()) {
            StringTokenizer strToken = new StringTokenizer(str, "&");
            ArrayList<String> arrayListDistrictIdAndName = new ArrayList<String>();
            for (int j = 0; j <= strToken.countTokens(); j++) {
                while (strToken.hasMoreElements()) {
                    arrayListDistrictIdAndName.add(strToken.nextToken());
                }
            }
            for (int i = 0; i <= arrayListDistrictIdAndName.size() - 1; i++) {
                StringTokenizer tokenDistrictIdAndName = new StringTokenizer(arrayListDistrictIdAndName.get(i), "*");
                arrayListDistrictId.add(tokenDistrictIdAndName.nextToken());
                arrayListDistrict.add(tokenDistrictIdAndName.nextToken());
            }
        } else {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdateAddressInfo.this);
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

    //######################### District ID #########################
    //######################### District ID #########################
    //######################### District ID #########################
    AdapterView.OnItemSelectedListener onItemSelectedListenerForDistrict = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mStrDistirctId = String.valueOf(arrayListDistrictId.get(position));
            if (mStrDistirctId != null && !mStrDistirctId.isEmpty()) {
                try {
                    mStrEncryptDistrictId = encryption.Encrypt(mStrDistirctId, GlobalData.getStrSessionId());
                    loadSpinnerThana();
                    ArrayAdapter<String> adapterThanaName = new ArrayAdapter<String>(KycUpdateAddressInfo.this,
                            android.R.layout.simple_spinner_item, arrayListThana);
                    adapterThanaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinnerThana.setAdapter(adapterThanaName);
                    mSpinnerThana.setOnItemSelectedListener(onItemSelectedListenerForThana);

                    if(GlobalData.getStrKYCAddInfoDistrictID()!=null && !GlobalData.getStrKYCAddInfoDistrictID().isEmpty())
                    {
                       // arrayListThanaId.add( GlobalData.getStrKYCAddInfoThanaId());
                       // arrayListThana.add( GlobalData.getStrKYCAddInfoThanaNAme());
                        int spinnerPosition = adapterThanaName.getPosition(GlobalData.getStrKYCAddInfoThanaNAme());
                        mSpinnerThana.setSelection(spinnerPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    //######################### Show Thana #########################
    //######################### Show Thana #########################
    //######################### Show Thana #########################
    private void loadSpinnerThana() {
        arrayListThana.clear();
        arrayListThanaId.clear();
        String strAllThanaIdAndName = GetThana.getThana(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptDistrictId);
        if (strAllThanaIdAndName != null && !strAllThanaIdAndName.isEmpty()) {
            StringTokenizer strToken = new StringTokenizer(strAllThanaIdAndName, "&");
            ArrayList<String> arrayListThanaIdAndName = new ArrayList<String>();
            for (int j = 0; j <= strToken.countTokens(); j++) {
                while (strToken.hasMoreElements()) {
                    arrayListThanaIdAndName.add(strToken.nextToken());
                }
            }
            for (int i = 0; i <= arrayListThanaIdAndName.size() - 1; i++) {
                StringTokenizer tokenThanaIdAndName = new StringTokenizer(arrayListThanaIdAndName.get(i), "*");
                arrayListThanaId.add(tokenThanaIdAndName.nextToken());
                arrayListThana.add(tokenThanaIdAndName.nextToken());
            }
        } else {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdateAddressInfo.this);
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

        if(GlobalData.getStrKYCAddInfoDistrictID()!=null && !GlobalData.getStrKYCAddInfoDistrictID().isEmpty())
        {
            arrayListThanaId.add( GlobalData.getStrKYCAddInfoThanaId());
            arrayListThana.add( GlobalData.getStrKYCAddInfoThanaNAme());
        }
    }

    //######################### Thana ID #########################
    //######################### Thana ID #########################
    //######################### Thana ID #########################
    AdapterView.OnItemSelectedListener onItemSelectedListenerForThana = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mStrThanaId = String.valueOf(arrayListThanaId.get(position));
            try{
                mStrEncryptThanaId=encryption.Encrypt(mStrThanaId,GlobalData.getStrSessionId());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void enableUiComponents() {
        mSpinnerDistrict.setEnabled(true);
        mSpinnerThana.setEnabled(true);
        mEditTextPresentAddress.setEnabled(true);
        mEditTextPermanentAddress.setEnabled(true);
        mEditTextOfficeAddress.setEnabled(true);
        mBtnSaveAddress.setEnabled(true);
    }

    private void disableUiComponents() {
        mSpinnerDistrict.setEnabled(false);
        mSpinnerThana.setEnabled(false);
        mEditTextPresentAddress.setEnabled(false);
        mEditTextPermanentAddress.setEnabled(false);
        mEditTextOfficeAddress.setEnabled(false);
        mBtnSaveAddress.setEnabled(false);
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
                startActivity(new Intent(KycUpdateAddressInfo.this, KycPreviewAddressInfo.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycUpdateAddressInfo.this, Login.class)
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
      //  GlobalData.setStrEncryptAccountNumber("");
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
            startActivity(new Intent(KycUpdateAddressInfo.this, KycPreviewAddressInfo.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
