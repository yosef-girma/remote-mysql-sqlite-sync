package com.android.mysqlsqllitesync.Modified;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.android.mysqlsqllitesync.R;

public class MyService extends IntentService {

    int numMessages = 0;

    public MyService() {
        super("MyService");
    }


/*
    @Override
    public void onCreate()
    {
        super.onCreate();
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent resultIntent = new Intent(this, SecondActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,

                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;

        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Sets an ID for the notification, so it can be updated

        int notifyID = 9001;

        mNotifyBuilder = new NotificationCompat.Builder(this)

                .setContentTitle("Alert")

                .setContentText("You've received new messages.")

                .setSmallIcon(R.drawable.ic_refresh);

        // Set pending intent

        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light

        int defaults = 0;

        defaults = defaults | Notification.DEFAULT_LIGHTS;

        defaults = defaults | Notification.DEFAULT_VIBRATE;

        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);

        // Set the content for Notification

        mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));

        // Set autocancel

        mNotifyBuilder.setAutoCancel(true);

        // Post a notification

        mNotificationManager.notify(notifyID, mNotifyBuilder.build());

        return super.onStartCommand(intent, flags, startId);


    }

    */
    /*
    @Override
    public void onStart(Intent intent, int startId)
    {

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


    }

    */

/*
    @Override
    public void onDestroy() {

        super.onDestroy();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();



    }

  */

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent resultIntent = new Intent(this, SecondActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,

                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;

        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Sets an ID for the notification, so it can be updated

        int notifyID = 9001;

        mNotifyBuilder = new NotificationCompat.Builder(this)

                .setContentTitle("Alert")

                .setContentText("You've received new messages.")

                .setSmallIcon(R.drawable.ic_refresh);

        // Set pending intent

        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light

        int defaults = 0;

        defaults = defaults | Notification.DEFAULT_LIGHTS;

        defaults = defaults | Notification.DEFAULT_VIBRATE;

        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);

        // Set the content for Notification

        mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));

        // Set autocancel

        mNotifyBuilder.setAutoCancel(true);

        // Post a notification

        mNotificationManager.notify(notifyID, mNotifyBuilder.build());

        return ;


    }

}
