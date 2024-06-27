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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.activity.UpdateActivity
import com.example.notepad.model.Note

class NotesAdapter(
    private var noteList: ArrayList<Note>,
    private val onMultiSelectModeChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()
    private var allItemsSelected = false
    private var multiSelectMode = false
        private set(value) {
            field = value
            onMultiSelectModeChanged(value)
        }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bind(note: Note, isSelected: Boolean) {
            tvTitle.text = note.title
            tvTime.text = "Last edit: ${note.editDate}"
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
            cardView.setCardBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = noteList[position]
        holder.bind(note, selectedItems.contains(position))

        holder.itemView.setOnClickListener {
            if (multiSelectMode) {
                toggleSelection(position)
            } else {
                val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                    putExtra("note_id", note.id)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (!multiSelectMode) {
                multiSelectMode = true
            }
            toggleSelection(position)
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: ArrayList<Note>) {
        noteList = newNotes
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilterList(noteList: ArrayList<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    private fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
            if (selectedItems.isEmpty()) {
                multiSelectMode = false
            }
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }

    fun toggleSelectAll() {
        if (allItemsSelected) {
            selectedItems.clear()
        } else {
            selectedItems.clear()
            selectedItems.addAll(noteList.indices)
        }
        allItemsSelected = !allItemsSelected
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItems.clear()
        allItemsSelected = false
        notifyDataSetChanged()
    }
    fun getSelectedItems(): Set<Int> = selectedItems
}