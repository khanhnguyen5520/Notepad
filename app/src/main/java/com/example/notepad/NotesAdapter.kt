package com.example.notepad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.activity.UpdateActivity
import com.example.notepad.model.Note

class NotesAdapter(private var noteList: ArrayList<Note>) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {


    var activity: Activity? = null
    private var selected = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val bgItem: LinearLayout = itemView.findViewById(R.id.bgItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = noteList[position]
        holder.tvTitle.text = note.title
        holder.tvTime.text = "Last edit: ${note.editDate}"
        holder.bgItem.setBackgroundColor(Color.parseColor(note.color))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: ArrayList<Note>) {
        noteList = newNotes
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectAll() {
        selected.clear()
        selected.addAll(noteList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilterList(noteList: ArrayList<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }
}