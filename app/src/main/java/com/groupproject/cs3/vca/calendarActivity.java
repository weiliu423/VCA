package com.groupproject.cs3.vca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.google.android.gms.common.internal.zzbq.checkArgument;

public class calendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Calendar mCalendar;
    int thisYear, month, day;
    DatePicker datePicker;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com");
    private FirebaseAuth mAuth;
    private String userID;
    ListView mListView;
    String monthName;
    ArrayList<String> events;
    String getEvent;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar_calendar);

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
        final TextView calendarView = findViewById(R.id.dateView);
        final Button setEvent = findViewById(R.id.setEventBtn);
        datePicker = findViewById(R.id.datePicker);
        final EditText event = findViewById(R.id.eventDes);
        mListView = findViewById(R.id.eventList);

        mCalendar = Calendar.getInstance();

        datePicker.setSpinnersShown(false);
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        month = mCalendar.get(Calendar.MONTH);
        thisYear = mCalendar.get(Calendar.YEAR);

        month++;
        userID = mAuth.getUid();

        calendarView.setText(String.format("Today is : %d/%d/%d", day, month, thisYear));
        setEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getEvent = event.getText().toString();
                 if (TextUtils.isEmpty(getEvent)) {
                        event.setError("Required.");
                 } else
                    {
                      day = datePicker.getDayOfMonth();
                      thisYear = datePicker.getYear();
                      @SuppressLint("SimpleDateFormat") SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                      String monthLow = monthName.toLowerCase();
                      monthName = month_date.format(mCalendar.getTime());
                      String date = day+getDayOfMonthSuffix(day)+" "+monthLow+" "+thisYear;
                      String eventDesc = event.getText().toString();

                      myRef.child("users").child(userID).child("Event").child(date).setValue(eventDesc);
                      Toast.makeText(calendarActivity.this.getApplicationContext(), "Event Saved For "+date, Toast.LENGTH_SHORT).show();
                     }
                event.setText("");
                }


        });


        mCalendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, final int year, int month, final int dayOfMonth) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                monthName = month_date.format(mCalendar.getTime());
                final String monthLow = monthName.toLowerCase();
                DatabaseReference refType = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com/users/"+userID+"/Event/");
                refType.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dates = dayOfMonth+getDayOfMonthSuffix(dayOfMonth)+ " " + monthLow + " " + year;
                        events = new ArrayList<>();
                        events.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String eventLoad = postSnapshot.getKey();
                            if (Objects.equals(eventLoad, dates)) {
                                String eventValue =  dataSnapshot.child(eventLoad).getValue().toString();
                                events.add("Event: "+eventValue);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(calendarActivity.this, android.R.layout.simple_list_item_1, events);
                        mListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        buttonTapEffect.buttonEffect(setEvent);


    }
    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
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
