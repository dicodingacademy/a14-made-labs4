package com.dicoding.picodiploma.mysettingpreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class MyPreferenceFramgent extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DEFAULT_VALUE = "Tidak Ada";

    private EditTextPreference name;
    private EditTextPreference email;
    private EditTextPreference age;
    private EditTextPreference phoneNumber;
    private CheckBoxPreference isLoveMu;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setSummary();

    }

    private void setSummary() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        init();
        name.setSummary(sh.getString("name", DEFAULT_VALUE));
        email.setSummary(sh.getString("email", DEFAULT_VALUE));
        age.setSummary(sh.getString("age", DEFAULT_VALUE));
        phoneNumber.setSummary(sh.getString("phone_number", DEFAULT_VALUE));
        isLoveMu.setChecked(sh.getBoolean("isLove", false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        init();
        if (key.equals("name")) {
            name.setSummary(sharedPreferences.getString("name", DEFAULT_VALUE));
        }

        if (key.equals("email")) {
            email.setSummary(sharedPreferences.getString("email", DEFAULT_VALUE));
        }

        if (key.equals("age")) {
            age.setSummary(sharedPreferences.getString("age", DEFAULT_VALUE));
        }

        if (key.equals("phone_number")) {
            phoneNumber.setSummary(sharedPreferences.getString("phone_number", DEFAULT_VALUE));
        }

        if (key.equals("isLove")) {
            isLoveMu.setChecked(sharedPreferences.getBoolean("isLove", false));
        }
    }

    private void init() {
        name = (EditTextPreference) findPreference("name");
        email = (EditTextPreference) findPreference("email");
        age = (EditTextPreference) findPreference("age");
        phoneNumber = (EditTextPreference) findPreference("phone_number");
        isLoveMu = (CheckBoxPreference) findPreference("isLove");
    }

}
