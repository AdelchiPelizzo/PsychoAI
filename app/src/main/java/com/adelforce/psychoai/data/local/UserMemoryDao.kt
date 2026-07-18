package com.adelforce.psychoai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserMemoryDao {


    @Query(
        """
        SELECT * 
        FROM user_memory
        WHERE id = 1
        LIMIT 1
        """
    )
    suspend fun getMemory(): UserMemoryEntity?


    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun saveMemory(
        memory: UserMemoryEntity
    )


    @Query(
        """
        UPDATE user_memory
        SET summary = :summary,
            updatedAt = :updatedAt,
            sourceMessageCount = :sourceMessageCount
        WHERE id = 1
        """
    )
    suspend fun updateMemory(
        summary: String,
        updatedAt: Long,
        sourceMessageCount: Int
    )


    @Query(
        """
        DELETE FROM user_memory
        """
    )
    suspend fun clearMemory()
}