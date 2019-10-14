package com.haltechbd.app.android.qpay;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.utils.BottomNavigationViewHelper;
import com.haltechbd.app.android.qpay.utils.CheckDeviceActiveStatus;
import com.haltechbd.app.android.qpay.utils.CustomGrid;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetAllFavoriteList;
import com.haltechbd.app.android.qpay.utils.GetBalance;
import com.haltechbd.app.android.qpay.utils.GetLastTransaction;
import com.haltechbd.app.android.qpay.utils.GetQrCodeContent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Animation.AnimationListener ,ImageInputHelper.ImageActionListener  {

    // private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
    private ProgressDialog mProgressDialog;
    private EncryptionDecryption encryption = new EncryptionDecryption();

    //#############################################################
    private SharedPreferences mSharedPreferencsOtp,msharePrefImage,msharePrefOfflinePin,mSharedPreferencsLogin;
    private SharedPreferences.Editor mSharedPreferencsOtpEditor,mshareprefImageEditor,mshareprefOfflinePINEditor,mSharedPreferencsLoginEditor;
    //#############################################################
    private ImageInputHelper imageInputHelper;
    private Animation anim;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    int intCount=0;
    Switch simpleSwitch;
    Boolean isSingleLine = true;
    private DrawerLayout drawer;
    private LinearLayout mLinearLayoutLastTransaction, mLinearLayoutRefreshBalance;
    private ScrollView mScrollView;
    private TextView mTextViewAccountHolderName, mTextViewAccountNumber, mTextViewAccountTypeAndStatus,
            mTextViewBalanceAmount, mTextViewBalanceType, mTextViewLastTransaction,txtviewNavBannername,txtviewNavBannerInfo;
    private ImageView mImgViewProfilePic, mImgBtnQucikPay, mImgBtnQr;
    private Button mBtnDefultAccount, mBtnLinkAccount, mBtnDiscountCard;
    private ImageButton mImgBtnReload;
    private GridView mGridView;
    private String mStrServicePackage, mStrQrCodeContent, mStrAccountNumber,mstrMenuResponse,
            mStrMasterKey, mStrCurrentDate, mStrEncryptAccountNumber,
            mStrEncryptFromDate, mStrEncryptToDate, mStrEncryptPin,
            mStrEncryptAccessCode, mStrEncryptParameter, mStrServerResponse, mStrLastTransaction,
            mStrOtpStatus, mStrMerchantRank, strMsg,
            mStrBankBin, mStrUrlForQrCode, mStrMethodName,
            strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr,
            mStrDestinationWallet, mStrDestinationName, mStrSourceWallet,
            mStrEncryptMerchatWalletForOtp, mStrCustomerWalletByQrCode, mStrEncryptCustomerWalletByQrCode,
             mStrAccountType, mStrAccountStatus, mStrAccountTypeAndStaus,strDefultWalletBalance,strDefultWalletQRCode,
            SourceRankCustomer, strSourceWallet;
    ArrayList<String> arrayAccountName = new ArrayList<String>();
    ArrayList<String> arrayAccountDefuly = new ArrayList<String>();
    ArrayList<String> arrayBankAccountNo = new ArrayList<String>();
    ArrayList<String> arrayBankAccountStatus = new ArrayList<String>();
    ArrayList<String> arrayBankAccountIsVerified = new ArrayList<String>();

    private int GALLERY_REQUEST = 1;

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    //---------------------------------------
//     BottomNavigationMenu.setNotification(notification, bottomNavigation.getItemsCount() - 1);
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
          //  BottomNavigationView.setNotification(notification, bottomNavigation.getItemsCount() - 1);
//          View v = BottomNavigationView.getChildAt(1);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
//
//        View badge = LayoutInflater.from(this)
//                .inflate(R.layout.homescreen_count, bottomNavigationMenuView, false);
//        TextView tv = badge.findViewById(R.id.notification_badge);
//    tv.setText("22+");
//    itemView.addView(badge);
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//                    finish();
//                    return true;
                case R.id.navigation_addtofav:
                    startActivity(new Intent(MainActivity.this, FavoriteTransaction.class));
                    finish();
                    return true;
                case R.id.navigation_call:
                   // mTextMessage.setText(R.string.title_notifications);

                    AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                    final View view = factory.inflate(R.layout.sample, null);
                    alertadd.setView(view);
                    alertadd.setNeutralButton("Call Later", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                        }
                    });
                    alertadd.setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                          String strCallNumber= GlobalData.getStrCallCenterPhoneNumber();
                            dialContactPhone(strCallNumber);
                        }
                    });

                    alertadd.show();

                    return true;
                case R.id.navigation_notifications:


                   // navigation_notifications.setNotification(notification, bottomNavigation.getItemsCount() - 1);
//                    bottomNavigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);
                    startActivity(new Intent(MainActivity.this, Notification_Show.class));
                    finish();


                    return true;
                case R.id.navigation_chat:
                    startActivity(new Intent(MainActivity.this, ComingSoon.class));
                    finish();
                    return true;
            }
            return false;
        }
    };
    //-----------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

       //-----------------------------
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // btn.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        checkOs();
        initUi();
        intToolBar();



        // ---------bottom menu bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bottom);
       // navigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //------------------------------
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
       // new QBadgeView(this).bindTarget(v).setBadgeNumber(5);

        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);

        TextView notificationCount = badge.findViewById(R.id.notificationsbadge);
        String strCount= String.valueOf(intCount);
        if(strCount.equalsIgnoreCase("0"))
        {

            notificationCount.setVisibility(View.GONE);
        }
        else
            {
                notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText(strCount);
        }
//        TextView txtnotificationbadge=findViewById(R.id.notificationsbadge);
//        txtnotificationbadge.setText("4");
        itemView.addView(badge);
        //
        //-----------------------------
         //---------------------------------------------
        //================ Quick Pay ==================
        mImgBtnQucikPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStrOtpStatus.equalsIgnoreCase("valid")) {


                    // OTP Valid
                    try
                    {

//                        IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
//                        scanIntegrator.setPrompt("Scan");
//                        scanIntegrator.setBeepEnabled(true);
//                        scanIntegrator.setOrientationLocked(true);
//                        scanIntegrator.setBarcodeImageEnabled(true);
//                        scanIntegrator.initiateScan();
//                        Intent intent = new Intent(ACTION_SCAN);
//                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                        startActivityForResult(intent, 0);

                        startActivity(new Intent(MainActivity.this, QrCodeScanActivity.class));
                        finish();
                        GlobalData.setStrPageRecord("Main");
                    }
                    catch (Exception anfe)
                    {
//                        showDialog(MainActivity.this, "No Scanner Found", "Download a QR Scanner App?", "Yes",
//                                "No").show();

                        AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                        myAlert.setMessage("   Scanner Found");
                        myAlert.setPositiveButton(
                                "Scan Again",
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

                    // OTP Expire
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                    myAlert.setMessage("OTP is expired. Generate a new OTP?");
                    myAlert.setPositiveButton(
                            "Generate OTP",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                    finish();
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

            }
        });


    }

//    public void sendNotification(View view) {
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this);
//
//        //Create the intent that’ll fire when the user taps the notification//
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        mBuilder.setContentIntent(pendingIntent);
//
//        mBuilder.setSmallIcon(R.drawable.icon_basic_info);
//        mBuilder.setTicker("Hearty365");
//        mBuilder.setContentTitle("My notification");
//        mBuilder.setContentText("Hello World!");
//        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND);
//        mBuilder.setPriority(Notification.PRIORITY_MAX);
//
//        mBuilder.setContentInfo("Info");
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        //================================
//
////        int icon = R.drawable.icon_basic_info;
////        CharSequence message = "Hello";
////        long when = System.currentTimeMillis();
////
////        Context context = getApplicationContext();
////        CharSequence contentTitle = "My notification";
////        CharSequence contentText = "Hello World!";
////        Intent notificationIntent = new Intent(this, FavoriteTransaction.class);
////        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
////        mNotificationManager.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
////
////        mNotificationManager.setLatestEventInfo(context, title, message, intent);
////        mNotificationManager.flags |= Notification_Show.FLAG_AUTO_CANCEL;
////        mNotificationManager.notify(0, notification);
//        //===================================
//        mNotificationManager.notify(001, mBuilder.build());
//    }

    private void sendNotificationnew(String msg,String catagories,String strDate,int notification) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, Notification_Show.class);
        String CHANNEL_ID = "my_channel_01";
        //int NOTIFICATION_ID=notification;
        final int NOTIFICATION_ID=generateRandom();
        intent.putExtra("yourpackage.notifyId", NOTIFICATION_ID);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this)
                .setContentTitle(catagories)
                .setSmallIcon(R.drawable.icon_notification)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                //.addAction(getNotificationIcon(), "Action Button", pIntent)
       // .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pIntent)
                .setContentText(msg)
                .setTicker("Hearty365")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentInfo("Info")
//                .setColor(Color.GREEN)
                .setSubText("Tap to view")
//                .setNumber(3)
//                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                .setChannelId(CHANNEL_ID)
                .setOngoing(true);



        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        finish();
    }

    //########################## Initialize UI ############################
    private void initUi() {
        mScrollView = findViewById(R.id.scrollView);
        mLinearLayoutRefreshBalance = findViewById(R.id.linearLayoutRefreshBalance);
        mLinearLayoutLastTransaction = findViewById(R.id.linearLayoutLastTransaction);
        mImgViewProfilePic = findViewById(R.id.imgViewProfilePic);
        mTextViewAccountHolderName = findViewById(R.id.txtViewAccountHolderName);
        mTextViewAccountNumber = findViewById(R.id.txtViewAccountNumber);
        mTextViewAccountTypeAndStatus = findViewById(R.id.txtViewAccountTypeAndStatus);
        mImgBtnQucikPay = findViewById(R.id.imgBtnQuickPay);
        mImgBtnQr = findViewById(R.id.imgBtnQr);
        mBtnDefultAccount = findViewById(R.id.btnQPayMenuDefultAccount);
        mBtnLinkAccount = findViewById(R.id.btnQPayMenuLinkAccount);
        mBtnDiscountCard = findViewById(R.id.btnQPayDiscountCard);
        mTextViewBalanceAmount = findViewById(R.id.txtViewBalance);
        mTextViewBalanceType = findViewById(R.id.txtViewLabelBalance);
        mImgBtnReload = findViewById(R.id.imgBtnReload);
        mGridView = findViewById(R.id.gridView);
        mTextViewLastTransaction = findViewById(R.id.textViewQPayMenuLastTransaction);
        mTextViewLastTransaction.setSingleLine(true);



        //set onClick event
        mImgViewProfilePic.setOnClickListener(this);
//        mImgBtnQucikPay.setOnClickListener(this);
        mImgBtnQr.setOnClickListener(this);
        mBtnDefultAccount.setOnClickListener(this);
        mBtnLinkAccount.setOnClickListener(this);
        mBtnDiscountCard.setOnClickListener(this);
        mImgBtnReload.setOnClickListener(this);
        //get Service PackagetxtViewForgetPin
        mStrMasterKey = GlobalData.getStrMasterKey();
        mStrServicePackage = GlobalData.getStrPackage();
     //   mStrMerchantRank = GlobalData.getStrAccountRank();
//        mStrServicePackage = "1312050001";// For Merchant
//        mStrServicePackage = "1205190003";// For Customer

       // mStrAccountNumber = GlobalData.getStrAccountNumber(); // new off
       // mStrSourceWallet = mStrAccountNumber + 1;  // new off


        //set TextView
        mTextViewAccountHolderName.setText(GlobalData.getStrAccountHolderName());
        mTextViewAccountNumber.setText(GlobalData.getStrAccountNumber());

      // txtviewNavBannername.setText(GlobalData.getStrAccountHolderName());

        //--------------------------------------------------------
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
        //----------------------------------------------------


        if (mStrServicePackage.equalsIgnoreCase("1312050001"))
        {
            mStrAccountType = "Merchant";
        }
        if (mStrServicePackage.equalsIgnoreCase("1205190003"))
        {
            mStrAccountType = "Customer";
        }

        //------------ set image ----------
//        if(GlobalData.getStrProPicBitmap()!=null) {
//            mImgViewProfilePic.setImageBitmap(GlobalData.getStrProPicBitmap());
//        }
        //--------------save into share prefarence
//        msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
//        mshareprefImageEditor = msharePrefImage.edit();
//        String strBitMap = msharePrefImage.getString("imagePreferance", "");
        //==================== get identification info for profile picture============================

        if(GlobalData.getStrProPicBitmap()!=null ) {
            mImgViewProfilePic.setImageBitmap(GlobalData.getStrProPicBitmap());
        }
        else
        {
            try {
//            mStrEncryptAccountNumber=encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
//            mStrEncryptPin=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//            String mStrEncryptIdentificationId = encryption.Encrypt("140824000000000001", GlobalData.getStrSessionId());
//            String  mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
//            String mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
//            String mstrparameten = encryption.Encrypt("G", GlobalData.getStrSessionId());
//            String mstrPictureID = encryption.Encrypt("", GlobalData.getStrSessionId());
//            String mstrPictureIDback = encryption.Encrypt("", GlobalData.getStrSessionId());
//            String  strgetIdentificationInfo = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
//                    mStrEncryptRemark, mstrparameten, mstrPictureID,mstrPictureIDback);
                if(GlobalData.getStrIndetificationInfoPicture().equalsIgnoreCase("")||GlobalData.getStrIndetificationInfoPicture().isEmpty()||GlobalData.getStrIndetificationInfoPicture()==null)
                {

                }
                else {
                    String strgetIdentificationInfo = GlobalData.getStrIndetificationInfoPicture();
                    if (strgetIdentificationInfo.equalsIgnoreCase("No Data Found")) {

                    } else {


                        String strIPictureId = strgetIdentificationInfo;


                        if (!strIPictureId.equalsIgnoreCase("No Data Found")) {
                            String imageEncoded = InsertKyc.KycGetDocument(strIPictureId);
                            // String imageEncoded = Base64.encodeToString(strImage, Base64.DEFAULT);
                            Bitmap bitImage = decodeBase64(imageEncoded);
                            GlobalData.setStrProPicBitmap(bitImage);
                            mImgViewProfilePic.setImageBitmap(bitImage);

                        } else {

                        }
                    }
                }
            }
            catch (Exception ex)
            {

            }

        }
        //============== profile picture end =============================
       // decodeBase64(strBitMap);
//        if(strBitMap!=null && !strBitMap.isEmpty())
//        {
//            final Bitmap selectedImage = decodeBase64(strBitMap);
//                  //  BitmapFactory.decodeStream(imageStream);
//            mImgViewProfilePic.setImageBitmap(selectedImage);
//        }
        //----------------------------------

        mLinearLayoutRefreshBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String strEncDefultWallet = encryption.Encrypt(GlobalData.getStrWallet(), GlobalData.getStrSessionId());
                    String strEncryptedPIN=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    String strDefalutAccBalance = GetBalance.getBalance(GlobalData.getStrUserId(), strEncryptedPIN, strEncDefultWallet, GlobalData.getStrDeviceId());

                    mTextViewBalanceAmount.setText(strDefalutAccBalance);

                   // loadLastTransactionDetails();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mLinearLayoutLastTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadLastTransactionDetails();
                if (isSingleLine) {
                    mTextViewLastTransaction.setSingleLine(false);
                    isSingleLine = false;
                } else {
                    mTextViewLastTransaction.setSingleLine(true);
                    isSingleLine = true;
                }
            }
        });

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        mStrCurrentDate = sdf.format(new Date());


        //############################################### OTP ###############################################
        //############################################### OTP ###############################################
        //############################################### OTP ###############################################
        mSharedPreferencsOtp = getSharedPreferences("otpPrefs", MODE_PRIVATE);
        mSharedPreferencsOtpEditor = mSharedPreferencsOtp.edit();
        String strExpireTime = mSharedPreferencsOtp.getString("otp_expire_time", "");
        String strOtp = mSharedPreferencsOtp.getString("generate_otp", "");
        if (strExpireTime != null && !strExpireTime.isEmpty() && strOtp != null && !strOtp.isEmpty()) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                // Current Time
                Date currentTime = Calendar.getInstance().getTime();
                String strCurrentTime = df.format(currentTime);
                // Expire Time
                Date timeCurrent = df.parse(strCurrentTime);
                Date timeExpire = df.parse(strExpireTime);
                if (timeCurrent.before(timeExpire))
                {
                    mStrOtpStatus = "valid";
                }
                else
                    {
                    mStrOtpStatus = "expire";
                }
            } catch (Exception e)
            {
                mStrOtpStatus = "expire";
            }
        } else {
            mStrOtpStatus = "expire";
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkInternet();


       // String strCheckAccountActiveStatus = CheckDeviceActiveStatus.checkAccountActiveStatus(mStrEncryptAccountNumber, GlobalData.getStrUserId(),GlobalData.getStrDeviceId());
        String strCheckAccountActiveStatus =GlobalData.getStrAccountStatus();
//        String strCheckAccountActiveStatus = "A";
//        String strCheckAccountActiveStatus = "I";
//        String strCheckAccountActiveStatus = "L";
        if (strCheckAccountActiveStatus.equalsIgnoreCase("A")) {
            if (mStrServicePackage.equalsIgnoreCase("1312050001")) // merchant
            {
                mStrAccountStatus = "Active";
                mImgViewProfilePic.setEnabled(true);
                mTextViewAccountHolderName.setEnabled(true);
                mTextViewAccountNumber.setEnabled(true);
                mImgBtnQucikPay.setEnabled(true);
                mImgBtnQr.setEnabled(true);
                mBtnDefultAccount.setEnabled(true);
               // mBtnLinkAccount.setVisibility(View.GONE);

                mBtnDiscountCard.setVisibility(View.VISIBLE);
                mBtnLinkAccount.setEnabled(true);
                mBtnLinkAccount.setText("   Link Merchant Account");
                mImgBtnReload.setEnabled(true);
                mGridView.setEnabled(true);
            }
            else {
                mStrAccountStatus = "Active";
                mImgViewProfilePic.setEnabled(true);
                mTextViewAccountHolderName.setEnabled(true);
                mTextViewAccountNumber.setEnabled(true);
                mImgBtnQucikPay.setEnabled(true);
                mImgBtnQr.setEnabled(true);
                mBtnDefultAccount.setEnabled(true);
                mBtnLinkAccount.setEnabled(true);
                mBtnDiscountCard.setEnabled(true);
                mImgBtnReload.setEnabled(true);
                mGridView.setEnabled(true);
            }
        } else if (strCheckAccountActiveStatus.equalsIgnoreCase("I")) {

            if (mStrServicePackage.equalsIgnoreCase("1312050001")) // merchant
            {
                mStrAccountStatus = "Inactive";
                mImgViewProfilePic.setEnabled(true);
                mTextViewAccountHolderName.setEnabled(false);
                mTextViewAccountNumber.setEnabled(false);
                mImgBtnQucikPay.setEnabled(false);
                mImgBtnQr.setEnabled(false);
                mBtnDefultAccount.setEnabled(false);
                mBtnLinkAccount.setVisibility(View.GONE);
                mBtnDiscountCard.setVisibility(View.GONE);
                mImgBtnReload.setEnabled(false);
                mGridView.setEnabled(false);
            }
            else
                {
                mStrAccountStatus = "Inactive";
                mImgViewProfilePic.setEnabled(true);
                mTextViewAccountHolderName.setEnabled(false);
                mTextViewAccountNumber.setEnabled(false);
                mImgBtnQucikPay.setEnabled(false);
                mImgBtnQr.setEnabled(false);
                mBtnDefultAccount.setEnabled(false);
                mBtnLinkAccount.setEnabled(false);
                mBtnDiscountCard.setEnabled(false);
                mImgBtnReload.setEnabled(false);
                mGridView.setEnabled(false);
            }
        } else if (strCheckAccountActiveStatus.equalsIgnoreCase("L")) {
            if (mStrServicePackage.equalsIgnoreCase("1312050001")) // merchant
            {
                mStrAccountStatus = "Locked";
                mImgViewProfilePic.setEnabled(false);
                mTextViewAccountHolderName.setEnabled(false);
                mTextViewAccountNumber.setEnabled(false);
                mImgBtnQucikPay.setEnabled(false);
                mImgBtnQr.setEnabled(false);
                mBtnDefultAccount.setEnabled(false);
                mBtnLinkAccount.setVisibility(View.GONE);
                mBtnDiscountCard.setVisibility(View.GONE);
                mImgBtnReload.setEnabled(false);
                mGridView.setEnabled(false);
            }
           else {
                mStrAccountStatus = "Locked";
                mImgViewProfilePic.setEnabled(false);
                mTextViewAccountHolderName.setEnabled(false);
                mTextViewAccountNumber.setEnabled(false);
                mImgBtnQucikPay.setEnabled(false);
                mImgBtnQr.setEnabled(false);
                mBtnDefultAccount.setEnabled(false);
                mBtnLinkAccount.setEnabled(false);
                mBtnDiscountCard.setEnabled(false);
                mImgBtnReload.setEnabled(false);
                mGridView.setEnabled(false);
            }

            //####################### Show Dialog ####################
            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
            myAlert.setMessage("Your Account is locked. Please contact with QPay.");
            myAlert.setNegativeButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = myAlert.create();
            alertDialog.show();
        } else {
            mImgViewProfilePic.setEnabled(false);
            mTextViewAccountHolderName.setEnabled(false);
            mTextViewAccountNumber.setEnabled(false);
            mImgBtnQr.setEnabled(false);
            mBtnDefultAccount.setEnabled(false);
            mBtnLinkAccount.setEnabled(false);
            mBtnDiscountCard.setEnabled(false);
            mImgBtnReload.setEnabled(false);
            mGridView.setEnabled(false);

            //####################### Show Dialog ####################
            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
            myAlert.setTitle("Account Status");
            myAlert.setMessage(strCheckAccountActiveStatus);
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

        mStrAccountTypeAndStaus = mStrAccountType + " | " + mStrAccountStatus;
        mTextViewAccountTypeAndStatus.setText(mStrAccountTypeAndStaus);
      // txtviewNavBannerInfo.setText(GlobalData.getStrAccountNumber()+"||"+mStrAccountTypeAndStaus);
        //========================Get Account list========================
        String strEAccountNO = null;
        try {
            strEAccountNO = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
      // String strGetList= CheckDeviceActiveStatus.GetAccountList(strEAccountNO,GlobalData.getStrUserId(),GlobalData.getStrDeviceId());
        String strGetList=GlobalData.getStrGetAccountList();
        GlobalData.setStrGetAccountList(strGetList);  // Account list

        if(!strGetList.isEmpty() && strGetList!=null  ) {
            if (strGetList.contains("*")) {

                StringTokenizer tokensTransaction = new StringTokenizer(strGetList, "*");
                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                    while (tokensTransaction.hasMoreElements()) {
                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                        String strName = tokensAllTransactionFinal.nextToken();
                        String strIsdefult = tokensAllTransactionFinal.nextToken();
                        String strAccountNo = tokensAllTransactionFinal.nextToken();
                        String strBankAccounStatus = tokensAllTransactionFinal.nextToken();
                        String strIsVerified = tokensAllTransactionFinal.nextToken();
                        String strBankID= tokensAllTransactionFinal.nextToken();
                        String strBalance=tokensAllTransactionFinal.nextToken();
                        String strQRcode=tokensAllTransactionFinal.nextToken();

                        arrayAccountName.add(strName);
                        arrayAccountDefuly.add(strIsdefult);
                        arrayBankAccountNo.add(strAccountNo);
                        arrayBankAccountStatus.add(strBankAccounStatus);
                        arrayBankAccountIsVerified.add(strIsVerified);

                        if (strIsdefult.equalsIgnoreCase("Y")) {
                            GlobalData.setStrWallet(strAccountNo);
                            strSourceWallet = strAccountNo;
                            strDefultWalletBalance=strBalance;
                            strDefultWalletQRCode=strQRcode;
                            GlobalData.setStrAccountTypename(strName);
                            mBtnDefultAccount.setText("  "+GlobalData.getStrAccountTypename()+"\n   (Default )");
                        }
                    }
                }
            }
            else
                {

            }
        }
        //===================================================

        try {
            if (mStrServicePackage.equalsIgnoreCase("1312050001")) { // merchant
//
//                String strEncWallet = encryption.Encrypt(GlobalData.getStrWallet(), GlobalData.getStrSessionId());
//                String strEncryptedPIN=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//                String strVoucherBalance = GetBalance.getBalance(GlobalData.getStrUserId(),strEncryptedPIN, strEncWallet, GlobalData.getStrDeviceId());
//                mStrQrCodeContent = GetQrCodeContent.getQrCode(strEncWallet, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
//                mTextViewBalanceAmount.setText(strVoucherBalance);
                mStrQrCodeContent = strDefultWalletQRCode;
                mTextViewBalanceAmount.setText(strDefultWalletBalance);
            }
            if (mStrServicePackage.equalsIgnoreCase("1205190003")) { // customer
//
//                String strEncWallet = encryption.Encrypt(GlobalData.getStrWallet(), GlobalData.getStrSessionId());
//                String strEncryptedPIN=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//                String strVoucherBalance = GetBalance.getBalance(GlobalData.getStrUserId(), strEncryptedPIN, strEncWallet, GlobalData.getStrDeviceId());
//
//                mStrQrCodeContent = GetQrCodeContent.getQrCode(strEncWallet, GlobalData.getStrUserId(), GlobalData.getStrDeviceId());
//                mTextViewBalanceAmount.setText(strVoucherBalance);
                mStrQrCodeContent = strDefultWalletQRCode;
                mTextViewBalanceAmount.setText(strDefultWalletBalance);
            }
            GlobalData.setStrQrCodeContent(mStrQrCodeContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
        initServiceMenu();
    }

    private  void intToolBar()
    {

            //================ Toolbar =================
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.txtviewNavdrawerBannername);
            navUsername.setText(GlobalData.getStrAccountHolderName());
            TextView navUserinfo = (TextView) headerView.findViewById(R.id.textViewnavDrawerBannerIfno);
            navUserinfo.setText(GlobalData.getStrAccountNumber());
            TextView navUserinfo2 = (TextView) headerView.findViewById(R.id.textViewnavDrawerBannerIfno2);
            navUserinfo2.setText(mStrAccountTypeAndStaus);

//            msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
//            mshareprefImageEditor = msharePrefImage.edit();
//            String strBitMap = msharePrefImage.getString("imagePreferance", "");
//            // decodeBase64(strBitMap);
//            if (strBitMap != null && !strBitMap.isEmpty()) {
//                final Bitmap selectedImage = decodeBase64(strBitMap);
//                ImageView navImage = (ImageView) headerView.findViewById(R.id.imgViewProfilePicnavHeader);
//                navImage.setImageBitmap(selectedImage);
//            }

        //==================== get identification info for profile picture============================

                    ImageView navImage = (ImageView) headerView.findViewById(R.id.imgViewProfilePicnavHeader);
        if(GlobalData.getStrProPicBitmap()!=null ) {
                    navImage.setImageBitmap(GlobalData.getStrProPicBitmap());
        }


            //--------------------end toolbar ----------------------------

    }



    //########################## Initialize Menu ############################
    private void initServiceMenu() {
        //#################### Merchant #######################
        //#################### Merchant #######################
        if (mStrServicePackage.equalsIgnoreCase("1312050001")) // merchant
        {
            //#################### Voucher Pay #########################
//            if (mStrMerchantRank.equalsIgnoreCase("VOUPAY")) {
//                mBtnVoucherPay.setEnabled(true);
//                mBtnCanteenPay.setEnabled(false);
//                mBtnSimBill.setEnabled(false);
                mBtnDefultAccount.setBackgroundColor(0xFFc0ecfc);
              //  mBtnLinkAccount.setBackgroundColor(0xFFFFFFF);
               // mBtnDiscountCard.setBackgroundColor(0xFFFFFFF);
//            }


            final String[] mStrArrayGridViewItemLabel = {
                    "Receive Payment",
                    "Utility Bill Pay",
                    "Recharge",
//                    "Lost and Found",
                    "Cash Advance",
                    "Fund Management",
                    "Cash IN",
                    "Shopping",
                    "Ticket Purchase",
//                    "Investment",
            "Tution Fee Payment"};
            int[] mIntGridViewItemImgId = {
                    R.drawable.icon_make_payment,
                    R.drawable.utilitybillpay,
                    R.drawable.recharge,
//                    R.drawable.recharge,
                    R.drawable.cashout,
                    R.drawable.fund_management,
                    R.drawable.cash_in,
                    R.drawable.shopping_commimngsoon_newv2,
                    R.drawable.airticketpurchage_commingsoon_newv2,
//                    R.drawable.investment_commingsoon_new,
                    R.drawable.tutionfees_commingsoon_new2
                    };
            CustomGrid gridAdapter = new CustomGrid(MainActivity.this, mStrArrayGridViewItemLabel,
                    mIntGridViewItemImgId);
            mGridView = findViewById(R.id.gridView);
            mGridView.setAdapter(gridAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mStrArrayGridViewItemLabel[+position].equals("Receive Payment")) {
                        startActivity(new Intent(MainActivity.this, MP_Through_M_C2M.class));
                        finish();
                    }

                    else if (mStrArrayGridViewItemLabel[+position].equals("Investment"))
                    {
                        startActivity(new Intent(MainActivity.this, ComingSoon.class));
                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Tution Fee Payment"))
                    {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_layout_single_button);
                        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                        TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                        textViewTitle.setText("Tution Fees");
                        textViewMessage.setText("This service will be available soon!!");
                        //textViewOperation.setText("Action");
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }
                        });
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Shopping"))
                    {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_layout_single_button);
                        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                        TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                        textViewTitle.setText("Shopping");
                        textViewMessage.setText("This service will be available soon!!");
                        //textViewOperation.setText("Action");
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }
                        });

//                        AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                        myAlert.setMessage("This service will be available soon!!");
//                        myAlert.setPositiveButton(
//                                "Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//
//                                    }
//                                });
//                        AlertDialog alertDialog = myAlert.create();
//                        alertDialog.show();

//                        startActivity(new Intent(MainActivity.this, ComingSoon.class));
//                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Utility Bill Pay"))
                    {
                        startActivity(new Intent(MainActivity.this, Utility_Bill.class));
                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Recharge"))
                    {
                            if (mStrOtpStatus.equalsIgnoreCase("valid"))
                            {
                            startActivity(new Intent(MainActivity.this, Topup.class));
                                finish();
                            }
                            else
                            {
                                // OTP Expire
                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                                myAlert.setMessage("OTP is expired. Generate a new OTP?");
                                myAlert.setPositiveButton(
                                        "Generate OTP",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                                finish();
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
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Lost and Found"))
                    {
                      //  startActivity(new Intent(MainActivity.this, LostFoundActivity.class));
                        startActivity(new Intent(MainActivity.this, ComingSoon.class));
                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Ticket Purchase"))
                    {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_layout_single_button);
                        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                        TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                        textViewTitle.setText("Ticket Purchase");
                        textViewMessage.setText("This service will be available soon!!");
                        //textViewOperation.setText("Action");
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }
                        });
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Cash Advance"))
                    {
                        startActivity(new Intent(MainActivity.this, Customer_Cash_withdrowal_Through_Agent.class));
                        finish();
                    }

                    else if (mStrArrayGridViewItemLabel[+position].equals("Fund Management"))
                    {
                        if (mStrOtpStatus.equalsIgnoreCase("valid"))
                        {
                            startActivity(new Intent(MainActivity.this, Fund_Management.class));
                            finish();
                        }
                        else
                        {
                            // OTP Expire
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                            myAlert.setMessage("OTP is expired. Generate a new OTP?");
                            myAlert.setPositiveButton(
                                    "Generate OTP",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                            finish();
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
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Cash IN"))
                    {
                        if (mStrOtpStatus.equalsIgnoreCase("valid"))
                        {
                            startActivity(new Intent(MainActivity.this, Cash_IN.class));
                            finish();
                        }
                        else
                        {
                            // OTP Expire
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                            myAlert.setMessage("OTP is expired. Generate a new OTP?");
                            myAlert.setPositiveButton(
                                    "Generate OTP",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                            finish();
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
                    }


                }

            });
        }
        //#################### Customer #######################
        //#################### Customer #######################
        else if (mStrServicePackage.equalsIgnoreCase("1205190003")) {
//            mBtnVoucherPay.setEnabled(true);
//            mBtnCanteenPay.setEnabled(true);
//            mBtnSimBill.setEnabled(true);
            mBtnDefultAccount.setBackgroundColor(0xFFc0ecfc);
            mBtnLinkAccount.setBackgroundColor(0xFFFFFFF);
            mBtnDiscountCard.setBackgroundColor(0xFFFFFFF);

            final String[] mStrArrayGridViewItemLabel = {
                    "Make Payment",
                    "Utility Bill Pay",
                    "Recharge",
//                    "Lost and Found",
                    "Cash Advance",
                    "Shopping",
                    "Ticket Purchase",
//                    "Investment",
                    "Tution Fee Payment"
            };
            int[] mIntGridViewItemImgId = {
                    R.drawable.icon_make_payment,
                    R.drawable.utilitybillpay,
                    R.drawable.recharge,
//                    R.drawable.recharge,
                    R.drawable.cashout,
                    R.drawable.shopping_commimngsoon_newv2,
                    R.drawable.airticketpurchage_commingsoon_newv2,
//                    R.drawable.investment_commingsoon_new,
                    R.drawable.tutionfees_commingsoon_new2

            };
            CustomGrid gridAdapter = new CustomGrid(MainActivity.this, mStrArrayGridViewItemLabel,
                    mIntGridViewItemImgId);
            mGridView = findViewById(R.id.gridView);
            mGridView.setAdapter(gridAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mStrArrayGridViewItemLabel[+position].equals("Make Payment"))
                    {
                        if (mStrOtpStatus.equalsIgnoreCase("valid"))
                        {
                            // OTP Valid
                            startActivity(new Intent(MainActivity.this, MP_Through_C_C2M.class));
                            finish();
                        }
                        else
                            {
                            // OTP Expire
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                            myAlert.setMessage("OTP is expired. Generate a new OTP?");
                            myAlert.setPositiveButton(
                                    "Generate OTP",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                            finish();
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
                    }

                    else if (mStrArrayGridViewItemLabel[+position].equals("Investment"))
                    {
                        startActivity(new Intent(MainActivity.this, ComingSoon.class));
                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Tution Fee Payment"))
                    {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_layout_single_button);
                        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                        TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                        textViewTitle.setText("Tution Fees");
                        textViewMessage.setText("This service will be available soon!!");
                        //textViewOperation.setText("Action");
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }
                        });
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Shopping"))
                    {

                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_layout_single_button);
                        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                        TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                        textViewTitle.setText("Shopping");
                        textViewMessage.setText("This service will be available soon!!");
                        //textViewOperation.setText("Action");
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }
                        });

//                        AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                        myAlert.setMessage("This service will be available soon!!");
//                        myAlert.setPositiveButton(
//                                "Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//
//                                    }
//                                });
//                        AlertDialog alertDialog = myAlert.create();
//                        alertDialog.show();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Utility Bill Pay"))
                    {
                        startActivity(new Intent(MainActivity.this, Utility_Bill.class));
                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Recharge"))
                    {
                        if (mStrOtpStatus.equalsIgnoreCase("valid"))
                        {
                        startActivity(new Intent(MainActivity.this, Topup.class));
                            finish();
                        }
                        else
                        {
                            // OTP Expire
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                            myAlert.setMessage("OTP is expired. Generate a new OTP?");
                            myAlert.setPositiveButton(
                                    "Generate OTP",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                            finish();
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
                    }

                    else if (mStrArrayGridViewItemLabel[+position].equals("Lost and Found"))
                    {
                       // startActivity(new Intent(MainActivity.this, LostFoundActivity.class));
                        startActivity(new Intent(MainActivity.this, ComingSoon.class));
                        finish();
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Ticket Purchase"))
                    {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_layout_single_button);
                        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                        TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                        textViewTitle.setText("Ticket Purchase");
                        textViewMessage.setText("This service will be available soon!!");
                        //textViewOperation.setText("Action");
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }
                        });
                    }
                    else if (mStrArrayGridViewItemLabel[+position].equals("Cash Advance"))
                    {
                        if (mStrOtpStatus.equalsIgnoreCase("valid"))
                        {
                        startActivity(new Intent(MainActivity.this, Customer_Cash_Withdrowal.class));
                        finish();
                        }
                        else
                        {
                            // OTP Expire
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                            myAlert.setMessage("OTP is expired. Generate a new OTP?");
                            myAlert.setPositiveButton(
                                    "Generate OTP",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                                            finish();
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
                    }

                }

            });
        }
    }

    //########################## All Click Events ############################
    @Override
    public void onClick(View v) {

        if (v == mImgViewProfilePic) {

            try {


//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);

                showPictureDialog();

            }
            catch (Exception e)
            {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();

            }

//            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//            photoPickerIntent.setType("image/*");
//            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
          //  startActivity(new Intent(MainActivity.this, KYC_Old.class));
        }
        if (v == mImgBtnQr) {
            startActivity(new Intent(MainActivity.this, QrScan.class));
            finish();
        }

        if (v == mBtnDefultAccount) {
            try {

                String strEncryptDefultWallet = encryption.Encrypt(GlobalData.getStrWallet(), GlobalData.getStrSessionId());
                String strEncryptedPIN=encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String strPrimaryWalletBalance = GetBalance.getBalance(GlobalData.getStrUserId(), strEncryptedPIN, strEncryptDefultWallet, GlobalData.getStrDeviceId());

                String strQrCodeContentPrimaryWallet = GetQrCodeContent.getQrCode(strEncryptDefultWallet, GlobalData.getStrUserId(),GlobalData.getStrDeviceId());
                mTextViewBalanceAmount.setText(strPrimaryWalletBalance);
                mBtnDefultAccount.setText(    GlobalData.getStrAccountTypename()+"\n   (Default )");

                GlobalData.setStrQrCodeContent(strQrCodeContentPrimaryWallet);
                mBtnDefultAccount.setBackgroundColor(0xFFc0ecfc);
                mBtnLinkAccount.setBackgroundColor(0xFFFFFFF);
                mBtnDiscountCard.setBackgroundColor(0xFFFFFFF);

            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }

        if (v == mBtnLinkAccount)
        {
            try
            {
                mBtnDefultAccount.setBackgroundColor(0xFFFFFFF);
                mBtnLinkAccount.setBackgroundColor(0xFFc0ecfc);
                mBtnDiscountCard.setBackgroundColor(0xFFFFFFF);
                startActivity(new Intent(MainActivity.this, Link_Account_New_V2.class));
                finish();

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

        }

        if (v == mBtnDiscountCard) {
            try {
                mBtnDefultAccount.setBackgroundColor(0xFFFFFFF);
                mBtnLinkAccount.setBackgroundColor(0xFFFFFFF);
                mBtnDiscountCard.setBackgroundColor(0xFFc0ecfc);
//                startActivity(new Intent(MainActivity.this, ComingSoon.class));
//                finish();
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_layout_single_button);
                TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
                TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
                TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                textViewTitle.setText("Discount Card");
                textViewMessage.setText("This service will be available soon!!");
                //textViewOperation.setText("Action");
                dialog.show();

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                        dialog.cancel();

                    }
                });
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }

        }

        if (v == mImgBtnReload) {
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            anim.setAnimationListener(this);
            mImgBtnReload.startAnimation(anim);
            loadLastTransactionDetails();
        }




    }

    //########################## Last Transaction ############################
    private void loadLastTransactionDetails() {
        try {
            mTextViewLastTransaction.setText("");
            mStrLastTransaction = "";
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String strCurrentDate = sdf.format(new Date());
            mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
            mStrEncryptPin = encryption.Encrypt(GlobalData.getStrPin(),  GlobalData.getStrSessionId());
            mStrEncryptFromDate = encryption.Encrypt(strCurrentDate,  GlobalData.getStrSessionId());
            mStrEncryptToDate = encryption.Encrypt(strCurrentDate,  GlobalData.getStrSessionId());
            mStrEncryptAccessCode = encryption.Encrypt("all",  GlobalData.getStrSessionId());
            mStrEncryptParameter = encryption.Encrypt("1",  GlobalData.getStrSessionId());

            String strLastTransaction = GetLastTransaction.getLastTransaction(
                    mStrEncryptAccountNumber, mStrEncryptPin, mStrEncryptFromDate,
                    mStrEncryptToDate, mStrEncryptAccessCode, mStrEncryptParameter, GlobalData.getStrUserId(),GlobalData.getStrDeviceId());

            if (strLastTransaction != null && !strLastTransaction.isEmpty()) {
                if (strLastTransaction.equalsIgnoreCase("No data found"))
                {
                    mStrLastTransaction = "No Transaction Info Found.";
                }
                else if (strLastTransaction.equalsIgnoreCase("anyType{}"))
                {
                    mStrLastTransaction = "No Transaction Info Found.";
                }
                else
                {
                    mStrLastTransaction = strLastTransaction;
                }
            }
            else
                {
                mStrLastTransaction = "No Transaction Info Found.";
            }
            mTextViewLastTransaction.setText(mStrLastTransaction);
            mTextViewLastTransaction.invalidate();


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    //############################## Check OS ################################
    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    //=============== Back Button ====================
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {

            drawer.closeDrawer(GravityCompat.START);


        } else
            {
            super.onBackPressed();
        }
    }

    //=============== Settings ====================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionRewardpoint) {

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_layout_single_button);
            TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitleOne);
            TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessageOne);
            TextView textViewOperation = (TextView) dialog.findViewById(R.id.textViewOperationOne);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            textViewTitle.setText("Reward Point");
            textViewMessage.setText("This service will be available soon!!");
            //textViewOperation.setText("Action");
            dialog.show();

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                    dialog.cancel();

                }
            });


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //=============== Clear Data From Global ====================
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
     //   GlobalData.setStrEncryptAccountNumber("");
        GlobalData.setStrWallet("");
        GlobalData.setStrAccountHolderName("");
        GlobalData.setStrSessionId("");
        GlobalData.setStrQrCodeContent("");

    }

    //########################## Internet ############################
    //########################## Internet ############################
    //########################## Internet ############################
    private void checkInternet() {
        if (isNetworkConnected()) {
            //                enableUiComponents();

            //------------------------------
//            mProgressDialog = ProgressDialog.show(MainActivity.this, null, "Login...", false, true);
//
//            mProgressDialog.setCancelable(true);
//
//            Thread t = new Thread(new Runnable() {
//
//                @Override
//                public void run() {

                  //  loadLastTransactionDetails();

                    //-------------------last transaction-------------
             if (GlobalData.getStrlastTransaction() != null && !GlobalData.getStrlastTransaction().isEmpty()) {
                if (GlobalData.getStrlastTransaction().equalsIgnoreCase("No data found"))
                {
                    mStrLastTransaction = "No Transaction Info Found.";
                }
                else if (GlobalData.getStrlastTransaction().equalsIgnoreCase("anyType{}"))
                {
                    mStrLastTransaction = "No Transaction Info Found.";
                }
                else if (GlobalData.getStrlastTransaction()==null)
                {
                    mStrLastTransaction = "No Transaction Info Found.";
                }
                else
                {
                    mStrLastTransaction = GlobalData.getStrlastTransaction();
                }
            }
            else
            {
                mStrLastTransaction = "No Transaction Info Found.";
            }
            mTextViewLastTransaction.setText(mStrLastTransaction);
            mTextViewLastTransaction.invalidate();
            //--------------------------------------

//            mTextViewLastTransaction.setText(GlobalData.getStrlastTransaction()); // last transaction
//            mTextViewLastTransaction.invalidate();
            try
            {
                mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                mStrEncryptPin= encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                String strNotificationBroadcast= GetAllFavoriteList.GetNotification_BroadCast(mStrEncryptAccountNumber,mStrEncryptPin,GlobalData.getStrUserId(),GlobalData.getStrDeviceId());

                if(!strNotificationBroadcast.equalsIgnoreCase("No data found"))
                        {
                            if(!strNotificationBroadcast.equalsIgnoreCase("anyType{}")) {
                                StringTokenizer tokensTransaction = new StringTokenizer(strNotificationBroadcast, "&");
                                intCount=tokensTransaction.countTokens();
                                for (int j = 0; j <= tokensTransaction.countTokens(); j++) {
                                    while (tokensTransaction.hasMoreElements()) {


                                        StringTokenizer tokensAllTransactionFinal = new StringTokenizer(tokensTransaction.nextToken(), ";");
                                        // listArray = new ArrayList<DATAMODEL>(tokensAllTransactionFinal.countTokens());
                                        String strDate = tokensAllTransactionFinal.nextToken();
                                        String strCatagories = tokensAllTransactionFinal.nextToken();
                                        String strText = tokensAllTransactionFinal.nextToken();
                                        int NOTIFICATION_ID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                                        sendNotificationnew(strText, strCatagories, strDate,NOTIFICATION_ID);
                                    }
                                }
                            }
                        }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            try {
//                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                                    mProgressDialog.dismiss();
//
//
//
//
//                                }
//                            } catch (Exception e) {
//                                // TODO: handle exception
//                            }
//                        }
//                    });
//                }
//            });
//
//            t.start();
            //------------------------------



        } else {
            //                disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
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

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    //########################## Animation Reload  ############################
    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    //########################## QR Quick Scan  ############################
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message,
                                          CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    Log.v("Tracing  Value: ", "Error!!!");
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }




//    //########################## Customer OTP  ############################
//    public void sendCustomerOtp(String strEncryptMerchantWallet,
//                                String strEncryptMerchantPin,
//                                String strEncryptCustomerWallet) {
//        String METHOD_NAME = "QPAY_GenerateOTP_Res";
//        String SOAP_ACTION = "http://www.bdmitech.com/m2b/QPAY_GenerateOTP_Res";
//        SoapObject request = new SoapObject(GlobalData.getStrNamespace(), METHOD_NAME);
//        // Declare the version of the SOAP request
//        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//
////        QPAY_GenerateOTP_Res
////        MerchantNo:
////        MerchantPIN:
////        CustomerNo:
////        strMasterKey:
//        PropertyInfo encryptMerchantWallet = new PropertyInfo();
//        encryptMerchantWallet.setName("E_MerchantNo");
//        encryptMerchantWallet.setValue(strEncryptMerchantWallet);
//        encryptMerchantWallet.setType(String.class);
//        request.addProperty(encryptMerchantWallet);
//
//        PropertyInfo encryptMerchantPin = new PropertyInfo();
//        encryptMerchantPin.setName("E_MerchantPIN");
//        encryptMerchantPin.setValue(strEncryptMerchantPin);
//        encryptMerchantPin.setType(String.class);
//        request.addProperty(encryptMerchantPin);
//
//        PropertyInfo encryptCustomerWallet = new PropertyInfo();
//        encryptCustomerWallet.setName("E_CustomerNo");
//        encryptCustomerWallet.setValue(strEncryptCustomerWallet);
//        encryptCustomerWallet.setType(String.class);
//        request.addProperty(encryptCustomerWallet);
//
//        PropertyInfo encryptMasterKey = new PropertyInfo();
//        encryptMasterKey.setName("UserID");
//        encryptMasterKey.setValue(GlobalData.getStrUserId());
//        encryptMasterKey.setType(String.class);
//        request.addProperty(encryptMasterKey);
//
//
//        PropertyInfo masterKey = new PropertyInfo();
//        masterKey.setName("DeviceID");
//        masterKey.setValue(GlobalData.getStrDeviceId());
//        masterKey.setType(String.class);
//        request.addProperty(masterKey);
//
//
//        envelope.dotNet = true;
//
//        envelope.setOutputSoapObject(request);
//        Log.v("myApp:", request.toString());
//        envelope.implicitTypes = true;
//        Object objSendCustomerOtp = null;
//        String strSendCustomerWalletResponse = "";
//
//        try {
//            HttpTransportSE androidHttpTransport = new HttpTransportSE(GlobalData.getStrUrl(), 1000);
//            androidHttpTransport.call(SOAP_ACTION, envelope);
//            objSendCustomerOtp = envelope.getResponse();
//            strSendCustomerWalletResponse = objSendCustomerOtp.toString();
////            mStrServerResponse = strSendCustomerWalletResponse;
//        } catch (Exception exception) {
////            mStrServerResponse = strSendCustomerWalletResponse;
//        }
//    }





    //########################## Navigation On Click ############################
    //########################## Navigation On Click ############################
    //########################## Navigation On Click ############################
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        txtviewNavBannername.setText(GlobalData.getStrAccountHolderName());
//         txtviewNavBannerInfo.setText(GlobalData.getStrAccountNumber()+"||"+mStrAccountTypeAndStaus);

        int id = item.getItemId();
        //Menu menu = navigationView.getMenu();
      //  if (mStrServicePackage.equalsIgnoreCase("1312050001")) // merchant
       // {
            if (id == R.id.nav_change_pin) {
                startActivity(new Intent(MainActivity.this, ChangePin.class));
                finish();
            } else if (id == R.id.nav_otp) {
                startActivity(new Intent(MainActivity.this, GenerateOtp.class));
                finish();
            } else if (id == R.id.nav_transaction_history) {
                startActivity(new Intent(MainActivity.this, TransactionHistory.class));
                finish();
            } else if (id == R.id.nav_KYC) {

                startActivity(new Intent(MainActivity.this, Kyc.class));
                finish();
            } else if (id == R.id.nav_logout) {
                clearDataFromGlobal();
                //mSharedPreferencsLoginEditor.putBoolean("rememberCredentials", false);
                mSharedPreferencsLogin = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                mSharedPreferencsLoginEditor = mSharedPreferencsLogin.edit();
                mSharedPreferencsLoginEditor.putString("loguotcredentials", "");
                mSharedPreferencsLoginEditor.putString("pin_submission", "");
                mSharedPreferencsLoginEditor.putString("userid", "");
                mSharedPreferencsLoginEditor.commit();
//            mSharedPreferencsLoginEditor.clear();
//            mSharedPreferencsLoginEditor.commit();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            } else if (id == R.id.nav_OfflinePIN) {

                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                myAlert.setMessage("Do you want to save Offline PIN ?");
                myAlert.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                dialog.cancel();
                                //--------------save into share prefarence
                                msharePrefOfflinePin = getSharedPreferences("OfflinePINPrefs", MODE_PRIVATE);
                                mshareprefOfflinePINEditor = msharePrefOfflinePin.edit();
                                //SharedPreferences.Editor editor = myPrefrence.edit();
                                //  editor.putString("namePreferance", itemNAme);
                                mshareprefOfflinePINEditor.putString("offlinepin", GlobalData.getStrPin());
                                mshareprefOfflinePINEditor.putString("offlineuser", GlobalData.getStrUserId());

                                mshareprefOfflinePINEditor.commit();
                                // startActivity(new Intent(QrScan.this, GenerateOtp.class));
                            }
                        });
                myAlert.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = myAlert.create();
                alertDialog.show();


//            simpleSwitch = (Switch) findViewById(R.id.Offline_pin_switch);
//            if (simpleSwitch.isChecked())
//            {
//                //--------------save into share prefarence
//                msharePrefOfflinePin = getSharedPreferences("OfflinePINPrefs", MODE_PRIVATE);
//                mshareprefOfflinePINEditor = msharePrefOfflinePin.edit();
//                //SharedPreferences.Editor editor = myPrefrence.edit();
//                //  editor.putString("namePreferance", itemNAme);
//                mshareprefOfflinePINEditor.putString("offlinepin", GlobalData.getStrPin());
//                mshareprefOfflinePINEditor.commit();
//
//            }

                // startActivity(new Intent(MainActivity.this, ComingSoon.class));
            }
//        else if (id == R.id.nav_OfflineQR)
//        {
//            startActivity(new Intent(MainActivity.this, ComingSoon.class));
//        }
            else if (id == R.id.nav_TermAndCondition) {
                startActivity(new Intent(MainActivity.this, Terms_And_Condition.class));
                finish();
            } else if (id == R.id.nav_About) {
                startActivity(new Intent(MainActivity.this, About_QucikPay.class));
                finish();
            } else if (id == R.id.nav_transactionPIN) {
                startActivity(new Intent(MainActivity.this, Set_Transaction_PIN.class));
                finish();
            } else if (id == R.id.nav_DeviceLocation) {
                startActivity(new Intent(MainActivity.this, Merchant_Location.class));
                finish();
            }
//        else if(id == R.id.Offline_pin_switch)
//        {
//            if (simpleSwitch.isChecked())
//            {
//                //--------------save into share prefarence
//                msharePrefOfflinePin = getSharedPreferences("OfflinePINPrefs", MODE_PRIVATE);
//                mshareprefOfflinePINEditor = msharePrefOfflinePin.edit();
//                //SharedPreferences.Editor editor = myPrefrence.edit();
//                //  editor.putString("namePreferance", itemNAme);
//                mshareprefOfflinePINEditor.putString("offlinepin", GlobalData.getStrPin());
//                mshareprefOfflinePINEditor.commit();
//
//            }
//        }


            /////////////////////////offline pin ////////////////////////
            //SwitchCompat drawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.switch_item).getActionView();
//        simpleSwitch = (Switch) findViewById(R.id.Offline_pin_switch);
//        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // do stuff
//                } else {
//                    // do other stuff
//                }
//            }
//        });
            // simpleSwitch = (Switch) findViewById(R.id.Offline_pin_switch);

            /////////////////////////////////////////////////////////
       // }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(GlobalData.getStrPackage().equalsIgnoreCase("1205190003")) // customer
        {
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem pinMenuItem = menu.findItem(R.id.actionRewardpoint);
            pinMenuItem.setTitle("0\n Reward Point");
        }
        else
        {

        }
        return true;
    }

    // check hobe pore
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        if (resultCode == this.RESULT_CANCELED) {
//            return;
//        }
//        else if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                //Encrypt QR Code
////            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
////            if (scanningResult != null) {
////                if (scanningResult.getContents() != null) {
////                    String mStrQrCodeContents = scanningResult.getContents();
//
////
////                    String mStrQrCodeContents = intent.getStringExtra("SCAN_RESULT");
////                    int intIndex01 = mStrQrCodeContents.indexOf(":");
////                    if (intIndex01 == -1) {
////                        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
////                        mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
////                        mAlertDialogBuilder.setNegativeButton(
////                                "OK",
////                                new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int id) {
////                                        dialog.cancel();
////                                    }
////                                });
////                        AlertDialog mAlertDialog = mAlertDialogBuilder.create();
////                        mAlertDialog.show();
////                    } else {
////                        String[] arrayQrCodeContents = mStrQrCodeContents.split(":");
////                        mStrBankBin = arrayQrCodeContents[0]; //003 bank bin for Merchant
////                        String strEncryptQrCode = arrayQrCodeContents[1]; //78HnkqX7uY2ebpIuhaThVDQJy83m2/WM0EROhKDzgd1P0M0dZlhQuSmUQmYDJb6pmE4RKhHjdHltEMD0FJ16mjPu3kRxwSeVkuz7hT32BCjpUOtwFvy6ygDDParmnhwN/zzcUOd7Kr4bPagq4EPnfA==
////
////                        if (mStrBankBin.equalsIgnoreCase("006"))  //For Huawei
////                        {
////                            mStrUrlForQrCode = GlobalData.getStrUrl();
////                            mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
////                        } else if (mStrBankBin.equalsIgnoreCase("002")) //For QPay
////                        {
////                            mStrUrlForQrCode = GlobalData.getStrUrl();
////                            mStrMethodName = "QPAY_Get_Account_BY_QR_CARD";
////                        }
////
////                        String strEncryptAccountNumberAndMasterKeyByQrCode = GetMasterKeyAndAccountNumber.getEncryptAccountNumberAndMasterKeyByQrCode(mStrQrCodeContents);
////                        if (!strEncryptAccountNumberAndMasterKeyByQrCode.equalsIgnoreCase("")) {
////                            int intIndex = strEncryptAccountNumberAndMasterKeyByQrCode.indexOf("*");
////                            if (intIndex == -1) {
////                                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
////                                mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
////                                mAlertDialogBuilder.setNegativeButton(
////                                        "OK",
////                                        new DialogInterface.OnClickListener() {
////                                            public void onClick(DialogInterface dialog, int id) {
////                                                dialog.cancel();
////                                            }
////                                        });
////                                AlertDialog mAlertDialog = mAlertDialogBuilder.create();
////                                mAlertDialog.show();
////                            } else {
////                                String[] parts = strEncryptAccountNumberAndMasterKeyByQrCode.split("\\*");
////                                strEncryptDestinationAccountNumberFromQr = parts[0];
////                                // strDestinationMasterKeyFromQr = parts[1];
////                                String strMerchantEncryptName = parts[1];
////
////
////                                //############################## Source Merchant ##############################
////                                //############################## Source Merchant ##############################
////                                if (mStrServicePackage.equalsIgnoreCase("1312050001")) {
////                                    try {
////                                        //################ Destination Account Number ####################
////                                        mStrDestinationWallet = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, GlobalData.getStrSessionId());
////
////                                        String strAccountTypeCode = mStrDestinationWallet.substring(3, 5);
////                                        //####################### Destination Merchant(M2M-FM) ############################
////                                        //####################### Destination Merchant(M2M-FM) ############################
////                                        //####################### Destination Merchant(M2M-FM) ############################
////                                        if (strAccountTypeCode.equalsIgnoreCase("11")) {
////                                            //################ Merchant Name ####################
////                                            //mStrDestinationName = GetMerchantName.getMerchantName(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
////                                            mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//////                                        String[] parts01 = mStrDestinationName.split("\\*");
//////                                        String strName = parts01[0];
//////                                        final String strType = parts01[1];
////
////
////                                            //################ Show Dialog ####################
////                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                            myAlert.setTitle("MERCHANT INFO");
////                                            myAlert.setMessage("MERCHANT NAME" + "\n" + mStrDestinationName + "\n" + "MERCHANT WALLET" + "\n" + mStrDestinationWallet);
////                                            myAlert.setPositiveButton(
////                                                    "OK",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
//////                                                        if (strType.equalsIgnoreCase(mStrMerchantRank)) {
//////                                                            GlobalData.setStrSourceWallet(mStrSourceWallet);
//////                                                            GlobalData.setStrDestinationWallet(mStrDestinationWallet);
//////                                                            GlobalData.setStrDestinationWalletName(mStrDestinationName);
//////                                                            startActivity(new Intent(MainActivity.this, FM_M2M_Quick.class));
//////                                                        } else {
////                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                                            myAlert.setMessage("Account Type not match. Please scan correct account type.");
////                                                            myAlert.setNegativeButton(
////                                                                    "OK",
////                                                                    new DialogInterface.OnClickListener() {
////                                                                        public void onClick(DialogInterface dialog, int id) {
////                                                                            dialog.cancel();
////                                                                        }
////                                                                    });
////                                                            AlertDialog alertDialog = myAlert.create();
////                                                            alertDialog.show();
//////                                                        }
////
////                                                        }
////                                                    });
////                                            myAlert.setNegativeButton(
////                                                    "CANCEL",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
////                                                            dialog.cancel();
////                                                        }
////                                                    });
////                                            AlertDialog alertDialog = myAlert.create();
////                                            alertDialog.show();
////
////                                        }
////                                        //####################### Destination Customer(M2C-MP_Through_C_C2M_AddToFav) ############################
////                                        //####################### Destination Customer(M2C-MP_Through_C_C2M_AddToFav) ############################
////                                        //####################### Destination Customer(M2C-MP_Through_C_C2M_AddToFav) ############################
////                                        if (strAccountTypeCode.equalsIgnoreCase("12")) {
//////                                        String strDestinationRank = mStrDestinationWallet.substring(13, 14);
//////                                        if (mStrMerchantRank.equalsIgnoreCase("VOUPAY")) {
//////                                            if (strDestinationRank.equalsIgnoreCase("1")) {
////                                            //################ Customer Name ####################
////                                            //mStrDestinationName = GetMerchantName.getMerchantName(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
////                                            mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
////                                            //################ Show Dialog ####################
////                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                            myAlert.setTitle("CUSTOMER INFO");
////                                            myAlert.setMessage("CUSTOMER NAME" + "\n" + mStrDestinationName + "\n" + "CUSTOMER WALLET" + "\n" + mStrDestinationWallet);
////                                            myAlert.setPositiveButton(
////                                                    "OK",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
////                                                            try {
////                                                                GlobalData.setStrSourceWallet(strSourceWallet);
////                                                                GlobalData.setStrDestinationWallet(mStrDestinationWallet);
////                                                                GlobalData.setStrDestinationWalletName(mStrDestinationName);
////                                                                mStrEncryptMerchatWalletForOtp = encryptionDecryption.Encrypt(strSourceWallet, GlobalData.getStrSessionId());
////                                                                mStrCustomerWalletByQrCode = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, GlobalData.getStrSessionId());
////                                                                mStrEncryptCustomerWalletByQrCode = encryptionDecryption.Encrypt(mStrCustomerWalletByQrCode, GlobalData.getStrSessionId());
////                                                                String strEncryptPIN = encryptionDecryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
////                                                                startActivity(new Intent(MainActivity.this, MP_Through_M_C2M_Quick.class));
////                                                                finish();
////                                                                ///-----------------####################################----------------------------------
//////                                                                    Intent i = new Intent(QPayMenuNew.this, MP_Through_M_C2M_Quick.class);
//////                                                                    Bundle bundle = new Bundle();
//////                                                                    bundle.putString("source", mStrSourceWallet.toString());
//////                                                                    bundle.putString("destination", mStrDestinationAccountNumberFromQr);
//////                                                                    i.putExtras(bundle);
//////                                                                    startActivity(i);
////
////                                                                ///-------------------=====##############################==================-------------------
////                                                                sendCustomerOtp(
////                                                                        mStrEncryptMerchatWalletForOtp,
////                                                                        strEncryptPIN,
////                                                                        mStrEncryptCustomerWalletByQrCode);
////                                                            } catch (Exception e) {
////                                                                e.printStackTrace();
////                                                            }
////
////                                                        }
////                                                    });
////                                            myAlert.setNegativeButton(
////                                                    "CANCEL",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
////                                                            dialog.cancel();
////                                                        }
////                                                    });
////                                            AlertDialog alertDialog = myAlert.create();
////                                            alertDialog.show();
//////                                            }
//////                                            else {
//////                                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//////                                                myAlert.setMessage("Account Type not match. Please scan correct account type.");
//////                                                myAlert.setNegativeButton(
//////                                                        "OK",
//////                                                        new DialogInterface.OnClickListener() {
//////                                                            public void onClick(DialogInterface dialog, int id) {
//////                                                                dialog.cancel();
//////                                                            }
//////                                                        });
//////                                                AlertDialog alertDialog = myAlert.create();
//////                                                alertDialog.show();
//////                                            }
//////                                        }
////
////
////                                        }
////
////                                    } catch (Exception exception) {
////                                        exception.printStackTrace();
////                                    }
////                                }
////
////
////                                //############################## Source Customer ##############################
////                                if (mStrServicePackage.equalsIgnoreCase("1205190003")) {
////                                    try {
////                                        //################ Destination Account Number ####################
////                                        // mStrDestinationWallet = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
////                                        mStrDestinationWallet = encryptionDecryption.Decrypt(strEncryptDestinationAccountNumberFromQr, GlobalData.getStrSessionId());
////                                        String strAccountTypeCode = mStrDestinationWallet.substring(3, 5);
////                                        //####################### Destination Merchant(C2M-MP_Through_C_C2M_AddToFav) ############################
////                                        //####################### Destination Merchant(C2M-MP_Through_C_C2M_AddToFav) ############################
////                                        //####################### Destination Merchant(C2M-MP_Through_C_C2M_AddToFav) ############################
////                                        if (strAccountTypeCode.equalsIgnoreCase("11")) {
////                                            //################ Merchant Name ####################
////                                            //mStrDestinationName = GetMerchantName.getMerchantName(strEncryptDestinationAccountNumberFromQr, strDestinationMasterKeyFromQr);
////                                            mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
//////
////
////                                            //################ Show Dialog ####################
////                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                            myAlert.setTitle("MERCHANT INFO");
////                                            myAlert.setMessage("MERCHANT NAME" + "\n" + mStrDestinationName + "\n" + "MERCHANT WALLET" + "\n" + mStrDestinationWallet);
////                                            myAlert.setPositiveButton(
////                                                    "OK",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
////                                                            GlobalData.setStrSourceWallet(strSourceWallet);
////
////                                                            GlobalData.setStrDestinationWallet(mStrDestinationWallet);
////                                                            GlobalData.setStrDestinationWalletName(mStrDestinationName);
////                                                            startActivity(new Intent(MainActivity.this, MP_Through_C_C2M_Quick.class));
////                                                            finish();
////                                                        }
////                                                    });
////                                            myAlert.setNegativeButton(
////                                                    "CANCEL",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
////                                                            dialog.cancel();
////                                                        }
////                                                    });
////                                            AlertDialog alertDialog = myAlert.create();
////                                            alertDialog.show();
////                                        }
////
////
////                                        //####################### Destination if Customer ############################
////                                        //####################### Destination if Customer ############################
////                                        if (strAccountTypeCode.equalsIgnoreCase("12")) {
////
////                                            mStrDestinationName = encryptionDecryption.Decrypt(strMerchantEncryptName, GlobalData.getStrSessionId());
////                                            //################ Show Dialog ####################
////                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                            myAlert.setTitle("BENEFICIARY INFO");
////                                            myAlert.setMessage("BENEFICIARY NAME" + "\n" + mStrDestinationName + "\n" + "BENEFICIARY WALLET" + "\n" + mStrDestinationWallet);
////                                            myAlert.setPositiveButton(
////                                                    "OK",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
//////                                                            GlobalData.setStrSourceWallet(mStrSourceWallet);
//////                                                            GlobalData.setStrDestinationWallet(mStrDestinationWallet);
//////                                                            GlobalData.setStrDestinationWalletName(mStrDestinationName);
//////                                                            startActivity(new Intent(MainActivity.this, FT_C2C_Quick.class));
////
////                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                                            myAlert.setMessage("Account Type not match. Please scan correct account type.");
////                                                            myAlert.setNegativeButton(
////                                                                    "OK",
////                                                                    new DialogInterface.OnClickListener() {
////                                                                        public void onClick(DialogInterface dialog, int id) {
////                                                                            dialog.cancel();
////                                                                        }
////                                                                    });
////                                                            AlertDialog alertDialog = myAlert.create();
////                                                            alertDialog.show();
////
////                                                        }
////                                                    });
////                                            myAlert.setNegativeButton(
////                                                    "CANCEL",
////                                                    new DialogInterface.OnClickListener() {
////                                                        public void onClick(DialogInterface dialog, int id) {
////                                                            dialog.cancel();
////                                                        }
////                                                    });
////                                            AlertDialog alertDialog = myAlert.create();
////                                            alertDialog.show();
//////
////                                        }
////
////
////                                    } catch (Exception exception) {
////                                        exception.printStackTrace();
////                                    }
////                                }
////
////
////                            }
////
////
////                        } else {
////                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
////                            mAlertDialogBuilder.setMessage("QR Scan fail. Please try again.");
////                            mAlertDialogBuilder.setNegativeButton(
////                                    "OK",
////                                    new DialogInterface.OnClickListener() {
////                                        public void onClick(DialogInterface dialog, int id) {
////                                            dialog.cancel();
////                                        }
////                                    });
////                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
////                            mAlertDialog.show();
////                        }
////                    }
//////                }
//////                else
//////                {
//////                    Toast.makeText(this, "Nothing scanned", Toast.LENGTH_SHORT).show();
//////                }
//////            }
//////            else
//////            {
//////                Toast.makeText(this, "Nothing scanned", Toast.LENGTH_SHORT).show();
//////            }
////
//           }
//        }
////        else if(requestCode==1)
////        {
////            try {
////
////                Uri imageUri = intent.getData();
////
//////                if (!Uri.EMPTY.equals(imageUri)) {
////                    if (imageUri != null && !imageUri.equals(Uri.EMPTY)) {
////                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
////                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////
////
////                        mImgViewProfilePic.setImageBitmap(selectedImage);
////
////
////                        //============save to server===========
////                        Bitmap immage = ((BitmapDrawable) mImgViewProfilePic.getDrawable()).getBitmap();
////
////                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////                        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
////                        byte[] imageByte = baos.toByteArray();
////                        String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
////                        Date date = new Date();
////                        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
////                        String formattedDate = dateFormat.format(date);
////                        String Imagefilename = formattedDate + ".PNG";
////
////                        String strUploadImageresponse = InsertKyc.GetKycImage(Imagefilename, imageEncoded);
////                        if (strUploadImageresponse.equalsIgnoreCase("000")) {
////
////                            try {
////                                mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
////                                String mStrEncryptPIN = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
////                                String mStrEncryptIdentificationId = encryption.Encrypt("140824000000000001", GlobalData.getStrSessionId());
////                                String mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
////                                String mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
////                                String mstrParameter = encryption.Encrypt("U", GlobalData.getStrSessionId());
////                                String mstrPictureID = encryption.Encrypt(Imagefilename, GlobalData.getStrSessionId());
////
////
////                                mStrServerResponse = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
////                                        mStrEncryptRemark, mstrParameter, mstrPictureID);
////                            }
////                            catch (Exception e)
////                            {}
////
////
////                            if (mStrServerResponse.equalsIgnoreCase("Update")) {
////                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                myAlert.setMessage("Image upload Successfully");
////                                myAlert.setNegativeButton(
////                                        "OK",
////                                        new DialogInterface.OnClickListener() {
////                                            public void onClick(DialogInterface dialog, int id) {
////                                                dialog.cancel();
//////
////                                                startActivity(new Intent(MainActivity.this, MainActivity.class));
////                                                finish();
////                                            }
////                                        });
////                                AlertDialog alertDialog = myAlert.create();
////                                alertDialog.show();
////                            } else {
////                                //####################### Show Dialog ####################
////                                //####################### Show Dialog ####################
////                                //####################### Show Dialog ####################
////                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                                myAlert.setMessage(mStrServerResponse);
////                                myAlert.setNegativeButton(
////                                        "OK",
////                                        new DialogInterface.OnClickListener() {
////                                            public void onClick(DialogInterface dialog, int id) {
////                                                dialog.cancel();
////                                            }
////                                        });
////                                AlertDialog alertDialog = myAlert.create();
////                                alertDialog.show();
////                            }
////                        }
////                        else
////                        {
////                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
////                            myAlert.setMessage("Please Try Again!");
////                            myAlert.setNegativeButton(
////                                    "OK",
////                                    new DialogInterface.OnClickListener() {
////                                        public void onClick(DialogInterface dialog, int id) {
////                                            dialog.cancel();
////                                        }
////                                    });
////                            AlertDialog alertDialog = myAlert.create();
////                            alertDialog.show();
////                        }
////
////                        //--------------save into share prefarence
//////                        msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
//////                        mshareprefImageEditor = msharePrefImage.edit();
//////
//////                        mshareprefImageEditor.putString("imagePreferance", encodeTobase64(selectedImage));
//////                        mshareprefImageEditor.commit();
////                        intToolBar();
////                    } else {
////                        startActivity(new Intent(MainActivity.this, MainActivity.class));
////                        finish();
////
////                    }
////
////            }
////            catch (FileNotFoundException e)
////            {
////                startActivity(new Intent(MainActivity.this, MainActivity.class));
////                finish();
////
////
////            }
////        }
//
//        else if (requestCode == GALLERY) {
//            if (intent != null) {
//                Uri contentURI = intent.getData();
//                try {
//                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    // String path = saveImage(bitmap);
//                    Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//
//                    Bitmap bitresized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//                    //============save to server===========
//                  //  Bitmap immage = ((BitmapDrawable) mImgViewProfilePic.getDrawable()).getBitmap();
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitresized.compress(Bitmap.CompressFormat.JPEG, 90, baos);
//                    byte[] imageByte = baos.toByteArray();
//                    String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
//
////                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
////                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//                    Date date = new Date();
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//                    String formattedDate = dateFormat.format(date);
//                   final String Imagefilename = formattedDate + ".PNG";
//
//                    String strUploadImageresponse = InsertKyc.GetKycImage(Imagefilename, imageEncoded);
//                    if (strUploadImageresponse.equalsIgnoreCase("000")) {
//
//                        try {
//                            mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
//                            String mStrEncryptPIN = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//                            String mStrEncryptIdentificationId = encryption.Encrypt("140824000000000001", GlobalData.getStrSessionId());
//                            String mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
//                            String mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
//                            String mstrParameter = encryption.Encrypt("U", GlobalData.getStrSessionId());
//                            String mstrPictureID = encryption.Encrypt(Imagefilename, GlobalData.getStrSessionId());
//                            String mstrPictureIDback = encryption.Encrypt("", GlobalData.getStrSessionId());
//
//                            mStrServerResponse = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
//                                    mStrEncryptRemark, mstrParameter, mstrPictureID,mstrPictureIDback);
//                        }
//                        catch (Exception e)
//                        {}
//
//
//                        if (mStrServerResponse.equalsIgnoreCase("Update")) {
//                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                            myAlert.setMessage("Image upload Successfully");
//                            myAlert.setNegativeButton(
//                                    "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
////
//                                            mImgViewProfilePic.setImageBitmap(bitmap);
//                                            GlobalData.setStrIndetificationInfoPicture(Imagefilename);
//                                            GlobalData.setStrProPicBitmap(bitmap);
//                                            intToolBar();
////                                            startActivity(new Intent(MainActivity.this, MainActivity.class));
////                                            finish();
//                                        }
//                                    });
//                            AlertDialog alertDialog = myAlert.create();
//                            alertDialog.show();
//                        } else {
//                            //####################### Show Dialog ####################
//                            //####################### Show Dialog ####################
//                            //####################### Show Dialog ####################
//                            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                            myAlert.setMessage(mStrServerResponse);
//                            myAlert.setNegativeButton(
//                                    "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alertDialog = myAlert.create();
//                            alertDialog.show();
//                        }
//                    }
//                    else
//                    {
//                        AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                        myAlert.setMessage("Please Try Again!");
//                        myAlert.setNegativeButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alertDialog = myAlert.create();
//                        alertDialog.show();
//                    }
//                    intToolBar();
//
//                    //=====================
//
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
//                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
//        else if (requestCode == CAMERA) {
//
//            onCaptureImageResult(intent);
//            final Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
//
//
//            //============save to server===========
//            Bitmap immage = ((BitmapDrawable) mImgViewProfilePic.getDrawable()).getBitmap();
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] imageByte = baos.toByteArray();
//            String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//            String formattedDate = dateFormat.format(date);
//          final  String Imagefilename = formattedDate + ".PNG";
//
//            String strUploadImageresponse = InsertKyc.GetKycImage(Imagefilename, imageEncoded);
//            if (strUploadImageresponse.equalsIgnoreCase("000")) {
//
//                try {
//                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
//                    String mStrEncryptPIN = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
//                    String mStrEncryptIdentificationId = encryption.Encrypt("140824000000000001", GlobalData.getStrSessionId());
//                    String mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
//                    String mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
//                    String mstrParameter = encryption.Encrypt("U", GlobalData.getStrSessionId());
//                    String mstrPictureID = encryption.Encrypt(Imagefilename, GlobalData.getStrSessionId());
//                    String mstrPictureIDback = encryption.Encrypt("", GlobalData.getStrSessionId());
//
//                    mStrServerResponse = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
//                            mStrEncryptRemark, mstrParameter, mstrPictureID,mstrPictureIDback);
//                }
//                catch (Exception e)
//                {}
//
//
//                if (mStrServerResponse.equalsIgnoreCase("Update")) {
//                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                    myAlert.setMessage("Image upload Successfully");
//                    myAlert.setNegativeButton(
//                            "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
////
//                                    mImgViewProfilePic.setImageBitmap(thumbnail);
//                                    GlobalData.setStrIndetificationInfoPicture(Imagefilename);
//                                    GlobalData.setStrProPicBitmap(thumbnail);
//                                    intToolBar();
////                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
////                                    finish();
//                                }
//                            });
//                    AlertDialog alertDialog = myAlert.create();
//                    alertDialog.show();
//                } else {
//                    //####################### Show Dialog ####################
//                    //####################### Show Dialog ####################
//                    //####################### Show Dialog ####################
//                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                    myAlert.setMessage(mStrServerResponse);
//                    myAlert.setNegativeButton(
//                            "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alertDialog = myAlert.create();
//                    alertDialog.show();
//                }
//            }
//            else
//            {
//                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
//                myAlert.setMessage("Please Try Again!");
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
//
//            intToolBar();
//            //=====================
//
//            // saveImage(thumbnail);
//
//            //--------------save into share prefarence
////            msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
////            mshareprefImageEditor = msharePrefImage.edit();
////
////            mshareprefImageEditor.putString("imagePreferance", encodeTobase64(thumbnail));
////            mshareprefImageEditor.commit();
//            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


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

    //----------------------------
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

    public void choosePhotoFromGallary()
    {
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
//        mImgViewProfilePic.setImageBitmap(thumbnail);
//    }

    //--------------------------------------
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
            myAlert.setMessage("Do you want to exit this application?");
            myAlert.setNeutralButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
//                            Process.killProcess(Process.myPid());
//                            finish();
//                            System.exit(0);
//                            finish();
                            moveTaskToBack(true);

                        }
                    });

            myAlert.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
//                                                            enableUiComponentAfterClick();
                            //startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    });
            AlertDialog alertDialog = myAlert.create();
            alertDialog.show();

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    //--------image crop----------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }
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
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
           // mImgView.setImageBitmap(bitmap);
            //=====================================


            //  Bitmap bitresized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            //============save to server===========
            //  Bitmap immage = ((BitmapDrawable) mImgViewProfilePic.getDrawable()).getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageByte = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(imageByte, Base64.DEFAULT);

//                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            String formattedDate = dateFormat.format(date);
            final String Imagefilename = formattedDate + ".PNG";

            String strUploadImageresponse = InsertKyc.GetKycImage(Imagefilename, imageEncoded);
            if (strUploadImageresponse.equalsIgnoreCase("000")) {

                try {
                    mStrEncryptAccountNumber = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                    String mStrEncryptPIN = encryption.Encrypt(GlobalData.getStrPin(), GlobalData.getStrSessionId());
                    String mStrEncryptIdentificationId = encryption.Encrypt("140824000000000001", GlobalData.getStrSessionId());
                    String mStrEncryptIdentificationNumber = encryption.Encrypt("", GlobalData.getStrSessionId());
                    String mStrEncryptRemark = encryption.Encrypt("", GlobalData.getStrSessionId());
                    String mstrParameter = encryption.Encrypt("U", GlobalData.getStrSessionId());
                    String mstrPictureID = encryption.Encrypt(Imagefilename, GlobalData.getStrSessionId());
                    String mstrPictureIDback = encryption.Encrypt("", GlobalData.getStrSessionId());

                    mStrServerResponse = InsertKyc.insertIndentificationInfo(mStrEncryptAccountNumber, mStrEncryptPIN, mStrEncryptIdentificationId, mStrEncryptIdentificationNumber,
                            mStrEncryptRemark, mstrParameter, mstrPictureID,mstrPictureIDback);
                }
                catch (Exception e)
                {}


                if (mStrServerResponse.equalsIgnoreCase("Update")) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
                    myAlert.setMessage("Image upload Successfully");
                    myAlert.setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
//
                                    mImgViewProfilePic.setImageBitmap(bitmap);
                                    GlobalData.setStrIndetificationInfoPicture(Imagefilename);
                                    GlobalData.setStrProPicBitmap(bitmap);
                                    intToolBar();
//                                            startActivity(new Intent(MainActivity.this, MainActivity.class));
//                                            finish();
                                }
                            });
                    AlertDialog alertDialog = myAlert.create();
                    alertDialog.show();
                } else {
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    //####################### Show Dialog ####################
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
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
            else
            {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(MainActivity.this);
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
            intToolBar();

            //=====================

            //--------------save into share prefarence
//                    msharePrefImage = getSharedPreferences("imagePrefs", MODE_PRIVATE);
//                    mshareprefImageEditor = msharePrefImage.edit();
//
//                    mshareprefImageEditor.putString("imagePreferance", encodeTobase64(bitmap));
//                    mshareprefImageEditor.commit();
            //intToolBar();

            //======================================
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
        }


    }
    //--------image crop----------------------------------------------------
}
