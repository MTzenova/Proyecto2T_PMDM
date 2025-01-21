package com.example.proyecto2t_pmdm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel()
{
    private val _param = MutableLiveData<String>()
    val param: LiveData<String> get() = _param

    fun setParam(value: String) {
        _param.value = value
    }


}