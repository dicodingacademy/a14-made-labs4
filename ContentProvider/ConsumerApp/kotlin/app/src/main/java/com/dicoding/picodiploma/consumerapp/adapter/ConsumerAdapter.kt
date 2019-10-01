package com.dicoding.picodiploma.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.consumerapp.CustomOnItemClickListener
import com.dicoding.picodiploma.consumerapp.FormActivity
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerapp.entity.NoteItem
import kotlinx.android.synthetic.main.item_consumer_note.view.*
import java.util.*

/**
 * Created by dicoding on 12/13/2016.
 */

class ConsumerAdapter(private val activity: Activity) : RecyclerView.Adapter<ConsumerAdapter.NoteViewHolder>() {

    var listNotes = ArrayList<NoteItem>()
        set(listNotes) {
            this.listNotes.clear()
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consumer_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, i: Int) {
        holder.bind(listNotes[i])
    }

    override fun getItemCount(): Int {
        return this.listNotes.size
    }

    inner class NoteViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(note: NoteItem) {
            with(itemView){
                tv_item_title.text = note.title
                tv_item_date.text = note.date
                tv_item_description.text = note.description
                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, FormActivity::class.java)

                        // Set intent dengan data uri row note by id
                        // content://com.dicoding.picodiploma.mynotesapp/note/id
                        val uri = "$CONTENT_URI/${listNotes[position].id}".toUri()
                        intent.data = uri
                        activity.startActivity(intent)
                    }
                }))
            }
        }
    }
}