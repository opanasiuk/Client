package com.road;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alexander.road.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String ApiKey = "AIzaSyCYpA4RVDcQApm204s1YykQmUXcttEqj1A";

    public GoogleMap mMap;
    GoogleDirection gd;
    Document mDoc;
    FloatingActionButton fab;

    AlertDialog.Builder alertBuilder;
    View view;
    EditText editText1;
    EditText editText2;

    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_direction);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(
                        getLastBestLocation().getLatitude(),
                        getLastBestLocation().getLongitude()), 15));

        gd = new GoogleDirection(this);
        gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                mDoc = doc;
                mMap.addPolyline(gd.getPolyline(doc, 3, Color.BLUE));
                ArrayList<LatLng> locat = gd.getDirection(doc);
                mMap.addMarker(new MarkerOptions().position(locat.get(0))
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED)));

                mMap.addMarker(new MarkerOptions().position(locat.get(locat.size() - 1))
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED)));

                LatLngBounds bounds;
                if (locat.get(0).latitude <= locat.get(locat.size() - 1).latitude) {
                    bounds = new LatLngBounds(locat.get(0), locat.get(locat.size() - 1));
                } else {
                    bounds = new LatLngBounds(locat.get(locat.size() - 1), locat.get(0));
                }

                Log.v("Location 0", locat.get(0).toString());
                Log.v("Location last", locat.get(locat.size() - 1).toString());
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1));
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBuilder = new AlertDialog.Builder(MapsActivity.this);
                view = MapsActivity.this.getLayoutInflater().inflate(R.layout.allert, null);
                editText1 = (EditText) view.findViewById(R.id.editText3);
                editText2 = (EditText) view.findViewById(R.id.editText4);

                Button search = (Button) view.findViewById(R.id.button);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gd.setLogging(true);
                        String start = editText1.getText().toString();
                        String end = editText2.getText().toString();
                        if (start.equals("")) {
                            gd.request(new LatLng(getLastBestLocation().getLatitude(), getLastBestLocation().getLongitude()), end, GoogleDirection.MODE_DRIVING);
                        } else {
                            gd.request(start, end, GoogleDirection.MODE_DRIVING);
                        }


                    }
                });

                alertBuilder.setView(view);
                alertBuilder.create();
                alertBuilder.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public Location getLastBestLocation() {
        Location locationGPS = null;
        Location locationNet = null;
        try {
            locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
