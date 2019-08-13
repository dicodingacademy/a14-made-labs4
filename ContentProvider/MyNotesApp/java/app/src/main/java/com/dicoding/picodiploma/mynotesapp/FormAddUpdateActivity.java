package com.dicoding.picodiploma.mynotesapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dicoding.picodiploma.mynotesapp.entity.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;

public class FormAddUpdateActivity extends AppCompatActivity
        implements View.OnClickListener {
    private EditText edtTitle, edtDescription;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;

    public static final int REQUEST_ADD = 100;
    public static final int REQUEST_UPDATE = 200;

    private Note note;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }
        // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        // Jika uri nya kosong berarti modenya adalah insert
        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {

                if (cursor.moveToFirst()) note = new Note(cursor);
                cursor.close();
            }
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {

            actionBarTitle = "Ubah";
            btnTitle = "Update";

            edtTitle.setText(note.getTitle());
            edtDescription.setText(note.getDescription());
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(actionBarTitle);

        btnSubmit.setText(btnTitle);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            boolean isEmpty = false;

             /*
            Jika fieldnya masih kosong maka tampilkan error
             */

            if (TextUtils.isEmpty(title)) {
                isEmpty = true;
                edtTitle.setError("Field can not be blank");
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, note);
            intent.putExtra(EXTRA_POSITION, position);

            note.setTitle(title);
            note.setDescription(description);

            if (!isEmpty) {

                // Gunakan contentvalues untuk menampung data
                ContentValues values = new ContentValues();
                values.put(TITLE, title);
                values.put(DESCRIPTION, description);

                /*
                Jika merupakan edit setresultnya UPDATE, dan jika bukan maka setresultnya ADD
                 */
                if (isEdit) {

                    // Gunakan uri dari intent activity ini
                    // content://com.dicoding.picodiploma.mynotesapp/note/id
                    getContentResolver().update(getIntent().getData(), values, null, null);
                    Toast.makeText(FormAddUpdateActivity.this, "Satu item berhasil diedit", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    values.put(DATE, getCurrentDate());
                    note.setDate(getCurrentDate());
                    // Gunakan content uri untuk insert
                    // content://com.dicoding.picodiploma.mynotesapp/note/
                    Toast.makeText(FormAddUpdateActivity.this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show();
                    getContentResolver().insert(CONTENT_URI, values);

                    finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ALERT_DIALOG_DELETE = 20;
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;

            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private final int ALERT_DIALOG_CLOSE = 10;

    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
    */

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            // Gunakan uri dari intent activity ini
                            // content://com.dicoding.picodiploma.mynotesapp/note/id
                            getContentResolver().delete(getIntent().getData(), null, null);
                            Toast.makeText(FormAddUpdateActivity.this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }
}
