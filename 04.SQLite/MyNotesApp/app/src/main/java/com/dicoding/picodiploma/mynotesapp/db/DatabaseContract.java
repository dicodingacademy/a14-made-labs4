package com.dicoding.picodiploma.mynotesapp.db;

import android.provider.BaseColumns;

/**
 * Created by dicoding on 10/12/2017.
 */

class DatabaseContract {

    static final class NoteColumns implements BaseColumns {
        static final String TABLE_NAME = "note";

        //Note title
        static final String TITLE = "title";
        //Note description
        static final String DESCRIPTION = "description";
        //Note date
        static final String DATE = "date";

    }
}
