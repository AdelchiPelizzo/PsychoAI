package com.adelforce.psychoai.memory


import android.util.Log
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.ThemeDao
import com.adelforce.psychoai.data.model.ThemeMemory
import com.adelforce.psychoai.memory.search.EmbeddingSearchEngine
import com.adelforce.psychoai.prompt.MemoryContext
import com.adelforce.psychoai.util.ConversationConfig


class MemoryRetriever(

    private val messageDao: MessageDao,
    private val themeDao: ThemeDao,
    private val searchEngine: EmbeddingSearchEngine

) {

    suspend fun buildContext(
        conversationId: Long,
        currentMessageId: Long,
        userMessage: String,
        currentEmbedding: List<Float>
    ): MemoryContext {

        Log.d(
            "MemoryRetriever",
            "Searching memory for: $userMessage"
        )

        Log.d(
            "MemoryRetriever",
            "Current embedding size: ${currentEmbedding.size}"
        )

        val matches =
            searchEngine.findNearest(
                currentEmbedding = currentEmbedding,
                currentMessageId = currentMessageId
            )

        val relevantMemories =
            matches.mapNotNull { match ->

                val message =
                    messageDao.getById(
                        match.messageId
                    )

                message?.text
            }

        Log.d(
            "MemoryRetriever",
            "TOP MATCHES = $matches"
        )

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
            relevantMemories = relevantMemories

        )
    }
}