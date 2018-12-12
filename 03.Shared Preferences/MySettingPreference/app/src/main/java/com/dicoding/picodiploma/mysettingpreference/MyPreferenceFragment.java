package com.dicoding.picodiploma.mysettingpreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DEFAULT_VALUE = "Tidak Ada";

    private EditTextPreference namePreference;
    private EditTextPreference emailPreference;
    private EditTextPreference agePreference;
    private EditTextPreference phonePreference;
    private CheckBoxPreference isLoveMuPreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        namePreference.setSummary(sh.getString("name", DEFAULT_VALUE));
        emailPreference.setSummary(sh.getString("email", DEFAULT_VALUE));
        agePreference.setSummary(sh.getString("age", DEFAULT_VALUE));
        phonePreference.setSummary(sh.getString("phone_number", DEFAULT_VALUE));
        isLoveMuPreference.setChecked(sh.getBoolean("isLove", false));
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
        if (key.equals("name")) {
            namePreference.setSummary(sharedPreferences.getString("name", DEFAULT_VALUE));
        }

        if (key.equals("email")) {
            emailPreference.setSummary(sharedPreferences.getString("email", DEFAULT_VALUE));
        }

        if (key.equals("age")) {
            agePreference.setSummary(sharedPreferences.getString("age", DEFAULT_VALUE));
        }

        if (key.equals("phone_number")) {
            phonePreference.setSummary(sharedPreferences.getString("phone_number", DEFAULT_VALUE));
        }

        if (key.equals("isLove")) {
            isLoveMuPreference.setChecked(sharedPreferences.getBoolean("isLove", false));
        }
    }

    private void init() {
        namePreference = (EditTextPreference) findPreference("name");
        emailPreference = (EditTextPreference) findPreference("email");
        agePreference = (EditTextPreference) findPreference("age");
        phonePreference = (EditTextPreference) findPreference("phone_number");
        isLoveMuPreference = (CheckBoxPreference) findPreference("isLove");
    }

}
