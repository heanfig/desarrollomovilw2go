package com.dz.notas.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dz.notas.DashboardActivity;
import com.dz.notas.NoteDetail;
import com.dz.notas.R;

import java.util.Date;

/**
 * Created by herman on 4/11/2016.
 */

public class CallReceiver extends OutgoingCallBroadcastReceiver {

    private Context mContext;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        mContext = ctx;
        sendNotification("Recibiste una llamada de " + number,number);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        mContext = ctx;
        sendNotification("Respondiste la llamada de " + number,number);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        mContext = ctx;
        sendNotification("Finalizo Tu llamada de " + number,number);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        mContext = ctx;
        sendNotification("Inicio la llamada de " + number,number);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        mContext = ctx;
        sendNotification("Finalizó la llamada de " + number,number);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        mContext = ctx;
        sendNotification("Tienes una llamada perdida de " + number,number);
    }

    private void sendNotification(String messageBody,String number) {
        Log.e("NOTIFICATION",messageBody);

        Intent intent = new Intent(mContext, NoteDetail.class);

        //String uri = "geo:" + 5.050183401112501 + "," + -75.48343253627934 + "?q=" + 5.050183401112501 + "," + -75.48343253627934;
        // intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle("¿Agregar Nota a " + number + "?")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}