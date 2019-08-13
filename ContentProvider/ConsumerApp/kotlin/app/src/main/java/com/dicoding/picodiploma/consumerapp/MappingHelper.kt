package com.dicoding.picodiploma.consumerapp

import android.database.Cursor

import com.dicoding.picodiploma.consumerapp.entity.NoteItem

import java.util.ArrayList

import android.provider.BaseColumns._ID
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.TITLE

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<NoteItem> {
        val notesList = ArrayList<NoteItem>()

        while (notesCursor.moveToNext()) {
            val id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID))
            val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE))
            val description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION))
            val date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE))
            notesList.add(NoteItem(id, title, description, date))
        }

        return notesList
    }
}
