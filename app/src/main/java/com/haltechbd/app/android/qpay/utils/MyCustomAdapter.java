package com.haltechbd.app.android.qpay.utils;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.QrScan;
import com.haltechbd.app.android.qpay.R;
import com.haltechbd.app.android.qpay.Update_Link_Account;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by bushra on 5/16/2018.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    ImageView imgVerified ,imgViewQR,imgView,imgEditlinkAcount;
String mStrEncryptAccountNumber,strGetAccountList;
    String strVerified;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountBalance = new ArrayList<String>();
    ArrayList<String> arrayQRCodelinkAccount = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();
    ArrayList<String> arrayBankID = new ArrayList<String>();

    public MyCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
       // return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_link_account_new, null);
        }

        //Handle TextView and display string from your list
//        TextView listItemText = (TextView)view.findViewById(R.id.linkaccountLebel);
//        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        // Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        //imgVerified=(ImageView)view.findViewById(R.id.imgVefiried);
        imgViewQR=(ImageView)view.findViewById(R.id.imgQR);
        imgEditlinkAcount=(ImageView)view.findViewById(R.id.imgEditlinkAcount);
       // Button addBtn = (Button) view.findViewById(R.id.btnQR);

        //========================Get Account list========================
        try
        {
        mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
        strGetAccountList = CheckDeviceActiveStatus.GetAccountList(mStrEncryptAccountNumber, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
         }
            catch (Exception ex)
            {

            }

        StringTokenizer tokensTransaction = new StringTokenizer(strGetAccountList, "*");
        for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
            while (tokensTransaction.hasMoreElements()) {


                StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());
                String strName = tokensAllTransactionFinal.nextToken();
                String strIsdefult = tokensAllTransactionFinal.nextToken();
                String strAccountNo = tokensAllTransactionFinal.nextToken();
                String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                String strIsVerified=tokensAllTransactionFinal.nextToken();
                String strBankID=tokensAllTransactionFinal.nextToken();
                String strBalance=tokensAllTransactionFinal.nextToken();
                String strQRcode=tokensAllTransactionFinal.nextToken();

                arrayAccountName.add(strName);
                arrayAccountDefuly.add(strIsdefult);
                arrayBankAccountNo.add(strAccountNo);
                arrayBankAccountStatus.add(strBankAccounStatus);
                arrayBankAccountIsVerified.add(strIsVerified);
                arrayBankID.add(strBankID);
                //------------ get balance and QR code -------------------
//                String strEncWallet = null;
//                String strEncryptedPIN="";
//                try {
//                    strEncWallet = encryption.Encrypt(strAccountNo, GlobalData.getStrSessionId());
//                    strEncryptedPIN=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                String strVoucherBalance = GetBalance.getBalance(GlobalData.getStrUserId(), strEncryptedPIN, strEncWallet, GlobalData.getStrDeviceId());
//                String mStrQrCodeContent = GetQrCodeContent.getQrCode(strEncWallet, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
//
                if (strBalance.equalsIgnoreCase("")) {
                    strBalance = "0";
                }
                if (strQRcode.equalsIgnoreCase("")) {
                    strQRcode = "data not found";
                }
                arrayBankAccountBalance.add(strBalance);
                arrayQRCodelinkAccount.add(strQRcode);
//
            }
        }

        String strValue = arrayAccountDefuly.get(position);
        String strStatus = arrayBankAccountStatus.get(position);
         strVerified=arrayBankAccountIsVerified.get(position);
        String strGetLinkACStatus = "";

        TextView textViewSourceWallet = view.findViewById(R.id.linkaccountLebel);
        TextView textViewInfo = view.findViewById(R.id.textViewAccountInfoLinkAccount);
        TextView textViewBalance = view.findViewById(R.id.textViewBalanceLinkAccount);

        if (strStatus.equalsIgnoreCase("A"))
        {
            strGetLinkACStatus = "Active";

        }
        else
        {
            strGetLinkACStatus = "InActive";

        }

        if (strValue.equalsIgnoreCase("Y"))
        {
            String strAccVerified="";
            if(arrayBankAccountIsVerified.get(position).equalsIgnoreCase("Y"))
            {

                strAccVerified="Verified";
            }
            else
            {
                strAccVerified="Not Verified";

            }
            textViewSourceWallet.setText(arrayAccountName.get(position) + "(Default A/C)");
            textViewInfo.setText( strGetLinkACStatus+" || "+strAccVerified );
            textViewBalance.setText( "Balance : " + arrayBankAccountBalance.get(position) + " .BDT");
        }
        else
        {
            String strAccVerified="";
            if(arrayBankAccountIsVerified.get(position).equalsIgnoreCase("Y"))
            {

                strAccVerified="Verified";
            }
            else
            {
                strAccVerified="Not Verified";

            }
            textViewSourceWallet.setText(arrayAccountName.get(position));
            textViewInfo.setText( strGetLinkACStatus+" || "+strAccVerified );

            textViewBalance.setText( "Balance: " + arrayBankAccountBalance.get(position) + " .BDT");

        }


        //===================================================

//        deleteBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                list.remove(position); //or some other task
//                notifyDataSetChanged();
//            }
//        });
        imgViewQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                // context.startActivity(QrScan.class);
                if( arrayBankAccountStatus.get(position).equalsIgnoreCase("A")) {
                    if (arrayBankAccountIsVerified.get(position).equalsIgnoreCase("Y")) {
                        GlobalData.setStrQrCodeContent(arrayQRCodelinkAccount.get(position));
                        GlobalData.setStrWallet(arrayBankAccountNo.get(position));
                        Intent intent = new Intent(context, QrScan.class);
                        context.startActivity(intent);
                    } else {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(v.getRootView().getContext());
//                    myAlert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        myAlert.setMessage("Account is not Verified");
                        myAlert.setPositiveButton(
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
                else
                {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(v.getRootView().getContext());
//                    myAlert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    myAlert.setMessage("Account is not Active");
                    myAlert.setPositiveButton(
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
        });


        imgEditlinkAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(arrayBankAccountIsVerified.get(position).equalsIgnoreCase("Y")) {

                    GlobalData.setStrLinkAccountDefult(arrayAccountDefuly.get(position));
                    GlobalData.setStrLinkAccountName(arrayAccountName.get(position));
                    GlobalData.setStrLinkAccountNo(arrayBankAccountNo.get(position));
                    GlobalData.setStrLinkAccountStatus(arrayBankAccountStatus.get(position));
                    GlobalData.setStrLinkAccountVerifiedStatus(arrayBankAccountIsVerified.get(position));
                    Intent intent = new Intent(context, Update_Link_Account.class);
                    context.startActivity(intent);
                }
                else
                {

                    AlertDialog.Builder myAlert = new AlertDialog.Builder(view.getRootView().getContext());
//                    myAlert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    myAlert.setMessage("Account is not Verified");
                    myAlert.setPositiveButton(
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
        });

    return view;



    }
}