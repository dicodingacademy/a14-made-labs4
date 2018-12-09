package com.dicoding.picodiploma.mysharedpreference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView tvName, tvAge, tvPhoneNo, tvEmail, tvIsLoveMU;
    private Button btnSave;
    private UserPreference mUserPreference;

    private boolean isPreferenceEmpty = false;
    private UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_name);
        tvAge = findViewById(R.id.tv_age);
        tvPhoneNo = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvIsLoveMU = findViewById(R.id.tv_is_love_mu);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        userModel = new UserModel();


        mUserPreference = new UserPreference(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("My User Preference");

        showExistingPreference();

    }

    /*
    Set tampilan menggunakan preferences
     */
    private void showExistingPreference() {
        mUserPreference.getUser(userModel);
        if (!userModel.getName().isEmpty()) {
            tvName.setText(userModel.getName());
            tvAge.setText(String.valueOf(userModel.getAge()));
            tvIsLoveMU.setText(userModel.isLove() ? "Ya" : "Tidak");
            tvEmail.setText(userModel.getEmail());
            tvPhoneNo.setText(userModel.getPhoneNumber());

            btnSave.setText(getString(R.string.change));
            isPreferenceEmpty = false;

        } else {
            final String TEXT_EMPTY = "Tidak Ada";

            tvName.setText(TEXT_EMPTY);
            tvAge.setText(TEXT_EMPTY);
            tvIsLoveMU.setText(TEXT_EMPTY);
            tvEmail.setText(TEXT_EMPTY);
            tvPhoneNo.setText(TEXT_EMPTY);

            btnSave.setText(getString(R.string.save));
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
                String name = userModel.getName();
                String email = userModel.getEmail();
                int age = userModel.getAge();
                String phoneNumber = userModel.getPhoneNumber();
                boolean isLove = userModel.isLove();
                UserModel userModel = new UserModel(name, email, age, phoneNumber, isLove);
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT);
                intent.putExtra("USER", userModel);
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
