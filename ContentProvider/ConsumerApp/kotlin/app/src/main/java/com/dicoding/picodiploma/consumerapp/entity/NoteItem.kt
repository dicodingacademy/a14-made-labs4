package com.dicoding.picodiploma.consumerapp.entity

import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import android.provider.BaseColumns

import com.dicoding.picodiploma.consumerapp.db.DatabaseContract

import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.getColumnInt
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.getColumnString
import kotlinx.android.parcel.Parcelize

/**
 * Created by dicoding on 12/13/2016.
 */

@Parcelize
class NoteItem (
        var id: Int = 0,
        var title: String? = null,
        var description: String? = null,
        var date: String? = null
): Parcelable{
    constructor(cursor: Cursor) : this() {
        this.id = getColumnInt(cursor, BaseColumns._ID)
        this.title = getColumnString(cursor, DatabaseContract.NoteColumns.TITLE)
        this.description = getColumnString(cursor, DatabaseContract.NoteColumns.DESCRIPTION)
        this.date = getColumnString(cursor, DatabaseContract.NoteColumns.DATE)
    }
}