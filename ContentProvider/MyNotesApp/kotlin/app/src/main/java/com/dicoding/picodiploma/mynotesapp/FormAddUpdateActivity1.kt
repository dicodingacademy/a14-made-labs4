package com.dicoding.picodiploma.mynotesapp

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.dicoding.picodiploma.mynotesapp.entity.Note
import kotlinx.android.synthetic.main.activity_form_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class FormAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false

    private var note: Note? = null
    private var position: Int = 0

    private val currentDate: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val REQUEST_UPDATE = 200
        private const val ALERT_DIALOG_CLOSE = 10
        private const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_add_update)

        btn_submit.setOnClickListener(this)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }
        // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        // Jika uri nya kosong berarti modenya adalah insert
        val uri = intent.data

        if (uri != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)

            if (cursor != null) {

                if (cursor.moveToFirst()) note = Note(cursor)
                cursor.close()
            }
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {

            actionBarTitle = "Ubah"
            btnTitle = "Update"

            note?.let { edt_title.setText(it.title) }
            note?.let { edt_description.setText(it.description) }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle

        btn_submit.text = btnTitle
    }


    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val title = edt_title.text.toString().trim { it <= ' ' }
            val description = edt_description.text.toString().trim { it <= ' ' }

            var isEmpty = false

            /*
            Jika fieldnya masih kosong maka tampilkan error
             */

            if (title.isEmpty()) {
                isEmpty = true
                edt_title.error = "Field can not be blank"
            }

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            note?.title = title
            note?.description = description

            if (!isEmpty) {

                // Gunakan contentvalues untuk menampung data
                val values = contentValuesOf(
                        TITLE to title,
                        DESCRIPTION to description
                )

                /*
                Jika merupakan edit setresultnya UPDATE, dan jika bukan maka setresultnya ADD
                 */
                if (isEdit) {

                    // Gunakan uri dari intent activity ini
                    // content://com.dicoding.picodiploma.mynotesapp/note/id
                    contentResolver.update(getIntent().data as Uri, values, null, null)
                    Toast.makeText(this@FormAddUpdateActivity, "Satu item berhasil diedit", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    values.put(DATE, currentDate)
                    note?.date = currentDate
                    // Gunakan content uri untuk insert
                    // content://com.dicoding.picodiploma.mynotesapp/note/
                    Toast.makeText(this@FormAddUpdateActivity, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show()
                    contentResolver.insert(CONTENT_URI, values)

                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)

            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
    */

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Note"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya") { dialog, id ->
                    if (isDialogClose) {
                        finish()
                    } else {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        // Gunakan uri dari intent activity ini
                        // content://com.dicoding.picodiploma.mynotesapp/note/id
                        contentResolver.delete(getIntent().data as Uri, null, null)
                        Toast.makeText(this@FormAddUpdateActivity, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
}
