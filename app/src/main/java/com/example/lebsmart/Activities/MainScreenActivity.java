package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lebsmart.About.EditProfileFragment;
import com.example.lebsmart.About.ListOfResidentsFragment;
import com.example.lebsmart.About.PrivacyPolicyFragment;
import com.example.lebsmart.About.TermsNConditionsFragment;
import com.example.lebsmart.BestServiceProviders.BSPFragment;
import com.example.lebsmart.CommitteeDR.CommitteeDRsFragment;
import com.example.lebsmart.Database.FirebaseDatabaseMethods;
import com.example.lebsmart.EWSourcesFragments.EWSources;
import com.example.lebsmart.FireDetection.FireDetectionFragment;
import com.example.lebsmart.LostFoundAnnouncements.LostFoundAnnouncementFragment;
import com.example.lebsmart.Others.AddBuildingFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.ApartmentsFragments.ApartmentsFragment;
import com.example.lebsmart.About.RateUsFragment;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.RandomFragments.MyProfileFragment;
import com.example.lebsmart.ReportProblemsFragments.ProblemsFragment;
import com.example.lebsmart.Settings.SettingsFragment;
import com.example.lebsmart.TheftsFragments.TheftFragment;
import com.example.lebsmart.MeetingsFragments.MeetingsFragment;
import com.example.lebsmart.Weather.WeatherFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

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

        // if the signed in user is the developers account, show the add building item in drawer menu
        // else, it is kept hidden
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("w1bTnJ8UP5PJyxpy4WaLF76oz9y2")) {
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.add_building).setVisible(true);
            }
        }

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
                                new ApartmentsFragment(), "Apartments").commit();
                        break;
                    case R.id.elec_water_src:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new EWSources()).commit();
                        break;
                    case R.id.lost_found_announcements:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new LostFoundAnnouncementFragment()).commit();
                        break;
                    case R.id.problems:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProblemsFragment()).commit();
                        break;
                    case R.id.service_providers:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new BSPFragment()).commit();
                        break;
                    case R.id.committee_decisions:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new CommitteeDRsFragment()).commit();
                        break;
                    case R.id.rateUs:
                        //Toast.makeText(MainScreenActivity.this, "Rate us", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new RateUsFragment()).commit();
                        break;
                        /*
                    case R.id.feedback:
                        Toast.makeText(MainScreenActivity.this, "Give Feedback", Toast.LENGTH_SHORT).show();
                        break;*/
                    case R.id.privacyPolicy:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new PrivacyPolicyFragment()).commit();
                        break;
                    case R.id.termsNConditions:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new TermsNConditionsFragment()).commit();
                        break;
                    case R.id.logout:
                        logout();
                        break;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new EditProfileFragment()).commit();
                        break;
                    case R.id.fire_detection:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FireDetectionFragment()).commit();
                        break;
                    case R.id.my_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MyProfileFragment()).commit();
                        break;
                    case R.id.add_building:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AddBuildingFragment()).commit();
                        break;
                    case R.id.residentsList:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ListOfResidentsFragment()).commit();
                        break;
                    case R.id.weather:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new WeatherFragment()).commit();
                        break;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SettingsFragment()).commit();
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

    public void logout() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
            Intent intent = new Intent(MainScreenActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finishAffinity();

            startActivity(intent);
            //finish();
            //finishAndRemoveTask();
        }
        else {
            Toast.makeText(MainScreenActivity.this, "You're already logged out!", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();
            LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
            Intent intent = new Intent(MainScreenActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finishAffinity();

            startActivity(intent);
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