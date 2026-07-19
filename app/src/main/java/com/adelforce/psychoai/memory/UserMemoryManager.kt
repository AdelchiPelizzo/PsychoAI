package com.adelforce.psychoai.memory

import com.adelforce.psychoai.data.local.UserMemoryDao
import com.adelforce.psychoai.data.local.UserMemoryEntity
import com.adelforce.psychoai.data.local.MessageDao


class UserMemoryManager(

    private val synthesizer: MemorySynthesizer,

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

        val memory =
            userMemoryDao.getMemory() ?: return

        val newMessages =
            messageDao.getUserMessagesAfter(
                memory.lastProcessedMessageId
            )

        if (newMessages.isEmpty()) {
            return
        }

        val summary =
            synthesizer.synthesize(
                previousMemory = memory.summary,
                newMessages = newMessages
            )

        val lastMessageId =
            newMessages.last().id

        userMemoryDao.updateMemory(

            summary = summary,

            updatedAt =
                System.currentTimeMillis(),

            sourceMessageCount =
                messageDao.countMessages(),

            lastProcessedMessageId =
                lastMessageId
        )
    }
}