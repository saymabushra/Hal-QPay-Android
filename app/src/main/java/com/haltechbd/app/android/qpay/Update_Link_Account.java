package com.haltechbd.app.android.qpay;

import android.app.Activity;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bushra on 5/16/2018.
 */

public class Update_Link_Account extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnSaveLink, btnEditLink, BtnEditLinkAccountStatus, btnSaveLinkAccountStatus, BtnEditLinkAccountDefultStatus, btnSaveLinkAccountDefultStatus;
    TextView txtLinkAccountNo, mTextViewShowServerResponse,txtviewUpdateLinkAccountname;
    EditText edittxtlinkaccountname;
    Spinner spLinkAccountStatus, spLinkAccountDefultStatus;

    String mStrMasterKey, mStrServerResponse, strName, strLinkAccountStatus, strLinkAccountDefultStatus;
    private ProgressDialog mProgressDialog = null;
    private EncryptionDecryption encryptDecrypt = new EncryptionDecryption();

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_link_account_new);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {

        txtLinkAccountNo = findViewById(R.id.txtlinkAccountNo);
        edittxtlinkaccountname = findViewById(R.id.editTextlinkAccountName);
        txtviewUpdateLinkAccountname= findViewById(R.id.txtviewUpdateLinkAccountname);
        mTextViewShowServerResponse = findViewById(R.id.txtViewupdateLinkAccountShowServerResponse);

        spLinkAccountStatus = findViewById(R.id.spinnerlinkaccountStatus);
        spLinkAccountDefultStatus = findViewById(R.id.spinnerlinkAccountDefultSatus);

        btnEditLink = findViewById(R.id.btnEditUpdatelinkAcc);
        btnSaveLink = findViewById(R.id.btnSaveUpdatelinkAcc);

//        BtnEditLinkAccountStatus = findViewById(R.id.EditBtnlinkAccountStatus);
//        btnSaveLinkAccountStatus = findViewById(R.id.SaveBtnLinkAccountStatus);
//
//        BtnEditLinkAccountDefultStatus = findViewById(R.id.EditBtnLinkAccountDefulStatus);
//        btnSaveLinkAccountDefultStatus = findViewById(R.id.SaveBtnLinkAccountDefultStatus);

        txtLinkAccountNo.setText("Account No: " + GlobalData.getStrLinkAccountNo());
        txtviewUpdateLinkAccountname.setText(GlobalData.getStrAccountHolderName());
        edittxtlinkaccountname.setText(GlobalData.getStrLinkAccountName());

        mStrMasterKey = GlobalData.getStrMasterKey();
        checkInternet();
        loadSpinnerLinkAccountStatus();
        loadSpinnerLinkAccountDefultStatus();

        spLinkAccountStatus.setEnabled(false);
        spLinkAccountDefultStatus.setEnabled(false);

        if (GlobalData.getStrLinkAccountDefult().equalsIgnoreCase("Y")) {
            spLinkAccountDefultStatus.setSelection(0);
        } else if (GlobalData.getStrLinkAccountDefult().equalsIgnoreCase("N")) {
            spLinkAccountDefultStatus.setSelection(1);
        }

        if (GlobalData.getStrLinkAccountStatus().equalsIgnoreCase("A")) {
            spLinkAccountStatus.setSelection(0);
        } else {
            spLinkAccountStatus.setSelection(1);
        }

        btnEditLink.setOnClickListener(this);
        btnSaveLink.setOnClickListener(this);

        btnEditLink.setEnabled(true);
        btnSaveLink.setEnabled(false);
//        BtnEditLinkAccountStatus.setOnClickListener(this);
//        btnSaveLinkAccountStatus.setOnClickListener(this);
//
//        BtnEditLinkAccountDefultStatus.setOnClickListener(this);
//        btnSaveLinkAccountDefultStatus.setOnClickListener(this);

    }

    public void loadSpinnerLinkAccountStatus() {
        spLinkAccountStatus = findViewById(R.id.spinnerlinkaccountStatus);
        List<String> list = new ArrayList<String>();
        list.add("Active");
        list.add("In Active");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLinkAccountStatus.setAdapter(dataAdapter);

    }

    public void loadSpinnerLinkAccountDefultStatus() {
        spLinkAccountDefultStatus = findViewById(R.id.spinnerlinkAccountDefultSatus);
        List<String> list = new ArrayList<String>();
        list.add("Default");
        list.add("Non Default");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLinkAccountDefultStatus.setAdapter(dataAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v == btnEditLink) {
            edittxtlinkaccountname.setEnabled(true);

            if (GlobalData.getStrLinkAccountDefult().equalsIgnoreCase("Y"))
            {
                spLinkAccountStatus.setClickable(false);
                spLinkAccountStatus.setEnabled(false);
            }
            else
            {
                spLinkAccountStatus.setClickable(true);
                spLinkAccountStatus.setEnabled(true);
            }


            spLinkAccountDefultStatus.setClickable(true);
            spLinkAccountDefultStatus.setEnabled(true);

            btnSaveLink.setEnabled(true);
            btnEditLink.setEnabled(false);
            btnEditLink.setImageResource(R.drawable.ic_edit_deselect);
            btnSaveLink.setImageResource(R.drawable.ic_save);
        }
        else if (v == btnSaveLink) {


            strName = edittxtlinkaccountname.getText().toString();
            strLinkAccountStatus = String.valueOf(spLinkAccountStatus.getSelectedItem());
            if (strLinkAccountStatus == "Active") {
                strLinkAccountStatus = "A";
            } else if (strLinkAccountStatus == "In Active") {
                strLinkAccountStatus = "I";
            }

            strLinkAccountDefultStatus = String.valueOf(spLinkAccountDefultStatus.getSelectedItem());
            if (strLinkAccountDefultStatus == "Default") {
                strLinkAccountDefultStatus = "Y";
            } else if (strLinkAccountDefultStatus == "Non Default") {
                strLinkAccountDefultStatus = "N";
            }

            mProgressDialog = ProgressDialog.show(Update_Link_Account.this, null, "Save...", false, true);
            // Cancel progress dialog on back key press
            mProgressDialog.setCancelable(true);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {

                    CallSaveinfo(strName, strLinkAccountStatus, strLinkAccountDefultStatus);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();

                                    if(mStrServerResponse.equalsIgnoreCase("Update")) {
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Update_Link_Account.this);
                                        myAlert.setMessage("Info Update Successfully");
                                        myAlert.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        try {
                                                          String mStrEncryptAccountNumber = encryptDecrypt.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                                                           String strGetAccountList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
                                                           GlobalData.setStrGetAccountList(strGetAccountList);
                                                       }
                                                       catch (Exception ex)
                                                       {

                                                       }

                                                        startActivity(new Intent(Update_Link_Account.this, Link_Account_New_V2.class));
                                                        finish();

                                                    }
                                                });

                                        AlertDialog alertDialog = myAlert.create();
                                        alertDialog.show();
                                    }
                                    else
                                    {
                                        if(mStrServerResponse.equalsIgnoreCase(""))
                                        {
                                            mStrServerResponse="Request is in process";
                                        }
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(Update_Link_Account.this);
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

            edittxtlinkaccountname.setEnabled(false);
            spLinkAccountStatus.setClickable(false);
            spLinkAccountStatus.setEnabled(false);
            spLinkAccountDefultStatus.setClickable(false);
            spLinkAccountDefultStatus.setEnabled(false);

            btnSaveLink.setEnabled(false);
            btnEditLink.setEnabled(true);
            btnEditLink.setImageResource(R.drawable.ic_edit);
            btnSaveLink.setImageResource(R.drawable.ic_save_deselect);

        }


        }



    public  void CallSaveinfo(String strName,String AccountStatus,String strDefultStatus)
    {
        try
        {

            String EncryptName = encryptDecrypt.Encrypt(strName,  GlobalData.getStrSessionId());
            String EncryptAccountStatus = encryptDecrypt.Encrypt(AccountStatus, GlobalData.getStrSessionId());
            String EncryptDefultStatus = encryptDecrypt.Encrypt(strDefultStatus,  GlobalData.getStrSessionId());
            String EncryptLinkAccount = encryptDecrypt.Encrypt(GlobalData.getStrLinkAccountNo(),  GlobalData.getStrSessionId());
            String EncptyAccountNo = encryptDecrypt.Encrypt(GlobalData.getStrAccountNumber(),  GlobalData.getStrSessionId());


            String METHOD_NAME = "QPAY_UpdateLinkAccount";
            String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_UpdateLinkAccount";
            SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);




            PropertyInfo encryptPin = new PropertyInfo();
            encryptPin.setName("E_AccountNO");
            encryptPin.setValue(EncptyAccountNo);
            encryptPin.setType(String.class);
            request.addProperty(encryptPin);

            PropertyInfo encryptLink = new PropertyInfo();
            encryptLink.setName("E_LinkAccountNO");
            encryptLink.setValue(EncryptLinkAccount);
            encryptLink.setType(String.class);
            request.addProperty(encryptLink);


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

            PropertyInfo linkAccount = new PropertyInfo();
            linkAccount.setName("E_LinkAccountName");
            linkAccount.setValue(EncryptName);
            linkAccount.setType(String.class);
            request.addProperty(linkAccount);

            PropertyInfo linkAccountstatus = new PropertyInfo();
            linkAccountstatus.setName("E_linkAccountStatus");
            linkAccountstatus.setValue(EncryptAccountStatus);
            linkAccountstatus.setType(String.class);
            request.addProperty(linkAccountstatus);

            PropertyInfo LinkAccountDefult = new PropertyInfo();
            LinkAccountDefult.setName("E_LinkAccountDefultStatus");
            LinkAccountDefult.setValue(EncryptDefultStatus);
            LinkAccountDefult.setType(String.class);
            request.addProperty(LinkAccountDefult);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            envelope.implicitTypes = true;
            Object objLoginResponse = null;


            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objLoginResponse = envelope.getResponse();
            String strLoginResponse = objLoginResponse.toString();
            mStrServerResponse =strLoginResponse;

        }
        catch (Exception ex)
        {

        }

    }
    private void checkOs() {

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Update_Link_Account.this);
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

    //########################## Back ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:0");
        return true;
    }

    //########################## Logout ############################
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Update_Link_Account.this, Link_Account_New_V2.class));
                finish();
                return true;
            case R.id.actionRewardpoint:

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Update_Link_Account.this, Link_Account_New_V2.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
