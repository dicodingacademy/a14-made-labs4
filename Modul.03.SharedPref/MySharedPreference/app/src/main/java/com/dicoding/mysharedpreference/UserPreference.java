package com.dicoding.mysharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sidiqpermana on 11/17/16.
 */

public class UserPreference {
    private String KEY_NAME = "name";
    private String KEY_EMAIL = "email";
    private String KEY_LOVE_MU = "love_mu";
    private String KEY_PHONE_NUMBER = "phone_number";
    private String KEY_AGE = "age";

    private String PREFS_NAME = "UserPref";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    
    public UserPreference(Context context){
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public void setName(String name){
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public String getName(){
        return preferences.getString(KEY_NAME, null);
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail(){
        return preferences.getString(KEY_EMAIL, null);
    }

    public void setLoveMU(boolean status){
        editor.putBoolean(KEY_LOVE_MU, status);
        editor.apply();
    }

    public boolean isLoveMU(){
        return preferences.getBoolean(KEY_LOVE_MU, false);
    }

    public void setPhoneNumber(String phoneNumber){
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.apply();
    }

    public String getPhoneNumber(){
        return preferences.getString(KEY_PHONE_NUMBER, null);
    }

    public void setAge(int age){
        editor.putInt(KEY_AGE, age);
        editor.apply();
    }

    public int getAge(){
        return preferences.getInt(KEY_AGE, 0);
    }
}
