package com.dicoding.picodiploma.mysharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sidiqpermana on 11/17/16.
 */

public class UserPreference {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String AGE = "age";
    public static final String PHONE_NUMBER = "phone";
    public static final String LOVE_MU = "islove";

    private SharedPreferences preferences;

    UserPreference(Context context) {
        String PREFS_NAME = "UserPref";
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setString(String value, String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    void setBool(boolean status, String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, status);
        editor.apply();
    }

    boolean getBool(String key) {
        return preferences.getBoolean(key, false);
    }

    void setInt(int value, String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    int getInt(String key) {
        return preferences.getInt(key, 0);
    }
}
