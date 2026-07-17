package com.adelforce.psychoai.repository

import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.ConversationDao
import com.adelforce.psychoai.data.local.ConversationEntity
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.MessageEntity
import com.adelforce.psychoai.data.local.MessageThemeDao
import com.adelforce.psychoai.util.ConversationConfig
import com.adelforce.psychoai.memory.ThemeRepository
import com.adelforce.psychoai.memory.ThemeExtractor
import com.adelforce.psychoai.data.local.MessageThemeEntity


class ConversationRepository(
    private val openAIService: OpenAIService,
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    private val themeExtractor: ThemeExtractor,
    private val themeRepository: ThemeRepository,
) {
    private var currentConversationId: Long? = null

    suspend fun getOrCreateConversation(): Long {
        val activeConversation =
            conversationDao.getActiveConversation()

        if (activeConversation != null) {
            val lastMessage =
                messageDao.getLastMessage(
                    activeConversation.id,
                )

            if (lastMessage == null) {
                currentConversationId =
                    activeConversation.id

                println(
                    "CONTINUING EMPTY CONVERSATION ${activeConversation.id}",
                )

                return activeConversation.id
            }

            val now =
                System.currentTimeMillis()

            val inactivity =
                now - lastMessage.timestamp

            if (inactivity < ConversationConfig.inactivityTimeoutMillis) {
                currentConversationId =
                    activeConversation.id

                println(
                    "CONTINUING RECENT CONVERSATION ${activeConversation.id}",
                )

                return activeConversation.id
            }

            conversationDao.closeConversation(
                activeConversation.id,
            )

            println(
                "CLOSED OLD CONVERSATION ${activeConversation.id}",
            )
        }

        val now =
            System.currentTimeMillis()

        val newConversationId =
            conversationDao.insert(
                ConversationEntity(
                    createdAt = now,
                    lastActivityAt = now,
                ),
            )

        currentConversationId =
            newConversationId

        println(
            "NEW CONVERSATION CREATED = $newConversationId",
        )

        return newConversationId
    }

    suspend fun sendMessage(
        text: String,
        conversationId: Long,
    ): String {

        val now =
            System.currentTimeMillis()


        val messageId =
            messageDao.insert(
                MessageEntity(
                    conversationId = conversationId,
                    role = "USER",
                    text = text,
                    timestamp = now
                )
            )


        val themes =
            themeExtractor.extractThemes(text)

        themeRepository.saveThemesForMessage(
            messageId,
            themes
        )


        conversationDao.updateLastActivity(
            conversationId,
            now,
        )


        val response =
            openAIService.askAI(text)


        val responseTime =
            System.currentTimeMillis()


        messageDao.insert(
            MessageEntity(
                conversationId = conversationId,
                role = "ASSISTANT",
                text = response,
                timestamp = responseTime,
            ),
        )


        conversationDao.updateLastActivity(
            conversationId,
            responseTime,
        )


        println(
            "REPOSITORY: ASSISTANT inserted",
        )


        return response
    }

    suspend fun getCurrentConversationId(): Long? =
        conversationDao
            .getActiveConversation()
            ?.id

    fun getCurrentConversationIdImmediate(): Long? = currentConversationId
}
