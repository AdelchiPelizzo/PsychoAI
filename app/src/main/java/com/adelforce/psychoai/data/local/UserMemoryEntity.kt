package com.adelforce.psychoai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_memory")
data class UserMemoryEntity(

    @PrimaryKey
    val id: Int = 1,

    val summary: String,

    val updatedAt: Long,

    val sourceMessageCount: Int,

    val lastProcessedMessageId: Long
)