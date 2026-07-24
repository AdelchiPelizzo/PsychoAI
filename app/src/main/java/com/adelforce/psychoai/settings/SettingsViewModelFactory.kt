package com.adelforce.psychoai.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SettingsViewModelFactory(
    private val settingsDataStore: SettingsDataStore
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                SettingsViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                settingsDataStore
            ) as T
        }


        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}