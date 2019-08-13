package com.dicoding.picodiploma.mypreloaddata.database

import android.provider.BaseColumns

/**
 * Created by dicoding on 10/18/2017.
 */

internal object DatabaseContract {

    var TABLE_NAME = "table_mahasiswa"

    internal class MahasiswaColumns : BaseColumns {
        companion object {
            // Mahasiswa nama
            const val NAMA = "nama"
            // Mahasiswa nim
            const val NIM = "nim"
        }

    }
}
