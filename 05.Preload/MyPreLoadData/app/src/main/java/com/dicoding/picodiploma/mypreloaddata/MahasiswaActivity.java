package com.dicoding.picodiploma.mypreloaddata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dicoding.picodiploma.mypreloaddata.adapter.MahasiswaAdapter;
import com.dicoding.picodiploma.mypreloaddata.database.MahasiswaHelper;
import com.dicoding.picodiploma.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MahasiswaAdapter mahasiswaAdapter;
    MahasiswaHelper mahasiswaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);
        recyclerView = findViewById(R.id.recyclerview);

        mahasiswaHelper = new MahasiswaHelper(this);
        mahasiswaAdapter = new MahasiswaAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(mahasiswaAdapter);

        mahasiswaHelper.open();

        // Ambil semua data mahasiswa di database
        ArrayList<MahasiswaModel> mahasiswaModels = mahasiswaHelper.getAllData();

        mahasiswaHelper.close();

        mahasiswaAdapter.setData(mahasiswaModels);
    }
}
