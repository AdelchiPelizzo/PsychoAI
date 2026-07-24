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


    fun map(
        settings: PsychoSettings
    ): ConversationSettings {


        val memory =
            mapMemoryPower(
                settings.memoryPowerLevel
            )


        val insight =
            mapInsightDepth(
                settings.insightDepthLevel
            )


        val conversation =
            mapConversationFlow(
                settings.conversationFlowLevel
            )


        return ConversationSettings(

            maxRamCacheEmbeddings =
                memory.maxRamCacheEmbeddings,

            memoryUpdateIntervalMinutes =
                memory.memoryUpdateIntervalMinutes,

            memoryMinMessagesBeforeUpdate =
                memory.memoryMinMessagesBeforeUpdate,


            memorySimilarityThreshold =
                insight.memorySimilarityThreshold,

            memorySearchLimit =
                insight.memorySearchLimit,


            inactivityTimeoutMinutes =
                conversation.inactivityTimeoutMinutes,

            wipeScreenTimeoutMinutes =
                conversation.wipeScreenTimeoutMinutes
        )
    }



    private fun mapMemoryPower(
        level: Int
    ): MemorySettings {


        return when(level) {


            0 ->
                MemorySettings(
                    maxRamCacheEmbeddings = 1000,
                    memoryUpdateIntervalMinutes = 60,
                    memoryMinMessagesBeforeUpdate = 50
                )


            1 ->
                MemorySettings(
                    maxRamCacheEmbeddings = 2500,
                    memoryUpdateIntervalMinutes = 30,
                    memoryMinMessagesBeforeUpdate = 30
                )


            2 ->
                MemorySettings(
                    maxRamCacheEmbeddings = 5000,
                    memoryUpdateIntervalMinutes = 15,
                    memoryMinMessagesBeforeUpdate = 10
                )


            3 ->
                MemorySettings(
                    maxRamCacheEmbeddings = 7500,
                    memoryUpdateIntervalMinutes = 10,
                    memoryMinMessagesBeforeUpdate = 5
                )


            else ->
                MemorySettings(
                    maxRamCacheEmbeddings = 10000,
                    memoryUpdateIntervalMinutes = 5,
                    memoryMinMessagesBeforeUpdate = 1
                )
        }
    }



    private fun mapInsightDepth(
        level: Int
    ): InsightSettings {


        return when(level) {


            0 ->
                InsightSettings(
                    memorySimilarityThreshold = 0.60f,
                    memorySearchLimit = 1
                )


            1 ->
                InsightSettings(
                    memorySimilarityThreshold = 0.50f,
                    memorySearchLimit = 2
                )


            2 ->
                InsightSettings(
                    memorySimilarityThreshold = 0.35f,
                    memorySearchLimit = 3
                )


            3 ->
                InsightSettings(
                    memorySimilarityThreshold = 0.25f,
                    memorySearchLimit = 5
                )


            else ->
                InsightSettings(
                    memorySimilarityThreshold = 0.10f,
                    memorySearchLimit = 8
                )
        }
    }



    private fun mapConversationFlow(
        level: Int
    ): ConversationFlowSettings {


        return when(level) {


            0 ->
                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 1,
                    wipeScreenTimeoutMinutes = 1
                )


            1 ->
                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 5,
                    wipeScreenTimeoutMinutes = 3
                )


            2 ->
                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 10,
                    wipeScreenTimeoutMinutes = 5
                )


            3 ->
                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 30,
                    wipeScreenTimeoutMinutes = 15
                )


            else ->
                ConversationFlowSettings(
                    inactivityTimeoutMinutes = 60,
                    wipeScreenTimeoutMinutes = 30
                )
        }
    }
}



private data class MemorySettings(

    val maxRamCacheEmbeddings: Int,

    val memoryUpdateIntervalMinutes: Int,

    val memoryMinMessagesBeforeUpdate: Int
)



private data class InsightSettings(

    val memorySimilarityThreshold: Float,

    val memorySearchLimit: Int
)



private data class ConversationFlowSettings(

    val inactivityTimeoutMinutes: Int,

    val wipeScreenTimeoutMinutes: Int
)