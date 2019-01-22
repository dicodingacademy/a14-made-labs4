package com.dicoding.picodiploma.consumerapp;

import android.database.Cursor;

interface LoadNotesCallback {
    void postExecute(Cursor notes);
}

