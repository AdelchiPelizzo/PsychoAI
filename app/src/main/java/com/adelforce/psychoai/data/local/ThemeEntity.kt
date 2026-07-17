package com.adelforce.psychoai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class ThemeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val createdAt: Long,

    val lastUsedAt: Long,

    val usageCount: Int = 1
)