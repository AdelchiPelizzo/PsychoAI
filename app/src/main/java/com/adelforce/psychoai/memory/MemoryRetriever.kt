package com.adelforce.psychoai.memory


import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.MessageEmbeddingDao
import com.adelforce.psychoai.data.local.ThemeDao
import com.adelforce.psychoai.data.model.ThemeMemory
import com.adelforce.psychoai.prompt.MemoryContext
import android.util.Log
import kotlinx.serialization.json.Json


class MemoryRetriever(

    private val messageEmbeddingDao: MessageEmbeddingDao,
    private val messageDao: MessageDao,
    private val themeDao: ThemeDao

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

        /*
            Temporary implementation.

            Later this will:
            - query themes table
            - query message_themes table
            - query embeddings
        */

        Log.d(
            "MemoryRetriever",
            "Current embedding size: ${currentEmbedding.size}"
        )

//        That won't scale.
//
//        Eventually you'll replace
//
//        messageEmbeddingDao.getAll()
//
//        with
//
//        Approximate Nearest Neighbor
//
//        HNSW
//
//        FAISS
//
//        Qdrant
//
//        Milvus

        val storedEmbeddings =
            messageEmbeddingDao.getAll()

        Log.d(
            "MemoryRetriever",
            "Stored embeddings count: ${storedEmbeddings.size}"
        )

        val matches =
            mutableListOf<Pair<Long, Float>>()

        for (item in storedEmbeddings) {

            if (item.messageId == currentMessageId) {
                continue
            }

            val oldEmbedding =
                try {
                    Json.decodeFromString<List<Float>>(item.embedding)
                } catch (e: Exception) {

                    Log.e(
                        "MemoryRetriever",
                        "Invalid embedding for message ${item.messageId}"
                    )

                    continue
                }

            Log.d(
                "MemoryRetriever",
                "Message ${item.messageId} vector size=${oldEmbedding.size}"
            )


            if (oldEmbedding.size != currentEmbedding.size) {

                Log.w(
                    "MemoryRetriever",
                    "Skipping message ${item.messageId}: invalid vector size ${oldEmbedding.size}"
                )

                continue
            }

            val similarity =
                CosineSimilarity.calculate(
                    currentEmbedding,
                    oldEmbedding
                )

            matches.add(
                item.messageId to similarity
            )

            val matchedMessage =
                messageDao.getById(item.messageId)

            Log.d(
                "MemoryRetriever",
                """ MEMORY MATCH id=${item.messageId}  text=${matchedMessage?.text}  similarity=$similarity """.trimIndent()
            )
        }

        val topMatches =
            matches
                .filter { it.second > 0.35f }
                .sortedByDescending { it.second }
                .take(3)

        val relevantMemories =
            topMatches.mapNotNull { match ->

                val messageId =
                    match.first

                val message =
                    messageDao.getById(
                        messageId
                    )

                message?.text
            }

        Log.d(
            "MemoryRetriever",
            "TOP MATCHES = $topMatches"
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