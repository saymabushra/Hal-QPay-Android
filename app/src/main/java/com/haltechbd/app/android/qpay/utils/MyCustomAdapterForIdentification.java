package com.haltechbd.app.android.qpay.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.KycPreviewIdentificationInfo;
import com.haltechbd.app.android.qpay.KycUpdateIdentificationInfo;
import com.haltechbd.app.android.qpay.MainActivity;
import com.haltechbd.app.android.qpay.QrScan;
import com.haltechbd.app.android.qpay.R;
import com.haltechbd.app.android.qpay.Update_Link_Account;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;
import static com.haltechbd.app.android.qpay.MainActivity.encodeTobase64;

/**
 * Created by bushra on 5/16/2018.
 */

public class MyCustomAdapterForIdentification extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    ImageView imgVerified ,imgViewQR,imgView,imgEditlinkAcount,imageViewCompleteIncomplete,imageViewCompleteComplete;
String mStrEncryptAccountNumber,strGetAccountList,mStrEncryptPIN,strgetIdentificationInfo,mStrEncryptIdentificationId
        ,mStrEncryptIdentificationNumber,mStrEncryptRemark,mstrparameten,mstrPictureID,strIdentNo,Remarks,PicID,mstrPictureIDback;
    String strVerified;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    ArrayList<String> arrayIdentifocationName = new ArrayList<String>();
    ArrayList<String> arrayIdentificationIDNo = new ArrayList<String>();

    ArrayList<String> arrayIdentificationIDNoget = new ArrayList<String>();
    ArrayList<String> arrayIdentificationNumber = new ArrayList<String>();
    ArrayList<String> arrayIdentificationPictureID = new ArrayList<String>();
    ArrayList<String> arrayIdentificationRemarks= new ArrayList<String>();
    ArrayList<String> arrayIdentificationPicIDBack= new ArrayList<String>();

    public MyCustomAdapterForIdentification(ArrayList<String> list, Context context) {
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
    public View getView(final int position, View view, final ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // view = inflater.inflate(R.layout.list_single_row_identification_document, null);
        View rowView = inflater.inflate(R.layout.list_single_row_identification_document, null, true);

        TextView listItemTextLabel = (TextView)rowView.findViewById(R.id.txtLabelidentification);
        TextView listItemTextIdentificationNumber = (TextView)rowView.findViewById(R.id.txtIdentificationNumber);
        imageViewCompleteIncomplete = (ImageView) rowView.findViewById(R.id.imgCompleteIncompleteident);
        imageViewCompleteComplete = (ImageView) rowView.findViewById(R.id.imgCompleteCompleteident);
        //========================Get IdentificationList list========================
        try
        {
            mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
            mStrEncryptPIN=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
            strGetAccountList = InsertKyc.getIdentificationInfo(mStrEncryptAccountNumber,mStrEncryptPIN);


        StringTokenizer tokensTransaction = new StringTokenizer(strGetAccountList, "&");
        for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
            while (tokensTransaction.hasMoreElements()) {

                StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), "*");
                String strIdentificationiD= tokensAllTransactionFinal.nextToken();
                String strIdentificationName = tokensAllTransactionFinal.nextToken();

                arrayIdentifocationName.add(strIdentificationName);
                arrayIdentificationIDNo.add(strIdentificationiD);

            }
        }


        }
        catch (Exception ex)
        {

        }



        //Handle TextView and display string from your list

        listItemTextLabel.setText(arrayIdentifocationName.get(position));

            //==================================
            //==================== get identification info============================
            try {
                mStrEncryptIdentificationId = encryption.Encrypt(arrayIdentificationIDNo.get(position), GlobalData.getStrSessionId());
                mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
                mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
                mstrparameten = encryption.Encrypt("G", GlobalData.getStrSessionId());
                mstrPictureID = encryption.Encrypt("", GlobalData.getStrSessionId());
                mstrPictureIDback = encryption.Encrypt("", GlobalData.getStrSessionId());
                strgetIdentificationInfo = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
                        mStrEncryptRemark, mstrparameten, mstrPictureID,mstrPictureIDback);

                if (strgetIdentificationInfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {

                    String[] parts = strgetIdentificationInfo.split("\\*");
                    String strIdentficationNo = parts[0];
                    String strRemarks = parts[1];
                    String strIPictureId = parts[2];
                    String strIdentificationiDget = parts[3];
                    String strPictureIDBack = parts[4];

                    //===========================================
                    arrayIdentificationIDNoget.add(strIdentificationiDget);
                    arrayIdentificationRemarks.add(strRemarks);
                    arrayIdentificationNumber.add(strIdentficationNo);
                    arrayIdentificationPictureID.add(strIPictureId);
                    arrayIdentificationPicIDBack.add(strPictureIDBack);
                    //============================================
                    if(arrayIdentifocationName.get(position).equalsIgnoreCase(("PROFILE PICTURE")))
                    {
                        listItemTextIdentificationNumber.setText(GlobalData.getStrAccountHolderName());
                    }
                    else {
                        listItemTextIdentificationNumber.setText(strIdentficationNo);
                    }
                    //======================Complete Incomplete button========================
                    if (!strIdentificationiDget.equalsIgnoreCase("No Data Found") && !strIdentficationNo.equalsIgnoreCase( "No Data Found") && !strIPictureId.equalsIgnoreCase("No Data Found")) {
                                //imageViewCompleteIncomplete = (ImageView) rowView.findViewById(R.id.imgCompleteIncomplete);
                                if(arrayIdentifocationName.get(position).equalsIgnoreCase(("NATIONAL ID")))
                                {
                                    if (!strPictureIDBack.equalsIgnoreCase("No Data Found"))
                                    {
                                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        imageViewCompleteComplete.setVisibility(View.GONE);
                                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                    {
                                    imageViewCompleteComplete.setVisibility(View.VISIBLE);
                                    imageViewCompleteIncomplete.setVisibility(View.GONE);
                                }
                    } else {
                                imageViewCompleteComplete.setVisibility(View.GONE);
                                imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                            }


                }
            }
            catch (Exception ex)
            {}




        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    GlobalData.setStrIdentificationname(arrayIdentifocationName.get(position));
                    GlobalData.setStrIdentificationID(arrayIdentificationIDNo.get(position));

                        //if (arrayIdentificationIDNoget.get(position).equalsIgnoreCase( arrayIdentificationIDNo.get(position))) {
                            GlobalData.setStrIdentificationNumber(arrayIdentificationNumber.get(position));
                            GlobalData.setStrIdentificationPictureID(arrayIdentificationPictureID.get(position));
                            GlobalData.setStrIdentificationRemarks(arrayIdentificationRemarks.get(position));
                            GlobalData.setStrIdentificationPicIDBack(arrayIdentificationPicIDBack.get(position));
                       // }
                if(arrayIdentifocationName.get(position).equalsIgnoreCase(("NATIONAL ID"))) {
                    if (!arrayIdentificationIDNoget.get(position).equalsIgnoreCase("No Data Found") && !arrayIdentificationNumber.get(position).equalsIgnoreCase("No Data Found") && !arrayIdentificationPictureID.get(position).equalsIgnoreCase("No Data Found") && !arrayIdentificationPicIDBack.get(position).equalsIgnoreCase("No Data Found")) {
                        Intent intent = new Intent(context, KycPreviewIdentificationInfo.class);
                        context.startActivity(intent);
                    }
                    else
                        {

                        Intent intent = new Intent(context, KycUpdateIdentificationInfo.class);
                        context.startActivity(intent);
                    }
                }
                else {

                    if (!arrayIdentificationIDNoget.get(position).equalsIgnoreCase("No Data Found") && !arrayIdentificationNumber.get(position).equalsIgnoreCase("No Data Found") && !arrayIdentificationPictureID.get(position).equalsIgnoreCase("No Data Found")) {
                        Intent intent = new Intent(context, KycPreviewIdentificationInfo.class);
                        context.startActivity(intent);
                    } else {

                        Intent intent = new Intent(context, KycUpdateIdentificationInfo.class);
                        context.startActivity(intent);
                    }
                }


                //Toast.makeText(parent.getContext(), "view clicked: " + arrayIdentifocationName.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;



    }


}