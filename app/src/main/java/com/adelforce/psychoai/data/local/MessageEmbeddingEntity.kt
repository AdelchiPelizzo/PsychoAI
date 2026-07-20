package com.adelforce.psychoai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "message_embeddings"
)
data class MessageEmbeddingEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val messageId: Long,

    val embedding: String,

    val createdAt: Long,

    val conversationId: Long
)