package com.dicoding.picodiploma.mysharedpreference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    TextView tvName, tvAge, tvPhoneNo, tvEmail, tvIsLoveMU;
    Button btnSave;
    private UserPreference mUserPreference;

    private boolean isPreferenceEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvPhoneNo = (TextView) findViewById(R.id.tv_phone);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvIsLoveMU = (TextView) findViewById(R.id.tv_is_love_mu);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        mUserPreference = new UserPreference(this);

        getSupportActionBar().setTitle("My User Preference");

        showExistingPreference();

    }

    /*
    Set tampilan menggunakan preferences
     */
    private void showExistingPreference() {
        if (!TextUtils.isEmpty(mUserPreference.getName())) {
            tvName.setText(mUserPreference.getName());
            tvAge.setText(String.valueOf(mUserPreference.getAge()));
            tvIsLoveMU.setText(mUserPreference.isLoveMU() ? "Ya" : "Tidak");
            tvEmail.setText(mUserPreference.getEmail());
            tvPhoneNo.setText(mUserPreference.getPhoneNumber());

            btnSave.setText("Ubah");
        } else {
            final String TEXT_EMPTY = "Tidak Ada";

            tvName.setText(TEXT_EMPTY);
            tvAge.setText(TEXT_EMPTY);
            tvIsLoveMU.setText(TEXT_EMPTY);
            tvEmail.setText(TEXT_EMPTY);
            tvPhoneNo.setText(TEXT_EMPTY);

            btnSave.setText("Simpan");

            isPreferenceEmpty = true;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            Intent intent = new Intent(MainActivity.this, FormUserPreferenceActivity.class);
            if (isPreferenceEmpty) {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD);
            } else {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT);
            }
            startActivityForResult(intent, FormUserPreferenceActivity.REQUEST_CODE);
        }
    }

    /*
    Akan dipanggil ketika formuserpreferenceactivity ditutup
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FormUserPreferenceActivity.REQUEST_CODE) {
            showExistingPreference();
        }
    }
}
