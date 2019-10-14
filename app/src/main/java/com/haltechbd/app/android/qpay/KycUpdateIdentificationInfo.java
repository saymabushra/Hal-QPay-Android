package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.ImageInputHelper;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class KycUpdateIdentificationInfo extends AppCompatActivity implements View.OnClickListener ,ImageInputHelper.ImageActionListener  {
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog = null;

    ArrayList<String> arrayListIdentificationId = new ArrayList<String>();
    ArrayList<String> arrayListIdentification = new ArrayList<String>();
    private ImageInputHelper imageInputHelper;
    private Spinner mSpinnerIdentificationDocType;
    private EditText mEditTextIdentificationNumber, mEditTextRemark;
    private Button mBtnSaveIdentificationInfo,mBtnChoosePhoto,mBtnChoosePhotoBack;
    private TextView mTextViewShowServerResponse,mTextidentificationname,mTextviewBack;
    ImageView mImageView,mImageViewBack;
    private String mStrMasterKey, mStrAccountNumber, mStrPin,
            mStrEncryptAccountNumber, mStrEncryptPin, mStrIdentificationId,mstrPicIdBack,
            mStrEncryptIdentificationId, mStrEncryptIdentificationNumber, mStrEncryptRemark,
            mStrServerResponse,strUploadImageresponseback,Imagefilename,Imagefilenameback,mStrEncryptPIN,mstrParameter,mstrPictureID,strUploadImageresponse;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2,GALLERY_Back=3,CAMERA_Back=4;
String strSelectBackImage="";
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identificaiton_document_upload_new_v2);
        checkOs();
        initUI();
    }

    // initialize all ui components and enable buttons for click event
    private void initUI() {


//        mSpinnerIdentificationDocType = findViewById(R.id.spinnerKycIdentificationInfoIdentificationDocType);
        mEditTextIdentificationNumber = findViewById(R.id.editTextIdentificationDocumentID);
        mEditTextIdentificationNumber.requestFocus();
        mEditTextRemark=findViewById(R.id.editTextIdentificationRemarks);
        mImageView=findViewById(R.id.imgViewIdentificationDocumentFrontPage);
        mImageViewBack=findViewById(R.id.imgViewIdentificationDocumentBackPage);
        mTextidentificationname = findViewById(R.id.TextIdentificationName);
        mBtnChoosePhoto=findViewById(R.id.btnIdentificationDocumentChooseFrontPage);
        mBtnChoosePhotoBack=findViewById(R.id.btnIdentificationDocumentChooseBackPage);
        mBtnSaveIdentificationInfo = findViewById(R.id.btnIdentificationDocumentUpload);
        mBtnSaveIdentificationInfo.setOnClickListener(this);
        mBtnChoosePhoto.setOnClickListener(this);
        mBtnChoosePhotoBack.setOnClickListener(this);
       mTextViewShowServerResponse = findViewById(R.id.textViewImageFile);
       mTextviewBack=findViewById(R.id.textViewImageFileback);
       // mStrMasterKey = GlobalData.getStrMasterKey();
        mStrAccountNumber = GlobalData.getStrAccountNumber();
        mStrPin = GlobalData.getStrPin();
        mTextidentificationname.setText(GlobalData.getStrIdentificationname());
        mEditTextIdentificationNumber.setHint("ENTER "+GlobalData.getStrIdentificationname()+" ID");
        if(GlobalData.getStrIdentificationname().equalsIgnoreCase("PROFILE PICTURE"))
        {
            mEditTextIdentificationNumber.setVisibility(View.GONE);
            mEditTextIdentificationNumber.setText("Gone");
        }
        else
        {
            mEditTextIdentificationNumber.setVisibility(View.VISIBLE);
            if(GlobalData.getStrIdentificationNumber().equalsIgnoreCase("No Data Found")) {
                mEditTextIdentificationNumber.setText("");
            }
            else
            {
                mEditTextIdentificationNumber.setText(GlobalData.getStrIdentificationNumber());
            }
        }

        if(GlobalData.getStrIdentificationname().equalsIgnoreCase("NATIONAL ID"))
        {
            mImageViewBack.setVisibility(View.VISIBLE);
            mBtnChoosePhotoBack.setVisibility(View.VISIBLE);
            mTextviewBack.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        checkAndRequestPermissions();
        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);
    }



    @Override
    public void onClick(View v) {
        if (v == mBtnSaveIdentificationInfo)
        {
            if(GlobalData.getStrIdentificationname().equalsIgnoreCase("NATIONAL ID"))
            {
                //================================================
                if (mTextViewShowServerResponse.getText().toString().length() == 0) {
                    mTextViewShowServerResponse.setText("Select Front Image First!!!");
                }
                else if (mTextviewBack.getText().toString().length() == 0) {
                    mTextviewBack.setText("Select Back Image !!!");
                }
                else if (mEditTextIdentificationNumber.getText().toString().length() == 0) {
                    mEditTextIdentificationNumber.setError("Field cannot be empty");
                }

//            else if (mImageView.getContext().toString().length()==0)
//            {
//                mTextViewShowServerResponse.setError("Select Image !!!");
//            }
                else {
                    try {
                        mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                        mStrEncryptPIN = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                        mStrEncryptIdentificationId = encryption.Encrypt(GlobalData.getStrIdentificationID(), GlobalData.getStrSessionId());
                        mStrEncryptIdentificationNumber = encryption.Encrypt(mEditTextIdentificationNumber.getText().toString(), GlobalData.getStrSessionId());
                        mStrEncryptRemark = encryption.Encrypt(mEditTextRemark.getText().toString(), GlobalData.getStrSessionId());
                        mstrParameter = encryption.Encrypt("U", GlobalData.getStrSessionId());
                        mstrPictureID = encryption.Encrypt(Imagefilename, GlobalData.getStrSessionId());
                        mstrPicIdBack= encryption.Encrypt(Imagefilenameback, GlobalData.getStrSessionId());

                        // Initialize progress dialog
                        mProgressDialog = ProgressDialog.show(KycUpdateIdentificationInfo.this, null, "Processing request...", false, true);
                        // Cancel progress dialog on back key press
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {

                                //=================================
                                Bitmap immage = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                                ;
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] imageByte = baos.toByteArray();
                                String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
                                strUploadImageresponse = InsertKyc.GetKycImage(Imagefilename, imageEncoded);

                                if (strUploadImageresponse.equalsIgnoreCase("000")) {

                                  //=========================================
                                    Bitmap immageback = ((BitmapDrawable) mImageViewBack.getDrawable()).getBitmap();

                                    ByteArrayOutputStream baosback = new ByteArrayOutputStream();
                                    immageback.compress(Bitmap.CompressFormat.PNG, 100, baosback);
                                    byte[] imageByteback = baosback.toByteArray();
                                    String imageEncodedback = Base64.encodeToString(imageByteback, Base64.DEFAULT);
                                    strUploadImageresponseback = InsertKyc.GetKycImage(Imagefilenameback, imageEncodedback);
                                    //===================================

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                    mProgressDialog.dismiss();
                                                    if (strUploadImageresponseback.equalsIgnoreCase("000")) {

                                                        mStrServerResponse = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
                                                                mStrEncryptRemark, mstrParameter, mstrPictureID,mstrPicIdBack);

                                                        if (mStrServerResponse.equalsIgnoreCase("Update")) {
                                                            //####################### Show Dialog ####################
                                                            //####################### Show Dialog ####################
                                                            //####################### Show Dialog ####################


                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
                                                            myAlert.setMessage("Successfully save Identification info.");
                                                            myAlert.setNegativeButton(
                                                                    "OK",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.cancel();
//                                                                Intent intent = new Intent(KycUpdateAddressInfo.this, KycPreviewAddressInfo.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                startActivity(intent);
                                                                            startActivity(new Intent(KycUpdateIdentificationInfo.this, KycListOfIdentificationInfo.class));
                                                                            finish();
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog = myAlert.create();
                                                            alertDialog.show();
                                                        } else {
                                                            //####################### Show Dialog ####################
                                                            //####################### Show Dialog ####################
                                                            //####################### Show Dialog ####################
                                                            if(mStrServerResponse.equalsIgnoreCase(""))
                                                            {
                                                                mStrServerResponse="Request is in process";
                                                            }

                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
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
                                                    } else {
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
                                                        myAlert.setMessage("Please Try Again!");
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
                                    //================================
                                }
                                else {
                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
                                    myAlert.setMessage("Please Try Again!");
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
                        });

                        t.start();


                    }
                    catch (Exception ex) {
                    }
                }
                //=====================================================

            }
            else {
                //================================================
                if (mTextViewShowServerResponse.getText().toString().length() == 0) {
                    mTextViewShowServerResponse.setText("Select Image First!!!");
                } else if (mEditTextIdentificationNumber.getText().toString().length() == 0) {
                    mEditTextIdentificationNumber.setError("Field cannot be empty");
                }

//            else if (mImageView.getContext().toString().length()==0)
//            {
//                mTextViewShowServerResponse.setError("Select Image !!!");
//            }
                else {
                    try {
                        mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                        mStrEncryptPIN = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                        mStrEncryptIdentificationId = encryption.Encrypt(GlobalData.getStrIdentificationID(), GlobalData.getStrSessionId());
                        mStrEncryptIdentificationNumber = encryption.Encrypt(mEditTextIdentificationNumber.getText().toString(), GlobalData.getStrSessionId());
                        mStrEncryptRemark = encryption.Encrypt(mEditTextRemark.getText().toString(), GlobalData.getStrSessionId());
                        mstrParameter = encryption.Encrypt("U", GlobalData.getStrSessionId());
                        mstrPictureID = encryption.Encrypt(Imagefilename, GlobalData.getStrSessionId());
                        mstrPicIdBack= encryption.Encrypt("", GlobalData.getStrSessionId());

                        // Initialize progress dialog
                        mProgressDialog = ProgressDialog.show(KycUpdateIdentificationInfo.this, null, "Processing request...", false, true);
                        // Cancel progress dialog on back key press
                        mProgressDialog.setCancelable(true);

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {

                                //=================================
                                final Bitmap immage = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                                ;
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] imageByte = baos.toByteArray();
                                String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
                                strUploadImageresponse = InsertKyc.GetKycImage(Imagefilename, imageEncoded);


                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                if (strUploadImageresponse.equalsIgnoreCase("000")) {

                                                    mStrServerResponse = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
                                                            mStrEncryptRemark, mstrParameter, mstrPictureID,mstrPicIdBack);

                                                    if (mStrServerResponse.equalsIgnoreCase("Update")) {
                                                        //####################### Show Dialog ####################
                                                        //####################### Show Dialog ####################
                                                        //####################### Show Dialog ####################


                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
                                                        myAlert.setMessage("Successfully save Identification info.");
                                                        myAlert.setNegativeButton(
                                                                "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
//                                                                Intent intent = new Intent(KycUpdateAddressInfo.this, KycPreviewAddressInfo.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                startActivity(intent);

                                                                        if(GlobalData.getStrIdentificationname().equalsIgnoreCase("PROFILE PICTURE"))
                                                                        {
                                                                             Bitmap immage2 = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                                                                            GlobalData.setStrProPicBitmap(immage2);
                                                                            GlobalData.setStrIndetificationInfoPicture(Imagefilename);
                                                                        }
                                                                        startActivity(new Intent(KycUpdateIdentificationInfo.this, KycListOfIdentificationInfo.class));
                                                                        finish();
                                                                    }
                                                                });
                                                        AlertDialog alertDialog = myAlert.create();
                                                        alertDialog.show();
                                                    } else {
                                                        //####################### Show Dialog ####################
                                                        //####################### Show Dialog ####################
                                                        //####################### Show Dialog ####################
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
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
                                                } else {
                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
                                                    myAlert.setMessage("Please Try Again!");
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


                    } catch (Exception ex) {
                    }
                }
                //=====================================================
            }
        }
        else if (v == mBtnChoosePhoto)
        {
            showPictureDialog();

        }
        else if (v == mBtnChoosePhotoBack)
        {
          //  showPictureDialogback();
            strSelectBackImage="111";
            showPictureDialog();

        }

//            if (mEditTextIdentificationNumber.getText().toString().length() == 0) {
//                mEditTextIdentificationNumber.setError("Field cannot be empty");
//            } else if (mEditTextRemark.getText().toString().length() == 0) {
//                mEditTextRemark.setError("Field cannot be empty");
//            } else {
////                disableUiComponentAfterClick();
//
//                try {
//                    mStrEncryptIdentificationId = encryption.Encrypt(mStrIdentificationId, mStrMasterKey);
//                    mStrEncryptIdentificationNumber = encryption.Encrypt(mEditTextIdentificationNumber.getText().toString(), mStrMasterKey);
//                    mStrEncryptRemark = encryption.Encrypt(mEditTextRemark.getText().toString(), mStrMasterKey);
//
//                    // Initialize progress dialog
//                    mProgressDialog = ProgressDialog.show(KycUpdateIdentificationInfo.this, null, "Processing request...", false, true);
//                    // Cancel progress dialog on back key press
//                    mProgressDialog.setCancelable(true);
//
//                    Thread t = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            mStrServerResponse = InsertKycIdentificationInfo.insertIndentificationInfo(
//                                    mStrEncryptAccountNumber,
//                                    mStrEncryptPin,
//                                    mStrEncryptIdentificationId,
//                                    mStrEncryptIdentificationNumber,
//                                    mStrEncryptRemark,
//                                    mStrMasterKey);
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    try {
//                                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                            mProgressDialog.dismiss();
//                                            if(mStrServerResponse.equalsIgnoreCase("Update")){
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
//                                                myAlert.setMessage("Successfully save identification info.");
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            }else{
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                //####################### Show Dialog ####################
//                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
//                                                myAlert.setMessage(mStrServerResponse);
//                                                myAlert.setNegativeButton(
//                                                        "OK",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                dialog.cancel();
//                                                            }
//                                                        });
//                                                AlertDialog alertDialog = myAlert.create();
//                                                alertDialog.show();
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        // TODO: handle exception
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                    t.start();
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }
//
    }


    private void checkInternet() {
        if (isNetworkConnected()) {
            enableUiComponents();
            try {
//                mStrEncryptAccountNumber = encryption.Encrypt(mStrAccountNumber, mStrMasterKey);
//                mStrEncryptPin = encryption.Encrypt(mStrPin, mStrMasterKey);
//                mStrServerResponse = GetKycIdentificationInfo.getIdentificationInfo(
//                        mStrEncryptAccountNumber,
//                        mStrEncryptPin,
//                        mStrMasterKey);
//                loadSpinner();
//                ArrayAdapter<String> adapterDistrictName = new ArrayAdapter<String>(KycUpdateIdentificationInfo.this,
//                        android.R.layout.simple_spinner_item, arrayListIdentification);
//                adapterDistrictName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mSpinnerIdentificationDocType.setAdapter(adapterDistrictName);
//                mSpinnerIdentificationDocType.setOnItemSelectedListener(onItemSelectedListenerForIdentificationDocType);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
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

    //######################### Show Identification Doc Type #########################
    //######################### Show Identification Doc Type #########################
    //######################### Show Identification Doc Type #########################
//    private void loadSpinner() {
//        arrayListIdentificationId.clear();
//        arrayListIdentification.clear();
//        String str = GetKycIdentificationInfo.getIdentificationInfo(mStrEncryptAccountNumber, mStrEncryptPin, mStrMasterKey);
//        if (str != null && !str.isEmpty()) {
//            StringTokenizer strToken = new StringTokenizer(str, "&");
//            ArrayList<String> arrayListDistrictIdAndName = new ArrayList<String>();
//            for (int j = 0; j <= strToken.countTokens(); j++) {
//                while (strToken.hasMoreElements()) {
//                    arrayListDistrictIdAndName.add(strToken.nextToken());
//                }
//            }
//            for (int i = 0; i <= arrayListDistrictIdAndName.size() - 1; i++) {
//                StringTokenizer tokenDistrictIdAndName = new StringTokenizer(arrayListDistrictIdAndName.get(i), "*");
//                arrayListIdentificationId.add(tokenDistrictIdAndName.nextToken());
//                arrayListIdentification.add(tokenDistrictIdAndName.nextToken());
//            }
//        } else {
//            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(KycUpdateIdentificationInfo.this);
//            mAlertDialogBuilder.setMessage("No Account Found.");
//            mAlertDialogBuilder.setNegativeButton(
//                    "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//            mAlertDialog.show();
//        }
//    }
//
//    //######################### Identification ID #########################
//    //######################### Identification ID #########################
//    //######################### Identification ID #########################
//    AdapterView.OnItemSelectedListener onItemSelectedListenerForIdentificationDocType = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            mStrIdentificationId = String.valueOf(arrayListIdentificationId.get(position));
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void enableUiComponents() {
//        mEditTextIdentificationNumber.setEnabled(true);
//        mEditTextRemark.setEnabled(true);
//        mBtnSaveIdentificationInfo.setEnabled(true);
    }

    private void disableUiComponents() {
//        mEditTextIdentificationNumber.setEnabled(false);
//        mEditTextRemark.setEnabled(false);
//        mBtnSaveIdentificationInfo.setEnabled(false);
    }



    //----------------------------

//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        if (resultCode == this.RESULT_CANCELED) {
//            return;
//        }
//
//        else if (requestCode == GALLERY) {
//            if (intent != null) {
//                Uri contentURI = intent.getData();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    Bitmap bitresized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//                    // String path = saveImage(bitmap);
//                    Toast.makeText(KycUpdateIdentificationInfo.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    mImageView.setImageBitmap(bitresized);
//
//                    Date date = new Date();
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//                    String formattedDate = dateFormat.format(date);
//
//                    Imagefilename = formattedDate + ".PNG";
//                    mTextViewShowServerResponse.setText("SELECT IMAGE:"+Imagefilename);
//                    //--------------save into share prefarence
////                    msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
////                    mshareprefImageEditor = msharePrefImage.edit();
////
////                    mshareprefImageEditor.putString("imagePreferance", encodeTobase64(bitmap));
////                    mshareprefImageEditor.commit();
//                    //intToolBar();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(KycUpdateIdentificationInfo.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else if (requestCode == CAMERA) {
//
//            onCaptureImageResult(intent);
//            Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
//            mImageView.setImageBitmap(thumbnail);
//
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//            String formattedDate = dateFormat.format(date);
//
//            Imagefilename = formattedDate + ".PNG";
//            mTextViewShowServerResponse.setText("SELECT IMAGE:"+Imagefilename);
//            // saveImage(thumbnail);
//
//            //--------------save into share prefarence
////            msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
////            mshareprefImageEditor = msharePrefImage.edit();
////
////            mshareprefImageEditor.putString("imagePreferance", encodeTobase64(thumbnail));
////            mshareprefImageEditor.commit();
//            Toast.makeText(KycUpdateIdentificationInfo.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
//        else if (requestCode == GALLERY_Back) {
//            if (intent != null) {
//                Uri contentURI = intent.getData();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    Bitmap bitresized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//                    // String path = saveImage(bitmap);
//                    Toast.makeText(KycUpdateIdentificationInfo.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    mImageViewBack.setImageBitmap(bitresized);
//
//                    Date date = new Date();
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//                    String formattedDate = dateFormat.format(date);
//
//                    Imagefilenameback = formattedDate + ".PNG";
//                    mTextviewBack.setText("SELECT Back IMAGE:"+Imagefilenameback);
//                    //--------------save into share prefarence
////                    msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
////                    mshareprefImageEditor = msharePrefImage.edit();
////
////                    mshareprefImageEditor.putString("imagePreferance", encodeTobase64(bitmap));
////                    mshareprefImageEditor.commit();
//                    //intToolBar();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(KycUpdateIdentificationInfo.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else if (requestCode == CAMERA_Back) {
//
//            onCaptureImageResultback(intent);
//            Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
//            mImageViewBack.setImageBitmap(thumbnail);
//
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//            String formattedDate = dateFormat.format(date);
//
//            Imagefilenameback = formattedDate + ".PNG";
//            mTextviewBack.setText("SELECT Back IMAGE:"+Imagefilenameback);
//            // saveImage(thumbnail);
//
//            //--------------save into share prefarence
////            msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
////            mshareprefImageEditor = msharePrefImage.edit();
////
////            mshareprefImageEditor.putString("imagePreferance", encodeTobase64(thumbnail));
////            mshareprefImageEditor.commit();
//            Toast.makeText(KycUpdateIdentificationInfo.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }

    private void showPictureDialog(){
        android.support.v7.app.AlertDialog.Builder pictureDialog = new android.support.v7.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select Photo From Gallery",
                "Capture Photo From Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();

                                break;
                            case 1:
                                takePhotoFromCamera();

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, GALLERY);
        imageInputHelper.selectImageFromGallery();
    }

    private void takePhotoFromCamera() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
        imageInputHelper.takePhotoWithCamera();
    }

//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mImageView.setImageBitmap(thumbnail);
//    }
//-------------------------------------------
//    private void showPictureDialogback(){
//        android.support.v7.app.AlertDialog.Builder pictureDialog = new android.support.v7.app.AlertDialog.Builder(this);
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Select Photo From Gallery",
//                "Capture Photo From Camera" };
//        pictureDialog.setItems(pictureDialogItems,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                choosePhotoFromGallaryback();
//
//                                break;
//                            case 1:
//                                takePhotoFromCameraback();
//
//                                break;
//                        }
//                    }
//                });
//        pictureDialog.show();
//    }
//
//    public void choosePhotoFromGallaryback() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, GALLERY_Back);
//
//    }
//
//    private void takePhotoFromCameraback() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA_Back);
//
//    }
//
//    private void onCaptureImageResultback(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mImageViewBack.setImageBitmap(thumbnail);
//    }
    //--------------------------------------


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
                startActivity(new Intent(KycUpdateIdentificationInfo.this, KycListOfIdentificationInfo.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycUpdateIdentificationInfo.this, Login.class)
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(KycUpdateIdentificationInfo.this, KycListOfIdentificationInfo.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

//----------------Crop image------------------------------------
    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        imageInputHelper.requestCropImage(uri, 300, 400, 3, 4);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        imageInputHelper.requestCropImage(uri, 300, 400, 3, 4);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

           // mImgView.setImageBitmap(bitmap);

               // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
               // Bitmap bitresized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                // String path = saveImage(bitmap);
                Toast.makeText(KycUpdateIdentificationInfo.this, "Image Saved!", Toast.LENGTH_SHORT).show();
               if(strSelectBackImage.equalsIgnoreCase("111") || strSelectBackImage!="")
               {
                   Date date = new Date();
                   SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
                   String formattedDate = dateFormat.format(date);
                   mImageViewBack.setImageBitmap(bitmap);
                   Imagefilenameback=formattedDate + ".PNG";
                   mTextviewBack.setText("SELECT IMAGE:"+Imagefilenameback);
               }
               else {
                   mImageView.setImageBitmap(bitmap);
                   Date date = new Date();
                   SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
                   String formattedDate = dateFormat.format(date);

                   Imagefilename = formattedDate + ".PNG";
                   mTextViewShowServerResponse.setText("SELECT IMAGE:"+Imagefilename);
               }


                //--------------save into share prefarence
//                    msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
//                    mshareprefImageEditor = msharePrefImage.edit();
//
//                    mshareprefImageEditor.putString("imagePreferance", encodeTobase64(bitmap));
//                    mshareprefImageEditor.commit();
                //intToolBar();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }
}
