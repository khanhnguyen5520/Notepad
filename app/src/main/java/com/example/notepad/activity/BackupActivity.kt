package com.example.notepad.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.databinding.ActivityBackupBinding

class BackupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBackupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbBackup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnShow.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            intent.putExtra("intruction","open")
            startActivity(intent)
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