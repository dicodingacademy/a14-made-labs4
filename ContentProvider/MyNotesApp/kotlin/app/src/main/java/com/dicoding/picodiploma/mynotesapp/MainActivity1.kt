package com.dicoding.picodiploma.mynotesapp

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mynotesapp.adapter.NoteAdapter
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.mynotesapp.entity.Note
import com.dicoding.picodiploma.mynotesapp.helper.MappingHelper.mapCursorToArrayList
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener, LoadNotesCallback {

    private lateinit var adapter: NoteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        private lateinit var handlerThread: HandlerThread
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Notes"

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)

        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

//        val myObserver = object : ContentObserver(handler) {
//            override fun onChange(self: Boolean) {
//                loadNoteAsync()
//            }
//        }
        val myObserver = DataObserver(handler, this)

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        fab_add.setOnClickListener(this)

        adapter = NoteAdapter(this)
        rv_notes.adapter = adapter

        if (savedInstanceState == null) {
            val loadNoteAsync = LoadNoteAsync(this, this)
            loadNoteAsync.doInBackground()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fab_add) {
            val intent = Intent(this@MainActivity, FormAddUpdateActivity::class.java)
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD)
        }
    }

    override fun preExecute() {
        runOnUiThread { progressbar.visibility = View.VISIBLE }
    }

    override fun postExecute(notes: Cursor) {
        progressbar.visibility = View.INVISIBLE

        val listNotes = mapCursorToArrayList(notes)
        if (listNotes.size > 0) {
            adapter.listNotes = listNotes
        } else {
            adapter.listNotes = ArrayList()
            showSnackbarMessage("Tidak ada data saat ini")
        }
    }

    /**
     * Tampilkan snackbar
     *
     * @param message inputan message
     */
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_notes, message, Snackbar.LENGTH_SHORT).show()
    }

    class DataObserver(handler: Handler, private var context: Context) : ContentObserver(handler) {
        override fun onChange(self: Boolean) {
            val loadNoteAsync = LoadNoteAsync(context, context as LoadNotesCallback)
            loadNoteAsync.doInBackground()
        }
    }

    private class LoadNoteAsync(context: Context, callback: LoadNotesCallback) {

        private val weakContext: WeakReference<Context> = WeakReference(context)
        private val weakCallback: WeakReference<LoadNotesCallback> = WeakReference(callback)

        fun onPreExecute() {
            weakCallback.get()?.preExecute()
        }

        fun doInBackground() {
            val context = weakContext.get()
            onPreExecute()
            GlobalScope.launch(Dispatchers.Main) {
                val notes = async(Dispatchers.IO) {
                    context?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                }
                onPostExecute(notes.await() as Cursor)
            }
        }

        fun onPostExecute(notes: Cursor) {
            weakCallback.get()?.postExecute(notes)
        }
    }

}