package com.dicoding.picodiploma.consumerapp


import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.dicoding.picodiploma.consumerapp.entity.NoteItem
import kotlinx.android.synthetic.main.activity_form.*
import java.text.SimpleDateFormat
import java.util.*


class FormActivity : AppCompatActivity(), View.OnClickListener {
    private var noteItem: NoteItem? = null
    private var isUpdate = false
    private var resolver: ContentResolver? = null

    private val currentDate: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        
        resolver = contentResolver
        btn_submit.setOnClickListener(this)

        // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        // Jika uri nya kosong berarti modenya adalah insert
        val uri = intent.data

        if (uri != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)

            if (cursor != null) {

                if (cursor.moveToFirst()) noteItem = NoteItem(cursor)
                cursor.close()
            }
        }

        val actionBarTitle:String
        val btnActionTitle: String

        if (noteItem != null) {
            isUpdate = true
            actionBarTitle = "Update"
            btnActionTitle = "Simpan"

            noteItem?.let { edt_title.setText(it.title) }
            noteItem?.let { edt_description.setText(it.description) }

        } else {
            actionBarTitle = "Tambah Baru"
            btnActionTitle = "Submit"
        }
        btn_submit.text = btnActionTitle
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val title = edt_title.text.toString().trim { it <= ' ' }
            val description = edt_description.text.toString().trim { it <= ' ' }

            var isEmptyField = false
            if (title.isEmpty()) {
                isEmptyField = true
                edt_title.error = "Field tidak boleh kosong"
            }

            if (!isEmptyField) {
                val mContentValues = contentValuesOf(
                        TITLE to title,
                        DESCRIPTION to description,
                        DATE to currentDate
                )

                if (isUpdate) {
                    val uri = intent.data
                    resolver?.update(uri as Uri, mContentValues, null, null)
                    Toast.makeText(this, "Satu catatan berhasil diupdate", Toast.LENGTH_SHORT).show()
                } else {
                    resolver?.insert(CONTENT_URI, mContentValues)
                    Toast.makeText(this, "Satu catatan berhasil diinputkan", Toast.LENGTH_SHORT).show()
                }
                resolver?.notifyChange(CONTENT_URI, MainActivity.DataObserver(Handler(), this))
                finish()

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isUpdate) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == android.R.id.home -> finish()
            item.itemId == R.id.action_delete -> showDeleteAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showDeleteAlertDialog() {
        val dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
        val dialogTitle = "Hapus Note"
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya") { dialog, id ->
                    val uri = intent.data
                    resolver?.delete(uri as Uri, null, null)
                    resolver?.notifyChange(CONTENT_URI, MainActivity.DataObserver(Handler(), this@FormActivity))
                    Toast.makeText(this@FormActivity, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}