package com.example.notepad

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        setSupportActionBar(binding.toolbarUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updateNote = Note(noteId,newTitle,newContent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this,"Changes Save",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_menu_update, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.redo -> {
                // Handle Option 1 click
                true
            }
            R.id.undo -> {
                // Handle Option 2 click
                true
            }
            R.id.share -> {
                // Handle Option 1 click
                true
            }
            R.id.delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("The '${binding.updateTitleEditText.text}' note will be deleted, Are you sure?")
                    .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                        db.deleteNote(noteId)
                        startActivity(Intent(this,MainActivity::class.java))
                        Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                    .show()

                true
            }
            R.id.search -> {
                // Handle Option 1 click
                true
            }
            R.id.exportTextFile -> {
                // Handle Option 2 click
                true
            }
            R.id.category -> {
                // Handle Option 1 click
                true
            }
            R.id.colorize -> {
                // Handle Option 2 click
                true
            }
            R.id.switchReadMode -> {
                // Handle Option 1 click
                true
            }
            R.id.print -> {
                // Handle Option 2 click
                true
            }
            R.id.showFormatBar -> {
                // Handle Option 1 click
                true
            }
            R.id.info -> {
                // Handle Option 2 click
                true
            }
            android.R.id.home -> {
                onBackPressed()  // Handle the back button click
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}