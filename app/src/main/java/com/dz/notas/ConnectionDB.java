package com.dz.notas;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by herman on 10/11/2016.
 */
public class ConnectionDB extends SQLiteOpenHelper {

    public static final String TABLE_ID = "_idNote";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

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
                        CONTENT + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }

    public void addNote(String title, String content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE,title);
        values.put(CONTENT, content);
        long affectedrows = db.insert(TABLE, null, values);
        Log.e("HERMAN",affectedrows+"");
        if(affectedrows > 0){
            Toast.makeText(contextap,"Inserto",Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public Cursor getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String columnas[] = {TABLE_ID,TITLE,CONTENT};
        Cursor c = db.query(TABLE, columnas, null, null, null, null, null);
        return c;
    }

    public void close(){
        this.close();
    }
}
