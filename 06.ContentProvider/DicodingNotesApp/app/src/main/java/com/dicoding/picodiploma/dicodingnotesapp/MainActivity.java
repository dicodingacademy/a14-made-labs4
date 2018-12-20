package com.dicoding.picodiploma.dicodingnotesapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dicoding.picodiploma.dicodingnotesapp.adapter.DicodingNotesAdapter;
import com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract;

import java.lang.ref.WeakReference;

import static com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;


public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener, LoadNotesCallback {

    private DicodingNotesAdapter dicodingNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Dicoding Notes");

        ListView lvNotes = findViewById(R.id.lv_notes);
        dicodingNotesAdapter = new DicodingNotesAdapter(this, null, true);
        lvNotes.setAdapter(dicodingNotesAdapter);
        lvNotes.setOnItemClickListener(this);

        new getData(this, this).execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new getData(this, this).execute();
    }

    @Override
    public void postExecute(Cursor notes) {
        dicodingNotesAdapter.swapCursor(notes);
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;


        private getData(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) dicodingNotesAdapter.getItem(i);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        intent.setData(Uri.parse(CONTENT_URI + "/" + id));
        startActivity(intent);
    }
}

