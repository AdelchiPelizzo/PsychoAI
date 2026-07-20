package com.adelforce.psychoai.util

object ConversationConfig {


    const val INACTIVITY_TIMEOUT_MINUTES = 1

    const val WIPE_SCREEN_TIMEOUT_MINUTES = 1


    // Memory settings

    const val MEMORY_MIN_MESSAGES_BEFORE_UPDATE = 1

    const val MEMORY_UPDATE_INTERVAL_MINUTES = 15



    val inactivityTimeoutMillis: Long
        get() =
            INACTIVITY_TIMEOUT_MINUTES * 60 * 1000L


    val chatScreenTimeoutMillis: Long
        get() =
            WIPE_SCREEN_TIMEOUT_MINUTES * 60 * 1000L


    val memoryUpdateIntervalMillis: Long
        get() =
            MEMORY_UPDATE_INTERVAL_MINUTES * 60 * 1000L
}