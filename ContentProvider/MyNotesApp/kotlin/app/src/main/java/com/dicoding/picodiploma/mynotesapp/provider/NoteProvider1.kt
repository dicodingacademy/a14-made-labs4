package com.dicoding.picodiploma.mynotesapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import com.dicoding.picodiploma.mynotesapp.MainActivity
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.AUTHORITY
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.mynotesapp.db.NoteHelper

/**
 * Created by dicoding on 12/13/2016.
 */

class NoteProvider : ContentProvider() {

    private lateinit var noteHelper: NoteHelper

    override fun onCreate(): Boolean {
        noteHelper = NoteHelper.getInstance(context as Context)

        return true
    }

    /*
    Method query digunakan ketika ingin menjalankan query Select
    Return cursor
     */
    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        noteHelper.open()
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            NOTE -> cursor = noteHelper.queryProvider()
            NOTE_ID -> cursor = noteHelper.queryByIdProvider(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        noteHelper.open()
        val added: Long = if (sUriMatcher.match(uri) == NOTE) noteHelper.insertProvider(contentValues as ContentValues)
        else 0

        context?.contentResolver?.notifyChange(CONTENT_URI, MainActivity.DataObserver(Handler(), context as Context))

        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        noteHelper.open()
        val updated: Int = if (sUriMatcher.match(uri) == NOTE_ID) noteHelper.updateProvider(uri.lastPathSegment.toString(), contentValues as ContentValues)
        else 0

        context?.contentResolver?.notifyChange(CONTENT_URI, MainActivity.DataObserver(Handler(), context as Context))

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        noteHelper.open()
        val deleted: Int = if (sUriMatcher.match(uri) == NOTE_ID) noteHelper.deleteProvider(uri.lastPathSegment.toString())
        else 0

        context?.contentResolver?.notifyChange(CONTENT_URI, MainActivity.DataObserver(Handler(), context as Context))

        return deleted
    }

    companion object {

        /*
    Integer digunakan sebagai identifier antara select all sama select by id
     */
        private const val NOTE = 1
        private const val NOTE_ID = 2

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        /*
    Uri matcher untuk mempermudah identifier dengan menggunakan integer
    misal
    uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
    uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
     */
        init {

            // content://com.dicoding.picodiploma.mynotesapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)

            // content://com.dicoding.picodiploma.mynotesapp/note/id
            sUriMatcher.addURI(AUTHORITY,
                    "$TABLE_NAME/#",
                    NOTE_ID)
        }
    }

}
