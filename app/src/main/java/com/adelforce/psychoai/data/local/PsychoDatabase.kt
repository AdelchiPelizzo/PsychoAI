package com.adelforce.psychoai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        MessageEntity::class,
        ConversationEntity::class,
        MessageThemeEntity::class,
        ThemeEntity::class,
        UserMemoryEntity::class,
        MessageEmbeddingEntity::class   // NEW
    ],
    version = 6
)
abstract class PsychoDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun conversationDao(): ConversationDao

    abstract fun themeDao(): ThemeDao

    abstract fun messageThemeDao(): MessageThemeDao

    abstract fun userMemoryDao(): UserMemoryDao

    abstract fun messageEmbeddingDao(): MessageEmbeddingDao
}