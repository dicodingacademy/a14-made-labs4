package com.dicoding.picodiploma.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.dicoding.picodiploma.mynotesapp.db.NoteHelper;

import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.AUTHORITY;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.TABLE_NAME;

/**
 * Created by dicoding on 12/13/2016.
 */

public class NoteProvider extends ContentProvider {

    /*
    Integer digunakan sebagai identifier antara select all sama select by id
     */
    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private NoteHelper noteHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
    Uri matcher untuk mempermudah identifier dengan menggunakan integer
    misal
    uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
    uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
     */
    static {

        // content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE);

        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        noteHelper = NoteHelper.getInstance(getContext());
        noteHelper.open();
        return true;
    }

    /*
    Method query digunakan ketika ingin menjalankan query Select
    Return cursor
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case NOTE:
                cursor = noteHelper.queryAll();
                break;
            case NOTE_ID:
                cursor = noteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        long added;
        if (sUriMatcher.match(uri) == NOTE) {
            added = noteHelper.insert(contentValues);
        } else {
            added = 0;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated;
        if (sUriMatcher.match(uri) == NOTE_ID) {
            updated = noteHelper.update(uri.getLastPathSegment(), contentValues);
        } else {
            updated = 0;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int deleted;
        if (sUriMatcher.match(uri) == NOTE_ID) {
            deleted = noteHelper.deleteById(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return deleted;
    }

}
