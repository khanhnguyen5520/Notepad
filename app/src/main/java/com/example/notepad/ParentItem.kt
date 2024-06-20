package com.example.notepad

data class ParentItem(
    val title : String,
    val childItem : String,
    var isExpandable : Boolean = false
)
