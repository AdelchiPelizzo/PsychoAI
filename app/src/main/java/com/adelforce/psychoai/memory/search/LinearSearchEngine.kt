package com.adelforce.psychoai.memory.search

import android.util.Log
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.MessageEmbeddingDao
import com.adelforce.psychoai.memory.CosineSimilarity
import com.adelforce.psychoai.memory.EmbeddingConverter
import com.adelforce.psychoai.util.ConversationConfig
import kotlinx.serialization.json.Json

class LinearSearchEngine(

    private val embeddingDao: MessageEmbeddingDao,
    private val messageDao: MessageDao

) : EmbeddingSearchEngine {


    override suspend fun findNearest(
        currentEmbedding: List<Float>,
        currentMessageId: Long
    ): List<SearchResult> {

        val storedEmbeddings =
            embeddingDao.getAllExceptCurrent(currentMessageId)

        Log.d(
            "LinearSearchEngine",
            "Stored embeddings count: ${storedEmbeddings.size}"
        )

        val matches =
            mutableListOf<SearchResult>()

        for (item in storedEmbeddings) {

            val oldEmbedding =
                try {
                    EmbeddingConverter.byteArrayToFloatList(
                        item.embedding
                    )
                } catch (e: Exception) {
                    Log.e(
                        "LinearSearchEngine",
                        "Invalid embedding for message ${item.messageId}"
                    )
                    continue
                }

            if (oldEmbedding.size != currentEmbedding.size) {

                Log.w(
                    "LinearSearchEngine",
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
                SearchResult(
                    messageId = item.messageId,
                    similarity = similarity
                )
            )

            val matchedMessage =
                messageDao.getById(
                    item.messageId
                )

            Log.d(
                "LinearSearchEngine",
                """
                MEMORY MATCH
                id=${item.messageId}
                text=${matchedMessage?.text}
                similarity=$similarity
                """.trimIndent()
            )

        }

        return matches
            .filter {
                it.similarity > ConversationConfig.MEMORY_SIMILARITY_THRESHOLD
            }
            .sortedByDescending {
                it.similarity
            }
            .take(ConversationConfig.MEMORY_SEARCH_LIMIT)
    }
}