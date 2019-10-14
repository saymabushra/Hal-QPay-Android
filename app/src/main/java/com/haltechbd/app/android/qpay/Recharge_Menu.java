package com.haltechbd.app.android.qpay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.haltechbd.app.android.qpay.utils.CustomGrid;

/**
 * Created by bushra on 6/3/2018.
 */

public class Recharge_Menu extends AppCompatActivity implements View.OnClickListener {

    private GridView mGridView;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_menu);
        checkOs();
        initUI();
    }

    private void initUI() {

        mGridView = findViewById(R.id.gridViewRechargemenu);
        initServiceMenu();

        checkInternet();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initServiceMenu()
    {
        final String[] mStrArrayGridViewItemLabel = {
                "Air Ticket",
                "Bus Ticket"
              };
        int[] mIntGridViewItemImgId = {
                R.drawable.buyairticket,
                R.drawable.buybusticket

        };
        CustomGrid gridAdapter = new CustomGrid(Recharge_Menu.this, mStrArrayGridViewItemLabel,
                mIntGridViewItemImgId);
        mGridView = findViewById(R.id.gridViewRechargemenu);
        mGridView.setAdapter(gridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mStrArrayGridViewItemLabel[+position].equals("Air Ticket")) {
                    startActivity(new Intent(Recharge_Menu.this, ComingSoon.class));
                    finish();
                }

                else if (mStrArrayGridViewItemLabel[+position].equals("Bus Ticket"))
                {
                    startActivity(new Intent(Recharge_Menu.this, ComingSoon.class));
                    finish();
                }



            }

        });
    }

    @Override
    public void onClick(View v)
    {

    }

    private void checkInternet() {

        if (isNetworkConnected()) {
            enableUiComponents();
        } else {
            disableUiComponents();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(Recharge_Menu.this);
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
//        mSpinnerWallet.setEnabled(true);
//        mEditTextMerchantAccount.setEnabled(true);
//        mEditTextAmount.setEnabled(true);
//        mEditTextOtp.setEnabled(true);
//        mEditTextReference.setEnabled(true);
//        mBtnSubmit.setEnabled(true);
    }

    private void disableUiComponents() {
//        mSpinnerWallet.setEnabled(false);
//        mEditTextMerchantAccount.setEnabled(false);
//        mEditTextAmount.setEnabled(false);
//        mEditTextOtp.setEnabled(false);
//        mEditTextReference.setEnabled(false);
//        mBtnSubmit.setEnabled(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void checkOs() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Recharge_Menu.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(ChangePin.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Recharge_Menu.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
