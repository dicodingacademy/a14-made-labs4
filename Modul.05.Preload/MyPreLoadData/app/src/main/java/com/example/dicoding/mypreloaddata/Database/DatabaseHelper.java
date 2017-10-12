package com.example.dicoding.mypreloaddata.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dicoding on 12/1/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static String DATABASE_NAME = "dbmahasiswa";
    public static String TABLE_NAME = "table_mahasiswa";
    public static String FIELD_ID = "id";
    public static String FIELD_NAMA = "nama";
    public static String FIELD_NIM = "nim";

    private static final int DATABASE_VERSION = 1;


    public static String CREATE_TABLE_MAHASISWA = "create table "+TABLE_NAME+" ("+FIELD_ID+" integer primary key autoincrement, " +
            FIELD_NAMA+" text not null, " +
            FIELD_NIM+" text not null);";


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
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
