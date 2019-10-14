package com.haltechbd.app.android.qpay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.QrScan;
import com.haltechbd.app.android.qpay.R;
import com.haltechbd.app.android.qpay.Update_Link_Account;

import java.util.ArrayList;
import java.util.BitSet;

import static com.haltechbd.app.android.qpay.MainActivity.decodeBase64;

/**
 * @author Mr. Nazmuzzaman, Umme Sayma Bushra, Muhammad Sadat Al-Jony
 * @version 1.0
 * @company Bangladesh Microtechnology Limited
 * @since 2015-02-01
 */

public class CustomListForLinkedAccout_V2 extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> array01;
    private final ArrayList<String> array02;
    private final ArrayList<String> array03;
    private final ArrayList<String> array04;
    private final ArrayList<String> array05;
    private final ArrayList<String> array06;
    private final ArrayList<String> array07;
    private final ArrayList<String> array08;
    //private final ArrayList<Bitmap> array08;
    ImageView imgVerified ,imgViewQR,imgView,imgEditlinkAcount;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    public CustomListForLinkedAccout_V2(Activity context,
                                        ArrayList<String> arr01,
                                        ArrayList<String> arr02,
                                        ArrayList<String> arr03,
                                        ArrayList<String> arr04,
                                        ArrayList<String> arr05,
                                        ArrayList<String> arr06,
                                        ArrayList<String> arr07,
                                        ArrayList<String> arr08) {
        super(context, R.layout.list_single_item_linked_account_v2, arr01);
        this.context = context;
        this.array01 = arr01;
        this.array02 = arr02;
        this.array03 = arr03;
        this.array04 = arr04;
        this.array05 = arr05;
        this.array06 = arr06;
        this.array07 = arr07;
        this.array08 = arr08;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_single_item_linked_account_v2, parent, false);

        LinearLayout LtMain= (LinearLayout) rowView.findViewById(R.id.layoutmainLA);
        if( array08.get(position).equalsIgnoreCase("170402004")) // Mycash bank id
        {
            LtMain.setBackgroundResource(R.drawable.bg_card_my_cash_transperent_border_new2);
        }
        else if( array08.get(position).equalsIgnoreCase("170402007")) // NBL bank id
        {
            LtMain.setBackgroundResource(R.drawable.bg_card_nbl_transperent_border_new2);
        }
        imgViewQR=(ImageView)rowView.findViewById(R.id.imgViewQrLA);
        imgEditlinkAcount=(ImageView)rowView.findViewById(R.id.imgEditlinkAcountLA);
        ImageView imgprofile=(ImageView)rowView.findViewById(R.id.imgViewProfilePicLA);
        //imgViewQR=(ImageView)view.findViewById(R.id.imgViewQrLA);
//        ImageView imgprofile=view.findViewById(R.id.imgViewProfilePicLA);
//        imgprofile.setImageBitmap(array08.get(position));
        TextView textView01 = rowView.findViewById(R.id.txt01);
        TextView textView02 = rowView.findViewById(R.id.txt02);
        TextView textView03 = rowView.findViewById(R.id.txt03);
        TextView textView04 = rowView.findViewById(R.id.txt04);
        textView01.setText(GlobalData.getStrAccountHolderName());

        String strValue = array02.get(position);
        String strStatus = array04.get(position);
        String strVerified=array05.get(position);
        String strGetLinkACStatus = "";

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
            if(strVerified.equalsIgnoreCase("Y"))
            {

                strAccVerified="Verified";
            }
            else
            {
                strAccVerified="Not Verified";

            }

            textView02.setText(array01.get(position) + "(Default)");
            textView04.setText( strGetLinkACStatus+" || "+strAccVerified );
            textView03.setText( "Balance : " + array06.get(position) + " .BDT");
        }
        else
        {
            String strAccVerified="";
            if(strVerified.equalsIgnoreCase("Y"))
            {

                strAccVerified="Verified";
            }
            else
            {
                strAccVerified="Not Verified";

            }

            textView02.setText(array01.get(position));
            textView04.setText( strGetLinkACStatus+" || "+strAccVerified );
            textView03.setText( "Balance : " + array06.get(position) + " .BDT");

        }


        //===================================================

        //===================-Pro picture-===========================---------------


                    imgprofile.setImageBitmap(GlobalData.getStrProPicBitmap());

        //===========================================
//        textView02.setText(array02.get(position));
//
//        textView03.setText(array03.get(position));

        imgViewQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                // context.startActivity(QrScan.class);
                if( array04.get(position).equalsIgnoreCase("A")) {
                    if (array05.get(position).equalsIgnoreCase("Y")) {
                        GlobalData.setStrQrCodeContent(array07.get(position));
                        GlobalData.setStrWallet(array03.get(position));
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

                if(array05.get(position).equalsIgnoreCase("Y")) {

                    GlobalData.setStrLinkAccountDefult(array02.get(position));
                    GlobalData.setStrLinkAccountName(array01.get(position));
                    GlobalData.setStrLinkAccountNo(array03.get(position));
                    GlobalData.setStrLinkAccountStatus(array04.get(position));
                    GlobalData.setStrLinkAccountVerifiedStatus(array05.get(position));
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

        return rowView;
    }

}
