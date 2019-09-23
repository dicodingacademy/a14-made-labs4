package com.dicoding.picodiploma.mypreloaddata.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Created by dicoding on 12/6/2016.
 */

class AppPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "MahasiswaPref"
        private const val APP_FIRST_RUN = "app_first_run"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var firstRun: Boolean?
        get() = prefs.getBoolean(APP_FIRST_RUN, true)
        set(input) {
            prefs.edit {
                putBoolean(APP_FIRST_RUN, input as Boolean)
            }
        }
}
