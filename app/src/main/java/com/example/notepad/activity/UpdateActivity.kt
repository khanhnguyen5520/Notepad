package com.example.notepad.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doOnTextChanged
import com.example.notepad.DAO.NotesDatabaseHelper
import com.example.notepad.R
import com.example.notepad.TrashRepository
import com.example.notepad.databinding.ActivityUpdateBinding
import com.example.notepad.model.Note
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Stack

@Suppress("DEPRECATION")
class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1
    private lateinit var note: Note
    private lateinit var currentColor: String
    private var deleteList = ArrayList<Note>()
    var currentSize = 18
    private lateinit var builder: SpannableStringBuilder
    private var start = 0
    var end = 0
    private val undoText = Stack<String>()
    private var isInUndoRedo = false


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

        binding.UpdateNote.setBackgroundColor(Color.parseColor(note.color))

        binding.btnUpdateSave.setOnClickListener {
            val newTitle = binding.edtUpdateTitle.text.toString()
            val newContent = binding.edtUpdateContent.text.toString()
            val updateNote = Note(noteId, newTitle, newContent, note.creDate, editDate)
            note.color = currentColor
            db.updateNote(updateNote)
            finish()
        }

        binding.edtUpdateContent.doOnTextChanged { _, _, _, _ ->
            binding.btnUndo.setTextColor(Color.WHITE)
        }
        binding.edtUpdateContent.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isInUndoRedo) {
                    val currentText = s.toString()
                    undoText.push(currentText)
                }
            }
        })

        binding.btnUndo.setOnClickListener {
            isInUndoRedo = true
            if (undoText.size > 1) {
                undoText.pop()
                val newText = undoText.peek()
                binding.edtUpdateContent.setText(newText)
            }
            if (undoText.size == 1) {
                binding.btnUndo.setTextColor(Color.GRAY)
            }
            isInUndoRedo = false
        }

        formatBar()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun formatBar() {

        binding.btnClose.setOnClickListener {
            binding.formatBar.visibility = View.GONE
        }

        binding.btnColorText.setOnClickListener {
            openColorPicker("text")
        }

        binding.btnTextSize.setOnClickListener {
            dialogTextSize()
            if (currentSize != 18) {
                binding.btnTextSize.setBackgroundColor(Color.GRAY)
            }
        }

        binding.btnBold.setOnClickListener {

            builder = SpannableStringBuilder(binding.edtUpdateContent.getText())
            start = binding.edtUpdateContent.selectionStart
            end = binding.edtUpdateContent.selectionEnd
            if (binding.btnBold.isClickable) {

                builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                builder.setSpan(
                    StyleSpan(Typeface.NORMAL),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                Toast.makeText(this, "no bold", Toast.LENGTH_SHORT).show()
                //binding.edtUpdateContent.setTypeface(null, Typeface.NORMAL)

            }
            binding.edtUpdateContent.text = builder
            // binding.btnBold.isClickable = !binding.btnBold.isClickable
        }

        binding.btnItalic.setOnClickListener {

            builder = SpannableStringBuilder(binding.edtUpdateContent.getText())
            start = binding.edtUpdateContent.selectionStart
            end = binding.edtUpdateContent.selectionEnd

            if (binding.btnItalic.isClickable) {

                builder.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    start,
                    end,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.btnItalic.isClickable = !binding.btnItalic.isClickable
                binding.edtUpdateContent.text = builder
            } else {
                binding.btnItalic.isClickable = !binding.btnItalic.isClickable
                builder.setSpan(
                    StyleSpan(Typeface.NORMAL),
                    start,
                    end,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                Toast.makeText(this, "no bold", Toast.LENGTH_SHORT).show()
                binding.edtUpdateContent.setTypeface(null, Typeface.NORMAL)
                binding.edtUpdateContent.text = builder
            }
        }
    }

    private fun dialogTextSize() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_text_size)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()

        val seekBar = dialog.findViewById<SeekBar>(R.id.seekBarSize)
        seekBar.progress = currentSize
        val text = dialog.findViewById<TextView>(R.id.textTest)
        val btnDefault = dialog.findViewById<Button>(R.id.btnDefault)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancelText)
        val btnOk = dialog.findViewById<TextView>(R.id.btnOkText)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentSize = progress
                text.text = "Text size: $currentSize"
                text.textSize = currentSize.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle when user starts touching SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle when user stops touching SeekBar
            }
        })
        btnDefault.setOnClickListener {
            currentSize = 18
            text.textSize = currentSize.toFloat()
            seekBar.progress = 18
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            builder = SpannableStringBuilder(binding.edtUpdateContent.getText())
            start = binding.edtUpdateContent.selectionStart
            end = binding.edtUpdateContent.selectionEnd
            val textSizeSpan = AbsoluteSizeSpan(currentSize, true)
            builder.setSpan(textSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.edtUpdateContent.text = builder
            dialog.dismiss()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu_update, menu)
        val searchItem = menu.findItem(R.id.noteSearch)
        val searchView = searchItem.actionView as SearchView

        val originalText = binding.edtUpdateContent.text.toString()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter your data based on newText and update the list

                val fullText = binding.edtUpdateContent.text.toString()

                val spannableString = SpannableStringBuilder(fullText)

                // Clear existing spans
                val spans = spannableString.getSpans(
                    0,
                    spannableString.length,
                    BackgroundColorSpan::class.java
                )
                for (span in spans) {
                    spannableString.removeSpan(span)
                }

                if (!newText.isNullOrEmpty() && fullText.contains(newText)) {
                    start = fullText.indexOf(newText)
                    while (start != -1) {
                        end = start + newText.length
                        if (start < end) {
                            val backgroundColorSpan = BackgroundColorSpan(Color.YELLOW)

                            spannableString.setSpan(
                                backgroundColorSpan,
                                start, end,
                                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        start = fullText.indexOf(newText, end)
                    }
                } else {
                    // Reset to original text if query is empty
                    spannableString.clear()
                    spannableString.append(originalText)
                }
                binding.edtUpdateContent.text = spannableString
                return true
            }
        })
        return true
    }

    @RequiresApi(35)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.redo -> {
                // Handle Option 1 click
                true
            }

            R.id.undoAll -> {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.apply {
                    setMessage("Remove all of the note changes note since the last opeing of the note?")
                    setPositiveButton("OK") { dialog, _ ->
                        val lst = undoText.removeFirst()
                        binding.edtUpdateContent.setText(lst)
                        dialog.dismiss()
                    }
                    setNegativeButton("CANCEL") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

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

                        TrashRepository.trashedNotes.add(note)
                        db.deleteNoteByID(noteId)
                        finish()
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }
                    .show()
                true
            }

            R.id.noteSearch -> {
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
                openColorPicker("background")
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
                binding.formatBar.visibility = View.VISIBLE
                true
            }

            R.id.info -> {
                infoPopup()
                true
            }

            android.R.id.home -> {
                onBackPressed()
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

    @SuppressLint("InflateParams")
    private fun openColorPicker(text: String) {

        val view = layoutInflater.inflate(R.layout.popup_color, null)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_color, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.elevation = 20F

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val v = popupWindow.contentView
        val colorPickerView = v.findViewById<ColorPickerView>(R.id.colorPickerView)
        val alphaSlideBar = v.findViewById<AlphaSlideBar>(R.id.alphaSlideBar)
        val brightnessSlideBar = v.findViewById<BrightnessSlideBar>(R.id.brightnessSlide)
        val test = v.findViewById<TextView>(R.id.testColor)

        colorPickerView.setColorListener(object : ColorEnvelopeListener {
            override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                test.setBackgroundColor(envelope!!.color)
                currentColor = "#" + envelope.hexCode
            }
        })


        val btnOk = v.findViewById<TextView>(R.id.btnColorOK)
        btnOk.setOnClickListener {
            if (text == "text") {
                builder = SpannableStringBuilder(binding.edtUpdateContent.getText())
                start = binding.edtUpdateContent.selectionStart
                end = binding.edtUpdateContent.selectionEnd
                val colorSpan = ForegroundColorSpan(Color.parseColor(currentColor))
                builder.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.edtUpdateContent.text = builder
            }
            if (text == "background") {
                binding.UpdateNote.setBackgroundColor(Color.parseColor(currentColor))
            }
            popupWindow.dismiss()
        }

        val btnCancel = v.findViewById<TextView>(R.id.btnColorCancel)
        btnCancel.setOnClickListener {
            popupWindow.dismiss()
        }
        colorPickerView.attachBrightnessSlider(brightnessSlideBar)
        colorPickerView.attachAlphaSlider(alphaSlideBar)
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
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
        }
    }
}