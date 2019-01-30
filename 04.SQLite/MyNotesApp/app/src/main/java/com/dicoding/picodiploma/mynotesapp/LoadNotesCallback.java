package com.dicoding.picodiploma.mynotesapp;

import com.dicoding.picodiploma.mynotesapp.entity.Note;

import java.util.ArrayList;


public interface LoadNotesCallback {
    void preExecute();

    void postExecute(ArrayList<Note> notes);
}

