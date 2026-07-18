package com.adelforce.psychoai.memory

import com.adelforce.psychoai.data.local.UserMemoryDao
import com.adelforce.psychoai.data.local.UserMemoryEntity
import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.MessageDao


class UserMemoryManager(
    private val openAIService: OpenAIService,
    private val messageDao: MessageDao,
    private val userMemoryDao: UserMemoryDao
) {


    suspend fun initializeMemoryIfNeeded() {

        val existing =
            userMemoryDao.getMemory()

        if (existing == null) {

            userMemoryDao.saveMemory(
                UserMemoryEntity(
                    summary = "No memory available yet.",
                    updatedAt = System.currentTimeMillis(),
                    sourceMessageCount = 0,
                    lastProcessedMessageId = 0
                )
            )
        }
    }


    suspend fun updateMemory() {

        userMemoryDao.updateMemory(
            summary =
                "User often discusses work pressure and anxiety.",

            updatedAt =
                System.currentTimeMillis(),

            sourceMessageCount =
                10
        )
    }
}