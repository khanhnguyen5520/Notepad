package com.example.notepad.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val mutableLiveData = MutableLiveData<String>()

    fun setText(s: String){
        mutableLiveData.value = s
    }

    fun getText(): MutableLiveData<String>{
        return mutableLiveData
    }

}
