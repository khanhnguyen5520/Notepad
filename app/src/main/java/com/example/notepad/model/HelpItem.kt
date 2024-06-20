package com.example.notepad.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "helpItems")
data class HelpItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title : String,
    val childItem : String,
    var isExpandable : Boolean = false
)
