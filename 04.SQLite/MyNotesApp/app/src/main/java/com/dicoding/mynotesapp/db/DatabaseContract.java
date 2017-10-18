package com.dicoding.mynotesapp.db;

import android.provider.BaseColumns;

/**
 * Created by dicoding on 10/12/2017.
 */

public class DatabaseContract {

    static String TABLE_NOTE = "note";

    static final class NoteColumns implements BaseColumns {

        //Note title
        static String TITLE = "title";
        //Note description
        static String DESCRIPTION = "description";
        //Note date
        static String DATE = "date";

    }
}
