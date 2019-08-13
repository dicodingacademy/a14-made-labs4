package com.dicoding.picodiploma.consumerapp


import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consumerapp.MappingHelper.mapCursorToArrayList
import com.dicoding.picodiploma.consumerapp.adapter.ConsumerAdapter
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*


class MainActivity : AppCompatActivity(), LoadNotesCallback {

    private lateinit var consumerAdapter: ConsumerAdapter
    private var myObserver: DataObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer App"

        consumerAdapter = ConsumerAdapter(this)
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        rv_notes.adapter = consumerAdapter
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, this)
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver!!)
        val loadNoteAsync = LoadNoteAsync(this, this)
        loadNoteAsync.doInBackground()
    }

    override fun postExecute(notes: Cursor) {

        val listNotes = mapCursorToArrayList(notes)
        if (listNotes.size > 0) {
            consumerAdapter.listNotes = listNotes
        } else {
            Toast.makeText(this, "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show()
            consumerAdapter.listNotes = ArrayList()
        }
    }

    private class LoadNoteAsync(context: Context, callback: LoadNotesCallback) {

        private val weakContext: WeakReference<Context> = WeakReference(context)
        private val weakCallback: WeakReference<LoadNotesCallback> = WeakReference(callback)

        fun doInBackground() {
            val context = weakContext.get()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            val intent = Intent(this@MainActivity, FormActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    internal class DataObserver(handler: Handler, val context: Context) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val loadNoteAsync = LoadNoteAsync(context, context as MainActivity)
            loadNoteAsync.doInBackground()
        }
    }
}

