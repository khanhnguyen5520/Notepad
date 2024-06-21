package com.example.notepad.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.DAO.NotesDatabaseHelper
import com.example.notepad.R
import com.example.notepad.databinding.ActivityUpdateNoteBinding
import com.example.notepad.model.Note

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        setSupportActionBar(binding.tbUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.edtUpdateTitle.setText(note.title)
        binding.edtUpdateContent.setText(note.content)

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.edtUpdateTitle.text.toString()
            val newContent = binding.edtUpdateContent.text.toString()
            val updateNote = Note(noteId, newTitle, newContent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this, "Changes Save", Toast.LENGTH_SHORT).show()
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
                builder.setMessage("The '${binding.edtUpdateTitle.text}' note will be deleted, Are you sure?")
                    .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                        db.deleteNote(noteId)
                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
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
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Categories can be added in the app's menu. To open the menu use the button in the top left corner of the note list screen.")
                    .setNegativeButton("OK") {
                        dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }.show()
                true
            }

            R.id.colorize -> {
                // Handle Option 2 click
                true
            }

            R.id.switchReadMode -> {
                readMode(binding.edtUpdateTitle)
                readMode(binding.edtUpdateContent)
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

    fun readMode(edt: EditText){
        edt.isFocusable = false
        edt.isClickable = false
        edt.isLongClickable = false
        edt.keyListener = null
        binding.edtUpdateTitle.setOnClickListener {
            Toast.makeText(this,"Tap twice to edit", Toast.LENGTH_LONG).show()
        }
        binding.edtUpdateTitle.setOnClickListener {
            Toast.makeText(this,"Tap twice to edit", Toast.LENGTH_LONG).show()
        }
    }

    fun writeMode(edt: EditText){
        edt.isFocusable = true
        edt.isClickable = true
        edt.isLongClickable = true
    }

}