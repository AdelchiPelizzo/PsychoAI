package com.adelforce.psychoai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ThemeDao {

    @Insert
    suspend fun insert(
        theme: ThemeEntity
    ): Long

    @Query("""
        SELECT *
        FROM themes
        WHERE name = :name
        LIMIT 1
    """)
    suspend fun findByName(
        name: String
    ): ThemeEntity?

    @Query("""
        UPDATE themes
        SET
            usageCount = usageCount + 1,
            lastUsedAt = :time
        WHERE id = :id
    """)
    suspend fun touchTheme(
        id: Long,
        time: Long
    )

    @Query("""
        SELECT *
        FROM themes
        ORDER BY usageCount DESC
    """)
    suspend fun getAll(): List<ThemeEntity>
}