package com.dz.notas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zopp.appxilio.R;
import com.dz.notas.models.User;
import com.dz.notas.services.LockService;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    //Cards
    private CardView cv_friends;
    private CardView cv_configuration;
    private CardView cv_gps;
    private CardView cv_card_sesionkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(getApplicationContext(), LockService.class));

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("ALERTA", refreshedToken);

        //get instance Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Firebase database instance
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }

        String id_user = auth.getCurrentUser().getUid();
        //Log.e("appxilio_debug",id_user);

        database.getReference("users").child(id_user).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        Toolbar collapsingToolbar = (Toolbar) findViewById(R.id.toolbar);
                        collapsingToolbar.setTitle("title");
                        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                        appbarLayout.setExpanded(true, true); // works

                        //Log.e("appxilio_debug",dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });


        /*String refreshedTokena = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(getApplication(),"",Toast.LENGTH_LONG);
        Log.e("ALERTA", refreshedTokena);
        Log.e("ALERTA", "Hermannn");
        */


        //if (getIntent().getExtras() != null) {
           // for (String key : getIntent().getExtras().keySet()) {
                //String value = getIntent().getExtras().getString(key);
          //      Toast.makeText(getApplicationContext(),"Demo",Toast.LENGTH_LONG).show();
                //Log.d("demo", "Key: " + key + " Value: " + value);
           // }
        //}
        //if (getIntent().hasExtra("message")) {
        //    Toast.makeText(getApplicationContext(),"Demo",Toast.LENGTH_LONG).show();
            //String theMessage = intent.getStringExtra("message");
            //do something
        //}

        /*
          * @name: @FIREBASE: Suscribe to topic beause notifications
          * @edit: @heanfig
          * @descpiption: @FIREBASE: Suscribe to topic beause notifications
        */
        FirebaseMessaging.getInstance().subscribeToTopic("noticias");

        /*
          * @name: float button of header activity
          * @edit: @heanfig
          * @descpiption: float button of header activity
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getApplication(), adduser.class);
                startActivity(t);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
          * @name: Alert when the gps is off
          * @edit: @heanfig
          * @descpiption: Alert when the gps is off
        */
        cv_friends = (CardView)findViewById(R.id.card_viewusers);
        cv_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Preferences = new Intent(getApplication(),ActivityMy_Friends.class);
                startActivity(Preferences);
            }
        });

        /*
          * @name: Alert when the gps is off
          * @edit: @heanfig
          * @descpiption: Alert when the gps is off
        */
        cv_configuration = (CardView)findViewById(R.id.card_viewconfig);
        cv_configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Preferences = new Intent(getApplication(),SettingsActivity.class);
                startActivity(Preferences);
            }
        });

        /*
          * @name: get attribute of button in card view GPS
          * @edit: @heanfig
          * @descpiption: get attribute of button in card view GPS
        */
        cv_gps = (CardView)findViewById(R.id.card_viewgps);
        cv_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        /*
          * @name: get attribute of button in card logout
          * @edit: @heanfig
          * @descpiption: get attribute of button in card logout
        */
        cv_card_sesionkill = (CardView)findViewById(R.id.card_sesionkill);
        cv_card_sesionkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        /*
          * @name: Alert when the gps is off DISPLAY ALERT DIALOG
          * @edit: @heanfig
          * @descpiption: Alert when the gps is off
        */

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this, R.style.MyDialogTheme);
            builder.setTitle(getString(R.string.alertGPSenabled_title));
            builder.setMessage(getString(R.string.alertGPSenabled));

            String positiveText = getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            cv_gps.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(),"SI",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            cv_gps.setVisibility(View.VISIBLE);
        }else{
            cv_gps.setVisibility(View.GONE);
        }
    }

    public void signOut() {
        auth.signOut();
        Intent closeAc = new Intent(getApplication(),LoginActivity.class);
        startActivity(closeAc);
    }

}
