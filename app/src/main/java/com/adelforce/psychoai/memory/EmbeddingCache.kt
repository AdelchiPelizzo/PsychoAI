package com.adelforce.psychoai.memory

import com.adelforce.psychoai.data.local.MessageEmbeddingDao
import android.util.Log
import com.adelforce.psychoai.util.ConversationConfig

class EmbeddingCache(
    private val embeddingDao: MessageEmbeddingDao
) {

    private val cache =
        mutableMapOf<Long, FloatArray>()


    suspend fun load() {

        val storedEmbeddings =
            embeddingDao.getLimited(
                ConversationConfig.MAX_RAM_CACHE_EMBEDDINGS
            )

        val newCache = mutableMapOf<Long, FloatArray>()

        for (item in storedEmbeddings) {

            if (newCache.size >= ConversationConfig.MAX_RAM_CACHE_EMBEDDINGS) {
                Log.w(
                    "EmbeddingCache",
                    "CACHE LIMIT REACHED: ${ConversationConfig.MAX_RAM_CACHE_EMBEDDINGS}"
                )
                break
            }

            try {

                val embedding =
                    EmbeddingConverter.byteArrayToFloatArray(
                        item.embedding
                    )

                newCache[item.messageId] =
                    embedding

            } catch (e: Exception) {

                Log.e(
                    "EmbeddingCache",
                    "Invalid embedding for message ${item.messageId}"
                )
            }
        }

        cache.clear()
        cache.putAll(newCache)


        Log.d(
            "EmbeddingCache",
            "CACHE LOADED: ${cache.size} embeddings"
        )
    }


    fun put(
        messageId: Long,
        embedding: FloatArray
    ) {
        cache[messageId] = embedding
    }


    fun get(
        messageId: Long
    ): FloatArray? =
        cache[messageId]


    fun entries():
            Set<Map.Entry<Long, FloatArray>> =
        cache.entries


    fun clear() {
        cache.clear()
    }
}