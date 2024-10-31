package com.example.medicinetracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "medicinetracker_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Log to indicate the alarm was received
        Log.d("AlarmReceiver", "Alarm received!");

        // Create the notification channel (for API 26+)
        createNotificationChannel(context);

        // Create an intent that will be fired when the user taps the notification
        Intent notificationIntent = new Intent(context, MainActivity.class); // Replace with your desired activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_access_alarms_24)
                .setContentTitle("Medicine Reminder")
                .setContentText("It's time to take your medicine")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Notify using the notification manager
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        } else {
            Log.e("AlarmReceiver", "NotificationManager is null");
        }
    }

    private void createNotificationChannel(Context context) {
        // Only create the notification channel on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Medicine Tracker Channel";
            String description = "Channel for Medicine Tracker reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.e("AlarmReceiver", "NotificationManager is null when creating channel");
            }
        }
    }

}
