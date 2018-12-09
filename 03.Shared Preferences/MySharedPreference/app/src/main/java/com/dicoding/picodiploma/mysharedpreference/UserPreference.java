package com.dicoding.picodiploma.mysharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sidiqpermana on 11/17/16.
 */

class UserPreference {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String AGE = "age";
    private static final String PHONE_NUMBER = "phone";
    private static final String LOVE_MU = "islove";

    private final SharedPreferences preferences;

    UserPreference(Context context) {
        String PREFS_NAME = "UserPref";
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void setUser(UserModel value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, value.name);
        editor.putString(EMAIL, value.email);
        editor.putInt(AGE, value.age);
        editor.putString(PHONE_NUMBER, value.phoneNumber);
        editor.putBoolean(LOVE_MU, value.isLove);
        editor.apply();
    }

    void getUser(UserModel value) {
        value.setName(preferences.getString(NAME, ""));
        value.setEmail(preferences.getString(EMAIL, ""));
        value.setAge(preferences.getInt(AGE, 0));
        value.setPhoneNumber(preferences.getString(PHONE_NUMBER, ""));
        value.setLove(preferences.getBoolean(LOVE_MU, true));
    }
}
