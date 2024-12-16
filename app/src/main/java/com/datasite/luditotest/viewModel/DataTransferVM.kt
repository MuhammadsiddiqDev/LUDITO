package com.datasite.luditotest.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DataTransferVM: ViewModel() {
    val title = mutableStateOf("")
    val subTitle = mutableStateOf("")
    val latitude = mutableStateOf(0.0)
    val longitude = mutableStateOf(0.0)

    private val _selectedData = MutableLiveData<Map<String, Any>>()
    val selectedData: LiveData<Map<String, Any>> = _selectedData



    fun updateData() {
        _selectedData.value = mapOf(
            "title" to title.value,
            "subTitle" to subTitle.value,
            "latitude" to latitude.value,
            "longitude" to longitude.value,
        )
    }
}