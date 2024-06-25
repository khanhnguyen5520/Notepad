package com.example.notepad.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.DAO.NotesDatabaseHelper
import com.example.notepad.NotesAdapter
import com.example.notepad.R
import com.example.notepad.databinding.ActivityTrashBinding
import com.example.notepad.model.Note
import com.google.android.material.navigation.NavigationView

class TrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrashBinding
    private lateinit var adapter: NotesAdapter
    private var deleteList = ArrayList<Note>()
    private lateinit var mainActivity: UpdateActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //db = NotesDatabaseHelper(this)
        setSupportActionBar(binding.tbTrash)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
}


}