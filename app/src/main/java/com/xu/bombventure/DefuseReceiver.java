package com.xu.bombventure;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;

import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * TODO For the Hackathon, should probably incude exiting for demonstration purposes
 */
public class DefuseReceiver extends BroadcastReceiver {
   //no screen for this, just a proximity alert. Should automatically work in map too
    //http://stackoverflow.com/questions/22102588/android-google-map-how-to-check-if-user-is-in-marker-circle-region
    private static final int NOTIFICATION_ID = 1000;

    //https://github.com/gauntface/Android-Proximity-Alerts-Example/blob/master/src/co/uk/gauntface/android/proximityalerts/receivers/ProximityAlert.java
    public static final String location_no="location";

    @Override
    public void onReceive(Context context, Intent intent){
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        //this key is an extra automatically created by pending intent
        //true means entering, false means exiting
        int locID= intent.getIntExtra("ID", 0);
        // fetch all coordinates
        Bundle stuff = intent.getExtras();
        Double lat = stuff.getDouble("userlat");
        Double lon = stuff.getDouble("userlon");

        Log.v("proxalert", "Proximity Alert intent received, Room ID ="+ locID);
        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
            //Create notifications

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Notification-based intent that launches AnswerActivity
            Intent notificationIntent = new Intent(context, PlaceholderActivity.class);
            notificationIntent.putExtra("ID",locID);
            //pendingintent serves as a shell to wrap around intent,  attach this to onReceive
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Build notification and add in pendingIntent to launch AnswerActivity
            Notification notification= new NotificationCompat.Builder(context)
                    .setContentTitle("Room Nearby! Answer it to earn coins!")
                    .setContentText("Click here to answer it!!")
                    .setSmallIcon(R.drawable.alert)
                    .setContentIntent(pendingIntent)
                    .build();

            int mNotificationId = 001;
            notificationManager.notify(mNotificationId,notification);
        }else {
            Log.d(getClass().getSimpleName(), "exiting");
            //do nothing
        }


    }


}
