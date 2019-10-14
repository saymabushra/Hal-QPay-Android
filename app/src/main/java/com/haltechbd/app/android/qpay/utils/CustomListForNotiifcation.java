package com.haltechbd.app.android.qpay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.KycPreviewIdentificationInfo;
import com.haltechbd.app.android.qpay.KycUpdateIdentificationInfo;
import com.haltechbd.app.android.qpay.Notification_Show;
import com.haltechbd.app.android.qpay.QrScan;
import com.haltechbd.app.android.qpay.R;
import com.haltechbd.app.android.qpay.Update_Link_Account;

import java.util.ArrayList;

/**
 * @author Mr. Nazmuzzaman, Umme Sayma Bushra, Muhammad Sadat Al-Jony
 * @version 1.0
 * @company Bangladesh Microtechnology Limited
 * @since 2015-02-01
 */

public class CustomListForNotiifcation extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> array01;
    private final ArrayList<String> array02;
    private final ArrayList<String> array03;
    private final ArrayList<String> array04;
    private final ArrayList<String> array05;
//    private final ArrayList<String> array06;
//    private final ArrayList<String> array07;
//    private final ArrayList<String> array08;
    //private final ArrayList<Bitmap> array08;
    ImageView imgVerified ,imgViewQR,imgView,imgEditlinkAcount;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    CardView crview;
    public CustomListForNotiifcation(Activity context,
                                     ArrayList<String> arr01,
                                     ArrayList<String> arr02,
                                     ArrayList<String> arr03,ArrayList<String> arr04,ArrayList<String> arr05) {
        super(context, R.layout.list_single_row_notification, arr01);
        this.context = context;
        this.array01 = arr01;
        this.array02 = arr02;
        this.array03 = arr03;
        this.array04 = arr04;
        this.array05 = arr05;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

         final View rowView = inflater.inflate(R.layout.list_single_row_notification, parent, false);

        TextView textView01 = rowView.findViewById(R.id.textViewHeader);
        TextView textView02 = rowView.findViewById(R.id.textViewBody);
        TextView textView03 = rowView.findViewById(R.id.textViewDateAndTime);
         crview=rowView.findViewById(R.id.card_root);
        ImageButton imgbtn=rowView.findViewById(R.id.imgViewCloseNotification);

        if(array05.get(position).equalsIgnoreCase("P")) {
            textView01.setText(array02.get(position));
            textView02.setText(array03.get(position));
            textView03.setText(array01.get(position));
            crview.setCardBackgroundColor(Color.parseColor("#e5ebf5"));
        }
        else
        {
            textView01.setText(array02.get(position));
            textView02.setText(array03.get(position));
            textView03.setText(array01.get(position));
            crview.setCardBackgroundColor(Color.WHITE);
        }
//
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                   final String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    final String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    final String strEncrpytedNotifyID=encryption.Encrypt(array04.get(position),GlobalData.getStrSessionId());
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(view.getRootView().getContext());
                    myAlert.setMessage(array02.get(position) + "\n" + array03.get(position) + "\n" + array01.get(position));
                    myAlert.setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    String strUpdate = GetAllFavoriteList.UpdateNotification(mStrEncryptAccountNumber, mStrEncryptPin,strEncrpytedNotifyID , GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
                                    if(strUpdate.equalsIgnoreCase("Update"))
                                    {
                                        crview=rowView.findViewById(R.id.card_root);
                                        crview.setCardBackgroundColor(Color.WHITE);
                                        //CustomListForNotiifcation.notifyDataSetChanged();
                                        //notifyDataSetChanged();

                                    }

                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();

                }
                catch (Exception ex)
                {

                }
                                //Toast.makeText(parent.getContext(), "view clicked: " + arrayIdentifocationName.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    final String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    final String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    final String strEncrpytedNotifyID = encryption.Encrypt(array04.get(position), GlobalData.getStrSessionId());
                    String strUpdate = GetAllFavoriteList.DeleteNotification(mStrEncryptAccountNumber, mStrEncryptPin, strEncrpytedNotifyID, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
                   // this.notifyDataSetChanged();
                    array01.remove(position);
                    array02.remove(position);
                    array03.remove(position);
                    array04.remove(position);
                    array05.remove(position);
                    //CustomListForNotiifcation.notifyDataSetChanged();
                    //CustomListAdapter.this._list.remove(pos);

                    ///CustomListAdapter.this.notifyDataSetChanged();
                    notifyDataSetChanged();
                }
                catch (Exception ex)
                {

                }
            }
        });


        return rowView;
    }


}
