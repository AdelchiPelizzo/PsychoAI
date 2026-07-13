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
    )


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

}