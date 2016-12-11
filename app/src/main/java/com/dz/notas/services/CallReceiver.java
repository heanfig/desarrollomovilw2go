package com.dz.notas.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dz.notas.AddContactItem;
import com.dz.notas.ChatAdapter;
import com.dz.notas.ChatMessage;
import com.dz.notas.ConnectionDB;
import com.dz.notas.DashboardActivity;
import com.dz.notas.NoteDetail;
import com.dz.notas.R;
import com.dz.notas.models.Contact;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.ViewGroup.LayoutParams;

/**
 * Created by herman on 4/11/2016.
 */

public class CallReceiver extends OutgoingCallBroadcastReceiver {

    private Context mContext;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        mContext = ctx;
        Log.e("PHONE", "inicio llamada");
        addInvitePopup(number,ctx);
        //sendNotification("Recibiste una llamada de " + number,number);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        mContext = ctx;
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        mContext = ctx;
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        mContext = ctx;
        addInvitePopup(number, ctx);
        Log.e("PHONE", "inicio llamada");
        //sendNotification("Inicio la llamada de " + number, number);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        mContext = ctx;
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        mContext = ctx;
        sendNotification("Tienes una llamada perdida de " + number,number);
    }

    public void addInvitePopup(String number, Context c) {

        final WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT,
                AbsListView.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.x = 0;
        params.y = 0;

        params.height = 500;
        params.width = width - 60;

        //Toast.makeText(c,"w"+width,Toast.LENGTH_SHORT).show();

        params.format = PixelFormat.TRANSLUCENT;
        final Context ct = c;

        final LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View myview = inflater.inflate(R.layout.layout_popup,null);

        wm.addView(myview, params);

        ListView messagesContainer = (ListView) myview.findViewById(R.id.messagesContainer);

        final Contact n = getContactName(number, mContext);
        String title = n.getName() == "EMPTY" ? "Agrégalo" : n.getName();

        TextView textViewuser = (TextView)myview.findViewById(R.id.meLbl);
        textViewuser.setText(title);

        ArrayList<String> datos = new ArrayList<String>();

        if(n.getName() != "EMPTY") {
            ConnectionDB db = new ConnectionDB(mContext);
            Cursor cursor = db.getNotes(n.getID());
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        datos.add(n.getName()+": "+cursor.getString(2));
                    } while (cursor.moveToNext());
                }
            }
        }else{
            datos.add("Agrega una nota");
        }

        ArrayAdapter<String> chatHistory = new ArrayAdapter<String>(c,R.layout.simple_list_item_1,datos);

        messagesContainer.setAdapter(chatHistory);

        Button btn_send = (Button)myview.findViewById(R.id.chatclosepopup);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(myview);
            }
        });

        Button btn_chat = (Button)myview.findViewById(R.id.chatSendButton);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"dsfsdfsd",Toast.LENGTH_LONG).show();
                Intent intent = null;
                if(n.getName() == "EMPTY") {
                    intent = new Intent(mContext, AddContactItem.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                }else{
                        intent = new Intent(mContext, NoteDetail.class);

                        Log.e("IMPORTANTE", "contact_id" + n.getID());
                        Log.e("IMPORTANTE", "value_phone" + n.getPhone());
                        Log.e("IMPORTANTE", "value_name" + n.getName());

                        if(intent.getExtras() != null){
                            Log.e("IMPORTANTE","SI HAY EXTRAS");
                        }else{
                            Log.e("IMPORTANTE","NO HAY EXTRAS");
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        intent.putExtra("contact_id", n.getID());
                        intent.putExtra("value_phone", n.getPhone());
                        intent.putExtra("value_name", n.getName());
                }
                mContext.startActivity(intent);
            }
        });

        myview.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = params;
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout(myview, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });

    }

    private void sendNotification(String messageBody,String number) {

        Log.e("NOTIFICATION",messageBody);

        Contact n = getContactName(number,mContext);
        String title = n.getName() == "EMPTY" ? "Agrega este número a contactos" : n.getName();

        Intent intent;
        if(n.getName() == "EMPTY"){
            intent = new Intent(mContext, AddContactItem.class);
        }else{
            intent = new Intent(mContext, NoteDetail.class);
            intent.putExtra("contact_id",n.getID());
            intent.putExtra("value_phone",n.getPhone());
            intent.putExtra("value_name",n.getName());
        }

        CharSequence boldUsernameMessage = null;

        if(n.getName() != "EMPTY") {
            String bufferData = "";
            int counter = 0;

            ConnectionDB db = new ConnectionDB(mContext);
            Cursor cursor = db.getNotes(n.getID());
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        if(counter <= 4){
                            bufferData += "<b>" + n.getName() +"</b>: " + cursor.getString(2) + "<br>";
                        }
                        counter++;
                    } while (cursor.moveToNext());
                }
            }

            boldUsernameMessage = Html.fromHtml(bufferData);
        }else{
            boldUsernameMessage = Html.fromHtml(messageBody);
            //boldUsernameMessage = Html.fromHtml("<b>@" + "Herman" + "</b> " + "texto<br><b>s</b>:sdadasdasdsadas");
        }




        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle(title)
                .setContentText(boldUsernameMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(boldUsernameMessage));

        if(n.getName() == "EMPTY"){
            notificationBuilder.addAction(R.drawable.ic_notifications_black_24dp,
                    "Agregar", pendingIntent);
        }else{
            notificationBuilder.addAction(R.drawable.ic_notifications_black_24dp,
                    "Ver Chats", pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
    public static Contact getContactName(String phoneNumber,Context context)
    {
        Log.e("NOTIFICATION","::" + "begin of activity" + "::");

        Cursor cursor;
        Contact c = new Contact();

        try{
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME,ContactsContract.PhoneLookup._ID}, null, null, null);
        }catch (NullPointerException e){
            cursor = null;
        }

        String contactName = "";
        String contactId = "";
        String phoneValue = "";

        Log.e("NOTIFICATION","::" + cursor + "::");

        if(cursor != null && cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));

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