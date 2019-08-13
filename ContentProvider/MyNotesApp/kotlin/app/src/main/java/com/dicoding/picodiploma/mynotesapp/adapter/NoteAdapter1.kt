package com.dicoding.picodiploma.mynotesapp.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.mynotesapp.CustomOnItemClickListener
import com.dicoding.picodiploma.mynotesapp.FormAddUpdateActivity
import com.dicoding.picodiploma.mynotesapp.R
import com.dicoding.picodiploma.mynotesapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.mynotesapp.entity.Note
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*

/**
 * Created by sidiqpermana on 11/23/16.
 */

class NoteAdapter(private val activity: Activity) : RecyclerView.Adapter<NoteAdapter.NoteViewholder>() {
    var listNotes = ArrayList<Note>()
        set(listNotes) {
            this.listNotes.clear()
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewholder(view)
    }

    override fun onBindViewHolder(holder: NoteViewholder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int {
        return this.listNotes.size
    }

    inner class NoteViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) {
            with(itemView){
                tv_item_title.text = note.title
                tv_item_date.text = note.date
                tv_item_description.text = note.description
                cv_item_note.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, FormAddUpdateActivity::class.java)

                        // Set intent dengan data uri row note by id
                        // content://com.dicoding.picodiploma.mynotesapp/note/id
                        val uri = Uri.parse(CONTENT_URI.toString() + "/" + note.id)
                        intent.data = uri
                        intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE, note)
                        activity.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}
