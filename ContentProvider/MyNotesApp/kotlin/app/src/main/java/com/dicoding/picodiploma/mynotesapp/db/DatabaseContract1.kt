package com.dicoding.picodiploma.mynotesapp.db

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by dicoding on 10/12/2017.
 */

object DatabaseContract {

    // Authority yang digunakan
    const val AUTHORITY = "com.dicoding.picodiploma.mynotesapp"
    private const val SCHEME = "content"

    /*
    Penggunaan Base Columns akan memudahkan dalam penggunaan suatu table
    Untuk id yang autoincrement sudah default ada di dalam kelas BaseColumns dengan nama field _ID
     */
    class NoteColumns : BaseColumns {

        companion object {
            // Note table name
            const val TABLE_NAME = "note"

            // Note title
            const val TITLE = "title"
            // Note description
            const val DESCRIPTION = "description"
            // Note date
            const val DATE = "date"

            // Base content yang digunakan untuk akses content provider
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }

    }

    /*
    Digunakan untuk mempermudah akses data di dalam cursor dengan parameter nama column
    */
    fun getColumnString(cursor: Cursor, columnName: String): String {
        return cursor.getString(cursor.getColumnIndex(columnName))
    }

    fun getColumnInt(cursor: Cursor, columnName: String): Int {
        return cursor.getInt(cursor.getColumnIndex(columnName))
    }
}
