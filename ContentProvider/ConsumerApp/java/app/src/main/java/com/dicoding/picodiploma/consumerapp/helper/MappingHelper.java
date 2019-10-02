package com.dicoding.picodiploma.consumerapp.helper;

import android.database.Cursor;

import com.dicoding.picodiploma.consumerapp.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Note> notesList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            notesList.add(new Note(id, title, description, date));
        }

        return notesList;
    }

    public static Note mapCursorToObject(Cursor notesCursor) {
        notesCursor.moveToFirst();
        int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
        String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
        String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
        String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));

        return new Note(id, title, description, date);
    }
}
