package com.adelforce.psychoai.repository

import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.ConversationDao
import com.adelforce.psychoai.data.local.ConversationEntity
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.MessageEntity


class ConversationRepository(
    private val openAIService: OpenAIService,
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao
) {

    private var activeConversationId: Long? = null


    suspend fun getActiveConversationId(): Long {

        if (activeConversationId == null) {

            activeConversationId =
                conversationDao.insert(
                    ConversationEntity(
                        createdAt = System.currentTimeMillis()
                    )
                )

            println(
                "NEW CONVERSATION ID = $activeConversationId"
            )
        }

        return activeConversationId!!
    }


    suspend fun sendMessage(
        text: String
    ): String {

        val conversationId =
            getActiveConversationId()


        messageDao.insert(
            MessageEntity(
                conversationId = conversationId,
                role = "USER",
                text = text,
                timestamp = System.currentTimeMillis()
            )
        )


        val response =
            openAIService.askAI(text)


        messageDao.insert(
            MessageEntity(
                conversationId = conversationId,
                role = "ASSISTANT",
                text = response,
                timestamp = System.currentTimeMillis()
            )
        )


        return response
    }
}