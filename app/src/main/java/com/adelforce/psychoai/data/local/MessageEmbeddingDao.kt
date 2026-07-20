package com.adelforce.psychoai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageEmbeddingDao {

    @Insert
    suspend fun insert(
        embedding: MessageEmbeddingEntity
    )

    @Query(
        """
        SELECT * FROM message_embeddings
        """
    )
    suspend fun getAll(): List<MessageEmbeddingEntity>

    @Query(
        """
        SELECT * FROM message_embeddings
        WHERE messageId = :messageId
        """
    )
    suspend fun getForMessage(
        messageId: Long
    ): MessageEmbeddingEntity?
}