package com.groupproject.cs3.vca;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.cs3.vca.gps_information.gpsCoordinates;
import com.groupproject.cs3.vca.information.gpsInformation;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.location.LocationManager.GPS_PROVIDER;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "testinggggggggggggggggg";
    String ADMIN = "ADMIN";
    String NORMAL = "NORMAL";
    String COMPANY = "COMPANY";
    private int caseNo;
    Button signin;
    Button signup;
    private EditText mEmail;
    private EditText mPassword;
    String email;
    String password;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com");
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    String address;
    String userID;
    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        OneSheeldSdk.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signin = findViewById(R.id.signInBtn);
        this.signup = findViewById(R.id.signupbtn);
        mEmail = findViewById(R.id.userNameEditText);
        mPassword = findViewById(R.id.passwordEditText);

        this.signin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                signIn(email, password);

            }
        });
        this.signup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                    Toast.makeText(MainActivity.this.getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    Intent signUpIntent = new Intent(MainActivity.this.getApplicationContext(), signupActivity.class);
                    MainActivity.this.startActivity(signUpIntent);
            }
        });



        buttonTapEffect.buttonEffect(signin);
        buttonTapEffect.buttonEffect(signup);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    public void onStop() {
        super.onStop();
       if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mAuth.getCurrentUser();
                            InputMethodManager inputManager =
                                    (InputMethodManager) MainActivity.this.
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(
                                    MainActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            final ProgressDialog progDailog = ProgressDialog.show(MainActivity.this, "Authentication with FireBase","Please wait...", true);
                            progDailog.setCancelable(false);
                            progDailog.show();
                            updateUI(user);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    progDailog.dismiss();
                                }
                            }, 5000);
                            Toast.makeText(MainActivity.this.getApplicationContext(), "Signed In", Toast.LENGTH_SHORT).show();

                            userID = mAuth.getUid();
                            if (user != null) {
                                DatabaseReference refType = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com/users/"+userID+"/type");
                                refType.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String type = (String) dataSnapshot.getValue();
                                        if (Objects.equals(type, ADMIN) || Objects.equals(type, COMPANY)) {
                                            caseNo = 1;
                                        } else if (Objects.equals(type, NORMAL)) {
                                            caseNo = 2;
                                        }
                                        switch (caseNo) {
                                            case 1:

                                                Intent login = new Intent(MainActivity.this.getApplicationContext(), ModeActivity.class);
                                                ActivityOptions options =
                                                        ActivityOptions.makeCustomAnimation(MainActivity.this.getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
                                                startActivity(login, options.toBundle());

                                                break;
                                            case 2:
                                                  Intent myIntent = new Intent(MainActivity.this.getApplicationContext(), ModeActivity.class);
                                                ActivityOptions options2 =
                                                        ActivityOptions.makeCustomAnimation(MainActivity.this.getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
                                                  MainActivity.this.startActivity(myIntent, options2.toBundle());
                                                  startService(new Intent(MainActivity.this, AndroidLocationServices.class));
                                                  updateUI(user);

                                                break;
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }




                        } else if (!isNetworkAvailable()) {
                            toastMessage("No Internet Access!!");
                        }
                        else
                         {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed: Wrong Password or Email.",
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }
    protected void updateUI(FirebaseUser user) {
          if (user != null) {

                  DatabaseReference refType = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com/users/"+userID+"/type");
                  refType.addListenerForSingleValueEvent(new ValueEventListener() {
                      @SuppressLint("MissingPermission")
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          String type = (String) dataSnapshot.getValue();
                          if (Objects.equals(type, ADMIN) || Objects.equals(type, COMPANY)) {
                              caseNo = 1;
                          } else if (Objects.equals(type, NORMAL)) {
                              caseNo = 2;
                          }
                          switch (caseNo) {
                              case 1:

                                  break;
                              case 2:

                                  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, MainActivity.this);
                                  locationManager.requestLocationUpdates(GPS_PROVIDER, 1000, 10, MainActivity.this);
                                  Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                  if (location == null){
                                      location = locationManager.getLastKnownLocation(GPS_PROVIDER);
                                  }
                                  if (location != null) {
                                      double lat = location.getLatitude();
                                      double lon = location.getLongitude();
                                      Geocoder geocoder;
                                      List<Address> addresses = null;
                                      geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                                      try {
                                          addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                      } catch (IOException e) {
                                          e.printStackTrace();
                                      }
                                      String ad = "Location Saved";
                                      Toast.makeText(MainActivity.this, ad,
                                              Toast.LENGTH_LONG).show();
                                      userID = mAuth.getUid();
                                      address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                      gpsInformation gpsInformation = new gpsInformation(address);
                                      gpsCoordinates gpsCoordinates = new gpsCoordinates(lat, lon);

                                      myRef.child("users").child(userID).child("GPS Location").child("Address").setValue(gpsInformation);
                                      myRef.child("users").child(userID).child("GPS Location").child("Coordinates").setValue(gpsCoordinates);
                                  }
                                  break;
                          }
                      }
                      @Override
                      public void onCancelled(DatabaseError databaseError) {

                      }
                  });

              }

    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onLocationChanged(Location location) {

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
}
