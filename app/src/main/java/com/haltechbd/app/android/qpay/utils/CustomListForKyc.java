package com.haltechbd.app.android.qpay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.R;

@SuppressLint({"ViewHolder", "InflateParams"})
public class CustomListForKyc extends ArrayAdapter<String> {

    private final Activity context;
    private final Integer[] intImgIcon;
    private final String[] strListLabel;
    private final Integer[] intImgCompleteIncomplete;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    ImageView imageViewCompleteIncomplete,imageViewCompleteComplete;
String strStep1,strStep2,strStep3,strStep4,strStep5,strStep6,strStep7,strStep8;

    public CustomListForKyc(Activity context, Integer[] intImgIcon, String[] strListLabel, Integer[] intImgCompleteIncomplete) {
        super(context, R.layout.list_single_row_kyc, strListLabel);
        this.context = context;
        this.intImgIcon = intImgIcon;
        this.strListLabel = strListLabel;
        this.intImgCompleteIncomplete = intImgCompleteIncomplete;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_row_kyc, null, true);
        ImageView imageViewIcon = (ImageView) rowView.findViewById(R.id.imgIcon);
        TextView textTitle = (TextView) rowView.findViewById(R.id.txt);

        TextView txtviewCompleteStatus=(TextView) rowView.findViewById(R.id.textViewKycCompleteStatus);

        imageViewCompleteIncomplete = (ImageView) rowView.findViewById(R.id.imgCompleteIncomplete);
        imageViewCompleteComplete = (ImageView) rowView.findViewById(R.id.imgCompleteComplete);



        imageViewIcon.setImageResource(intImgIcon[position]);
        textTitle.setText(strListLabel[position]);


        try
        {
            //------------------for personal info------------------------------
            if(strListLabel[position].equalsIgnoreCase("Personal Info"))
            {
                String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String mStrEncryptDateOfBirth = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptOccupation = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptOrganizationName = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptGender = encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete = encryption.Encrypt("G", GlobalData.getStrSessionId());

                String strPersonalinfo = InsertKyc.insertPersonalInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptDateOfBirth,
                        mStrEncryptOccupation,
                        mStrEncryptOrganizationName,
                        mStrEncryptGender,
                        strParamete);

                if (strPersonalinfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {


                    String[] parts = strPersonalinfo.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strDOB = parts[0];//Login Successfully
                    String Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                    String ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                    String Gender = parts[3];
                    if (strDOB != "" && Occupation != "" && ORG_name != "" && Gender != "") {
                        //imageViewCompleteIncomplete = (ImageView) rowView.findViewById(R.id.imgCompleteIncomplete);
                        strStep1="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }

                }

                //--------------------------end personal info------------------------------
            }
            else if(strListLabel[position].equalsIgnoreCase("Parents/Spouse Info")) //------------------for parent and Spouse Info info------------------------------
            {
                String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String mStrEncryptFaterName = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptMotherNAme = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSposeTitle= encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSpouseNAme= encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete = encryption.Encrypt("G", GlobalData.getStrSessionId());

                String strPersonalinfo = InsertKyc.insertParentsAndSpouseInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptFaterName,
                        mStrEncryptMotherNAme,
                        mStrEncryptSposeTitle,
                        mStrEncryptSpouseNAme,
                        strParamete);

                if (strPersonalinfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {


                    String[] parts = strPersonalinfo.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strDOB = parts[0];//Login Successfully
                    String Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                    String ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                    String Gender = parts[3];
                    if (strDOB != "" && Occupation != "" && ORG_name != "" && Gender != "") {
                        //imageViewCompleteIncomplete = (ImageView) rowView.findViewById(R.id.imgCompleteIncomplete);
                        strStep2="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }

                }

                //------------------End parent and Spouse Info info------------------------------
            }
            else if(strListLabel[position].equalsIgnoreCase("Address Info")) // Start Address info
            {
                String  mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(),  GlobalData.getStrSessionId());
                String  mStrPresentAddress = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrPermanentAddress = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrOfficeAddress = encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete=encryption.Encrypt("G", GlobalData.getStrSessionId());
                String mStrEncryptThanaId=encryption.Encrypt("", GlobalData.getStrSessionId());


                String strGet= InsertKyc.insertAddressInfo(
                        mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptThanaId,
                        mStrPresentAddress,
                        mStrPermanentAddress,
                        mStrOfficeAddress,
                        strParamete);


                //-----------------------------------------------

                if (strGet.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {
                    // strDecryptDOB + "*" + strDecryptOccupation + "*" + strDecryptOrg_Name + "*" + strDecryptGender

                    String[] parts = strGet.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strPresentAdd = parts[0];//Login Successfully
                    String strParmanetAdd = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                    String strOfficeAddres = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                    String strThanaAName = parts[3];
                    String strThanaAID = parts[4];
                    String strDistrictName = parts[5];
                    String strDistrictID = parts[6];


                    if (strPresentAdd != "" && strParmanetAdd != "" && strOfficeAddres != "" && strThanaAName != "" && strThanaAID != ""  && strDistrictName != ""  && strDistrictID != "" ) {

                        strStep3="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }


                }
                //---------------------------------------
            }
             //-------------------- End of Address info -------------------------------


            else if(strListLabel[position].equalsIgnoreCase("Contact Info")) // Start Contact info
            {
                String  mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(),  GlobalData.getStrSessionId());
//                String  mStrPresentAddress = encryption.Encrypt("", GlobalData.getStrSessionId());
//                String mStrPermanentAddress = encryption.Encrypt("", GlobalData.getStrSessionId());
//                String mStrOfficeAddress = encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete=encryption.Encrypt("G", GlobalData.getStrSessionId());
//                String mStrEncryptThanaId=encryption.Encrypt("", GlobalData.getStrSessionId());


                String strGet= InsertKyc.updateContactInfo(
                        mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        strParamete);


                //-----------------------------------------------

                if (strGet.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {
                    // strDecryptDOB + "*" + strDecryptOccupation + "*" + strDecryptOrg_Name + "*" + strDecryptGender

                    String[] parts = strGet.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strEmail = parts[0];//Login Successfully
                    String strPhoneno = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==



                    if (strEmail != "" && strPhoneno != "") {
                       strStep4="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }


                }
                //---------------------------------------
            }
            //---------------- end of contact info
            else if(strListLabel[position].equalsIgnoreCase("Nominee Info")) //------------------for Nominee Info info------------------------------
            {
                String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String mStrEncryptFaterName = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptMotherNAme = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSposeTitle= encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSpouseNAme= encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptRemarks= encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete = encryption.Encrypt("G", GlobalData.getStrSessionId());

                String strPersonalinfo = InsertKyc.insertNomineeInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptFaterName,
                        mStrEncryptMotherNAme,
                        mStrEncryptSposeTitle,
                        mStrEncryptSpouseNAme,
                        mStrEncryptRemarks,
                        strParamete);

                if (strPersonalinfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {


                    String[] parts = strPersonalinfo.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strDOB = parts[0];//Login Successfully
                    String Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                    String ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                    String Gender = parts[3];
                    String Remarks = parts[4];
                    if (strDOB != "" && Occupation != "" && ORG_name != "" && Gender != "" && Remarks!="" ) {
                       strStep5="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }

                }

                //------------------End of Nominee Info info------------------------------
            }

            else if(strListLabel[position].equalsIgnoreCase("Introducer Info")) //------------------for Introducer info------------------------------
            {
                String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String mStrEncryptFaterName = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptMotherNAme = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSposeTitle= encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSpouseNAme= encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptRemarks= encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete = encryption.Encrypt("G", GlobalData.getStrSessionId());

                String strPersonalinfo = InsertKyc.insertIntroducerInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptFaterName,
                        mStrEncryptMotherNAme,
                        mStrEncryptSposeTitle,
                        mStrEncryptSpouseNAme,
                        mStrEncryptRemarks,
                        strParamete);

                if (strPersonalinfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {


                    String[] parts = strPersonalinfo.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strDOB = parts[0];//Login Successfully
                    String Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                    String ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                    String Gender = parts[3];
                    String Remarks = parts[4];
                    if (strDOB != "" && Occupation != "" && ORG_name != "" && Gender != "" && Remarks!="" ) {
                        strStep6="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }

                }

                //------------------End of Introducer info------------------------------
            }
            else if(strListLabel[position].equalsIgnoreCase("Bank Info")) //------------------for Bank info------------------------------
            {
                String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String mStrEncryptFaterName = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptMotherNAme = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSposeTitle= encryption.Encrypt("", GlobalData.getStrSessionId());

                String mStrEncryptRemarks= encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete = encryption.Encrypt("G", GlobalData.getStrSessionId());

                String strPersonalinfo = InsertKyc.insertBankInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptFaterName,
                        mStrEncryptMotherNAme,
                        mStrEncryptSposeTitle,
                        mStrEncryptRemarks,
                        strParamete);

                if (strPersonalinfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {


                    String[] parts = strPersonalinfo.split("\\*");//Login Successfully&7A8NTA5Z0iFBeBcqhyKSBg==&xXnV1UduuUGrB4sz9/ibzA==
                    String strDOB = parts[0];//Login Successfully
                    String Occupation = parts[1];//7A8NTA5Z0iFBeBcqhyKSBg==
                    String ORG_name = parts[2];//xXnV1UduuUGrB4sz9/ibzA==
                    String Gender = parts[3];

                    if (strDOB != "" && Occupation != "" && ORG_name != "" && Gender != "" ) {
                        strStep7="C";
                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                    } else {
                        imageViewCompleteComplete.setVisibility(View.GONE);
                        imageViewCompleteIncomplete.setVisibility(View.VISIBLE);

                    }

                }

                //------------------End of bank info------------------------------
            }
             //================== Document =========================================
            else if(strListLabel[position].equalsIgnoreCase("Upload Document/Picture")) //------------------for Document info------------------------------
            {
                String mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                String mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String mStrEncryptFaterName = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptMotherNAme = encryption.Encrypt("", GlobalData.getStrSessionId());
                String mStrEncryptSposeTitle= encryption.Encrypt("", GlobalData.getStrSessionId());

                String mStrEncryptRemarks= encryption.Encrypt("", GlobalData.getStrSessionId());
                String strParamete = encryption.Encrypt("A", GlobalData.getStrSessionId());
                String mStrEncryptidback= encryption.Encrypt("", GlobalData.getStrSessionId());
                String strPersonalinfo = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber,
                        mStrEncryptPin,
                        mStrEncryptFaterName,
                        mStrEncryptMotherNAme,
                        mStrEncryptSposeTitle,
                        strParamete,
                        mStrEncryptRemarks,mStrEncryptidback
                        );

                if (strPersonalinfo.equalsIgnoreCase("No Data Found")) {
                    imageViewCompleteComplete.setVisibility(View.GONE);
                    imageViewCompleteIncomplete.setVisibility(View.VISIBLE);
                } else {

                        imageViewCompleteComplete.setVisibility(View.VISIBLE);
                        imageViewCompleteIncomplete.setVisibility(View.GONE);
                }

                //------------------End of bank info------------------------------
            }

//
//            if(strStep1.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 10% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("C") && strStep2.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 20% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("C") && strStep2.equalsIgnoreCase("C")&& strStep3.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 30% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("C") && strStep2.equalsIgnoreCase("C")&& strStep3.equalsIgnoreCase("C")&& strStep4.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 40% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("C") && strStep2.equalsIgnoreCase("C")&& strStep3.equalsIgnoreCase("C")&& strStep4.equalsIgnoreCase("C")&& strStep5.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 50% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("C") && strStep2.equalsIgnoreCase("C")&& strStep3.equalsIgnoreCase("C")&& strStep4.equalsIgnoreCase("C")&& strStep5.equalsIgnoreCase("C")&& strStep6.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 60% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("C") && strStep2.equalsIgnoreCase("C")&& strStep3.equalsIgnoreCase("C")&& strStep4.equalsIgnoreCase("C")&& strStep5.equalsIgnoreCase("C")&& strStep6.equalsIgnoreCase("C")&& strStep7.equalsIgnoreCase("C"))
//            {
//                txtviewCompleteStatus.setText("Your KYC is 80% Completed!! ");
//            }
//            if(strStep1.equalsIgnoreCase("") && strStep2.equalsIgnoreCase("")&& strStep3.equalsIgnoreCase("")&& strStep4.equalsIgnoreCase("")&& strStep5.equalsIgnoreCase("")&& strStep6.equalsIgnoreCase("")&& strStep7.equalsIgnoreCase(""))
//            {
//                txtviewCompleteStatus.setText("Please Complete Your Profile!! ");
//            }

        }
        catch (Exception ex)
        {

        }

       imageViewCompleteIncomplete.setImageResource(intImgCompleteIncomplete[position]);



        return rowView;
    }
}