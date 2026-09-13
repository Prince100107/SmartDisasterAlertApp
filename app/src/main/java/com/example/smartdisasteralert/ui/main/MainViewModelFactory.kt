package com.example.smartdisasteralert.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartdisasteralert.data.local.AppPreferences
import com.example.smartdisasteralert.data.repository.DisasterRepository

class MainViewModelFactory(
    private val repository: DisasterRepository,
    private val preferences: AppPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
