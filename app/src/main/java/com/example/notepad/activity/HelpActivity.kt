package com.example.notepad.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.HelpItemAdapter
import com.example.notepad.databinding.ActivityHelpBinding
import com.example.notepad.model.HelpItem


class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding
    private lateinit var parentList: ArrayList<HelpItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbHelp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        parentList = ArrayList()

        prepareData()
        val adapter = HelpItemAdapter(parentList)
        binding.recyclerView.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun prepareData() {
        val childItems1="Con1"
        parentList.add(HelpItem(1,"Expand all answers", childItems1))
        val childItem2 = "Con2"
        parentList.add(HelpItem(2,"How can I use the app?",  childItem2))
        val childItem3 = "Con3"
        parentList.add(HelpItem(3,"Cursor is jumping to the start/end after the latest update. What happened?",childItem3))
        val childItem4 = "Con4"
        parentList.add(HelpItem(4,"My notes are lost. How can I recover them?",childItem4))
        val childItem5 = "Con5"
        parentList.add(HelpItem(5,"How can I transfer notes to a new device?",childItem5))

    }

}