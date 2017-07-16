package com.dicoding.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.dicoding.mynotesapp.db.DatabaseHelper;

/**
 * Created by dicoding on 12/13/2016.
 */

public class NoteProvider extends ContentProvider {


    private static final String AUTHORITY = "com.dicoding.mynotesapp";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );


    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#", NOTES_ID);
    }


    private SQLiteDatabase sqLiteDatabase;




    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor = null;


        if (uriMatcher.match(uri) == NOTES){
            cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, DatabaseHelper.FIELD_ID+" DESC");
        }


        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);


        if (id > 0) {
            Uri mUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(uri, null);
            return mUri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);
    }


    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                delCount =  sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, s , strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                updCount =  sqLiteDatabase.update(DatabaseHelper.TABLE_NAME, contentValues, s , strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }


}
