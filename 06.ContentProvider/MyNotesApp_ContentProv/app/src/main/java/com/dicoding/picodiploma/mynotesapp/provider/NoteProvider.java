package com.dicoding.picodiploma.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

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

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
    Uri matcher untuk mempermudah identifier dengan menggunakan integer
    misal
    uri com.dicoding.picodiploma.mynotesapp di cocokan dengan integer 1
    uri com.dicoding.picodiploma.mynotesapp/# dicodokan dengan integer 2
     */
    static {

        // content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE);

        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                NOTE_ID);
    }

    private NoteHelper noteHelper;

    @Override
    public boolean onCreate() {
        noteHelper = new NoteHelper(getContext());
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
                cursor = noteHelper.queryProvider();
                break;
            case NOTE_ID:
                cursor = noteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
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

        switch (sUriMatcher.match(uri)) {
            case NOTE:
                added = noteHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case NOTE_ID:
                updated = noteHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case NOTE_ID:
                deleted = noteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

}
