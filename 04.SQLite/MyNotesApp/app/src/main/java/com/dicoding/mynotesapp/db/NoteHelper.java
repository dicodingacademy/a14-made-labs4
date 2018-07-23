package com.dicoding.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dicoding.mynotesapp.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.TABLE_NAME;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;


/**
 * Created by sidiqpermana on 11/23/16.
 */

public class NoteHelper {
    private static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil query berbentuk array model note
     */
    public ArrayList<Note> query() {
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " DESC", null);
        cursor.moveToFirst();
        Note note;
        if (cursor.getCount() > 0) {
            do {

                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk query insert
     *
     * @param note model note yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    public long insert(Note note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, note.getTitle());
        initialValues.put(DESCRIPTION, note.getDescription());
        initialValues.put(DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Gunakan method ini untuk query update
     *
     * @param note model note yang akan diubah
     * @return int jumlah dari row yang ter-update, jika tidak ada yang diupdate maka nilainya 0
     */
    public int update(Note note) {
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + note.getId() + "'", null);
    }

    /**
     * Gunakan method ini untuk query delete
     *
     * @param id id yang akan di delete
     * @return int jumlah row yang di delete
     */
    public int delete(int id) {
        return database.delete(TABLE_NAME, _ID + " = '" + id + "'", null);
    }
}
