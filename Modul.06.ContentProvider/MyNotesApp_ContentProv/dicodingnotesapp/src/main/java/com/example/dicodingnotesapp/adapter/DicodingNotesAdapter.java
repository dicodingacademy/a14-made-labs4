package com.example.dicodingnotesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.dicodingnotesapp.R;

/**
 * Created by dicoding on 12/13/2016.
 */

public class DicodingNotesAdapter extends CursorAdapter {


    public static String FIELD_TITLE = "title";
    public static String FIELD_DESCRIPTION = "description";
    public static String FIELD_DATE = "date";


    public DicodingNotesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dicoding_note, viewGroup, false);
        return view;
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


            tvTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TITLE)));
            tvDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DATE)));
            tvDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DESCRIPTION)));
        }
    }
}