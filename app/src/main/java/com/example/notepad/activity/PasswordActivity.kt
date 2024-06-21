package com.example.notepad.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.R
import com.example.notepad.databinding.ActivityPasswordBinding


class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.lShow.setOnClickListener {
            binding.swShow.isChecked = !binding.swShow.isChecked
        }
        binding.lBiometrics.setOnClickListener {
            binding.swBiometrics.isChecked = !binding.swBiometrics.isChecked
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

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    fun showPopup(view: View) {

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_layout,null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.elevation = 20F


        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val v = popupWindow.contentView
        val btnClose = v.findViewById<TextView>(R.id.btnCancel)
        val btnOk = v.findViewById<TextView>(R.id.btnOk)
        val edtNew = v.findViewById<EditText>(R.id.edtNewPass)
        val edtRepeat = v.findViewById<EditText>(R.id.edtRepeatPass)
        val edtEmail = v.findViewById<EditText>(R.id.edtEmail)
        val tvError = v.findViewById<TextView>(R.id.tvError)


        btnClose.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }

        btnOk.setOnTouchListener { v, event ->
            if (edtNew.text.isEmpty() && edtRepeat.text.isEmpty()) {
                if (edtEmail.text.isEmpty() || !isValidEmail(edtEmail.text)) {
                    tvError.text = "Invalid email address"
                }
            } else {
                if (edtNew.text.isEmpty() || edtRepeat.text.isEmpty()) {
                    tvError.text = "Repeated password is different"
                } else {
                    password = edtNew.text.toString()
                    Toast.makeText(this,"Saved $password", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}