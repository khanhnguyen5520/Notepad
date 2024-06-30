package com.example.notepad.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.CategoryRepository
import com.example.notepad.R
import com.example.notepad.adapter.CategoryAdapter
import com.example.notepad.databinding.ActivityCategoriesBinding
import com.example.notepad.model.Category

class CategoryActivity : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var saveButton: Button

    //private lateinit var adapter: CategoryAdapter
    private val categories = mutableListOf<Category>()
    private lateinit var binding: ActivityCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_categories)

        categoryNameEditText = findViewById(R.id.categoryName)
        saveButton = findViewById(R.id.btnAddCategory)

        //adapter = CategoryAdapter(categories)
        binding.cateRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CategoryAdapter(CategoryRepository.categories)
        binding.cateRecyclerView.adapter = adapter

        saveButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()
//            if (categoryName.isNotEmpty()) {
//                val newCategory = Category(categoryName)
//                categories.add(newCategory)
//                categoryAdapter.notifyDataSetChanged()
//
//                // Return the new category name to MainActivity
//                val resultIntent = Intent().apply {
//                    putExtra(MainActivity.EXTRA_CATEGORY_NAME, categoryName)
//                }
//                setResult(Activity.RESULT_OK, resultIntent)
//                finish()
//            } else {
//                Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show()
//            }
            if (categoryName.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra(MainActivity.EXTRA_CATEGORY_NAME, categoryName)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}