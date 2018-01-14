package com.groupproject.cs3.vca;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ModeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    String userID;
    String ADMIN = "ADMIN";
    String NORMAL = "NORMAL";
    private int caseNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar_mode);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        final ImageButton userImg = findViewById(R.id.userImg);
        final ImageButton adminimg = findViewById(R.id.adminImg);

        buttonTapEffect.buttonEffect(userImg);
        buttonTapEffect.buttonEffect(adminimg);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = mAuth.getUid();
        if (user != null) {
            DatabaseReference refType = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com/users/"+userID+"/type");
            refType.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String type = (String) dataSnapshot.getValue();
                    if (Objects.equals(type, ADMIN)) {
                        caseNo = 1;
                    } else if (Objects.equals(type, NORMAL)) {
                        caseNo = 2;
                    }
                    switch (caseNo) {
                        case 1:
                            userImg.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View v)
                                {
                                    Intent userMenuIntent = new Intent(ModeActivity.this.getApplicationContext(), userMenuActivity.class);
                                    ModeActivity.this.startActivity(userMenuIntent);
                                }
                            });

                            adminimg.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View v)
                                {
                                    Intent adminMenuIntent = new Intent(ModeActivity.this.getApplicationContext(), MapsActivity.class);
                                    ModeActivity.this.startActivity(adminMenuIntent);
                                }
                            });
                            break;
                        case 2:
                            userImg.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View v)
                                {
                                    Intent userMenuIntent = new Intent(ModeActivity.this.getApplicationContext(), userMenuActivity.class);
                                    ModeActivity.this.startActivity(userMenuIntent);
                                }
                            });
                            adminimg.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View v)
                                {
                                    Toast.makeText(ModeActivity.this.getApplicationContext(), "Not Authorised", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mAuth.getInstance().signOut();
            Intent signOutIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(signOutIntent);
            Toast.makeText(getApplicationContext(), "User Sign Out: Success", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Logging out ...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.gameNav) {
            finish();
            Intent gameIntent = new Intent(getApplicationContext(), gameActivity.class);
            startActivity(gameIntent);
        } else if (id == R.id.weatherNav) {
            finish();
            Intent weatherIntent = new Intent(getApplicationContext(), weatherActivity.class);
            startActivity(weatherIntent);

        } else if (id == R.id.ledNav) {
            finish();
            Intent ledIntent = new Intent(getApplicationContext(), connectActivity.class);
            startActivity(ledIntent);
        } else if (id == R.id.heatingNav) {

        } else if (id == R.id.takeAwayNav) {
            finish();
            Intent foodIntent = new Intent(getApplicationContext(), takeAwayActivity.class);
            startActivity(foodIntent);
        } else if (id == R.id.taxiNav) {
            finish();
            Intent taxiIntent = new Intent(getApplicationContext(), taxiActivity.class);
            startActivity(taxiIntent);
        } else if (id == R.id.signOutNav) {
            mAuth.getInstance().signOut();
            finish();
            Intent signOutIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(signOutIntent);
            Toast.makeText(getApplicationContext(), "User Sign Out: Success", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Logging out ...", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.homeNav) {
            finish();
            Intent homeIntent = new Intent(getApplicationContext(), ModeActivity.class);
            startActivity(homeIntent);
        }else if (id == R.id.calendarNav) {
            finish();
            Intent calendarIntent = new Intent(getApplicationContext(), calendarActivity.class);
            startActivity(calendarIntent);
        }else if (id == R.id.shopNav) {
            finish();
            Intent shopIntent = new Intent(getApplicationContext(), shoppingActivity.class);
            startActivity(shopIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
