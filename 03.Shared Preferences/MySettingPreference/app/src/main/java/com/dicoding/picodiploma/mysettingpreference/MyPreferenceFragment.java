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
        namePreference.setSummary(sh.getString("NAME", DEFAULT_VALUE));
        emailPreference.setSummary(sh.getString("EMAIL", DEFAULT_VALUE));
        agePreference.setSummary(sh.getString("AGE", DEFAULT_VALUE));
        phonePreference.setSummary(sh.getString("PHONE", DEFAULT_VALUE));
        isLoveMuPreference.setChecked(sh.getBoolean("ISLOVE", false));
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
        if (key.equals("NAME")) {
            namePreference.setSummary(sharedPreferences.getString("NAME", DEFAULT_VALUE));
        }

        if (key.equals("EMAIL")) {
            emailPreference.setSummary(sharedPreferences.getString("EMAIL", DEFAULT_VALUE));
        }

        if (key.equals("AGE")) {
            agePreference.setSummary(sharedPreferences.getString("AGE", DEFAULT_VALUE));
        }

        if (key.equals("PHONE")) {
            phonePreference.setSummary(sharedPreferences.getString("PHONE", DEFAULT_VALUE));
        }

        if (key.equals("ISLOVE")) {
            isLoveMuPreference.setChecked(sharedPreferences.getBoolean("ISLOVE", false));
        }
    }

    private void init() {
        namePreference = (EditTextPreference) findPreference("NAME");
        emailPreference = (EditTextPreference) findPreference("EMAIL");
        agePreference = (EditTextPreference) findPreference("AGE");
        phonePreference = (EditTextPreference) findPreference("PHONE");
        isLoveMuPreference = (CheckBoxPreference) findPreference("ISLOVE");
    }

}
