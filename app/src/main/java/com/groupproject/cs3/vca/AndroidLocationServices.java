package com.groupproject.cs3.vca;

/**
 * Created by Gaming_PC on 21/11/2017.
 */

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupproject.cs3.vca.gps_information.gpsCoordinates;
import com.groupproject.cs3.vca.information.gpsInformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AndroidLocationServices extends Service {

    WakeLock wakeLock;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com");
    private FirebaseAuth mAuth;
    String address;
    String userID;
    private LocationManager locationManager;

    public AndroidLocationServices() {
        // TODO Auto-generated constructor stub
        isGPSEnabled = false;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

        Log.e("Google", "Service Created");
        mAuth = FirebaseAuth.getInstance();


    }
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("Google", "Service Started");
        Toast.makeText(getApplicationContext(), "Service Started",
                Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                15*10,0, listener);
        return Service.START_STICKY;
    }

    public boolean isGPSEnabled;
    protected void updateLocationOnDB(FirebaseUser user, double lat, double lon) {
        if (user != null) {


                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String ad = "Location Saved";
                Toast.makeText(this, ad,
                        Toast.LENGTH_SHORT).show();
                userID = mAuth.getUid();
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                gpsInformation gpsInformation = new gpsInformation(address);
                gpsCoordinates gpsCoordinates = new gpsCoordinates(lat, lon);
                myRef.child("users").child(userID).child("GPS Location").child("Address").setValue(gpsInformation);
                myRef.child("users").child(userID).child("GPS Location").child("Coordinates").setValue(gpsCoordinates);

        }
    }
    protected LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            Log.e("Google", "Location Changed");

            if (location == null){
                Toast.makeText(getApplicationContext
                                (), "Location Not Found",
                        Toast.LENGTH_SHORT).show();
                return;

            }else{
                Toast.makeText(getApplicationContext(), "Updating Location....",
                        Toast.LENGTH_SHORT).show();


            }

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {


                try {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    double lat =  location.getLatitude();
                    double lon = location.getLongitude();
                    updateLocationOnDB(currentUser, lat , lon);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "In Exp "+e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "onProviderDisabled",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "onProviderEnabled",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "onStatusChanged",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy",Toast.LENGTH_LONG).show();
        wakeLock.release();

    }



}