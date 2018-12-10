package com.dicoding.picodiploma.mysharedpreference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormUserPreferenceActivity extends AppCompatActivity
        implements View.OnClickListener {

    private EditText edtName, edtEmail, edtPhone, edtAge;
    private RadioGroup rgLoveMu;
    private RadioButton rbYes, rbNo;

    public static final String EXTRA_TYPE_FORM = "extra_type_form";
    public static final int RESULT_CODE = 101;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    private int formType;
    UserModel userModel;

    final String FIELD_REQUIRED = "Field tidak boleh kosong";
    final String FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik";
    final String FIELD_IS_NOT_VALID = "Email tidak valid";
    public final static String KEY_VALUE = "KEY_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user_preference);

        edtName = findViewById(R.id.edt_name);
        edtAge = findViewById(R.id.edt_age);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        rgLoveMu = findViewById(R.id.rg_love_mu);
        rbYes = findViewById(R.id.rb_yes);
        rbNo = findViewById(R.id.rb_no);
        Intent i = getIntent();
        userModel = i.getParcelableExtra("USER");
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);

        String actionBarTitle = "";
        String btnTitle = "";

        switch (formType) {
            case TYPE_ADD:
                actionBarTitle = "Tambah Baru";
                btnTitle = "Simpan";
                break;
            case TYPE_EDIT:
                actionBarTitle = "Ubah";
                btnTitle = "Update";
                showPreferenceInForm();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setText(btnTitle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPreferenceInForm() {
        edtName.setText(userModel.getName());
        edtEmail.setText(userModel.getEmail());
        edtAge.setText(String.valueOf(userModel.getAge()));
        edtPhone.setText(userModel.getPhoneNumber());
        if (userModel.isLove()) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String age = edtAge.getText().toString().trim();
            String phoneNo = edtPhone.getText().toString().trim();
            boolean isLoveMU = rgLoveMu.getCheckedRadioButtonId() == R.id.rb_yes;

            if (TextUtils.isEmpty(name)) {
                edtName.setError(FIELD_REQUIRED);
                return;
            }

            if (TextUtils.isEmpty(email)) {
                edtEmail.setError(FIELD_REQUIRED);
                return;
            }

            if (!isValidEmail(email)) {
                edtEmail.setError(FIELD_IS_NOT_VALID);
                return;
            }

            if (TextUtils.isEmpty(age)) {
                edtAge.setError(FIELD_REQUIRED);
                return;
            }

            if (TextUtils.isEmpty(phoneNo)) {
                edtPhone.setError(FIELD_REQUIRED);
                return;
            }

            if (!TextUtils.isDigitsOnly(phoneNo)) {
                edtPhone.setError(FIELD_DIGIT_ONLY);
                return;
            }

            UserPreference userPreference = new UserPreference(this);
            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setAge(Integer.parseInt(age));
            userModel.setPhoneNumber(phoneNo);
            userModel.setLove(isLoveMU);
            userPreference.setUser(userModel);
            Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(KEY_VALUE, userModel);
            setResult(RESULT_CODE, resultIntent);
            finish();
        }
    }


    /**
     * Cek apakah emailnya valid
     *
     * @param email inputan email
     * @return true jika email valid
     */

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
