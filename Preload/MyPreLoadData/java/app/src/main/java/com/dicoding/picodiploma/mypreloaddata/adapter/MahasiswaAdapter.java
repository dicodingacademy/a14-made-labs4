package com.dicoding.picodiploma.mypreloaddata.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.mypreloaddata.R;
import com.dicoding.picodiploma.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dicoding on 12/6/2016.
 */
public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder> {
    private final ArrayList<MahasiswaModel> listMahasiswa = new ArrayList<>();

    public MahasiswaAdapter() {
    }

    public void setData(ArrayList<MahasiswaModel> listMahasiswa) {

        if (listMahasiswa.size() > 0) {
            this.listMahasiswa.clear();
        }

        this.listMahasiswa.addAll(listMahasiswa);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MahasiswaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa_row, parent, false);
        return new MahasiswaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaHolder holder, int position) {
        holder.bind(listMahasiswa.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    static class MahasiswaHolder extends RecyclerView.ViewHolder {
        private final TextView textViewNim;
        private final TextView textViewName;
        private final ImageView imageViewIcon;

        MahasiswaHolder(View itemView) {
            super(itemView);

            textViewNim = itemView.findViewById(R.id.txt_nim);
            textViewName = itemView.findViewById(R.id.txt_name);
            imageViewIcon = itemView.findViewById(R.id.imageView);
        }

        void bind(MahasiswaModel mahasiswa) {
            textViewNim.setText(mahasiswa.getNim());
            textViewName.setText(mahasiswa.getName());

            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            imageViewIcon.setColorFilter(color);
        }

    }
}