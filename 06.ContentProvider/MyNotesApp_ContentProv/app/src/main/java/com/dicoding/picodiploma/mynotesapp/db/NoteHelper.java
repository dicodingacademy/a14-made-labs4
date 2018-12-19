package com.dicoding.picodiploma.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.mynotesapp.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.TABLE_NAME;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;


/**
 * Created by sidiqpermana on 11/23/16.
 */

public class NoteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private final DatabaseHelper dataBaseHelper;
    private static NoteHelper INSTANCE;

    private SQLiteDatabase database;

    private NoteHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static NoteHelper getInstance(Context context) {
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    /*
    METHOD DIBAWAH INI UNTUK QUERY YANG LAMA ATAU UNTUK PROJECT SQLITE
     */

    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil query berbentuk array model note
     */
    public ArrayList<Note> query() {
        ArrayList<Note> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null, _ID + " DESC"
                , null);
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

    /*
    METHOD DI BAWAH INI ADALAH QUERY UNTUK CONTENT PROVIDER
    NILAI BALIK CURSOR
    */

    /**
     * Ambil data dari note berdasarakan parameter id
     * Gunakan method ini untuk ambil data di dalam provider
     *
     * @param id id note yang dicari
     * @return cursor hasil query
     */
    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    /**
     * Ambil data dari semua note yang ada di dalam database
     * Gunakan method ini untuk ambil data di dalam provider
     *
     * @return cursor hasil query
     */
    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    /**
     * Simpan data ke dalam database
     * Gunakan method ini untuk query insert di dalam provider
     *
     * @param values nilai data yang akan di simpan
     * @return long id dari data yang baru saja di masukkan
     */
    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    /**
     * Update data dalam database
     *
     * @param id     data dengan id berapa yang akan di update
     * @param values nilai data baru
     * @return int jumlah data yang ter-update
     */
    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    /**
     * Delete data dalam database
     *
     * @param id data dengan id berapa yang akan di delete
     * @return int jumlah data yang ter-delete
     */
    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}


