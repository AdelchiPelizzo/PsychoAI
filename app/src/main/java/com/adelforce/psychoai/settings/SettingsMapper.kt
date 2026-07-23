package com.adelforce.psychoai.settings

data class ConversationSettings(
    val maxRamCacheEmbeddings: Int,
    val memoryUpdateIntervalMinutes: Int,
    val memoryMinMessagesBeforeUpdate: Int,
    val memorySimilarityThreshold: Float,
    val memorySearchLimit: Int,
    val inactivityTimeoutMinutes: Int,
    val wipeScreenTimeoutMinutes: Int,
)

object SettingsMapper {
    fun map(settings: PsychoSettings): ConversationSettings {
        val recollection =
            mapRecollection(
                settings.recollectionLevel,
            )

        val insight =
            mapInsight(
                settings.insightLevel,
            )

        val conversation =
            mapConversation(
                settings.conversationLevel,
            )

        return ConversationSettings(
            maxRamCacheEmbeddings =
                recollection.maxRamCacheEmbeddings,
            memoryUpdateIntervalMinutes =
                recollection.memoryUpdateIntervalMinutes,
            memoryMinMessagesBeforeUpdate =
                recollection.memoryMinMessagesBeforeUpdate,
            memorySimilarityThreshold =
                insight.similarityThreshold,
            memorySearchLimit =
                insight.searchLimit,
            inactivityTimeoutMinutes =
                conversation.inactivityTimeoutMinutes,
            wipeScreenTimeoutMinutes =
                conversation.wipeScreenTimeoutMinutes,
        )
    }

    private fun mapRecollection(level: Float): RecollectionSettings =
        when {
            level < 0.33f ->

                RecollectionSettings(
                    maxRamCacheEmbeddings = 1000,
                    memoryUpdateIntervalMinutes = 60,
                    memoryMinMessagesBeforeUpdate = 30,
                )

            level < 0.66f ->

                RecollectionSettings(
                    maxRamCacheEmbeddings = 2500,
                    memoryUpdateIntervalMinutes = 30,
                    memoryMinMessagesBeforeUpdate = 20,
                )

            else ->

                RecollectionSettings(
                    maxRamCacheEmbeddings = 5000,
                    memoryUpdateIntervalMinutes = 15,
                    memoryMinMessagesBeforeUpdate = 10,
                )
        }

    private fun mapInsight(level: Float): InsightSettings =
        when {
            level < 0.33f ->

                InsightSettings(
                    similarityThreshold = 0.50f,
                    searchLimit = 1,
                )

            level < 0.66f ->

                InsightSettings(
                    similarityThreshold = 0.35f,
                    searchLimit = 3,
                )

            else ->

                InsightSettings(
                    similarityThreshold = 0.20f,
                    searchLimit = 5,
                )
        }

    private fun mapConversation(level: Float): ConversationFlowSettings =
        when {
            level < 0.33f ->

                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 1,
                    wipeScreenTimeoutMinutes = 1,
                )

            level < 0.66f ->

                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 10,
                    wipeScreenTimeoutMinutes = 5,
                )

            else ->

                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 30,
                    wipeScreenTimeoutMinutes = 15,
                )
        }
}

private data class RecollectionSettings(
    val maxRamCacheEmbeddings: Int,
    val memoryUpdateIntervalMinutes: Int,
    val memoryMinMessagesBeforeUpdate: Int,
)

private data class InsightSettings(
    val similarityThreshold: Float,
    val searchLimit: Int,
)

private data class ConversationFlowSettings(
    val inactivityTimeoutMinutes: Int,
    val wipeScreenTimeoutMinutes: Int,
)
