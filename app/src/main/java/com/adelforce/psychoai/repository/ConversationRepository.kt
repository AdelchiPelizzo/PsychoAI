package com.adelforce.psychoai.repository

import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.MessageEntity


class ConversationRepository(
    private val openAIService: OpenAIService,
    private val messageDao: MessageDao
) {

    suspend fun saveMessage(message: MessageEntity) {
        messageDao.insert(message)
    }


    suspend fun sendMessage(
        text: String
    ): String {

        println("REPOSITORY: inserting USER message")

        messageDao.insert(
            MessageEntity(
                id = System.currentTimeMillis(),
                role = "USER",
                text = text,
                timestamp = System.currentTimeMillis()
            )
        )

        println("REPOSITORY: USER inserted")

        val response =
            openAIService.askAI(text)

        println("REPOSITORY: ASSISTANT inserted")

        messageDao.insert(
            MessageEntity(
                id = System.currentTimeMillis(),
                role = "ASSISTANT",
                text = response,
                timestamp = System.currentTimeMillis()
            )
        )

        println("REPOSITORY: ASSISTANT inserted")

        return response
    }

}