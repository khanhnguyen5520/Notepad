package com.example.notepad.model

import java.io.Serializable

data class Note(
    var id: Int,
    val title: String,
    val content: String,
    val creDate: String,
    val editDate: String
) : Serializable {
    lateinit var color: String
}
