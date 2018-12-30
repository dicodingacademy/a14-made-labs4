package com.dicoding.picodiploma.dicodingnotesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dicoding.picodiploma.dicodingnotesapp.CustomOnItemClickListener;
import com.dicoding.picodiploma.dicodingnotesapp.FormActivity;
import com.dicoding.picodiploma.dicodingnotesapp.R;
import com.dicoding.picodiploma.dicodingnotesapp.entity.NoteItem;

import java.util.ArrayList;

import static com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract.NoteColumns.TITLE;
import static com.dicoding.picodiploma.dicodingnotesapp.db.DatabaseContract.getColumnString;

/**
 * Created by dicoding on 12/13/2016.
 */

public class DicodingNotesAdapter extends RecyclerView.Adapter<DicodingNotesAdapter.NoteViewHolder> {

    private final ArrayList<NoteItem> listNotes = new ArrayList<>();
    private final Activity activity;

    public DicodingNotesAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<NoteItem> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<NoteItem> listNotes) {
        if (listNotes.size() > 0){
            this.listNotes.clear();
        }
        this.listNotes.addAll(listNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dicoding_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int i) {
        holder.tvTitle.setText(getListNotes().get(i).getTitle());
        holder.tvDate.setText(getListNotes().get(i).getDate());
        holder.tvDescription.setText(getListNotes().get(i).getDescription());
        holder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormActivity.class);

                // Set intent dengan data uri row note by id
                // content://com.dicoding.picodiploma.mynotesapp/note/id
                Uri uri = Uri.parse(CONTENT_URI + "/" + getListNotes().get(position).getId());
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription, tvDate;
        View rootView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
        }
    }
}