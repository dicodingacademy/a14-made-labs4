package com.dicoding.picodiploma.mypreloaddata.database

import android.provider.BaseColumns

/**
 * Created by dicoding on 10/18/2017.
 */

internal object DatabaseContract {

    var TABLE_NAME = "table_mahasiswa"

    internal class MahasiswaColumns : BaseColumns {
        companion object {
            const val NAMA = "nama"
            const val NIM = "nim"
        }
    }
}
