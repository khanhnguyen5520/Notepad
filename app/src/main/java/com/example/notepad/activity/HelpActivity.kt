package com.example.notepad.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.adapter.HelpAdapter
import com.example.notepad.databinding.ActivityHelpBinding
import com.example.notepad.model.HelpItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbHelp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val helpItems = loadHelpItems()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = HelpAdapter(helpItems)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadHelpItems(): List<HelpItem> {
        val inputStream = resources.openRawResource(R.raw.help_data)
        val reader = InputStreamReader(inputStream)
        val helpItemType = object : TypeToken<List<HelpItem>>() {}.type
        return Gson().fromJson(reader, helpItemType)
    }

}