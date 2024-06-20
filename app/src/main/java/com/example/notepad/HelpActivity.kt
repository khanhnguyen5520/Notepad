package com.example.notepad

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.blogc.android.views.ExpandableTextView
import com.example.notepad.databinding.ActivityHelpBinding


class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding
    private lateinit var parentRecyclerView: RecyclerView
    private lateinit var parentList: ArrayList<ParentItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbHelp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        parentRecyclerView = findViewById(R.id.parentRecyclerView)
        parentRecyclerView.setHasFixedSize(true)
        parentRecyclerView.layoutManager = LinearLayoutManager(this)
        parentList = ArrayList()

        prepareData()
        val adapter = ParentRecyclerViewAdapter(parentList)
        parentRecyclerView.adapter = adapter
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
        parentList.add(ParentItem("Expand all answers", childItems1))
        val childItem2 = "Con2"
        parentList.add(ParentItem("How can I use the app?",  childItem2))
        val childItem3 = "Con3"
        parentList.add(ParentItem("Cursor is jumping to the start/end after the latest update. What happened?",childItem3))
        val childItem4 = "Con4"
        parentList.add(ParentItem("My notes are lost. How can I recover them?",childItem4))
        val childItem5 = "Con5"
        parentList.add(ParentItem("How can I transfer notes to a new device?",childItem5))

    }

}