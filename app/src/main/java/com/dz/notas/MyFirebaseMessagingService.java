package com.dz.notas;

import com.google.firebase.messaging.FirebaseMessagingService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.zopp.appxilio.R;

/**
 * Created by herman on 11/09/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

     private static final String TAG = "MyFirebaseMsgService";

        /**
         * Called when message is received.
         *
         * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
         */
        // [START receive_message]
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // [START_EXCLUDE]
            // There are two types of messages data messages and notification messages. Data messages are handled
            // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
            // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
            // is in the foreground. When the app is in the background an automatically generated notification is displayed.
            // When the user taps on the notification they are returned to the app. Messages containing both notification
            // and data payloads are treated as notification messages. The Firebase console always sends notification
            // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
            // [END_EXCLUDE]

            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getBody());
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
        // [END receive_message]

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, DashboardActivity.class);

        //String uri = "geo:" + 5.050183401112501 + "," + -75.48343253627934 + "?q=" + 5.050183401112501 + "," + -75.48343253627934;
        // intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
