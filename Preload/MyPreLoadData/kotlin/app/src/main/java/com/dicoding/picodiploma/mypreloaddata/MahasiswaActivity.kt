package com.dicoding.picodiploma.mypreloaddata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mypreloaddata.adapter.MahasiswaAdapter
import com.dicoding.picodiploma.mypreloaddata.database.MahasiswaHelper
import kotlinx.android.synthetic.main.activity_mahasiswa.*

class MahasiswaActivity : AppCompatActivity() {

    private lateinit var mahasiswaAdapter: MahasiswaAdapter
    private lateinit var mahasiswaHelper: MahasiswaHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mahasiswa)

        mahasiswaHelper = MahasiswaHelper(this)
        mahasiswaAdapter = MahasiswaAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)

        recyclerview.adapter = mahasiswaAdapter

        mahasiswaHelper.open()

        // Ambil semua data mahasiswa di database
        val mahasiswaModels = mahasiswaHelper.allData

        mahasiswaHelper.close()

        mahasiswaAdapter.setData(mahasiswaModels)
    }
}
