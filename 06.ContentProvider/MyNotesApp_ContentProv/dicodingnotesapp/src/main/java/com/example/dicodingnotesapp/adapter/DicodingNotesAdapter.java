package com.example.dicodingnotesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.dicodingnotesapp.R;

import static com.example.dicodingnotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.example.dicodingnotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.example.dicodingnotesapp.db.DatabaseContract.NoteColumns.TITLE;
import static com.example.dicodingnotesapp.db.DatabaseContract.getColumnString;

/**
 * Created by dicoding on 12/13/2016.
 */

public class DicodingNotesAdapter extends CursorAdapter {

    public DicodingNotesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_dicoding_note, viewGroup, false);
    }


    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_item_title);
            TextView tvDate = (TextView)view.findViewById(R.id.tv_item_date);
            TextView tvDescription = (TextView)view.findViewById(R.id.tv_item_description);


            tvTitle.setText(getColumnString(cursor,TITLE));
            tvDescription.setText(getColumnString(cursor,DESCRIPTION));
            tvDate.setText(getColumnString(cursor,DATE));
        }
    }
}