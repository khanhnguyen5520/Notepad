package com.example.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.model.HelpItem

class HelpItemAdapter(private val parentItemList: List<HelpItem>) :
    RecyclerView.Adapter<HelpItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentTitle: TextView = itemView.findViewById(R.id.parentTitleTv)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.childRecyclerView)
        val lItem: LinearLayout = itemView.findViewById(R.id.lItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.help_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val parentItem = parentItemList[position]

        holder.parentTitle.text = parentItem.title

        holder.childRecyclerView.setHasFixedSize(true)
        holder.childRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)

        val isExpandable = parentItem.isExpandable
        holder.childRecyclerView.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.lItem.setOnClickListener {
            isAnyItemExpanded(position)
            parentItem.isExpandable = !parentItem.isExpandable
            notifyItemChanged(position)
        }
    }
    private fun isAnyItemExpanded(position: Int) {

        val temp = parentItemList.indexOfFirst {
            it.isExpandable
        }

        if (temp >= 0 && temp != position) {
            parentItemList[temp].isExpandable = false
            notifyItemChanged(temp)
        }
    }

    override fun getItemCount(): Int {
        return parentItemList.size
    }
}