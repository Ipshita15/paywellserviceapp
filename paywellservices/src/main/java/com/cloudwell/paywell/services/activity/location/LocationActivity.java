package com.cloudwell.paywell.services.activity.location;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class LocationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    public static double PLACE_LATITUDE = 0;
    public static double PLACE_LONGITUDE = 0;

    private MapView mapView;
    private GoogleMap map;

    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mAppHandler = new AppHandler(this);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        // Sets the map type to be "hybrid"
//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//        // Gets to GoogleMap from the MapView and does initialization stuff
//        //map = mapView.getMapAsync();
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.setMyLocationEnabled(true);
//
//        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//        try {
//            MapsInitializer.initialize(LocationActivity.this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.gps__location, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        PLACE_LATITUDE = (location.getLatitude());
        PLACE_LONGITUDE = (location.getLongitude());

        if(!mAppHandler.getLatitude().equalsIgnoreCase(String.valueOf(PLACE_LATITUDE))) {
            mAppHandler.setLatitude(String.valueOf(PLACE_LATITUDE));
        }
        if(!mAppHandler.getLongitude().equalsIgnoreCase(String.valueOf(PLACE_LONGITUDE))) {
            mAppHandler.setLongitude(String.valueOf(PLACE_LONGITUDE));
        }
        //showMapView();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected void showMapView() {
        // Updates the location and zoom of the MapView
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(PLACE_LATITUDE, PLACE_LONGITUDE), 10);
//        map.animateCamera(cameraUpdate);
        //private GoogleMap googleMap = ((MapView) rootView.findViewById(R.id.YOURMAPID)).getMap();
    }

    protected void showMap() {

        boolean installedMaps = false;

        // CHECK IF GOOGLE MAPS IS INSTALLED
        PackageManager pkManager = getPackageManager();
        try {
            @SuppressWarnings("unused")
            PackageInfo pkInfo = pkManager.getPackageInfo("com.google.android.apps.maps", 0);
            installedMaps = true;
        } catch (Exception e) {
            e.printStackTrace();
            installedMaps = false;
        }

        // SHOW THE MAP USING CO-ORDINATES FROM THE CHECKIN
        if (installedMaps == true) {
            String geoCode = "geo:0,0?q=" + PLACE_LATITUDE + ","
                    + PLACE_LONGITUDE + "(" + "" + ")";
            Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(geoCode));
            startActivity(sendLocationToMap);
        } else if (installedMaps == false) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    LocationActivity.this);

            // SET THE ICON
            alertDialogBuilder.setIcon(R.drawable.ic_location);

            // SET THE TITLE
            alertDialogBuilder.setTitle("Google Maps Not Found");

            // SET THE MESSAGE
            alertDialogBuilder
                    .setMessage("Google map not installed")
                    .setCancelable(false)
                    .setNeutralButton("Got It",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                }
                            });

            // CREATE THE ALERT DIALOG
            AlertDialog alertDialog = alertDialogBuilder.create();

            // SHOW THE ALERT DIALOG
            alertDialog.show();
        }
    }
}