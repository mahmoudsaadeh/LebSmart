package com.example.lebsmart.Settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.Activities.MainScreenActivity;
import com.example.lebsmart.Notifications.NotificationReceiver;
import com.example.lebsmart.Notifications.Notifications;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.ReportProblemsFragments.Problem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class SettingsFragment extends Fragment {

    ViewGroup viewGroup;

    Switch fireNotificationsSwitch, weatherNotificationsSwitch;

    Notifications notifications;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.settings_fragment, container, false);

        fireNotificationsSwitch = viewGroup.findViewById(R.id.fireNotificationsSwitch);
        weatherNotificationsSwitch = viewGroup.findViewById(R.id.weatherNotificationsSwitch);

        progressDialog = new ProgressDialog(getActivity());

        //checkNotificationsDb();

        if (MainScreenActivity.fireNotis.equals("on")) {
            fireNotificationsSwitch.setChecked(true);
        }
        else {
            fireNotificationsSwitch.setChecked(false);
        }

        if (MainScreenActivity.weatherNotis.equals("on")) {
            weatherNotificationsSwitch.setChecked(true);
        }
        else {
            weatherNotificationsSwitch.setChecked(false);
        }

        fireNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // check if repeating alarm is on
                    // if no, trigger set alarm (notification), else, do nothing
                    //Log.i("fireswitch", String.valueOf(isChecked));
                    MainScreenActivity.fireNotis = "off";
                    notifications = new Notifications("off", MainScreenActivity.weatherNotis);
                    updateDb(notifications);
                }
                else {
                    //Log.i("fireswitch", String.valueOf(isChecked));
                    MainScreenActivity.fireNotis = "on";
                    notifications = new Notifications("on", MainScreenActivity.weatherNotis);
                    updateDb(notifications);
                }
            }
        });

        weatherNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // check if repeating alarm is on
                    // if no, trigger set alarm (notification), else, do nothing
                    //Log.i("weatherswitch", String.valueOf(isChecked));
                    MainScreenActivity.weatherNotis = "off";
                    notifications = new Notifications(MainScreenActivity.fireNotis, "off");
                    updateDb(notifications);
                }
                else {
                    //Log.i("weatherswitch", String.valueOf(isChecked));
                    MainScreenActivity.weatherNotis = "on";
                    //setRepeatingAlarm("Your daily forecasts", "It is 15\u00B0C today, partly cloudy. Click for more info!", 100, 1);
                    notifications = new Notifications(MainScreenActivity.fireNotis, "on");
                    updateDb(notifications);
                }
            }
        });

        return viewGroup;
    }


    public void updateDb(Notifications notifications) {
        CommonMethods.displayLoadingScreen(progressDialog);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("updateDbNotis", "success");
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Log.i("updateDbNotis", "failed");
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        });
    }

    /*public void setRepeatingAlarm(String reminderTitle, String reminderContent, int requestCode, int uniqueNotificationId) {
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("reminderTitle", reminderTitle);
        intent.putExtra("reminderContent", reminderContent);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("uniqueNotificationId", uniqueNotificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // https://stackoverflow.com/questions/4556670/how-to-check-if-alarmmanager-already-has-an-alarm-set

        boolean alarmUp = checkIfAlarmUp(intent, requestCode);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        //switchState = aSwitch.isChecked();
        Log.i("set alarm", "entered in settings");

        int repeatEveryXMinutes = 1;

        //if(!alarmUp){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            calendar.set(Calendar.MINUTE, 31);
            calendar.set(Calendar.SECOND, 0);

            Time currentTime = new Time();
            currentTime.setToNow();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * repeatEveryXMinutes, pendingIntent);

            Log.i("alarm", "set successfully");
        //}

        // add it in settings fragment
        //if(switchState == false){
            //alarmManager.cancel(pendingIntent);
            //Toast.makeText(MainActivity.this, "Reminder Disabled.", Toast.LENGTH_SHORT).show();
        //}

    }*/

   /* public boolean checkIfAlarmUp(Intent intent, int requestCode) {
        boolean alarmUp = (PendingIntent.getBroadcast(getActivity(), requestCode,
                intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d("alarm", "Alarm is already active");
            return true;
        }
        else {
            return false;
        }
    }*/

}
