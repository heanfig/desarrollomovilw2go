package com.dz.notas;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by herman on 10/11/2016.
 */
public class ConnectionDB extends SQLiteOpenHelper {

    public static final String TABLE_ID = "_idNote";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String CONTACT_ID = "contact_id";
    public static final String DATETIME = "datetime";

    private static final String DATABASE = "Note";
    private static final String TABLE = "notes";

    private Context contextap;

    public ConnectionDB(Context context) {
        super(context, DATABASE, null, 1);
        this.contextap = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE + " (" +
                        TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE + " TEXT," +
                        CONTACT_ID + " TEXT," +
                        DATETIME + " TEXT," +
                        CONTENT + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }

    @Deprecated
    public void removeSingleNote_FIX(String id_contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE + " WHERE " + TABLE_ID + "= " + id_contact);
        db.close();
    }

    public boolean removeSingleNote(long id) {
        String where = TABLE_ID + "=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, where, null) != 0;
    }

    public long addNote(String title, String content, String noteid, String datefull){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE,title);
        values.put(CONTENT, content);
        values.put(CONTACT_ID, noteid);
        values.put(DATETIME, datefull);
        long affectedrows = db.insert(TABLE, null, values);
        db.close();

        if(affectedrows > 0){
            Toast.makeText(contextap,"Se inserto la Nota",Toast.LENGTH_LONG).show();
            return affectedrows;
        }else{
            Toast.makeText(contextap,"No se pudo insertar la nota",Toast.LENGTH_LONG).show();
            return -1;
        }

    }

    public Cursor getNotes(String idnote){
        SQLiteDatabase db = this.getReadableDatabase();
        String columnas[] = {TABLE_ID,TITLE,CONTENT,DATETIME};
        Cursor c = db.query(TABLE, columnas, CONTACT_ID + "=?", new String[] { idnote }, null, null, null);
        return c;
    }

    public void close(){
        this.close();
    }
}
