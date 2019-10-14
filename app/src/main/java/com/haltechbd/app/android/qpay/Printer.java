package com.haltechbd.app.android.qpay;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;



import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.R;
import com.haltechbd.app.android.qpay.utils.DateAndTime;
import com.haltechbd.app.android.qpay.utils.PrinterSettings;

public class Printer extends Activity {
    private Spinner mDeviceListSpinner;
    private Button mBtnConnectPrinter, mBtnEnablePrinter, mBtnPrintCustomerCopy, mBtnPrintAgentCopy;
    private ProgressDialog mProgressDialog, mConnectingDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private com.haltechbd.app.android.qpay.utils.PrinterSettings mConnector;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
String strPrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer);
        mDeviceListSpinner = (Spinner) findViewById(R.id.spinnerDeviceList);
        mBtnConnectPrinter = (Button) findViewById(R.id.btnConnectPrinter);
        mBtnEnablePrinter = (Button) findViewById(R.id.btnEnablePrinter);
        mBtnPrintCustomerCopy = (Button) findViewById(R.id.btnPrintCustomerCopy);
       mBtnPrintAgentCopy = (Button) findViewById(R.id.btnPrintAgentCopy);
//        mBtnPrintBankCopy = (Button) findViewById(R.id.btnPrintBankCopy);

        // Get the bundle
        Bundle bundle = getIntent().getExtras();
        // Extract the data�
        if (bundle != null) {
            strPrint = bundle.getString("print");
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                showDisabled();
            } else {
                showEnabled();

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices != null) {
                    mDeviceList.addAll(pairedDevices);
                    updateDeviceList();
                }
            }

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Scanning...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mBluetoothAdapter.cancelDiscovery();
                }
            });

            mConnectingDialog = new ProgressDialog(this);
            mConnectingDialog.setMessage("Connecting...");
            mConnectingDialog.setCancelable(false);

            mConnector = new com.haltechbd.app.android.qpay.utils.PrinterSettings(new com.haltechbd.app.android.qpay.utils.PrinterSettings.P25ConnectionListener() {

                @Override
                public void onStartConnecting() {
                    mConnectingDialog.show();
                }

                @Override
                public void onConnectionSuccess() {
                    mConnectingDialog.dismiss();

                    showConnected();
                }

                @Override
                public void onConnectionFailed(String error) {
                    mConnectingDialog.dismiss();
                }

                @Override
                public void onConnectionCancelled() {
                    mConnectingDialog.dismiss();
                }

                @Override
                public void onDisconnected() {
                    showDisonnected();
                }
            });

            // enable bluetooth
            mBtnEnablePrinter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1000);
                }
            });

            // connect/disconnect
            mBtnConnectPrinter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        connect();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            // print text
            mBtnPrintCustomerCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                   String text =("          "+"Quick Pay \n"
                            + "*******************************" + "\n"
                            + "Printed Date: " + DateAndTime.getCurrentDate() + "\n"
                            + "Printed Time: " + DateAndTime.getCurrentTime() + "\n"

                            + "Branch: " + "Dhaka" + "\n"
                            + "*******************************" + "\n"
                            + "" + strPrint+ "\n"
                            + "*******************************" + "\n"
                            + "This is computer generated copy.Customer Sign is required." + "\n"
//                            + "*******************************" + "\n"
                            + "\n"
                            + "Signature: "+"_____________________"
                            + "\n"
                            + "         Customer Copy" + "\n"
                            + "Thank you for using " + "Quick Pay"+ "\n"
                            + "Powered by Hal QuickPay" + "\n"
                            + "*******************************" + "\n" + "\n" + "\n" + "\n");
                    try {
                        printText(text);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

//            // print text
            mBtnPrintAgentCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    String text =("          "+"Quick Pay \n"
                            + "*******************************" + "\n"
                            + "Printed Date: " + DateAndTime.getCurrentDate() + "\n"
                            + "Printed Time: " + DateAndTime.getCurrentTime() + "\n"

                            + "Branch: " + "Dhaka" + "\n"
                            + "*******************************" + "\n"
                            + "" + strPrint+ "\n"
                            + "*******************************" + "\n"
                            + "Thank you for using " + "Quick Pay"+ "\n"
                            + "*******************************" + "\n"
                            + "Powered by Hal QuickPay" + "\n"
                            + "*******************************" + "\n"
                            + "This is computer generated copy.Customer Sign is required." + "\n"
                            + "*******************************" + "\n"
                            + "\n"
                            + "Signature: "+"_____________________"
                            + "\n"
                            + "         Agent Copy" + "\n"
                            + "*******************************" + "\n" + "\n" + "\n" + "\n");
                    try {
                        printText(text);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
//
//            // print text
//            mBtnPrintBankCopy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    String text = ("             " + GlobalData.getStrBankName() + "\n"
//                            + "*******************************" + "\n"
//                            + "       " + GlobalData.getStrTransactionHeader() + "\n"
//                            + "*******************************" + "\n"
//                            + "Date: " + DateAndTime.getCurrentDate() + "\n"
//                            + "Time: " + DateAndTime.getCurrentTime() + "\n"
//                            + "Terminal ID: " + GlobalData.getStrAgentAccountNumber() + "\n"
//                            + "Transaction: " + GlobalData.getStrTransactionType() + "\n"
//                            + "Branch: " + "Dhaka" + "\n"
//                            + "*******************************" + "\n"
//                            + "" + GlobalData.getStrBankPrintCopy() + "\n"
//                            + "*******************************" + "\n"
//                            + "Thank you for using " + GlobalData.getStrBankName() + "\n"
//                            + "*******************************" + "\n"
//                            + "Powered by Microtech QPay, authorized distributor of MYCash" + "\n"
//                            + "*******************************" + "\n"
//                            + "This is computer generated copy. No seal or sign required." + "\n"
//                            + "*******************************" + "\n"
//                            + "           Bank Copy" + "\n"
//                            + "*******************************" + "\n" + "\n" + "\n" + "\n");
//                    try {
//                        printText(text);
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            });

        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(mReceiver, filter);

        // logout after 60 sec if user is customer
//        logOut();
    }

    private void connect() throws Exception {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }

        BluetoothDevice device = mDeviceList.get(mDeviceListSpinner.getSelectedItemPosition());

        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                createBond(device);
            } catch (Exception e) {
                showToast("Failed to pair device");

                return;
            }
        }

        try {
            if (!mConnector.isConnected()) {
                mConnector.connect(device);
            } else {
                mConnector.disconnect();

                showDisonnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDeviceList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
                getArray(mDeviceList));

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        mDeviceListSpinner.setAdapter(adapter);
        mDeviceListSpinner.setSelection(0);
    }

    private void showDisabled() {
        showToast("Bluetooth disabled");
        mBtnEnablePrinter.setVisibility(View.VISIBLE);
        mBtnConnectPrinter.setVisibility(View.GONE);
        mDeviceListSpinner.setVisibility(View.GONE);
    }

    private void showEnabled() {
        showToast("Bluetooth enabled");
        mBtnEnablePrinter.setVisibility(View.GONE);
        mBtnConnectPrinter.setVisibility(View.VISIBLE);
        mDeviceListSpinner.setVisibility(View.VISIBLE);
    }

    private void showUnsupported() {
        showToast("Bluetooth is unsupported by this device");
        mBtnConnectPrinter.setEnabled(false);
        mBtnPrintCustomerCopy.setEnabled(false);
        mBtnPrintAgentCopy.setEnabled(false);
       // mBtnPrintBankCopy.setEnabled(false);
        mDeviceListSpinner.setEnabled(false);
    }

    private void showConnected() {
        showToast("Connected");
        mBtnConnectPrinter.setText("Disconnect");
        mBtnPrintCustomerCopy.setEnabled(true);
        mBtnPrintAgentCopy.setEnabled(true);
        //mBtnPrintBankCopy.setEnabled(true);
        mDeviceListSpinner.setEnabled(false);
    }

    private void showDisonnected() {
        showToast("Disconnected");
        mBtnConnectPrinter.setText("Connect");
        mBtnPrintCustomerCopy.setEnabled(false);
        mBtnPrintAgentCopy.setEnabled(false);
       // mBtnPrintBankCopy.setEnabled(false);
        mDeviceListSpinner.setEnabled(true);
    }

    private String[] getArray(ArrayList<BluetoothDevice> data) {
        String[] list = new String[0];

        if (data == null)
            return list;

        int size = data.size();
        list = new String[size];

        for (int i = 0; i < size; i++) {
            list[i] = data.get(i).getName();
        }

        return list;
    }

    private void createBond(BluetoothDevice device) throws Exception {

        try {
            Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par = {};

            Method method = cl.getMethod("createBond", par);

            method.invoke(device);

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }

    private void printText(String text) throws Exception {
        byte[] line = printfont(text, com.haltechbd.app.android.qpay.utils.PrinterSettings.FONT_24PX, com.haltechbd.app.android.qpay.utils.PrinterSettings.Align_LEFT, (byte) 0x1A,
                com.haltechbd.app.android.qpay.utils.PrinterSettings.LANGUAGE_ENGLISH);
        sendData(line);
    }

    private void sendData(byte[] bytes) throws Exception {
        try {
            mConnector.sendData(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] printfont(String content, byte fonttype, byte fontalign, byte linespace, byte language) {

        if (content != null && content.length() > 0) {

            content = content + "\n";
            byte[] temp = null;
            temp = PrinterSettings.convertPrintData(content, 0, content.length(), language, fonttype, fontalign,
                    linespace);

            return temp;
        } else {
            return null;
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showEnabled();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    showDisabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDialog.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDialog.dismiss();

                updateDeviceList();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device: " + device.getName());
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED) {
                    showToast("Paired");

                    try {
                        connect();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        if (mConnector != null) {

            try {
                mConnector.disconnect();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    private void logOut() {
//        new CountDownTimer(60000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                // here you can have your logic to set text to edittext
//            }
//
//            public void onFinish() {
//                startActivity(new Intent(Printer.this, Login.class));
//            }
//
//        }.start();
//    }

}
