package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lebsmart.EWSourcesFragments.EWSources;
import com.example.lebsmart.LostFoundAnnouncements.LostFoundAnnouncement;
import com.example.lebsmart.R;
import com.example.lebsmart.ApartmentsFragments.ApartmentsFragment;
import com.example.lebsmart.ReportCheckTheftFragments.TheftFragment;
import com.example.lebsmart.MeetingsFragments.MeetingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainScreenActivity extends AppCompatActivity {

    Toolbar toolbar;

    private DrawerLayout drawerLayout;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.theft:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new TheftFragment()).commit();
                        break;
                    case R.id.meetings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MeetingsFragment()).commit();
                        break;
                    case R.id.apartments:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ApartmentsFragment()).commit();
                        break;
                    case R.id.elec_water_src:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new EWSources()).commit();
                        break;
                    case R.id.lost_found_announcements:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new LostFoundAnnouncement()).commit();
                        break;
                    case R.id.rateUs:
                        Toast.makeText(MainScreenActivity.this, "Rate us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.feedback:
                        Toast.makeText(MainScreenActivity.this, "Give Feedback", Toast.LENGTH_SHORT).show();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ApartmentsFragment()).commit();
            navigationView.setCheckedItem(R.id.apartments);
        }


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}