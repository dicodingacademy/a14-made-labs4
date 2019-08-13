package com.dicoding.picodiploma.consumerapp

import android.database.Cursor

internal interface LoadNotesCallback {
    fun postExecute(notes: Cursor)
}

