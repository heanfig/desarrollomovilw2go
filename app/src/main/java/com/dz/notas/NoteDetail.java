package com.dz.notas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dz.notas.models.Contact;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteDetail extends AppCompatActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private Contact c;

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"onResume",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_note_detail);

        Bundle extras = getIntent().getExtras();

        String value_id = "";
        String value_phone = "";
        String value_name = "";

        if (extras != null) {
            value_id = extras.getString("contact_id");
            value_phone = extras.getString("value_phone");
            value_name = extras.getString("value_name");
        }

        c = new Contact();
        c.setID(value_id);
        c.setPhone(value_phone);
        c.setName(value_name);

        Log.e("PHONE", value_id);

        //Toast.makeText(getApplicationContext(),"value_id=" + value_id, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"value_phone=" +value_phone, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"value_name=" + value_name, Toast.LENGTH_LONG).show();

        initControls();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(),"onResume",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_note_detail);

        Bundle extras = getIntent().getExtras();

        String value_id = "";
        String value_phone = "";
        String value_name = "";

        if (extras != null) {
            value_id = extras.getString("contact_id");
            value_phone = extras.getString("value_phone");
            value_name = extras.getString("value_name");
        }

        c = new Contact();
        c.setID(value_id);
        c.setPhone(value_phone);
        c.setName(value_name);

        Log.e("PHONE",value_id);

        initControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Bundle extras = getIntent().getExtras();

        String value_id = "";
        String value_phone = "";
        String value_name = "";

        if (extras != null) {
            value_id = extras.getString("contact_id");
            value_phone = extras.getString("value_phone");
            value_name = extras.getString("value_name");
        }

        c = new Contact();
            c.setID(value_id);
            c.setPhone(value_phone);
            c.setName(value_name);

        Log.e("PHONE",value_id);

        initControls();
    }

    @Override
    public void onNewIntent(Intent intent){
        Intent i = getIntent();
        i.replaceExtras(new Bundle());
        i.setAction("");
        i.setData(null);
        i.setFlags(0);
        setIntent(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_detail, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_item, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        long itemID = info.position;
        menu.setHeaderTitle(c.getName());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        final long selectid = menuinfo.id;
        final int selectpos = menuinfo.position;

        switch (item.getItemId()) {
            case R.id.editnote:

                ChatMessage chat_message = (ChatMessage)adapter.getItem(selectpos);
                final EditText txtUrl = new EditText(this);
                txtUrl.setText(chat_message.getMessage());

                txtUrl.setHint("Ejm. Comprar Queso todos los dias");
                new AlertDialog.Builder(this)
                        .setTitle("Editar Nota")
                        .setMessage("Escriba el nuevo Texto a editar:")
                        .setView(txtUrl)
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String text = txtUrl.getText().toString();

                                ChatMessage chat_message = (ChatMessage)adapter.getItem(selectpos);

                                if(TextUtils.isEmpty(text)){
                                    Toast.makeText(getApplicationContext(),"No debe de estar Vacia",Toast.LENGTH_LONG).show();
                                }else{
                                    ConnectionDB db = new ConnectionDB(getApplicationContext());
                                    long status = db.UpdateNote(chat_message.getId(),text);
                                    if(status > 0){
                                        Toast.makeText(getApplicationContext(),"Actualizado",Toast.LENGTH_SHORT).show();
                                        adapter.EditItemText(text,selectpos);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"No se pudo Editar",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
                break;
            case R.id.deletenote:

                ChatMessage _chat_message = (ChatMessage)adapter.getItem(selectpos);

                ConnectionDB db = new ConnectionDB(getApplicationContext());
                boolean status = db.removeSingleNote(_chat_message.getId());

                if(status){
                    Toast.makeText(getApplicationContext(),"Se eliminó la nota",Toast.LENGTH_LONG).show();
                    adapter.removeItem(selectpos);
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo eliminar la nota",Toast.LENGTH_LONG).show();
                }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent t = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(t);
        }else if(id == R.id.empty_chat){
            Toast.makeText(getApplicationContext(),"Vaciar Chat",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        companionLabel.setText(this.c.getName());

        loadDummyHistory(this.c.getID());

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                String contact_id = c.getID();
                String datetime = DateFormat.getDateTimeInstance().format(new Date());
                String Message = messageText;
                String phonename = c.getName();

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageText);
                chatMessage.setDate(datetime);
                chatMessage.setMe(true);

                ConnectionDB db = new ConnectionDB(getApplicationContext());
                long id = db.addNote("Notas de " + phonename, messageText, contact_id, datetime);
                chatMessage.setId(id);
                //Toast.makeText(getApplicationContext(),"ID" + id,Toast.LENGTH_LONG).show();
                messageET.setText("");

                displayMessage(chatMessage);
            }
        });


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(String idnote){

        chatHistory = new ArrayList<ChatMessage>();

        /*ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Bienvenido a Notas");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("Estamos en desarrollo beta agrega una ntoa");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));*/

        ConnectionDB db = new ConnectionDB(getApplicationContext());
        Cursor cursor = db.getNotes(idnote);

        if (cursor.moveToFirst()) {
            do {
                ChatMessage msg = new ChatMessage();
                msg.setId(cursor.getInt(0));
                msg.setMe(false);
                msg.setMessage(cursor.getString(2));
                msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatHistory.add(msg);
            } while (cursor.moveToNext());
        }

        adapter = new ChatAdapter(NoteDetail.this, new ArrayList<ChatMessage>());

        messagesContainer.setAdapter(adapter);
        registerForContextMenu(messagesContainer);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }
}
