package com.dicoding.picodiploma.mynotesapp.entity

import android.database.Cursor
import android.os.Parcelable
import android.provider.BaseColumns
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract
import kotlinx.android.parcel.Parcelize


/**
 * Created by sidiqpermana on 11/23/16.
 */
@Parcelize
class Note (
        var id: Int = 0,
        var title: String? = null,
        var description: String? = null,
        var date: String? = null
): Parcelable{
    constructor(cursor: Cursor) : this() {
        this.id = DatabaseContract.getColumnInt(cursor, BaseColumns._ID)
        this.title = DatabaseContract.getColumnString(cursor, DatabaseContract.NoteColumns.TITLE)
        this.description = DatabaseContract.getColumnString(cursor, DatabaseContract.NoteColumns.DESCRIPTION)
        this.date = DatabaseContract.getColumnString(cursor, DatabaseContract.NoteColumns.DATE)
    }
}