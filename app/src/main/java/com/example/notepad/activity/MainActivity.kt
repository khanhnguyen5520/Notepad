package com.example.notepad.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.DAO.NotesDatabaseHelper
import com.example.notepad.NotesAdapter
import com.example.notepad.R
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.model.Note
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var adapter: NotesAdapter
    private var noteList = ArrayList<Note>()
    private var searchList = ArrayList<Note>()

    //get current datetime
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
    val current = formatter.format(time)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        getData()

        adapter = NotesAdapter(searchList)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = adapter

        binding.addBtn.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        //chạy quảng cáo
        ads()
    }

    private fun getData() {
        noteList = db.getAllNote()
        searchList.addAll(noteList)
    }

    override fun onResume() {
        super.onResume()
        adapter.refreshData(db.getAllNote())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu_main, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.notesRecyclerView.adapter!!.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {

                    noteList.forEach {

                        if (it.title.lowercase(Locale.getDefault()).contains(searchText)) {

                            searchList.add(it)

                        }
                    }

                } else {
                    searchList.clear()
                    searchList.addAll(noteList)

                }
                adapter.setFilterList(searchList)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.nav_edit -> {
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.nav_backup -> {
                startActivity(Intent(this, BackupActivity::class.java))
                true
            }

            R.id.nav_trash -> {
                Toast.makeText(this, "Trash", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.nav_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }

            R.id.nav_ads -> {
                Toast.makeText(this, "Ads", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.nav_rate -> {
                rateApp() // chưa public nên sẽ ko tìm thấy
                true
            }

            R.id.nav_help -> {
                startActivity(Intent(this, HelpActivity::class.java))
                true
            }

            R.id.nav_privacy -> {
                startActivity(Intent(this, PrivacyActivity::class.java))
                true
            }

            else -> false
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select -> {
                adapter.selectAll()
                true
            }

            R.id.imports -> {
                var import = Intent(Intent.ACTION_OPEN_DOCUMENT)
                import.setType("*/*")
                import = Intent.createChooser(import, "Choose a file")
                sActivityResultLauncher.launch(import)
                true
            }

            R.id.export -> {
                // Handle Option 1 click
                true
            }

            R.id.sort -> {
                sortFilter()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun sortFilter() {
        val view = layoutInflater.inflate(R.layout.popup_sort, null)

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_sort, null)

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
        val rdEditNewest = v.findViewById<RadioButton>(R.id.rdEditNewest)
        val rdEditOldest = v.findViewById<RadioButton>(R.id.rdEditOldest)
        val rdAZ = v.findViewById<RadioButton>(R.id.rdAZ)
        val rdZA = v.findViewById<RadioButton>(R.id.rdZA)
        val rdCreationNewest = v.findViewById<RadioButton>(R.id.rdCreationNewest)
        val rdCreationOldest = v.findViewById<RadioButton>(R.id.rdCreationOldest)
        val rdColor = v.findViewById<RadioButton>(R.id.rdColor)


        btnClose.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }

        btnOk.setOnTouchListener { _, _ ->

            if (rdAZ.isChecked) {
                searchList.sortByDescending {
                    it.title.lowercase()
                }
                searchList.reverse()
            }
            if (rdZA.isChecked) {
                searchList.reverse()
            }
            adapter.setFilterList(searchList)
            popupWindow.dismiss()
            true

            if (rdCreationNewest.isChecked) {
                searchList.sortByDescending {
                    it.creDate.lowercase()
                }
            }
            if (rdCreationOldest.isChecked) {
                searchList.reverse()
            }
            adapter.setFilterList(searchList)
            popupWindow.dismiss()
            true

            if (rdEditNewest.isChecked) {
                searchList.sortByDescending {
                    it.creDate.lowercase()
                }
            }
            if (rdEditOldest.isChecked) {
                searchList.reverse()
            }
            adapter.setFilterList(searchList)
            popupWindow.dismiss()
            true

        }
    }

    private val sActivityResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                readFromUri(uri)
            }
        }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri, context: Context): String? {
        var res: String? = null
        if (uri.scheme.equals("content")) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
            if (res == null) {
                res = uri.path
                val cut = res?.lastIndexOf("/")
                if (cut != -1) {
                    res = res?.substring(cut!! + 1)
                }
            }
        }
        return res
    }

    private fun readFromUri(uri: Uri) {
        val `in` = contentResolver.openInputStream(uri)
        val r = BufferedReader(InputStreamReader(`in`))
        val total = java.lang.StringBuilder()
        var line: String?
        try {
            while ((r.readLine().also { line = it }) != null) {
                total.append(line).append('\n')
            }
        } catch (_: Exception) {

        }
        val title = getFileName(uri, applicationContext)
        val content = total.toString()
        val note = Note(5, title!!, content, current, current)
        db.insertNote(note)
        Toast.makeText(this, "Imported", Toast.LENGTH_SHORT).show()
    }

    private fun rateApp() {
        val appPackageName = "com.example.notepad"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=$appPackageName")
            setPackage("com.android.vending") // Play Store app package
        }
        try {
            startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            // nếu chưa cài play store thì sẽ tìm trên web
            val playStoreUri =
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            val playStoreIntent = Intent(Intent.ACTION_VIEW, playStoreUri)
            startActivity(playStoreIntent)
        }
    }

    private fun ads() {
        //thêm quảng cáo
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }
}