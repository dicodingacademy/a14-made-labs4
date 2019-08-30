package com.dicoding.picodiploma.mypreloaddata.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dicoding on 12/6/2016.
 */

public class AppPreference {

    private static final String PREFS_NAME = "MahasiswaPref";
    private static final String APP_FIRST_RUN = "app_first_run";
    private final SharedPreferences prefs;

    public AppPreference(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setFirstRun(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_FIRST_RUN, input);
        editor.apply();
    }

    public Boolean getFirstRun() {
        return prefs.getBoolean(APP_FIRST_RUN, true);
    }
}