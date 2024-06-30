package com.example.notepad.model

import android.os.Parcelable

data class Category(val name: String, val notes: MutableList<Note> = mutableListOf())
