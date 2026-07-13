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
        "SELECT * FROM conversations ORDER BY createdAt DESC LIMIT 1"
    )
    suspend fun getLatestConversation():
            ConversationEntity?

}