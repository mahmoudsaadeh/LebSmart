package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
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
import com.example.lebsmart.Notifications.NotificationReceiver;
import com.example.lebsmart.Notifications.Notifications;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStreamReader;
import java.util.Calendar;

public class MainScreenActivity extends AppCompatActivity {

    Toolbar toolbar;

    private DrawerLayout drawerLayout;

    NavigationView navigationView;

    Notifications notifications;

    //Switch fireNotificationsSwitch, weatherNotificationsSwitch;
    public static String fireNotis, weatherNotis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        /*fireNotificationsSwitch = findViewById(R.id.fireNotificationsSwitch);
        weatherNotificationsSwitch = findViewById(R.id.weatherNotificationsSwitch);*/
        fireNotis = "";
        weatherNotis = "";

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

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications")
                    .child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        checkNotificationsDb();
                    }
                    else {
                        createNotificationsDb();
                    }
                    Log.i("main act", "settings");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("error", error.getMessage());
                }
            });
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

    public void checkNotificationsDb() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("fireNotis").getValue().toString().equals("off")) {
                    //fireNotificationsSwitch.setChecked(false);
                    fireNotis = "off";
                }
                else {
                    //fireNotificationsSwitch.setChecked(true);
                    fireNotis = "on";
                }

                if (snapshot.child("weatherNotis").getValue().toString().equals("off")) {
                    //weatherNotificationsSwitch.setChecked(false);
                    weatherNotis = "off";
                }
                else {
                    //weatherNotificationsSwitch.setChecked(true);
                    weatherNotis = "on";

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
            }
        });
    }

    public void createNotificationsDb() {
        // all notis are on by default
        notifications = new Notifications("on", "on");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("notis", "created in db");
                    /*fireNotificationsSwitch.setChecked(true);
                    weatherNotificationsSwitch.setChecked(true);*/
                    weatherNotis = "on";
                    fireNotis = "on";

                    //setRepeatingAlarm("Your daily forecasts", "It is 25\u00B0C today, partly cloudy. Click for more info!", 100, 1);

                }
                else {
                    Log.i("notis", "not created in db!!!");
                    /*fireNotificationsSwitch.setChecked(false);
                    weatherNotificationsSwitch.setChecked(false);*/
                    weatherNotis = "off";
                    fireNotis = "off";
                }
            }
        });
    }

    /*public void setRepeatingAlarm(String reminderTitle, String reminderContent, int requestCode, int uniqueNotificationId) {
        Intent intent = new Intent(MainScreenActivity.this, NotificationReceiver.class);
        intent.putExtra("reminderTitle", reminderTitle);
        intent.putExtra("reminderContent", reminderContent);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("uniqueNotificationId", uniqueNotificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // https://stackoverflow.com/questions/4556670/how-to-check-if-alarmmanager-already-has-an-alarm-set

        boolean alarmUp = checkIfAlarmUp(intent, requestCode);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Log.i("set alarm", "entered in mainscreen act");

        //switchState = aSwitch.isChecked();

        int repeatEveryXMinutes = 1;

        if(!alarmUp){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            calendar.set(Calendar.MINUTE, 38);
            calendar.set(Calendar.SECOND, 0);

            Time currentTime = new Time();
            currentTime.setToNow();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * repeatEveryXMinutes, pendingIntent);

            Log.i("alarm", "set successfully");
        }

        // add it in settings fragment
        //if(switchState == false){
          //  alarmManager.cancel(pendingIntent);
            //Toast.makeText(MainActivity.this, "Reminder Disabled.", Toast.LENGTH_SHORT).show();
        //}

    }*/

    /*public boolean checkIfAlarmUp(Intent intent, int requestCode) {
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), requestCode,
                intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d("alarm", "Alarm is already active");
            return true;
        }
        else {
            return false;
        }
    }*/

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