package com.dicoding.picodiploma.mynotesapp

import android.database.Cursor

internal interface LoadNotesCallback {
    fun preExecute()

    fun postExecute(notes: Cursor)
}

