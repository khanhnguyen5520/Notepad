package com.example.notepad.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.model.Note

class TrashAdapter(
    private val trashedNotes: List<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {

    class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return TrashViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        val note = trashedNotes[position]
        holder.tvTitle.text = note.title
        holder.tvTime.text = note.editDate
        holder.itemView.setOnClickListener {
            onNoteClick(note)
        }
    }

    override fun getItemCount() = trashedNotes.size
}
