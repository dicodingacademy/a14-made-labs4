package com.dicoding.picodiploma.mynotesapp;

import android.app.Application;

import com.dicoding.picodiploma.mynotesapp.db.NoteHelper;

public class NoteApp extends Application {

    public static NoteHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = new NoteHelper(this);
        db.open();

    }
}
