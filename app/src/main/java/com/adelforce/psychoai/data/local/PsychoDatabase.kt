package com.adelforce.psychoai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        MessageEntity::class,
        ConversationEntity::class,
        ThemeEntity::class,
        MessageThemeEntity::class
    ],
    version = 3
)
abstract class PsychoDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun conversationDao(): ConversationDao

    abstract fun themeDao(): ThemeDao

    abstract fun messageThemeDao(): MessageThemeDao
}