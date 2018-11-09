package com.dicoding.picodiploma.mypreloaddata.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.dicoding.picodiploma.mypreloaddata.database.DatabaseContract.MahasiswaColumns.NAMA;
import static com.dicoding.picodiploma.mypreloaddata.database.DatabaseContract.MahasiswaColumns.NIM;
import static com.dicoding.picodiploma.mypreloaddata.database.DatabaseContract.TABLE_NAME;

/**
 * Created by dicoding on 12/1/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbmahasiswa";

    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_MAHASISWA = "create table " + TABLE_NAME +
            " (" + _ID + " integer primary key autoincrement, " +
            NAMA + " text not null, " +
            NIM + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MAHASISWA);
    }

    /*
    Method onUpgrade akan di panggil ketika terjadi perbedaan versi
    Gunakan method onUpgrade untuk melakukan proses migrasi data
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
         */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
