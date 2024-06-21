package com.example.notepad.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.DAO.NotesDatabaseHelper
import com.example.notepad.databinding.ActivityAddNoteBinding
import com.example.notepad.model.Note

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        setSupportActionBar(binding.toolbarUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveButton.setOnClickListener {
            var title = binding.edtTitle.text.toString()
            val content = binding.edtContent.text.toString()
            if(title.isEmpty()){
                title = "Untitled"
            }
            val note = Note(0, title,content)
            db.insertNote(note)
            finish()
            Toast.makeText(this,"Note saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }else -> super.onOptionsItemSelected(item)
       }
    }
}