package com.dz.notas.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by herman on 12/09/2016.
 */
public class ScreenReceiver extends BroadcastReceiver {

    int contador = 0;
    public static boolean wasScreenOn = true;

    public static Date secondsLastUpdate = null;
    public static Date beginUpdate = null;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("LOB","onReceive");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            secondsLastUpdate = new Date();
            //wasScreenOn = false;
            long difernciaSegundos = -1;
            if(beginUpdate == null){
                difernciaSegundos = -1;
            }else{
                long diff = secondsLastUpdate.getTime() - beginUpdate.getTime();
                difernciaSegundos = diff / 1000;
            }
            //Log.e("DEBUG_SERVICIO1",difernciaSegundos + " Segundos");

            if( difernciaSegundos < 10){
                contador++;
            }else{
                contador = 0;
            }

            Log.e("DEBUG_SERVICIO2",contador + " Contador");

            if(contador == 3){
                new NetworkAccess().execute();
            }
            //Log.e("LOB","wasScreenOn"+wasScreenOn);
            //Log.e("LOB","numero" + contador);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            beginUpdate = new Date();
            //wasScreenOn = true;

            long difernciaSegundos = -1;
            if(secondsLastUpdate == null){
                difernciaSegundos = -1;
            }else{
                long diff = beginUpdate.getTime() - secondsLastUpdate.getTime();
                difernciaSegundos = diff / 1000;
            }

            if(difernciaSegundos < 10){
                contador++;
            }else{
                contador = 0;
            }

            Log.e("DEBUG_SERVICIO2",contador + " Contador");

            if(contador == 3){
                new NetworkAccess().execute();
            }
            //Log.e("LOB","numero" + contador);

        }else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            //Log.e("LOB","userpresent");
            //Log.e("LOB", "wasScreenOn" + wasScreenOn);

            //String url = "http://www.stackoverflow.com";
            //Intent i = new Intent(Intent.ACTION_VIEW);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.setData(Uri.parse(url));
            //context.startActivity(i);
        }
    }

    public class NetworkAccess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // call some loader
        }
        @Override
        protected Void doInBackground(Void... params) {
            sendhttprequest("");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            contador = 0;
            // dismiss loader
            // update ui
        }

        private void sendhttprequest(String data){

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .build();

            Request request = new Request.Builder()
                    .url("http://67.23.236.79/~notas/push_notification.php")
                    .build();

            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
