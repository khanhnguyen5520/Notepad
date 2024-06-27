package com.example.notepad.model

data class Note(var id: Int, val title:String, val content:String, val creDate: String, val editDate: String){
    lateinit var color: String
}
