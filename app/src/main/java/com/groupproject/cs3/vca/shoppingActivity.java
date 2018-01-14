package com.groupproject.cs3.vca;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class shoppingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private String userID;
    private ListView list;
    ArrayList<String> shopList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<String> urlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar_shopping);

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


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();


        final EditText getShop = findViewById(R.id.getTAShop);
        final EditText gettaShop = findViewById(R.id.taShopUrl);
        Button saveTA = findViewById(R.id.saveShop);

        list = findViewById(R.id.listviewShop);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview, shopList);
        list.setAdapter(adapter);

        saveTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myFood = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com");
                String nameNo = getShop.getText().toString();
                final String urlNo = gettaShop.getText().toString();
                myFood.child("users").child(userID).child("Shop").child(nameNo).setValue(urlNo);
                shopList.clear();
                urlList.clear();;
                DatabaseReference myRef = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com/users/" + userID + "/Shop");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            if (key != null) {
                                String value = dataSnapshot.child(key).getValue().toString();
                                urlList.add(value);
                                shopList.add(key);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                getShop.setText("");
                gettaShop.setText("");
            }
        });

        DatabaseReference myRef = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com/users/" + userID + "/Shop");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    if (key != null) {
                        String value = dataSnapshot.child(key).getValue().toString();
                        urlList.add(value);
                        shopList.add(key);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = urlList.get(position);

                final ProgressDialog progDailog = ProgressDialog.show(shoppingActivity.this, "Loading","Please wait...", true);
                progDailog.setCancelable(false);
                final WebView myWebView = new WebView(shoppingActivity.this);
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.getSettings().setLoadWithOverviewMode(true);
                myWebView.getSettings().setUseWideViewPort(true);
                myWebView.setWebViewClient(new WebViewClient(){

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent intent = new Intent(getApplicationContext(), webViewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                        return true;

                    }
                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        progDailog.dismiss();
                    }
                });

                myWebView.loadUrl("http://"+url);
               /* WebView mWebView;
                mWebView = new WebView(shoppingActivity.this);
                mWebView.setWebViewClient(new WebViewClient()
                {
                    // Override URL
                    public boolean shouldOverrideUrlLoading(WebView view, String url)
                    {
                        Intent intent = new Intent(getApplicationContext(), webViewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                        return true;
                    }
                });*/

            }
        });
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

