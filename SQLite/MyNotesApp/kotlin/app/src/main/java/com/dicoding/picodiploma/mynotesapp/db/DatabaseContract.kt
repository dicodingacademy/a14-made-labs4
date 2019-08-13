package com.dicoding.picodiploma.mynotesapp.db

import android.provider.BaseColumns

/**
 * Created by dicoding on 10/12/2017.
 */

internal class DatabaseContract {

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "note"

            //Note id
            val _ID = "_id"
            //Note title
            const val TITLE = "title"
            //Note description
            const val DESCRIPTION = "description"
            //Note date
            const val DATE = "date"
        }

    }
}
