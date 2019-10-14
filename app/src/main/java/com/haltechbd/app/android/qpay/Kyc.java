package com.haltechbd.app.android.qpay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.utils.CustomListForKyc;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GlobalData;
import com.haltechbd.app.android.qpay.utils.InsertKyc;

import java.io.ByteArrayOutputStream;


public class Kyc extends AppCompatActivity {
    private ListView mListMenuServices;

    ImageView imgProfile;
    TextView txtViewName,txtviewInfo;
    private SharedPreferences mSharedPreferencsOtp,msharePrefImage;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor,mshareprefImageEditor;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    String mStrEncryptAccountNumber,mStrEncryptPin;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc);
        mListMenuServices = (ListView) findViewById(R.id.listViewkyc);
        imgProfile=(ImageView)findViewById(R.id.imgViewProfilePickyc) ;
        txtViewName=findViewById(R.id.txtviewKycname);
        txtviewInfo=findViewById(R.id.textViewKycIfno);

        checkOs();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initServiceMenu();

//        msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
//        mshareprefImageEditor = msharePrefImage.edit();
//        String strBitMap = msharePrefImage.getString("imagePreferance", "");
//        // decodeBase64(strBitMap);
//        if(strBitMap!=null && !strBitMap.isEmpty())
//        {
//            final Bitmap selectedImage = decodeBase64(strBitMap);
//
//            imgProfile.setImageBitmap(selectedImage);
//        }

        //==================== get identification info for profile picture============================
        if(GlobalData.getStrProPicBitmap()!=null ) {
            imgProfile.setImageBitmap(GlobalData.getStrProPicBitmap());
        }



        //============== profile picture end =============================
        txtViewName.setText(GlobalData.getStrAccountHolderName());
        txtviewInfo.setText(GlobalData.getStrAccountNumber());

    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    // method for green banking services menu list
    private void initServiceMenu() {

        Integer[] intImgIcon = {
                R.drawable.kyc_personal_info,
                R.drawable.kyc_parents_spouse_info,
                R.drawable.kyc_address_info,
                R.drawable.kyc_contact_info,
//                R.drawable.identificationinfo,
                R.drawable.kyc_bank_info,
                R.drawable.kyc_nominee_info,
                R.drawable.kyc_introducer_info,
                R.drawable.attached_icon_color

        };
        final String[] strListLabel = {
                "Personal Info",
                "Parents/Spouse Info",
                "Address Info",
                "Contact Info",
//                "Identification Info",
                "Bank Info",
                "Nominee Info",
                "Introducer Info",
                "Upload Document/Picture"
        };
        Integer[] intImgCompleteIncomplete = {
                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete,
//                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete,
                R.drawable.icon_kyc_incomplete
        };
        CustomListForKyc adapter = new CustomListForKyc(Kyc.this, intImgIcon, strListLabel, intImgCompleteIncomplete);
        mListMenuServices.setAdapter(adapter);
        mListMenuServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strListLabel[+position] == "Personal Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewPersonalInfo.class));
                    finish();
                } else if (strListLabel[+position] == "Parents/Spouse Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewParentsAndSpouseInfo.class));
                    finish();
                } else if (strListLabel[+position] == "Address Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewAddressInfo.class));
                    finish();
                } else if (strListLabel[+position] == "Contact Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewContactInfo.class));
                    finish();
                }
//                else if (strListLabel[+position] == "Identification Info") {
//                    startActivity(new Intent(Kyc.this, KycPreviewIdentificationInfo.class));
//                    finish();
//                }
                else if (strListLabel[+position] == "Bank Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewBankInfo.class));
                    finish();
                } else if (strListLabel[+position] == "Nominee Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewNomineeInfo.class));
                    finish();
                } else if (strListLabel[+position] == "Introducer Info") {
                    startActivity(new Intent(Kyc.this, KycPreviewIntroducerInfo.class));
                    finish();
                }
                else if (strListLabel[+position] == "Upload Document/Picture") {
                    startActivity(new Intent(Kyc.this, KycListOfIdentificationInfo.class));
                    finish();
                }

            }
        });


    }

    //########################## Back ############################
    //########################## Back ############################
    //########################## Back ############################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
//        pinMenuItem.setTitle("Reward Point:678");
        return true;
    }

    //########################## Logout ############################
    //########################## Logout ############################
    //########################## Logout ############################
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Kyc.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(Kyc.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Kyc.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
