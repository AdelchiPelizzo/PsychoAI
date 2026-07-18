package com.adelforce.psychoai.memory


import com.adelforce.psychoai.data.model.ThemeMemory
import com.adelforce.psychoai.prompt.MemoryContext


class MemoryRetriever {


    suspend fun buildContext(
        userMessage: String
    ): MemoryContext {


        /*
            Temporary implementation.

            Later this will:
            - query themes table
            - query message_themes table
            - query embeddings
        */


        val themes =
            listOf(
                ThemeMemory(
                    themeName = "anxiety",
                    occurrenceCount = 23,
                    lastDetectedAt =
                        System.currentTimeMillis(),
                    averageConfidence = 0.86f
                )
            )


        return MemoryContext(

            recurringThemes = themes,

            recentMessages = emptyList(),

            relevantMemories =
                listOf(
                    "User previously discussed feeling overwhelmed"
                )
        )
    }
}