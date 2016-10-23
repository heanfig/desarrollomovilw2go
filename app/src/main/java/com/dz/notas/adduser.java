package com.dz.notas;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adduser extends AppCompatActivity {

    private ProgressBar progressBar;
    Button add_user;
    TextView txt_get_email;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void setEditingEnabled(boolean enabled) {
        txt_get_email.setEnabled(enabled);
        if (enabled) {
            add_user.setVisibility(View.VISIBLE);
        } else {
            add_user.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        //get instance Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Get Firebase database instance
        database = FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(adduser.this, LoginActivity.class));
            finish();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar_user);
        add_user = (Button)findViewById(R.id.btn_add_user);
        txt_get_email = (TextView)findViewById(R.id.email_user);

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = txt_get_email.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Introduzca un email válido", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                Query recentPostsQuery = database.getReference("friends");
                recentPostsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("Insertarr",dataSnapshot.toString());
                        //http://stackoverflow.com/questions/34537369/how-to-search-for-a-value-in-firebase-android
                        //http://stackoverflow.com/questions/35951648/firebase-addlistenerforsinglevalueevent-excute-later-in-loop+
                        //Log.e("Demoo",dataSnapshot.toString());
                        /*for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            Log.e("Demoo",postSnapshot.toString());
                        }*/

                        /*boolean exist = false;
                        long counter = dataSnapshot.getChildrenCount();

                        if(counter>0){
                            for (DataSnapshot itemDataSnapshot : dataSnapshot.getChildren()){
                                Map<String, Object> newPost = (Map<String, Object>) itemDataSnapshot.getValue();
                                if(email.equals(newPost.get("email"))){
                                    exist = true;
                                    return;
                                }
                            }
                        }else{
                           exist = false;
                        }

                        if( !exist ){
                            System.out.println("Exist");
                        }else{
                            System.out.println("Not Exist");
                        }*/

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //Query queryRef =  database.getReference("friends").orderByChild("fullName").equalTo("gooner");
                // database.getReference("friends").

            }
        });
    }
}
