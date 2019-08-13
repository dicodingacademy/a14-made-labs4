package com.dicoding.picodiploma.mynotesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.dicoding.picodiploma.mynotesapp.entity.Note

import java.util.ArrayList

import android.provider.BaseColumns._ID
import androidx.core.content.contentValuesOf
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TITLE

/**
 * Created by sidiqpermana on 11/23/16.
 */

class NoteHelper private constructor(context: Context) {
    private val dataBaseHelper: DatabaseHelper = DatabaseHelper(context)

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: NoteHelper? = null

        fun getInstance(context: Context): NoteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NoteHelper(context)
                    }
                }
            }
            return INSTANCE as NoteHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    /*
    METHOD DIBAWAH INI UNTUK QUERY YANG LAMA ATAU UNTUK PROJECT SQLITE
     */

    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil query berbentuk array model note
     */
    fun query(): ArrayList<Note> {
        val arrayList = ArrayList<Note>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID DESC", null)
        cursor.moveToFirst()
        var note: Note
        if (cursor.count > 0) {
            do {
                note = Note()
                note.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                note.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                note.description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                note.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))

                arrayList.add(note)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    /**
     * Gunakan method ini untuk query insert
     *
     * @param note model note yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    fun insert(note: Note): Long {
        val initialValues = contentValuesOf(
                TITLE to note.title,
                DESCRIPTION to note.description,
                DATE to note.date
        )
        return database.insert(DATABASE_TABLE, null, initialValues)
    }

    /**
     * Gunakan method ini untuk query update
     *
     * @param note model note yang akan diubah
     * @return int jumlah dari row yang ter-update, jika tidak ada yang diupdate maka nilainya 0
     */
    fun update(note: Note): Int {
        val args = contentValuesOf(
                TITLE to note.title,
                DESCRIPTION to note.description,
                DATE to note.date
        )
        return database.update(DATABASE_TABLE, args, _ID + "= '" + note.id + "'", null)
    }

    /**
     * Gunakan method ini untuk query delete
     *
     * @param id id yang akan di delete
     * @return int jumlah row yang di delete
     */
    fun delete(id: Int): Int {
        return database.delete(TABLE_NAME, "$_ID = '$id'", null)
    }

    /*
    METHOD DI BAWAH INI ADALAH QUERY UNTUK CONTENT PROVIDER
    NILAI BALIK CURSOR
    */

    /**
     * Ambil data dari note berdasarakan parameter id
     * Gunakan method ini untuk ambil data di dalam provider
     *
     * @param id id note yang dicari
     * @return cursor hasil query
     */
    fun queryByIdProvider(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    /**
     * Ambil data dari semua note yang ada di dalam database
     * Gunakan method ini untuk ambil data di dalam provider
     *
     * @return cursor hasil query
     */
    fun queryProvider(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC")
    }

    /**
     * Simpan data ke dalam database
     * Gunakan method ini untuk query insert di dalam provider
     *
     * @param values nilai data yang akan di simpan
     * @return long id dari data yang baru saja di masukkan
     */
    fun insertProvider(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    /**
     * Update data dalam database
     *
     * @param id     data dengan id berapa yang akan di update
     * @param values nilai data baru
     * @return int jumlah data yang ter-update
     */
    fun updateProvider(id: String, values: ContentValues): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    /**
     * Delete data dalam database
     *
     * @param id data dengan id berapa yang akan di delete
     * @return int jumlah data yang ter-delete
     */
    fun deleteProvider(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = ?", arrayOf(id))
    }
}


