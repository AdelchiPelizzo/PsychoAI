package com.adelforce.psychoai.memory.search

data class SearchResult(
    val messageId: Long,
    val similarity: Float
)

interface EmbeddingSearchEngine {

    suspend fun findNearest(
        currentEmbedding: List<Float>,
        currentMessageId: Long,
        limit: Int = 3
    ): List<SearchResult>
}