package com.dicoding.picodiploma.mynotesapp

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mynotesapp.adapter.NoteAdapter
import com.dicoding.picodiploma.mynotesapp.databinding.ActivityMainBinding
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.mynotesapp.entity.Note
import com.dicoding.picodiploma.mynotesapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Notes"

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.setHasFixedSize(true)
        adapter = NoteAdapter(this)
        binding.rvNotes.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)?.also { adapter.listNotes = it }
        }

    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                // CONTENT_URI = content://com.dicoding.picodiploma.mynotesapp/note
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            binding.progressbar.visibility = View.INVISIBLE
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
                showSnackBarMessage("Tidak ada data saat ini")
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    /**
     * Tampilkan snackbar
     *
     * @param message inputan message
     */
    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvNotes, message, Snackbar.LENGTH_SHORT).show()
    }
}