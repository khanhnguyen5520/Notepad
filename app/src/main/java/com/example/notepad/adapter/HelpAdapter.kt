package com.example.notepad.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.model.HelpItem

class HelpAdapter(private val helpItems: List<HelpItem>) : RecyclerView.Adapter<HelpAdapter.HelpViewHolder>() {

    inner class HelpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.help_item, parent, false)
        return HelpViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        val currentItem = helpItems[position]
        holder.questionTextView.text = currentItem.question
        holder.answerTextView.text = currentItem.answer

        // Toggle the visibility of the answer
        holder.itemView.setOnClickListener {
            currentItem.isExpanded = !currentItem.isExpanded
            notifyItemChanged(position)
        }

        holder.answerTextView.visibility = if (currentItem.isExpanded) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = helpItems.size
}
