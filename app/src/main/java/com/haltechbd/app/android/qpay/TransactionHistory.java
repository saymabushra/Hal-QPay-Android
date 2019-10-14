package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

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
import java.util.StringTokenizer;

/**
 * @author Mr. Nazmuzzaman, Kowshik Ahmed, Umme Sayma Bushra, Muhammad Sadat
 *         Al-Jony
 * @version 1.0
 * @company Bangladesh Microtechnology Ltd.
 * @since Nov 2015
 */


@SuppressWarnings("FieldCanBeLocal")
public class TransactionHistory extends AppCompatActivity implements OnClickListener {
    private String SOAP_ACTION, METHOD_NAME;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog;

    private Calendar cal;
    private int day;
    private int month;
    private int year;
    String strget;
    // initialize all ui components
    private EditText mEditTextFromDate, mEditTextToDate;
    private Button mBtnSubmit;
    private ListView mListViewTransactionHistory;

    private String mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptAccessCode,
            mStrEncryptFromDate, mStrEncryptToDate, mStrMasterKey, mStrCurrentDate,
            mStrEncryptParameter, mStrLastFiveTransaction, mStrAllTransaction,PrintTxt;

    ArrayList<String> arrayListLastFiveTransaction = new ArrayList<String>();
    ArrayList<String> arrayListAllTransaction = new ArrayList<String>();
//    ArrayList<String> arrayListReferenceId = new ArrayList<String>();
//    ArrayList<String> arrayListAccessCode = new ArrayList<String>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history);
        checkOs();
        initUI();
    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    private void initUI() {
        mEditTextFromDate = findViewById(R.id.editTextAllResponseFromDate);
        mEditTextToDate = findViewById(R.id.editTextAllResponseToDate);
        mBtnSubmit = findViewById(R.id.btnAllResponseSubmit);
        mBtnSubmit.setOnClickListener(this);
        mListViewTransactionHistory = findViewById(R.id.listViewAllResponse);
        mStrMasterKey = GlobalData.getStrMasterKey();

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mStrCurrentDate = sdf.format(new Date());

        mEditTextFromDate.setText(mStrCurrentDate);
        mEditTextToDate.setText(mStrCurrentDate);

        mEditTextFromDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogFromDate();
            }
        });

        mEditTextToDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogToDate();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();


    }

    private void loadLastFiveTransactionHistory() {
        arrayListLastFiveTransaction.clear();
//        arrayListReferenceId.clear();
//        arrayListAccessCode.clear();
        try {
            mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
            mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
            mStrEncryptFromDate = encryption.Encrypt(mStrCurrentDate, GlobalData.getStrSessionId());
            mStrEncryptToDate = encryption.Encrypt(mStrCurrentDate, GlobalData.getStrSessionId());
            //######################### Need to Modify ##########################
            mStrEncryptAccessCode = encryption.Encrypt("all", GlobalData.getStrSessionId());
            //######################### Need to Modify ##########################
            mStrEncryptParameter = encryption.Encrypt("5", GlobalData.getStrSessionId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Initialize progress dialog
        mProgressDialog = ProgressDialog.show(TransactionHistory.this, null, "Loading Last 5 Transaction History...", false, true);
        // Cancel progress dialog on back key press
        mProgressDialog.setCancelable(true);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                getLastFiveTransactionHistory(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptFromDate,
                        mStrEncryptToDate, mStrEncryptAccessCode, mStrEncryptParameter);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                                //================== Show Transaction History List ======================
                                //================== Show Transaction History List ======================
                                //================== Show Transaction History List ======================
//                                Collections.reverse(arrayListLastFiveTransaction);
                                ArrayAdapter<String> arrayAdapterTransaction = new ArrayAdapter<String>(
                                        TransactionHistory.this, android.R.layout.simple_list_item_1, arrayListLastFiveTransaction);
                                mListViewTransactionHistory.setAdapter(arrayAdapterTransaction);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        t.start();

        //-=====================================

        mListViewTransactionHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View view, int position, long id) {

                strget=arrayListLastFiveTransaction.get(position);

                AlertDialog.Builder myAlert = new AlertDialog.Builder(TransactionHistory.this);
                myAlert.setTitle("Print Receipt");
                myAlert.setMessage(strget);
                myAlert.setPositiveButton(
                        "Print",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // --------------------------------
                                int intIndex = strget.indexOf("Balance:");
                                if (intIndex == -1)
                                {
                                    PrintTxt=strget;

                                }
                                else {
                                    String[] split = strget.split("Balance:");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    //String newString1 = firstSubString.replace("Balance:", "");
                                    String[] split2 = secondSubString.split("TXN ID:");
                                    String firstSubString2 = split2[0];
                                    String secondSubString2 = split2[1];
                                    String full = firstSubString + "TXN ID: " + secondSubString2;
                                    PrintTxt=full;
                                }
                                Intent i = new Intent(
                                        TransactionHistory.this,
                                        Printer.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("print",
                                        PrintTxt);
                                // Add the bundle to the
                                // intent
                                i.putExtras(bundle);
                                startActivity(i);
                                // --------------------------------
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
        });

        //======================================

    }

    public void getLastFiveTransactionHistory(
            String strEncryptWallet,
            String strEncryptPin,
            String strEncryptFromDate,
            String strEncryptToDate,
            String sttEncryptAccessCode,
            String sttEncryptNumberOfTransactions) {
        METHOD_NAME = "QPAY_GetAllResponse_lastFiveOrOther ";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetAllResponse_lastFiveOrOther ";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//        AgentNo:
//        AgentPIN:
//        FromDate:
//        Todate:
//        AccesCode:
//        strMasterKey:

        PropertyInfo encryptAgentAccountNumber = new PropertyInfo();
        encryptAgentAccountNumber.setName("E_AgentNo");
        encryptAgentAccountNumber.setValue(strEncryptWallet);
        encryptAgentAccountNumber.setType(String.class);
        request.addProperty(encryptAgentAccountNumber);

        PropertyInfo encryptAgentPin = new PropertyInfo();
        encryptAgentPin.setName("E_AgentPIN");
        encryptAgentPin.setValue(strEncryptPin);
        encryptAgentPin.setType(String.class);
        request.addProperty(encryptAgentPin);

        PropertyInfo encryptFromDate = new PropertyInfo();
        encryptFromDate.setName("E_FromDate");
        encryptFromDate.setValue(strEncryptFromDate);
        encryptFromDate.setType(String.class);
        request.addProperty(encryptFromDate);

        PropertyInfo encryptToDate = new PropertyInfo();
        encryptToDate.setName("E_Todate");
        encryptToDate.setValue(strEncryptToDate);
        encryptToDate.setType(String.class);
        request.addProperty(encryptToDate);

        PropertyInfo encryptAccessCode = new PropertyInfo();
        encryptAccessCode.setName("E_AccesCode");
        encryptAccessCode.setValue(sttEncryptAccessCode);
        encryptAccessCode.setType(String.class);
        request.addProperty(encryptAccessCode);

        PropertyInfo encryptNumberOfTransactions = new PropertyInfo();
        encryptNumberOfTransactions.setName("E_Parameter");
        encryptNumberOfTransactions.setValue(sttEncryptNumberOfTransactions);
        encryptNumberOfTransactions.setType(String.class);
        request.addProperty(encryptNumberOfTransactions);

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
        Object objLastFiveTransactions = null;
        String strLastFiveTransactions = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objLastFiveTransactions = envelope.getResponse();
            strLastFiveTransactions = objLastFiveTransactions.toString();
            if (strLastFiveTransactions != null && !strLastFiveTransactions.isEmpty()) {
                if (strLastFiveTransactions.contains("**")) {
                    StringTokenizer tokensTransaction = new StringTokenizer(strLastFiveTransactions, "**");
                    for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                        while (tokensTransaction.hasMoreElements()) {
                            StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), "&&");
                            String strTransactionMsg = tokensAllTransactionFinal.nextToken();
                            if (strTransactionMsg.equalsIgnoreCase("Data not found")) {

                            } else {
                                arrayListLastFiveTransaction.add(strTransactionMsg);
                            }
                        }
                    }
                } else {
                    mStrLastFiveTransaction = strLastFiveTransactions;
                }
            } else {
                mStrLastFiveTransaction = "No Transaction Info Found.";
            }



        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            loadAllTransactionHistory();
        }
    }

    private void loadAllTransactionHistory() {
        arrayListAllTransaction.clear();
//        arrayListReferenceId.clear();
//        arrayListAccessCode.clear();
        try {
            mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
            mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(),  GlobalData.getStrSessionId());
            mStrEncryptFromDate = encryption.Encrypt(mEditTextFromDate.getText().toString(),  GlobalData.getStrSessionId());
            mStrEncryptToDate = encryption.Encrypt(mEditTextToDate.getText().toString(),  GlobalData.getStrSessionId());
            //######################### Need to Modify ##########################
            mStrEncryptAccessCode = encryption.Encrypt("all",  GlobalData.getStrSessionId());
            //######################### Need to Modify ##########################
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Initialize progress dialog
        mProgressDialog = ProgressDialog.show(TransactionHistory.this, null, "Loading All Transaction History...", false, true);
        // Cancel progress dialog on back key press
        mProgressDialog.setCancelable(true);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                getAllTransactionHistory(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptFromDate,
                        mStrEncryptToDate, mStrEncryptAccessCode);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                                //================== Show Transaction History List ======================
                                //================== Show Transaction History List ======================
                                //================== Show Transaction History List ======================
//                                Collections.reverse(arrayListAllTransaction);
                                ArrayAdapter<String> arrayAdapterTransaction = new ArrayAdapter<String>(
                                        TransactionHistory.this, android.R.layout.simple_list_item_1, arrayListAllTransaction);
                                mListViewTransactionHistory.setAdapter(arrayAdapterTransaction);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        t.start();

        //-=====================================

        mListViewTransactionHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View view, int position, long id) {

                strget=arrayListAllTransaction.get(position);

                AlertDialog.Builder myAlert = new AlertDialog.Builder(TransactionHistory.this);
                myAlert.setTitle("Print Receipt");
                myAlert.setMessage(strget);
                myAlert.setPositiveButton(
                        "Print",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(TransactionHistory.this, Printer.class));
                                // --------------------------------
                                int intIndex = strget.indexOf("Balance:");
                                if (intIndex == -1)
                                {
                                    PrintTxt=strget;

                                }
                                else {
                                    String[] split = strget.split("Balance:");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    //String newString1 = firstSubString.replace("Balance:", "");
                                    String[] split2 = secondSubString.split("TXN ID:");
                                    String firstSubString2 = split2[0];
                                    String secondSubString2 = split2[1];
                                    String full = firstSubString + "TXN ID: " + secondSubString2;
                                    PrintTxt=full;
                                }
                                Intent i = new Intent(
                                        TransactionHistory.this,
                                        Printer.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("print",
                                        PrintTxt);
                                // Add the bundle to the
                                // intent
                                i.putExtras(bundle);
                                startActivity(i);
                                // --------------------------------
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


        });

        //======================================

    }



    // method for get balance
    public void getAllTransactionHistory(
            String strEncryptWallet,
            String strEncryptPin,
            String strEncryptFromDate,
            String strEncryptToDate,
            String sttAccessCode) {
        METHOD_NAME = "QPAY_GetAllResponse ";
        SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GetAllResponse ";
        SoapObject request = new SoapObject(GlobalData.getStrNamespace().replaceAll(" ", "%20"), METHOD_NAME);
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

//        AgentNo:
//        AgentPIN:
//        FromDate:
//        Todate:
//        AccesCode:
//        strMasterKey:

        PropertyInfo encryptAgentAccountNumber = new PropertyInfo();
        encryptAgentAccountNumber.setName("E_AgentNo");
        encryptAgentAccountNumber.setValue(strEncryptWallet);
        encryptAgentAccountNumber.setType(String.class);
        request.addProperty(encryptAgentAccountNumber);

        PropertyInfo encryptAgentPin = new PropertyInfo();
        encryptAgentPin.setName("E_AgentPIN");
        encryptAgentPin.setValue(strEncryptPin);
        encryptAgentPin.setType(String.class);
        request.addProperty(encryptAgentPin);

        PropertyInfo fromDate = new PropertyInfo();
        fromDate.setName("E_FromDate");
        fromDate.setValue(strEncryptFromDate);
        fromDate.setType(String.class);
        request.addProperty(fromDate);

        PropertyInfo toDate = new PropertyInfo();
        toDate.setName("E_Todate");
        toDate.setValue(strEncryptToDate);
        toDate.setType(String.class);
        request.addProperty(toDate);

        PropertyInfo accessCode = new PropertyInfo();
        accessCode.setName("E_AccesCode");
        accessCode.setValue(sttAccessCode);
        accessCode.setType(String.class);
        request.addProperty(accessCode);

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
        Object objAllResponse = null;
        String strAllResponse = "";

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl().replaceAll(" ", "%20"), 1000000);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            objAllResponse = envelope.getResponse();
            strAllResponse = objAllResponse.toString();
            if (strAllResponse != null && !strAllResponse.isEmpty()) {
                if (strAllResponse.contains("**")) {
                    StringTokenizer tokensTransaction = new StringTokenizer(strAllResponse, "**");
                    for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                        while (tokensTransaction.hasMoreElements()) {
                            StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), "&&");
                            String strTransactionMsg = tokensAllTransactionFinal.nextToken();
                            if (strTransactionMsg.equalsIgnoreCase("Data not found")) {

                            } else {
                                arrayListAllTransaction.add(strTransactionMsg);
                            }
                        }
                    }
                } else {
                    mStrAllTransaction = strAllResponse;
                }
            } else {
                mStrAllTransaction = "No Transaction Info Found.";
            }
//            // Check Null or Empty Response
//            // Check Null or Empty Response
//            // Check Null or Empty Response
//            if (strAllResponse != null && !strAllResponse.isEmpty()) {
//                if (strAllResponse.contains("**")) {
//                    //get individual record
//                    //get individual record
//                    //get individual record
//                    StringTokenizer tokensTransaction = new StringTokenizer(strAllResponse, "**");
//                    for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
//                        while (tokensTransaction.hasMoreElements()) {
//                            //get transaction msg and reference id with label
//                            StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), "&&");
//                            String strServiceCodeWithLabel = tokensAllTransactionFinal.nextToken(); /// l+name ---service: FM
//                            strMsg = tokensAllTransactionFinal.nextToken(); //msg  3,2
//                            if (strMsg.contains("//")) {
//                                String[] tokensReferenceId = strServiceCodeWithLabel.split(":");
//                                String strServiceLabel = tokensReferenceId[0];
//                                String strAccessCode = tokensReferenceId[1];
//
//                                //----------------------------------------------------------
//                                // FT
//                                if (strAccessCode.equalsIgnoreCase("Fund Transfer")) {
//                                    int intIndex = strMsg.indexOf("successful");
//                                    if (intIndex == -1) {
//                                        int intIndex1 = strMsg.indexOf("not allow");
//                                        if (intIndex1 == -1) {
//                                        } else {
//                                            String[] parts = strMsg.split(",");
//                                            String strResponse = parts[0];
//                                            String strExtra = parts[1];
//                                            strMsg = strResponse;
//                                        }
//                                        int intIndex2 = strMsg.indexOf("wrong");
//                                        if (intIndex2 == -1) {
//                                        } else {
//                                            String[] parts = strMsg.split(",");
//                                            String strResponse = parts[0];
//                                            String strExtra = parts[1];
//                                            strMsg = strResponse;
//                                        }
//                                    } else {
//                                        String[] parts = strMsg.split("//");
//                                        String strResponse = parts[0];//
//                                        String strExtra = parts[1];
//                                        strMsg = strResponse;
//                                    }
//                                }
//                                //FM
//                                if (strAccessCode.equalsIgnoreCase("Fund Management")) {
//                                    int intIndex = strMsg.indexOf("successful");
//                                    if (intIndex == -1) {
//                                        int intIndex1 = strMsg.indexOf("not allow");
//                                        if (intIndex1 == -1) {
//                                        } else {
//                                            String[] parts = strMsg.split(",");
//                                            String strResponse = parts[0];
//                                            String strExtra = parts[1];
//                                            strMsg = strResponse;
//                                        }
//                                        int intIndex2 = strMsg.indexOf("wrong");
//                                        if (intIndex2 == -1) {
//                                        } else {
//                                            String[] parts = strMsg.split(",");
//                                            String strResponse = parts[0];
//                                            String strExtra = parts[1];
//                                            strMsg = strResponse;
//                                        }
//                                    } else {
//                                        String[] parts = strMsg.split("//");
//                                        String strResponse = parts[0];//
//                                        String strExtra = parts[1];
//                                        strMsg = strResponse;
//                                    }
//                                }
//
//
//                                //Merchant Payment
//                                if (strAccessCode.equalsIgnoreCase("Merchant Payment")) {
//                                    //Get Package
//                                    String strPackage = GlobalData.getStrPackage();
//                                    //MP_Initiated_M_M2M
//                                    if (strPackage.equalsIgnoreCase("1312050001")) {
//                                        int intIndex = strMsg.indexOf("successful");
//                                        if (intIndex == -1) {
//                                            int intIndex1 = strMsg.indexOf("not allow");
//                                            if (intIndex1 == -1) {
//                                            } else {
//                                                String[] parts = strMsg.split(",");
//                                                String strResponse = parts[0];
//                                                String strExtra = parts[1];
//                                                strMsg = strResponse;
//                                            }
//                                            int intIndex2 = strMsg.indexOf("wrong");
//                                            if (intIndex2 == -1) {
//                                            } else {
//                                                String[] parts = strMsg.split(",");
//                                                String strResponse = parts[0];
//                                                String strExtra = parts[1];
//                                                strMsg = strResponse;
//                                            }
//                                        } else {
//                                            String[] parts = strMsg.split("//");
//                                            String strResponse = parts[0];//
//                                            String strExtra01 = parts[1];
//                                            strMsg = strResponse;
//                                        }
//                                    }
//                                    //MP_Initiated_C_C2M
//                                    else if (strPackage.equalsIgnoreCase("1205190003")) {
//                                        int intIndex = strMsg.indexOf("successful");
//                                        if (intIndex == -1) {
//                                            int intIndex1 = strMsg.indexOf("not allow");
//                                            if (intIndex1 == -1) {
//                                            } else {
//                                                String[] parts = strMsg.split(",");
//                                                String strResponse = parts[0];
//                                                String strExtra = parts[1];
//                                                strMsg = strResponse;
//                                            }
//                                            int intIndex2 = strMsg.indexOf("wrong");
//                                            if (intIndex2 == -1) {
//                                            } else {
//                                                String[] parts = strMsg.split(",");
//                                                String strResponse = parts[0];
//                                                String strExtra = parts[1];
//                                                strMsg = strResponse;
//                                            }
//                                        } else {
//                                            String[] parts = strMsg.split("//");
//                                            String strResponse = parts[0];//
//                                            String strExtra = parts[1];
//                                            strMsg = strResponse;
//                                        }
//                                    }
//                                }
//                                arrayListTransaction.add(strMsg);
//                            } else {
//                                arrayListTransaction.add(strMsg);
//                            }
//                        }
//                        arrayListTransaction.add(strMsg);
//                    }
//                    arrayListTransaction.add(strMsg);
//                } else {
//                    arrayListTransaction.add(strMsg);
//                }
//            } else {
//                AlertDialog.Builder myAlert = new AlertDialog.Builder(TransactionHistory.this);
//                myAlert.setMessage("No Transaction Info Found.");
//                myAlert.setNegativeButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = myAlert.create();
//                alertDialog.show();
//            }


//            //get individual record
//            StringTokenizer tokensTransaction = new StringTokenizer(strAllResponse, "**");
//
//            for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
//                while (tokensTransaction.hasMoreElements()) {
//                    //get transaction msg and reference id with label
//                    StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), "&&");
//                    String strServiceCodeWithLabel = tokensAllTransactionFinal.nextToken(); /// l+name ---service: FM
//                     strMsg = tokensAllTransactionFinal.nextToken(); //msg  3,2
//                    if(strMsg.contains("//")){
//                        String[] tokensReferenceId = strServiceCodeWithLabel.split(":");
//                        String strServiceLabel = tokensReferenceId[0];
//                        String strAccessCode = tokensReferenceId[1];
//
//                        //----------------------------------------------------------
//                        // FT
//                        if (strAccessCode.equalsIgnoreCase("Fund Transfer")) {
//                            int intIndex = strMsg.indexOf("successful");
//                            if (intIndex == -1) {
//                                int intIndex1 = strMsg.indexOf("not allow");
//                                if (intIndex1 == -1) {
//
//                                } else {
//                                    String[] parts = strMsg.split(",");
//                                    String strResponse = parts[0];
//                                    String strExtra = parts[1];
//                                    strMsg = strResponse;
//                                }
//                                int intIndex2 = strMsg.indexOf("wrong");
//                                if (intIndex2 == -1) {
//
//                                } else {
//                                    String[] parts = strMsg.split(",");
//                                    String strResponse = parts[0];
//                                    String strExtra = parts[1];
//                                    strMsg = strResponse;
//                                }
//                            } else {
//                                String[] parts = strMsg.split("//");
//                                String strResponse = parts[0];//
//                                String strExtra = parts[1];
//                                strMsg = strResponse;
//                            }
//                        }
//                        //FM
//                        if (strAccessCode.equalsIgnoreCase("Fund Management")) {
//                            int intIndex = strMsg.indexOf("successful");
//                            if (intIndex == -1) {
//                                int intIndex1 = strMsg.indexOf("not allow");
//                                if (intIndex1 == -1) {
//
//                                } else {
//                                    String[] parts = strMsg.split(",");
//                                    String strResponse = parts[0];
//                                    String strExtra = parts[1];
//                                    strMsg = strResponse;
//                                }
//                                int intIndex2 = strMsg.indexOf("wrong");
//                                if (intIndex2 == -1) {
//
//                                } else {
//                                    String[] parts = strMsg.split(",");
//                                    String strResponse = parts[0];
//                                    String strExtra = parts[1];
//                                    strMsg = strResponse;
//                                }
//                            } else {
//                                String[] parts = strMsg.split("//");
//                                String strResponse = parts[0];//
//                                String strExtra = parts[1];
//                                strMsg = strResponse;
//                            }
//                        }
//
//                        String strPackage = GlobalData.getStrPackage();
//                        if (strPackage.equalsIgnoreCase("1312050001")) {
//                            //MP_Initiated_M_M2M
//                            if (strAccessCode.equalsIgnoreCase("Merchant Payment")) {
//                                int intIndex = strMsg.indexOf("successful");
//                                if (intIndex == -1) {
//                                    int intIndex1 = strMsg.indexOf("not allow");
//                                    if (intIndex1 == -1) {
//
//                                    } else {
//                                        String[] parts = strMsg.split(",");
//                                        String strResponse = parts[0];
//                                        String strExtra = parts[1];
//                                        strMsg = strResponse;
//                                    }
//                                    int intIndex2 = strMsg.indexOf("wrong");
//                                    if (intIndex2 == -1) {
//
//                                    } else {
//                                        String[] parts = strMsg.split(",");
//                                        String strResponse = parts[0];
//                                        String strExtra = parts[1];
//                                        strMsg = strResponse;
//                                    }
//                                } else {
//                                    String[] parts = strMsg.split("//");
//                                    String strResponse = parts[0];//
//                                    String strExtra01 = parts[1];
////                        String strExtra02 = parts[2];
//                                    strMsg = strResponse;
//                                }
//                            }
//                        }
//                        if (strPackage.equalsIgnoreCase("1205190003")) {
//                            //MP_Initiated_C_C2M
//                            if (strAccessCode.equalsIgnoreCase("Merchant Payment")) {
//                                int intIndex = strMsg.indexOf("successful");
//                                if (intIndex == -1) {
//                                    int intIndex1 = strMsg.indexOf("not allow");
//                                    if (intIndex1 == -1) {
//
//                                    } else {
//                                        String[] parts = strMsg.split(",");
//                                        String strResponse = parts[0];
//                                        String strExtra = parts[1];
//                                        strMsg = strResponse;
//                                    }
//                                    int intIndex2 = strMsg.indexOf("wrong");
//                                    if (intIndex2 == -1) {
//
//                                    } else {
//                                        String[] parts = strMsg.split(",");
//                                        String strResponse = parts[0];
//                                        String strExtra = parts[1];
//                                        strMsg = strResponse;
//                                    }
//                                } else {
//                                    String[] parts = strMsg.split("//");
//                                    String strResponse = parts[0];//
//                                    String strExtra = parts[1];
//                                    strMsg = strResponse;
//                                }
//                            }
//                        }
//                        //--------------------------------------------------------
//                        //  String strAccessCode = tokensReferenceId[2];
////                    arrayListTransaction.add(strTransactionMsg);
////                    arrayListReferenceId.add(strReferenceId);
////                    arrayListAccessCode.add(strAccessCode);
//
//                    }else{
//                       // String str01=strReferenceIdMsg;
//                        arrayListTransaction.add(strMsg);
//
//                    }
//
//
//                }
//                arrayListTransaction.add(strMsg);
//            }

//            //get individual record
//            tokensTransaction = new StringTokenizer(strAllResponse, "&");
//
//            for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
//                while (tokensTransaction.hasMoreElements()) {
//                    //get transaction msg and reference id with label
//                    tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
//                    String strTransactionMsg = tokensAllTransactionFinal.nextToken();
//                    String strReferenceIdMsg = tokensAllTransactionFinal.nextToken();
//
//                    //get reference id value
//                    String[] tokensReferenceId = strReferenceIdMsg.split(":");
//                    String strReferenceLabel = tokensReferenceId[0];
//                    String strReferenceId = tokensReferenceId[1];
//                    String strAccessCode = tokensReferenceId[2];
//                    arrayListTransaction.add(strTransactionMsg);
//                    arrayListReferenceId.add(strReferenceId);
//                    arrayListAccessCode.add(strAccessCode);
//                }
//            }

            //================== Show individual Transaction in Alert Dialog =================
            //================== Show individual Transaction in Alert Dialog =================
            //================== Show individual Transaction in Alert Dialog =================
//            mListViewTransactionHistory.setOnItemClickListener(new OnItemClickListener() {
//                public void onItemClick(AdapterView<?> myAdapter, View view, int position, long id) {
//                    mStrReferenceId = (arrayListReferenceId.get(position));
//                    mStrAccessCode = (arrayListAccessCode.get(position));
//                    String strTransactionMsg = (arrayListTransaction.get(position));
//
//                    String[] strArrayTransactionMsg = strTransactionMsg.split("\n");
//                    String strServerCode = strArrayTransactionMsg[0];
//                    String strAgent = strArrayTransactionMsg[1];
//                    String strCustomer = strArrayTransactionMsg[2];
//                    String strTime = strArrayTransactionMsg[3];
//                    String strAmount = strArrayTransactionMsg[4];
//                    String strTransactionId = strArrayTransactionMsg[5];
//
//                    String[] strArrayServiceCode = strServerCode.split(":");
//                    String strServerCodeLabel = strArrayServiceCode[0];
//                    mStrServiceCode = strArrayServiceCode[1];
//
//                    String[] strArrayTransactionTime = strTime.split(":");
//                    String strTimeLabel = strArrayTransactionTime[0];
//                    mStrTransactionTime = strArrayTransactionTime[1];
//
//                    String[] tokensTransactionId = strTransactionId.split(":");
//                    String strTransactionIdLabel = tokensTransactionId[0];
//                    mStrTransactionId = tokensTransactionId[1];
//
//                    //############## Get Individual Response ##################
//                    //############## Get Individual Response ##################
//                    //############## Get Individual Response ##################
//                    getResponse();
//                }
//            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//    public void getResponse() {
//        try {
//            String strEncryptAccNo = encryption.Encrypt(GlobalData.getStrAccountNumber(), mStrMasterKey);
//            String strEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), mStrMasterKey);
//            String strEncryptReferenceId = encryption.Encrypt(mStrReferenceId, mStrMasterKey);
//            String strIndividualServerResponse = GetResponse.getResponse(strEncryptAccNo,
//                    strEncryptPin, strEncryptReferenceId, mStrMasterKey);
//
//            // FT
//            if (mStrAccessCode.equalsIgnoreCase("Fund Transfer")) {
//                int intIndex = strIndividualServerResponse.indexOf("successful");
//                if (intIndex == -1) {
//                    int intIndex1 = strIndividualServerResponse.indexOf("not allow");
//                    if (intIndex1 == -1) {
//
//                    } else {
//                        String[] parts = strIndividualServerResponse.split(",");
//                        String strResponse = parts[0];
//                        String strExtra = parts[1];
//                        strIndividualServerResponse = strResponse;
//                    }
//                    int intIndex2 = strIndividualServerResponse.indexOf("wrong");
//                    if (intIndex2 == -1) {
//
//                    } else {
//                        String[] parts = strIndividualServerResponse.split(",");
//                        String strResponse = parts[0];
//                        String strExtra = parts[1];
//                        strIndividualServerResponse = strResponse;
//                    }
//                } else {
//                    String[] parts = strIndividualServerResponse.split("//");
//                    String strResponse = parts[0];//
//                    String strExtra = parts[1];
//                    strIndividualServerResponse = strResponse;
//                }
//            }
//            //FM
//            if (mStrAccessCode.equalsIgnoreCase("Fund Management")) {
//                int intIndex = strIndividualServerResponse.indexOf("successful");
//                if (intIndex == -1) {
//                    int intIndex1 = strIndividualServerResponse.indexOf("not allow");
//                    if (intIndex1 == -1) {
//
//                    } else {
//                        String[] parts = strIndividualServerResponse.split(",");
//                        String strResponse = parts[0];
//                        String strExtra = parts[1];
//                        strIndividualServerResponse = strResponse;
//                    }
//                    int intIndex2 = strIndividualServerResponse.indexOf("wrong");
//                    if (intIndex2 == -1) {
//
//                    } else {
//                        String[] parts = strIndividualServerResponse.split(",");
//                        String strResponse = parts[0];
//                        String strExtra = parts[1];
//                        strIndividualServerResponse = strResponse;
//                    }
//                } else {
//                    String[] parts = strIndividualServerResponse.split("//");
//                    String strResponse = parts[0];//
//                    String strExtra = parts[1];
//                    strIndividualServerResponse = strResponse;
//                }
//            }
//
//            String strPackage = GlobalData.getStrPackage();
//            if (strPackage.equalsIgnoreCase("1312050001")) {
//                //MP_Initiated_M_M2M
//                if (mStrAccessCode.equalsIgnoreCase("Merchant Payment")) {
//                    int intIndex = strIndividualServerResponse.indexOf("successful");
//                    if (intIndex == -1) {
//                        int intIndex1 = strIndividualServerResponse.indexOf("not allow");
//                        if (intIndex1 == -1) {
//
//                        } else {
//                            String[] parts = strIndividualServerResponse.split(",");
//                            String strResponse = parts[0];
//                            String strExtra = parts[1];
//                            strIndividualServerResponse = strResponse;
//                        }
//                        int intIndex2 = strIndividualServerResponse.indexOf("wrong");
//                        if (intIndex2 == -1) {
//
//                        } else {
//                            String[] parts = strIndividualServerResponse.split(",");
//                            String strResponse = parts[0];
//                            String strExtra = parts[1];
//                            strIndividualServerResponse = strResponse;
//                        }
//                    } else {
//                        String[] parts = strIndividualServerResponse.split("//");
//                        String strResponse = parts[0];//
//                        String strExtra01 = parts[1];
////                        String strExtra02 = parts[2];
//                        strIndividualServerResponse = strResponse;
//                    }
//                }
//            }
//            if (strPackage.equalsIgnoreCase("1205190003")) {
//                //MP_Initiated_C_C2M
//                if (mStrAccessCode.equalsIgnoreCase("Merchant Payment")) {
//                    int intIndex = strIndividualServerResponse.indexOf("successful");
//                    if (intIndex == -1) {
//                        int intIndex1 = strIndividualServerResponse.indexOf("not allow");
//                        if (intIndex1 == -1) {
//
//                        } else {
//                            String[] parts = strIndividualServerResponse.split(",");
//                            String strResponse = parts[0];
//                            String strExtra = parts[1];
//                            strIndividualServerResponse = strResponse;
//                        }
//                        int intIndex2 = strIndividualServerResponse.indexOf("wrong");
//                        if (intIndex2 == -1) {
//
//                        } else {
//                            String[] parts = strIndividualServerResponse.split(",");
//                            String strResponse = parts[0];
//                            String strExtra = parts[1];
//                            strIndividualServerResponse = strResponse;
//                        }
//                    } else {
//                        String[] parts = strIndividualServerResponse.split("//");
//                        String strResponse = parts[0];//
//                        String strExtra = parts[1];
//                        strIndividualServerResponse = strResponse;
//                    }
//                }
//            }
//
//
//            AlertDialog.Builder myAlert = new AlertDialog.Builder(TransactionHistory.this);
//            myAlert.setMessage(strIndividualServerResponse);
//            myAlert.setNegativeButton(
//                    "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alertDialog = myAlert.create();
//            alertDialog.show();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }

    public void datePickerDialogFromDate() {

        OnDateSetListener listener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                mEditTextFromDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();

    }

    public void datePickerDialogToDate() {

        OnDateSetListener listener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                mEditTextToDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();

    }

    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            loadLastFiveTransactionHistory();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(TransactionHistory.this);
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
        mEditTextFromDate.setEnabled(true);
        mEditTextToDate.setEnabled(true);
        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
        mEditTextFromDate.setEnabled(false);
        mEditTextToDate.setEnabled(false);
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
//        pinMenuItem.setTitle("Reward Point:678");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(TransactionHistory.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(TransactionHistory.this, Login.class)
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
            startActivity(new Intent(TransactionHistory.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}