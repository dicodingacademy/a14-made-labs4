package com.dicoding.picodiploma.mysharedpreference

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_form_user_preference.*

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {

    private var formType: Int = 0
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_user_preference)

        val intent = intent
        userModel = intent.getParcelableExtra("USER")
        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener(this)

        formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when (formType) {
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSave.text = btnTitle

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPreferenceInForm() {
        edt_name.setText(userModel.name)
        edt_email.setText(userModel.email)
        edt_age.setText(userModel.age.toString())
        edt_phone.setText(userModel.phoneNumber)
        if (userModel.isLove) {
            rb_yes.isChecked = true
        } else {
            rb_no.isChecked = true
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_save) {
            val name = edt_name.text.toString().trim { it <= ' ' }
            val email = edt_email.text.toString().trim { it <= ' ' }
            val age = edt_age.text.toString().trim { it <= ' ' }
            val phoneNo = edt_phone.text.toString().trim { it <= ' ' }
            val isLoveMU = rg_love_mu.checkedRadioButtonId == R.id.rb_yes

            if (TextUtils.isEmpty(name)) {
                edt_name.error = FIELD_REQUIRED
                return
            }

            if (TextUtils.isEmpty(email)) {
                edt_email.error = FIELD_REQUIRED
                return
            }

            if (!isValidEmail(email)) {
                edt_email.error = FIELD_IS_NOT_VALID
                return
            }

            if (TextUtils.isEmpty(age)) {
                edt_age.error = FIELD_REQUIRED
                return
            }

            if (TextUtils.isEmpty(phoneNo)) {
                edt_phone.error = FIELD_REQUIRED
                return
            }

            if (!TextUtils.isDigitsOnly(phoneNo)) {
                edt_phone.error = FIELD_DIGIT_ONLY
                return
            }

            saveUser(name, email, age, phoneNo, isLoveMU)

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)
            setResult(RESULT_CODE, resultIntent)

            finish()
        }
    }

    /*
    Save data ke dalam preferences
     */
    private fun saveUser(name: String, email: String, age: String, phoneNo: String, isLoveMU: Boolean) {
        val userPreference = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phoneNo
        userModel.isLove = isLoveMU

        userPreference.user = userModel
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }

    /**
     * Cek apakah emailnya valid
     *
     * @param email inputan email
     * @return true jika email valid
     */

    private fun isValidEmail(email: CharSequence): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {

        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2
        
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }
}
