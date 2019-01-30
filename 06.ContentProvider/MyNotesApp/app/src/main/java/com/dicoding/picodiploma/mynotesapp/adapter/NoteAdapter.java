package com.dicoding.picodiploma.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dicoding.picodiploma.mynotesapp.CustomOnItemClickListener;
import com.dicoding.picodiploma.mynotesapp.FormAddUpdateActivity;
import com.dicoding.picodiploma.mynotesapp.R;
import com.dicoding.picodiploma.mynotesapp.entity.Note;

import java.util.ArrayList;

import static com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;

/**
 * Created by sidiqpermana on 11/23/16.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewholder> {
    private final ArrayList<Note> listNotes = new ArrayList<>();
    private final Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Note> listNotes) {
        this.listNotes.clear();
        this.listNotes.addAll(listNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewholder holder, int position) {
        holder.tvTitle.setText(getListNotes().get(position).getTitle());
        holder.tvDate.setText(getListNotes().get(position).getDate());
        holder.tvDescription.setText(getListNotes().get(position).getDescription());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormAddUpdateActivity.class);

                // Set intent dengan data uri row note by id
                // content://com.dicoding.picodiploma.mynotesapp/note/id
                Uri uri = Uri.parse(CONTENT_URI + "/" + getListNotes().get(position).getId());
                intent.setData(uri);
                intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE, listNotes.get(position));
                activity.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    class NoteViewholder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription, tvDate;
        final CardView cvNote;

        NoteViewholder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
