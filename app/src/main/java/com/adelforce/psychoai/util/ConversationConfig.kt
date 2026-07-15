package com.adelforce.psychoai.util

object ConversationConfig {

    // TEST: conversation expires after 1 minute without activity
    const val INACTIVITY_TIMEOUT_MINUTES = 1


    // TEST: wipe chat screen after 1 minute without activity
    const val WIPE_SCREEN_TIMEOUT_MINUTES = 1


    val inactivityTimeoutMillis: Long
        get() =
            INACTIVITY_TIMEOUT_MINUTES *
                    60 *
                    1000L


    val chatScreenTimeoutMillis: Long
        get() =
            WIPE_SCREEN_TIMEOUT_MINUTES *
                    60 *
                    1000L
}