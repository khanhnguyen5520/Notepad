package com.example.notepad.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.R
import com.example.notepad.TrashRepository
import com.example.notepad.adapter.TrashAdapter
import com.example.notepad.databinding.ActivityTrashBinding
import com.example.notepad.model.Note

class TrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrashBinding
    private lateinit var adapter: TrashAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbTrash)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerViewTrash.layoutManager = LinearLayoutManager(this)

        adapter = TrashAdapter(TrashRepository.trashedNotes) { note ->
            showDeleteDialog(note)
        }
        binding.recyclerViewTrash.adapter = adapter

    }

    private fun showDeleteDialog(note: Note) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_trash, null)
        val noteTitle: TextView = dialogView.findViewById(R.id.dialogNoteTitle)
        val noteContent: TextView = dialogView.findViewById(R.id.dialogNoteContent)
        val rdDelete: RadioButton = dialogView.findViewById(R.id.rdDelete)
        val rdUndelete: RadioButton = dialogView.findViewById(R.id.rdUndelete)

        noteTitle.text = note.title
        noteContent.text = note.content

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Manage Note")
            .setPositiveButton("Delete") { _, _ ->
                TrashRepository.trashedNotes.remove(note)
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Undelete") { _, _ ->
                Log.e("undelete", "showDeleteDialog: ${note.title}")
                undeleteNote(note)
            }
            .show()
    }

    private fun undeleteNote(note: Note) {
        TrashRepository.trashedNotes.remove(note)
        adapter.notifyDataSetChanged()

        val intent = Intent().apply {
            putExtra("undeleted_note", note)
        }
        setResult(Activity.RESULT_OK, intent)
        Log.e("undelete", "showDeleteDialog: ${note.title} complete")
        finish()
    }

}