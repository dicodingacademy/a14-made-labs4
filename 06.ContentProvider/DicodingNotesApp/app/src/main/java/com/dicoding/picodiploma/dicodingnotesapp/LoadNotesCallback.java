package com.dicoding.picodiploma.dicodingnotesapp;

import android.database.Cursor;

interface LoadNotesCallback {

    void postExecute(Cursor notes);
}

