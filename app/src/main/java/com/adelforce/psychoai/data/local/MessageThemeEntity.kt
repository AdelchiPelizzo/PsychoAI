package com.adelforce.psychoai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "message_themes"
)
data class MessageThemeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val messageId: Long,

    val themeId: Long,

    val confidence: Float
)