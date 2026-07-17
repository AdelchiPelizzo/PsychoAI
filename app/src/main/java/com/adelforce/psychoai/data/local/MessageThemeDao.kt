package com.adelforce.psychoai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageThemeDao {

    @Insert
    suspend fun insert(
        messageTheme: MessageThemeEntity
    ): Long

    @Query("""
        SELECT themeId
        FROM message_themes
        WHERE messageId = :messageId
    """)
    suspend fun getThemesForMessage(
        messageId: Long
    ): List<Long>

    @Query("""
        SELECT messageId
        FROM message_themes
        WHERE themeId = :themeId
    """)
    suspend fun getMessagesForTheme(
        themeId: Long
    ): List<Long>
}