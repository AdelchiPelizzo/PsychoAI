package com.adelforce.psychoai.util

import com.adelforce.psychoai.settings.ConversationSettings

object ConversationConfig {

    // RAM cache

    var MAX_RAM_CACHE_EMBEDDINGS = 5000


    // Conversation

    var INACTIVITY_TIMEOUT_MINUTES = 1

    var WIPE_SCREEN_TIMEOUT_MINUTES = 1


    // Memory update

    var MEMORY_MIN_MESSAGES_BEFORE_UPDATE = 10

    var MEMORY_UPDATE_INTERVAL_MINUTES = 15


    // Memory search

    var MEMORY_SIMILARITY_THRESHOLD = 0.35f

    var MEMORY_SEARCH_LIMIT = 3


    val inactivityTimeoutMillis: Long
        get() =
            INACTIVITY_TIMEOUT_MINUTES * 60 * 1000L


    val chatScreenTimeoutMillis: Long
        get() =
            WIPE_SCREEN_TIMEOUT_MINUTES * 60 * 1000L


    val memoryUpdateIntervalMillis: Long
        get() =
            MEMORY_UPDATE_INTERVAL_MINUTES * 60 * 1000L


    fun apply(
        settings: ConversationSettings
    ) {

        MAX_RAM_CACHE_EMBEDDINGS =
            settings.maxRamCacheEmbeddings

        MEMORY_UPDATE_INTERVAL_MINUTES =
            settings.memoryUpdateIntervalMinutes

        MEMORY_MIN_MESSAGES_BEFORE_UPDATE =
            settings.memoryMinMessagesBeforeUpdate

        MEMORY_SIMILARITY_THRESHOLD =
            settings.memorySimilarityThreshold

        MEMORY_SEARCH_LIMIT =
            settings.memorySearchLimit

        INACTIVITY_TIMEOUT_MINUTES =
            settings.inactivityTimeoutMinutes

        WIPE_SCREEN_TIMEOUT_MINUTES =
            settings.wipeScreenTimeoutMinutes
    }
}