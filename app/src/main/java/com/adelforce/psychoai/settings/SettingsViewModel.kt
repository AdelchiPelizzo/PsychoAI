package com.adelforce.psychoai.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adelforce.psychoai.util.ConversationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SettingsDataStore
) : ViewModel() {
    private val _settings =
        MutableStateFlow(
            PsychoSettings(),
        )

    val settings: StateFlow<PsychoSettings> =
        _settings.asStateFlow()

    private fun update(
        transform: (PsychoSettings)->PsychoSettings
    ) {

        val newSettings =
            transform(
                _settings.value
            )

        _settings.value =
            newSettings

        viewModelScope.launch {

            dataStore.save(
                newSettings
            )

            ConversationConfig.apply(
                SettingsMapper.map(
                    newSettings
                )
            )
        }
    }

    fun setMemoryPower(level: Int) {

        updateSettings {
            it.copy(
                memoryPowerLevel = level
            )
        }
    }

    fun setInsightDepth(level: Int) {

        updateSettings {
            it.copy(
                insightDepthLevel = level
            )
        }
    }

    fun setConversationFlow(level: Int) {

        updateSettings {
            it.copy(
                conversationFlowLevel = level
            )
        }
    }

    private fun updateSettings(
        transform: (PsychoSettings) -> PsychoSettings
    ) {

        val newSettings =
            transform(
                _settings.value
            )

        _settings.value =
            newSettings

        viewModelScope.launch {

            dataStore.save(
                newSettings
            )

            ConversationConfig.apply(

                SettingsMapper.map(
                    newSettings
                )
            )
        }
    }
}
