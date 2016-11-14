package com.dz.notas.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dz.notas.AddContactItem;
import com.dz.notas.DashboardActivity;
import com.dz.notas.NoteDetail;
import com.dz.notas.R;
import com.dz.notas.models.Contact;

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

        //Log.e("NOTIFICATION",messageBody);
        //Log.e("NOTIFICATION",number);
        // Log.e("NOTIFICATION",getContactName(number,mContext));

        Contact n = getContactName(number,mContext);
        String title = n.getName() == "EMPTY" ? "Agrega este número a contactos" : n.getName();
        //Log.e("PHONE",n.getID());

        Intent intent;
        if(n.getName() == "EMPTY"){
            intent = new Intent(mContext, AddContactItem.class);
        }else{
            intent = new Intent(mContext, NoteDetail.class);
            intent.putExtra("contact_id",n.getID());
            intent.putExtra("value_phone",n.getPhone());
            intent.putExtra("value_name",n.getName());
        }

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
    public static Contact getContactName(String phoneNumber,Context context)
    {
        Cursor cursor;
        Contact c = new Contact();

        try{
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME,ContactsContract.PhoneLookup._ID}, null, null, null);
        }catch (NullPointerException e){
            cursor = null;
        }

        if (cursor == null) {
            return null;
        }

        String contactName = "";
        String contactId = "";
        String phoneValue = "";

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                //Log.e("PHONE",contactId);

                phoneValue = phoneNumber;

                if(contactName == ""){
                    contactName = "EMPTY";
                }

                c.setID(contactId);
                c.setName(contactName);
                c.setPhone(phoneValue);
            }
        }else{
                c.setID("EMPTY");
                c.setName("EMPTY");
                c.setPhone("EMPTY");
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return c;
    }
}