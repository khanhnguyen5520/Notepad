package com.example.notepad.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbSetting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvPass.setOnClickListener {
            startActivity(Intent(this, PasswordActivity::class.java))
        }
        binding.lMove.setOnClickListener {
            binding.swMove.isChecked = !binding.swMove.isChecked
        }
        binding.lAttach.setOnClickListener {
            binding.swAttach.isChecked = !binding.swAttach.isChecked
        }
        binding.lHide.setOnClickListener {
            binding.swHide.isChecked = !binding.swHide.isChecked
        }
        binding.lBackup.setOnClickListener {
            binding.swBackup.isChecked = !binding.swBackup.isChecked
        }
        binding.tvPrivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyActivity::class.java))
        }
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