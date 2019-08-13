package com.dicoding.picodiploma.mysharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Created by sidiqpermana on 11/17/16.
 */

internal class UserPreference(context: Context) {

    private val preferences: SharedPreferences

    var user: UserModel
        get() {
            val model = UserModel()
            model.name = preferences.getString(NAME, "")
            model.email = preferences.getString(EMAIL, "")
            model.age = preferences.getInt(AGE, 0)
            model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
            model.isLove = preferences.getBoolean(LOVE_MU, false)

            return model
        }
        set(value) {
            preferences.edit {
                putString(NAME, value.name)
                putString(EMAIL, value.email)
                putInt(AGE, value.age)
                putString(PHONE_NUMBER, value.phoneNumber)
                putBoolean(LOVE_MU, value.isLove)
            }
        }

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val LOVE_MU = "islove"
    }
}
