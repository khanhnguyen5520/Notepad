package com.example.notepad.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.databinding.ActivityPrivacyBinding

class PrivacyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbSetting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val content = readTextFile(this, "policy.txt")
        binding.edtPolicy.setText(content)
    }

    private fun readTextFile(context: Context, filename: String): String {
        val assetManager = context.assets
        return assetManager.open(filename).bufferedReader().use { it.readText() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()  // Handle the back button click
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}