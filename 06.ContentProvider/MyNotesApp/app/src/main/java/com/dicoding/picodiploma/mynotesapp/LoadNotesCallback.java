package com.dicoding.picodiploma.mynotesapp;

import android.database.Cursor;

interface LoadNotesCallback {
    void preExecute();

    void postExecute(Cursor notes);
}

