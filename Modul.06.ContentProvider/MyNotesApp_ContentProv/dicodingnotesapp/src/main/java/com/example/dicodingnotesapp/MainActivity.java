package com.example.dicodingnotesapp;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dicodingnotesapp.adapter.DicodingNotesAdapter;
import com.example.dicodingnotesapp.entity.NoteItem;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener{

    private DicodingNotesAdapter dicodingNotesAdapter;
    private ListView lvNotes;

    private final int LOAD_NOTES_ID = 110;
    private static final String AUTHORITY = "com.dicoding.mynotesapp";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setTitle("Dicoding Notes");


        lvNotes = (ListView)findViewById(R.id.lv_notes);
        dicodingNotesAdapter = new DicodingNotesAdapter(this, null, true);
        lvNotes.setAdapter(dicodingNotesAdapter);
        lvNotes.setOnItemClickListener(this);


        getSupportLoaderManager().initLoader(LOAD_NOTES_ID, null, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_NOTES_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dicodingNotesAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dicodingNotesAdapter.swapCursor(null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_NOTES_ID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) dicodingNotesAdapter.getItem(i);


        NoteItem item = new NoteItem();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        item.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));


        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        intent.putExtra(FormActivity.EXTRA_NOTE_ITEM, item);
        startActivity(intent);
    }
}

