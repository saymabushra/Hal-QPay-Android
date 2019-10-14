package com.haltechbd.app.android.qpay;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haltechbd.app.android.qpay.utils.EncryptionDecryption;
import com.haltechbd.app.android.qpay.utils.GetAllMerchant;
import com.haltechbd.app.android.qpay.utils.GlobalData;

import java.util.ArrayList;

public class Merchant_Location extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    LocationManager locationManager;
    String lattitude, longitude;
    private static final int REQUEST_LOCATION = 1;
    TextView mTextViewLocatoin;
    FloatingActionButton floatingActionMyLocation;
    private EncryptionDecryption encryption = new EncryptionDecryption();
    private Marker mMarkerMyPos;

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    private Handler mHandler = new Handler();
    String strEncWallet,strEncryptedLat,strEncryptLong;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //   listPoints = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkOs();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        floatingActionMyLocation = (FloatingActionButton) findViewById(R.id.menu_myloc);

        floatingActionMyLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                mMarkerMyPos.remove();
                LatLng syd = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(syd, 16.0f));
                mMarkerMyPos = mMap.addMarker(new MarkerOptions().position(syd).title("Your Location").draggable(true));


                Toast.makeText(getApplicationContext(), "Your Location", Toast.LENGTH_SHORT).show();
            }

        });


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMarkerMyPos.remove();
                mMap.clear();
//                mMarkerMyPos = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()).draggable(true));

                mMarkerMyPos = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));

                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "Use this Location?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SET", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lattitude = String.valueOf(mMarkerMyPos.getPosition().latitude);
                                longitude = String.valueOf(mMarkerMyPos.getPosition().longitude);
                                try {
                                    strEncWallet = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                                    strEncryptedLat= encryption.Encrypt(lattitude, GlobalData.getStrSessionId());
                                    strEncryptLong=encryption.Encrypt(longitude, GlobalData.getStrSessionId());;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String strSetLocation= GetAllMerchant.SelMerchantlocation(strEncWallet,strEncryptedLat,strEncryptLong);
                                if(strSetLocation.equalsIgnoreCase("Update")) {
                                    Snackbar.make(findViewById(android.R.id.content), "Success!", Snackbar.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Snackbar.make(findViewById(android.R.id.content), "Don't Save.Please try again!", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });

                snackbar.show();

            }

            @Override
            public void onError(Status status) {

            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Merchant_Location.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Merchant_Location.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Merchant_Location.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
//                mTextViewLocatoin.setText("Your current location is" + "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);
            } else if (location1 != null) {
                double lat = location1.getLatitude();
                double lon = location1.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
//                mTextViewLocatoin.setText("Your current location is" + "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);
            } else if (location2 != null) {
                double lat = location2.getLatitude();
                double lon = location2.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
//                mTextViewLocatoin.setText("Your current location is" + "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);
            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();


    }

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
                startActivity(new Intent(Merchant_Location.this, MainActivity.class));
                finish();
                return true;
            case R.id.actionRewardpoint:
//                clearDataFromGlobal();
//                Intent intent = new Intent(KycPreviewBankInfo.this, Login.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkOs() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Merchant_Location.this, MainActivity.class));
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            getLocation();
            Toast.makeText(Merchant_Location.this, "Showing New Location", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this, 5000);
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        LatLng sydney = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));
        mMarkerMyPos = mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location").draggable(true));

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Use this Location?", Snackbar.LENGTH_INDEFINITE)
                .setAction("SET", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lattitude = String.valueOf(mMarkerMyPos.getPosition().latitude);
                        longitude = String.valueOf(mMarkerMyPos.getPosition().longitude);
                        try {
                            strEncWallet = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                            strEncryptedLat= encryption.Encrypt(lattitude, GlobalData.getStrSessionId());
                            strEncryptLong=encryption.Encrypt(longitude, GlobalData.getStrSessionId());;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String strSetLocation= GetAllMerchant.SelMerchantlocation(strEncWallet,strEncryptedLat,strEncryptLong);
                        if(strSetLocation.equalsIgnoreCase("Update")) {
                            Snackbar.make(findViewById(android.R.id.content), "Success!", Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Snackbar.make(findViewById(android.R.id.content), "Don't Save.Please try again!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

        snackbar.show();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Use this Location?", Snackbar.LENGTH_INDEFINITE)
                .setAction("SET", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lattitude = String.valueOf(mMarkerMyPos.getPosition().latitude);
                        longitude = String.valueOf(mMarkerMyPos.getPosition().longitude);
                        try {
                            strEncWallet = encryption.Encrypt(GlobalData.getStrAccountNumber(), GlobalData.getStrSessionId());
                            strEncryptedLat= encryption.Encrypt(lattitude, GlobalData.getStrSessionId());
                            strEncryptLong=encryption.Encrypt(longitude, GlobalData.getStrSessionId());;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String strSetLocation= GetAllMerchant.SelMerchantlocation(strEncWallet,strEncryptedLat,strEncryptLong);
                        if(strSetLocation.equalsIgnoreCase("Update")) {
                            Snackbar.make(findViewById(android.R.id.content), "Success!", Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Snackbar.make(findViewById(android.R.id.content), "Don't Save.Please try again!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

        snackbar.show();


    }
}
