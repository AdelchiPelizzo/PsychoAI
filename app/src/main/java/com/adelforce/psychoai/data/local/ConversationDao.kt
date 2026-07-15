package com.adelforce.psychoai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConversationDao {




    @Insert
    suspend fun insert(
        conversation: ConversationEntity
    ): Long


    @Query(
        """
        SELECT * FROM conversations
        WHERE status = 'ACTIVE'
        ORDER BY createdAt DESC
        LIMIT 1
        """
    )
    suspend fun getActiveConversation(): ConversationEntity?

    @Query("""
        UPDATE conversations
        SET status='CLOSED'
        WHERE status='ACTIVE'
        """)
    suspend fun closeAllActiveConversations()


    @Query(
        """
        UPDATE conversations
        SET status = 'CLOSED'
        WHERE id = :conversationId
        """
    )
    suspend fun closeConversation(
        conversationId: Long
    )


    @Query(
        """
        UPDATE conversations
        SET lastActivityAt = :time
        WHERE id = :conversationId
        """
    )
    suspend fun updateLastActivity(
        conversationId: Long,
        time: Long
    )

}