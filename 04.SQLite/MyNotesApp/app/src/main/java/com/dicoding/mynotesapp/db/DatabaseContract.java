package com.dicoding.mynotesapp.db;

import android.provider.BaseColumns;

/**
 * Created by dicoding on 10/12/2017.
 */

public class DatabaseContract {

    public static String TABLE_NOTE = "note";

    public static final class NoteColumns implements BaseColumns {

        //Note title
        public static String TITLE = "title";
        //Note description
        public static String DESCRIPTION = "description";
        //Note date
        public static String DATE = "date";

    }
}
