package com.example.lebsmart.Notifications;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.lebsmart.Activities.MainScreenActivity;
import com.example.lebsmart.FireDetection.FireDetectionFragment;
import com.example.lebsmart.Weather.WeatherFragment;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String reminderContent = intent.getStringExtra("reminderContent");
        String reminderTitle = intent.getStringExtra("reminderTitle");
        int requestCode = intent.getIntExtra("requestCode", 100);
        // used to make multiple notifications
        int uniqueNotificationId = intent.getIntExtra("uniqueNotificationId", 1);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeatingIntent;
        if (requestCode == 100) {
            repeatingIntent = new Intent(context, WeatherFragment.class);
        }
        else {
            repeatingIntent = new Intent(context, FireDetectionFragment.class);
        }

        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //oreo = android 8.0

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel("ID");

            if (mChannel == null) {
                mChannel = new NotificationChannel("ID", "my noti", importance);
                notificationManager.createNotificationChannel(mChannel);
            }

            builder = new NotificationCompat.Builder(context, "ID");

            intent = new Intent(context, MainScreenActivity.class);// may change
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            pendingIntent = PendingIntent.getActivity(context, requestCode, repeatingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            builder.setContentTitle(reminderTitle)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(reminderContent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(soundUri)
                    .setWhen(System.currentTimeMillis());

        }
        else {
            builder = new NotificationCompat.Builder(context, "ID");

            intent = new Intent(context, MainScreenActivity.class);// may change
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            pendingIntent = PendingIntent.getActivity(context, requestCode, repeatingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            builder.setContentTitle(reminderTitle)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(reminderContent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setSound(soundUri);
        }

        Notification notification = builder.build();
        notificationManager.notify(uniqueNotificationId, notification);
    }


}
