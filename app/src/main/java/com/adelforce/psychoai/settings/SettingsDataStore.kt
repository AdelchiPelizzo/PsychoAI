package com.adelforce.psychoai.settings

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(
    name = "psycho_settings"
)


class SettingsDataStore(
    private val context: Context
) {

    companion object {

        val MEMORY_POWER =
            intPreferencesKey("memory_power")

        val INSIGHT_DEPTH =
            intPreferencesKey("insight_depth")

        val CONVERSATION_FLOW =
            intPreferencesKey("conversation_flow")
    }

    val settingsFlow: Flow<PsychoSettings> =
        context.dataStore.data.map { preferences ->

            PsychoSettings(

                memoryPowerLevel =
                    preferences[MEMORY_POWER] ?: 2,

                insightDepthLevel =
                    preferences[INSIGHT_DEPTH] ?: 2,

                conversationFlowLevel =
                    preferences[CONVERSATION_FLOW] ?: 2
            )
        }

    suspend fun save(settings: PsychoSettings) {

        context.dataStore.edit { preferences ->

            preferences[MEMORY_POWER] =
                settings.memoryPowerLevel

            preferences[INSIGHT_DEPTH] =
                settings.insightDepthLevel

            preferences[CONVERSATION_FLOW] =
                settings.conversationFlowLevel
        }
    }
}