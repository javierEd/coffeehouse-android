package ve.org.coffeehouse.napkin.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by javier on 25/09/16.
 */

public class NotesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "coffeehouse.db";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "content TEXT NOT NULL," +
                "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE (id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    public Note insertNote(String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        long id = db.insert("notes", null, contentValues);
        int id1 =  (int) (long) id;
        Cursor res = getNote(id1);
        Note note = new Note(id1, content, res.getString(res.getColumnIndex("created_at")));
        return note;
    }

    public Cursor getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notes where id="+id+"", null );
        res.moveToFirst();
        return res;
    }

    public Cursor deleteNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "delete from notes where id="+id+"", null );
        res.moveToFirst();
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "notes");
        return numRows;
    }

    public ArrayList<Note> getAllNotes()
    {
        ArrayList<Note> array_list = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notes order by id desc", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Note note = new Note(res.getInt(res.getColumnIndex("id")),
                    res.getString(res.getColumnIndex("content")),
                    res.getString(res.getColumnIndex("created_at")));
            array_list.add(note);
            res.moveToNext();
        }
        return array_list;
    }
}
