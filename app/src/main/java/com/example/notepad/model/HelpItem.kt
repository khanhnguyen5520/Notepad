package com.example.notepad.model

import com.google.gson.annotations.SerializedName

data class HelpItem(
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String,
    var isExpanded: Boolean = false
)
