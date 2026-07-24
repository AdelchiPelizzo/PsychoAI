package com.adelforce.psychoai.util

import com.adelforce.psychoai.settings.ConversationSettings

object ConversationConfig {

    var MAX_RAM_CACHE_EMBEDDINGS = 5000
        private set

    var INACTIVITY_TIMEOUT_MINUTES = 1
        private set

    var WIPE_SCREEN_TIMEOUT_MINUTES = 1
        private set

    var MEMORY_MIN_MESSAGES_BEFORE_UPDATE = 10
        private set

    var MEMORY_UPDATE_INTERVAL_MINUTES = 15
        private set

    var MEMORY_SIMILARITY_THRESHOLD = 0.35f
        private set

    var MEMORY_SEARCH_LIMIT = 3
        private set


    val inactivityTimeoutMillis: Long
        get() = INACTIVITY_TIMEOUT_MINUTES * 60 * 1000L


    val chatScreenTimeoutMillis: Long
        get() = WIPE_SCREEN_TIMEOUT_MINUTES * 60 * 1000L


    val memoryUpdateIntervalMillis: Long
        get() = MEMORY_UPDATE_INTERVAL_MINUTES * 60 * 1000L

    fun debugPrint() {

        println("========== ConversationConfig ==========")

        println("MAX_RAM_CACHE_EMBEDDINGS = $MAX_RAM_CACHE_EMBEDDINGS")
        println("MEMORY_UPDATE_INTERVAL_MINUTES = $MEMORY_UPDATE_INTERVAL_MINUTES")
        println("MEMORY_MIN_MESSAGES_BEFORE_UPDATE = $MEMORY_MIN_MESSAGES_BEFORE_UPDATE")
        println("MEMORY_SIMILARITY_THRESHOLD = $MEMORY_SIMILARITY_THRESHOLD")
        println("MEMORY_SEARCH_LIMIT = $MEMORY_SEARCH_LIMIT")
        println("INACTIVITY_TIMEOUT_MINUTES = $INACTIVITY_TIMEOUT_MINUTES")
        println("WIPE_SCREEN_TIMEOUT_MINUTES = $WIPE_SCREEN_TIMEOUT_MINUTES")

        println("========================================")
    }


    fun apply(settings: ConversationSettings) {

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

        debugPrint()

    }
}