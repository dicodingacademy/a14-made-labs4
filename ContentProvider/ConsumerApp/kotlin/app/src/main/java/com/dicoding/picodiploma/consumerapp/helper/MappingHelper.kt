package com.dicoding.picodiploma.helper.consumerapp

import android.database.Cursor
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion._ID
import com.dicoding.picodiploma.consumerapp.entity.Note

import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<Note> {
        val notesList = ArrayList<Note>()

        while (notesCursor.moveToNext()) {
            val id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID))
            val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE))
            val description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION))
            val date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE))
            notesList.add(Note(id, title, description, date))
        }

        return notesList
    }

    fun mapCursorToObject(notesCursor: Cursor): Note {
        notesCursor.moveToNext()
        val id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID))
        val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE))
        val description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION))
        val date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE))
        return Note(id, title, description, date)
    }
}
