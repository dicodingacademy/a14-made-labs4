package com.dicoding.mynotesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.dicoding.mynotesapp.adapter.NoteAdapter;
import com.dicoding.mynotesapp.db.NoteHelper;
import com.dicoding.mynotesapp.entity.Note;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{
    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    private LinkedList<Note> list;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");

        rvNotes = (RecyclerView)findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        fabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        list = new LinkedList<>();

        adapter = new NoteAdapter(this);
        adapter.setListNotes(list);
        rvNotes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadNoteAsync().execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Note>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if (list.size() > 0){
                list.clear();
            }
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return noteHelper.getAllData();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list.addAll(notes);
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            if (list.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        Akan dipanggil jika result codenya ADD
         */
        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), 0);
            }
        }
        /*
        Akan dipanggil jika result codenya UPDATE
         */
        else if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE){
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                showSnackbarMessage("Satu item berhasil diubah");
                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), position);
            }
        }
        /*
        Akan dipanggil jika result codenya DELETE
         */
        else if (resultCode == FormAddUpdateActivity.RESULT_DELETE){
            int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
            list.remove(position);
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            showSnackbarMessage("Satu item berhasil dihapus");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    /**
     * Tampilkan snackbar dengan
     * @param message inputan message
     */
    private void showSnackbarMessage(String message){
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }
}
