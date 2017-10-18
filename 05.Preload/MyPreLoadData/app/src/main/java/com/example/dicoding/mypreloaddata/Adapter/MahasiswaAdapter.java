package com.example.dicoding.mypreloaddata.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dicoding.mypreloaddata.Database.MahasiswaHelper;
import com.example.dicoding.mypreloaddata.Model.MahasiswaModel;
import com.example.dicoding.mypreloaddata.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dicoding on 12/6/2016.
 */
public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder> {
    private ArrayList<MahasiswaModel> mData = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;

    public MahasiswaAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MahasiswaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa_row, parent, false);
        return new MahasiswaHolder(view);
    }

    public void addItem(ArrayList<MahasiswaModel> mData) {

        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MahasiswaHolder holder, int position) {
        holder.textViewNim.setText(mData.get(position).getNim());
        holder.textViewNama.setText(mData.get(position).getName());

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MahasiswaHolder extends RecyclerView.ViewHolder {
        private TextView textViewNim;
        private TextView textViewNama;

        public MahasiswaHolder(View itemView) {
            super(itemView);

            textViewNim = (TextView) itemView.findViewById(R.id.txt_nim);
            textViewNama = (TextView) itemView.findViewById(R.id.txt_nama);
        }

    }
}