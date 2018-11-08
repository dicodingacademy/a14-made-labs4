package com.dicoding.picodiploma.mynotesapp;

import android.app.Application;

import com.dicoding.picodiploma.mynotesapp.db.NoteHelper;

public class NoteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NoteHelper.noteInstances(this);
    }
}
