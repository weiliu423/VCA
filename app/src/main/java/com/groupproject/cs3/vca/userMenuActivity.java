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
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class userMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar_usermenu);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        Button ledBtn = findViewById(R.id.ledBtn);
        ledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent connectLed = new Intent(getApplicationContext(), connectActivity.class);
                startActivity(connectLed);
            }
        });

        Button gameBtn = findViewById(R.id.gamebtn);
        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(getApplicationContext(), gameActivity.class);
                startActivity(gameIntent);
            }
        });

        Button weatherBtn = findViewById(R.id.weatherBtn);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherIntent = new Intent(getApplicationContext(), weatherActivity.class);
                startActivity(weatherIntent);
            }
        });

        Button takeAwayBtn = findViewById(R.id.takeawayBtn);
        takeAwayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent foodIntent = new Intent(getApplicationContext(), takeAwayActivity.class);
                startActivity(foodIntent);
            }
        });
        Button calendarBtn = findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventIntent = new Intent(getApplicationContext(), calendarActivity.class);
                startActivity(eventIntent);
            }
        });
        Button taxiBtn = findViewById(R.id.taxiBtn);
        taxiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taxiIntent = new Intent(getApplicationContext(), taxiActivity.class);
                startActivity(taxiIntent);
            }
        });
        Button shopBtn = findViewById(R.id.shoppingbtn);
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopIntent = new Intent(getApplicationContext(), shoppingActivity.class);
                startActivity(shopIntent);
            }
        });
        buttonTapEffect.buttonEffect(ledBtn);
        buttonTapEffect.buttonEffect(gameBtn);
        buttonTapEffect.buttonEffect(weatherBtn);
        buttonTapEffect.buttonEffect(takeAwayBtn);
        buttonTapEffect.buttonEffect(calendarBtn);
        buttonTapEffect.buttonEffect(taxiBtn);
        buttonTapEffect.buttonEffect(shopBtn);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            FirebaseAuth.getInstance().signOut();
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
            FirebaseAuth.getInstance().signOut();
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
