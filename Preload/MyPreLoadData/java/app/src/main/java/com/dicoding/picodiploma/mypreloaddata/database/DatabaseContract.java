package com.dicoding.picodiploma.mypreloaddata.database;

import android.provider.BaseColumns;

/**
 * Created by dicoding on 10/18/2017.
 */

class DatabaseContract {

    public static final String TABLE_NAME = "table_mahasiswa";

    public static final class MahasiswaColumns implements BaseColumns {

        // Mahasiswa nama
        public static final String NAMA = "nama";
        // Mahasiswa nim
        public static final String NIM = "nim";

    }
}
