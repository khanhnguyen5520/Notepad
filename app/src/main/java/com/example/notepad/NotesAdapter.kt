package com.example.notepad

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.activity.UpdateNoteActivity
import com.example.notepad.model.MainViewModel
import com.example.notepad.model.Note

class NotesAdapter(private var notes: List<Note>, context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    var activity: Activity? = null
    var arrayList: ArrayList<Note>? = null
    var selectList: ArrayList<Note>? = null
    var mainViewModel= MainViewModel()

    var isEnabled: Boolean = false
    var isSelectAll: Boolean = false
    private var isClicked: Boolean =false

    private var selected = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val bgItem: LinearLayout = itemView.findViewById(R.id.bgItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent, false)

        //mainViewModel =ViewModelProvider((activity as FragmentActivity?)!!)[MainViewModel::class.java]

        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id",note.id)
            }
            holder.itemView.context.startActivity(intent)

        }

//        holder.itemView.setOnLongClickListener { v ->
//            if (!isEnabled) {
//                val callback: ActionMode.Callback =
//                    object : ActionMode.Callback {
//                        override fun onCreateActionMode(
//                            mode: ActionMode,
//                            menu: Menu
//                        ): Boolean {
//                            val menuInflater = mode.menuInflater
//                            menuInflater.inflate(R.menu.custom_menu, menu)
//                            return true
//                        }
//
//                        override fun onPrepareActionMode(
//                            mode: ActionMode,
//                            menu: Menu
//                        ): Boolean {
//                            isEnabled = true
//                            ClickItem(holder)
//                            mainViewModel.getText().observe((activity as LifecycleOwner?)!!
//                            ) { s -> mode.title = String.format("%s Selected", s) }
//                            return true
//                        }
//
//                        override fun onActionItemClicked(
//                            mode: ActionMode,
//                            item: MenuItem
//                        ): Boolean {
//                            val menuItem = item.itemId
//                            if (menuItem == R.id.delete) {
//                                for (s in selectList!!) {
//                                    arrayList!!.remove(s)
//                                }
//                                if (arrayList!!.isEmpty()) {
//                                }
//                                mode.finish()
//                            }
//                            if (menuItem == R.id.select_all) {
//                                if (selectList!!.size == arrayList!!.size) {
//                                    isSelectAll = false
//                                    selectList!!.clear()
//                                } else {
//                                    isSelectAll = true
//                                    selectList!!.clear()
//                                    selectList!!.addAll(arrayList!!)
//                                }
//                                mainViewModel.setText(selectList!!.size.toString())
//                                notifyDataSetChanged()
//                            }
//                            return true
//                        }
//
//                        override fun onDestroyActionMode(mode: ActionMode) {
//                            isEnabled = false
//                            isSelectAll = false
//                            selectList!!.clear()
//                            notifyDataSetChanged()
//                        }
//                    }
//
//                (v.context as AppCompatActivity).startActionMode(callback)
//            } else {
//                ClickItem(holder)
//            }
//            true
//        }

//        if (isSelectAll) {
//            holder.bgItem.setBackgroundResource(R.drawable.rounded_selected_item)
//        } else {
//            holder.bgItem.setBackgroundResource(R.drawable.rounded_item)
//        }

    }

//    private fun ClickItem(holder: ViewHolder) {
//        val s: Note? = arrayList?.get(holder.adapterPosition)
//        if (!isClicked) {
//            isClicked= true
//            s?.let { selectList?.add(it) }
//        } else {
//            isClicked = false
//            selectList!!.remove(s)
//        }
//        mainViewModel.setText(selectList!!.size.toString())
//    }
    fun refreshData(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }

    fun selectAll() {
        selected.clear()
        selected.addAll(notes)
        notifyDataSetChanged()
    }
}