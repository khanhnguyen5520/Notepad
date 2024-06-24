package com.example.notepad.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.notepad.DAO.NotesDatabaseHelper
import com.example.notepad.R
import com.example.notepad.databinding.ActivityUpdateBinding
import com.example.notepad.model.Note
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

@Suppress("DEPRECATION")
class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1
    private lateinit var note: Note
    private var defaultColor: Int = 0

    //get current datetime
    private val time = Calendar.getInstance().time

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
    private val editDate = formatter.format(time)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        defaultColor = ContextCompat.getColor(this, R.color.background)


        setSupportActionBar(binding.tbUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        note = db.getNoteByID(noteId)
        binding.edtUpdateTitle.setText(note.title)
        binding.edtUpdateContent.setText(note.content)

        //setBackground()
        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.edtUpdateTitle.text.toString()
            val newContent = binding.edtUpdateContent.text.toString()
            val updateNote = Note(noteId, newTitle, newContent, note.creDate, editDate)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this, "Changes Save", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun setBackground() {
//        binding.main.setBackgroundColor(note.color)
//        binding.main.setBackgroundColor(note.color)
//    }

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
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plan"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                intent.putExtra(Intent.EXTRA_TEXT, note.content)
                startActivity(Intent.createChooser(intent, "Share via"))
                true
            }

            R.id.delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("The '${binding.edtUpdateTitle.text}' note will be deleted, Are you sure?")
                    .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        db.deleteNote(noteId)
                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
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
                exportFile()
                true
            }

            R.id.category -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Categories can be added in the app's menu. To open the menu use the button in the top left corner of the note list screen.")
                    .setNegativeButton("OK") { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }.show()
                true
            }

            R.id.colorize -> {
                openColorPicker()
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
                true
            }

            R.id.info -> {
                infoPopup()
                true
            }

            android.R.id.home -> {
                onBackPressed()  // Handle the back button click
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exportFile() {

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TITLE, "${note.title}.txt")
        startActivityForResult(intent, 1)
    }

    private fun openColorPicker() {
        val ambilWarnaDialog =
            AmbilWarnaDialog(this, defaultColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    defaultColor = color
                    note.color = defaultColor
                }

            })
        ambilWarnaDialog.show()
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams", "SetTextI18n")
    private fun infoPopup() {
        val view = layoutInflater.inflate(R.layout.popup_info, null)

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_info, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.elevation = 20F

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        val v = popupWindow.contentView

        val tvWord = v.findViewById<TextView>(R.id.tvWord)
        val words = note.content.split(Regex("\\s+"))
        val count = words.filter { it.isNotBlank() }.size
        tvWord.text = "Words: $count"

        val tvLine = v.findViewById<TextView>(R.id.tvWrapped)
        if (note.content.lines().size == 1) {
            tvLine.text = "Wrapped lines: ${note.content.lines().size} "
        } else {
            tvLine.text = "Wrapped lines: ${note.content.lines().size - 1} "
        }

        val tvCharacter = v.findViewById<TextView>(R.id.tvCharacters)
        tvCharacter.text = "Characters: ${binding.edtUpdateContent.text.length} "

        val tvSpace = v.findViewById<TextView>(R.id.tvNoWhitespaces)
        val whitespace = binding.edtUpdateContent.text.replace("\\s".toRegex(), "")
        tvSpace.text = "Characters without whitespaces: ${whitespace.length}"

        val tvCre = v.findViewById<TextView>(R.id.tvCreDate)
        tvCre.text = "Created at: ${note.creDate}"

        val tvEdit = v.findViewById<TextView>(R.id.tvEdiDate)
        tvEdit.text = "Last saved at: ${note.editDate}"

        val btnOk = v.findViewById<TextView>(R.id.btnOk)
        btnOk.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }

    }

    private fun readMode(edt: EditText) {
        edt.isFocusable = false
        edt.isClickable = false
        edt.isLongClickable = false
        edt.keyListener = null
        binding.edtUpdateTitle.setOnClickListener {
            Toast.makeText(this, "Tap twice to edit", Toast.LENGTH_LONG).show()
        }
        binding.edtUpdateTitle.setOnClickListener {
            Toast.makeText(this, "Tap twice to edit", Toast.LENGTH_LONG).show()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {

                    val uri = data!!.data
                    val outputStream = contentResolver.openOutputStream(uri!!)
                    outputStream?.write(note.content.toByteArray())
                    outputStream?.close()

                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "faill no", Toast.LENGTH_SHORT).show()
            }
        }
    }
}