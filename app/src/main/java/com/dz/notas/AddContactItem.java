package com.dz.notas;

import android.content.Intent;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddContactItem extends AppCompatActivity {

    Button btncontact;
    Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_item);

        btncontact = (Button)findViewById(R.id.btn_add_contact);
        btncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addContactIntent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
                addContactIntent.putExtra(Contacts.Intents.Insert.NAME, "Nombre");
                //addContactIntent.putExtra(Contacts.Intents.Insert.PHONE, "Nombre");
                startActivity(addContactIntent);
            }
        });

        backbtn = (Button)findViewById(R.id.btn_add_contact);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContactIntent = new Intent(getApplicationContext(),ListContacts.class);
                startActivity(addContactIntent);
            }
        });
    }
}
