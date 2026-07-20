package com.adelforce.psychoai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {


    @Insert
    suspend fun insert(
        message: MessageEntity
    ): Long

    @Query(
        """
        SELECT * FROM messages
        WHERE conversationId = :conversationId
        ORDER BY timestamp ASC
        """
    )
    fun getMessagesForConversation(
        conversationId: Long
    ): Flow<List<MessageEntity>>

    @Query(
        """
    SELECT * FROM messages
    WHERE conversationId = :conversationId
    ORDER BY timestamp DESC
    LIMIT 1
    """
    )
    suspend fun getLastMessage(
        conversationId: Long
    ): MessageEntity?

    @Query(
        """
    SELECT *
    FROM messages
    WHERE id > :lastProcessedMessageId
      AND role = 'USER'
    ORDER BY id ASC
    """
    )
    suspend fun getUserMessagesAfter(
        lastProcessedMessageId: Long
    ): List<MessageEntity>

    @Query(
        """
    SELECT COUNT(*)
    FROM messages
    """
    )
    suspend fun countMessages(): Int

    @Query(
        """
    SELECT * FROM messages
    WHERE id = :id
    """
    )
    suspend fun getById(
        id: Long
    ): MessageEntity?

    @Query("""
    SELECT COUNT(*)
    FROM messages
    WHERE id > :lastProcessedMessageId
    AND role = 'USER'
    """)
    suspend fun countMessagesAfter(
        lastProcessedMessageId: Long
    ): Int

}