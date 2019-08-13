package com.dicoding.picodiploma.mynotesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import androidx.core.content.contentValuesOf
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.dicoding.picodiploma.mynotesapp.entity.Note
import java.util.*


/**
 * Created by sidiqpermana on 11/23/16.
 */

class NoteHelper private constructor(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: NoteHelper? = null

        private lateinit var database: SQLiteDatabase

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

    init {
        dataBaseHelper = DatabaseHelper(context)
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

    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil getGetAllNotes berbentuk array model note
     */
    val getAllNotes: ArrayList<Note>
        get() {
            val arrayList = ArrayList<Note>()
            val cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
                    "$_ID ASC", null)
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
     * Gunakan method ini untuk insertNote
     *
     * @param note model note yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    fun insertNote(note: Note): Long {
        val args = contentValuesOf(
                TITLE to note.title,
                DESCRIPTION to note.description,
                DATE to note.date
        )
        return database.insert(DATABASE_TABLE, null, args)
    }

    /**
     * Gunakan method ini untuk updateNote
     *
     * @param note model note yang akan diubah
     * @return int jumlah dari row yang ter-updateNote, jika tidak ada yang diupdate maka nilainya 0
     */
    fun updateNote(note: Note): Int {
        val args = contentValuesOf(
                TITLE to note.title,
                DESCRIPTION to note.description,
                DATE to note.date
        )
        return database.update(DATABASE_TABLE, args, _ID + "= '" + note.id + "'", null)
    }

    /**
     * Gunakan method ini untuk deleteNote
     *
     * @param id id yang akan di deleteNote
     * @return int jumlah row yang di deleteNote
     */
    fun deleteNote(id: Int): Int {
        return database.delete(TABLE_NAME, "$_ID = '$id'", null)
    }
}
