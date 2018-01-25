package com.example.dicoding.mypreloaddata.database;

import android.provider.BaseColumns;

/**
 * Created by dicoding on 10/18/2017.
 */

public class DatabaseContract {

    static String TABLE_NAME = "table_mahasiswa";

    static final class MahasiswaColumns implements BaseColumns {

        // Mahasiswa nama
        static String NAMA = "nama";
        // Mahasiswa nim
        static String NIM = "nim";

    }
}
