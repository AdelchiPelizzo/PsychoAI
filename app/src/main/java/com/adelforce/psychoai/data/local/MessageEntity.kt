package com.adelforce.psychoai.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "messages"
)
data class MessageEntity(

    @PrimaryKey
    val id: Long,

    val role: String,

    val text: String,

    val timestamp: Long
)