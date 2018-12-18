package com.dicoding.picodiploma.mynotesapp;

import android.database.Cursor;

import com.dicoding.picodiploma.mynotesapp.entity.Note;

import java.util.ArrayList;


public interface LoadNotesCallback {
    void preExecute();

    void postExecute(Cursor notes);
}

