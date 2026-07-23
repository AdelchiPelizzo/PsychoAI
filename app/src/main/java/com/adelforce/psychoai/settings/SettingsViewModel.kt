package com.adelforce.psychoai.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {
    private val _settings =
        MutableStateFlow(
            PsychoSettings(),
        )

    val settings: StateFlow<PsychoSettings> =
        _settings.asStateFlow()

    fun setRecollection(value: Float) {
        _settings.value =
            _settings.value.copy(
                recollectionLevel = value,
            )
    }

    fun setInsight(value: Float) {
        _settings.value =
            _settings.value.copy(
                insightLevel = value,
            )
    }

    fun setConversation(value: Float) {
        _settings.value =
            _settings.value.copy(
                conversationLevel = value,
            )
    }
}
