package com.example.notepad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNote(),this)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addBtn.setOnClickListener{
            startActivity(Intent(this,AddNoteActivity::class.java))
        }

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.nav_open,R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNote())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_menu_main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.nav_edit -> Toast.makeText(this,"Edit",Toast.LENGTH_SHORT).show()
            R.id.nav_backup -> Toast.makeText(this,"Backup",Toast.LENGTH_SHORT).show()
            R.id.nav_trash -> Toast.makeText(this,"Trash",Toast.LENGTH_SHORT).show()
            R.id.nav_setting -> Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show()
            R.id.nav_ads -> Toast.makeText(this,"Ads",Toast.LENGTH_SHORT).show()
            R.id.nav_rate -> Toast.makeText(this,"Rate",Toast.LENGTH_SHORT).show()
            R.id.nav_help -> Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show()
            R.id.nav_privacy -> Toast.makeText(this,"Policy",Toast.LENGTH_SHORT).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select -> {
                // Handle Option 1 click
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val sActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data!!
            readFromUri(uri)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            10 -> {
                if(resultCode == RESULT_OK){
                    val path = data?.data?.path
                    binding.tvPath.text = path
                }
                true
            }
            else -> false
        }
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
        val title = getFileName(uri,applicationContext)
        val content = total.toString()
        val note = Note(5,title!!,content)
        db.insertNote(note)
        finish()
        Toast.makeText(this,"Imported", Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("Range")
    fun getFileName(uri: Uri, context: Context): String? {
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
                val cutt = res?.lastIndexOf("/")
                if (cutt != -1) {
                    res = res?.substring(cutt!! + 1)
                }
            }
        }
        return res
    }
}