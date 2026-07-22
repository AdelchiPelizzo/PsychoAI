package com.adelforce.psychoai.repository

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.ConversationDao
import com.adelforce.psychoai.data.local.ConversationEntity
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.local.MessageEmbeddingDao
import com.adelforce.psychoai.data.local.MessageEmbeddingEntity
import com.adelforce.psychoai.data.local.MessageEntity
import com.adelforce.psychoai.data.local.MessageThemeDao
import com.adelforce.psychoai.util.ConversationConfig
import com.adelforce.psychoai.memory.ThemeRepository
import com.adelforce.psychoai.memory.ThemeExtractor
import com.adelforce.psychoai.data.local.MessageThemeEntity
import com.adelforce.psychoai.memory.EmbeddingConverter
import com.adelforce.psychoai.memory.MemoryRetriever
import com.adelforce.psychoai.memory.MemoryUpdateWorker
import com.adelforce.psychoai.memory.UserMemoryManager
import com.adelforce.psychoai.prompt.PromptBuilder

class ConversationRepository(

    private val context: Context,

    private val openAIService: OpenAIService,

    private val messageDao: MessageDao,

    private val conversationDao: ConversationDao,

    private val themeExtractor: ThemeExtractor,

    private val themeRepository: ThemeRepository,

    private val messageThemeDao: MessageThemeDao,

    private val memoryRetriever: MemoryRetriever,

    private val promptBuilder: PromptBuilder,

    private val userMemoryManager: UserMemoryManager,

    private val messageEmbeddingDao: MessageEmbeddingDao,

    ) {
    private var currentConversationId: Long? = null

    suspend fun getOrCreateConversation(): Long {

        userMemoryManager.initializeMemoryIfNeeded()

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

        val embedding =
            openAIService.createEmbedding(text)

        println(
            "Embedding size = ${embedding.size}"
        )

        val inputData =
            workDataOf(
                "min_messages" to ConversationConfig.MEMORY_MIN_MESSAGES_BEFORE_UPDATE
            )

        val request =
            OneTimeWorkRequestBuilder<MemoryUpdateWorker>()
                .setInputData(inputData)
                .build()

        WorkManager.getInstance(context)
            .enqueue(request)

        val themes =
            themeExtractor.extractThemes(text)

        Log.d(
            "ThemeDebug",
            "Extracted themes: $themes"
        )

        themeRepository.saveThemesForMessage(
            messageId,
            themes
        )


        conversationDao.updateLastActivity(
            conversationId,
            now,
        )

        messageEmbeddingDao.insert(
            MessageEmbeddingEntity(
                messageId = messageId,
                conversationId = conversationId,
                embedding = EmbeddingConverter.floatListToByteArray( embedding ),
                createdAt = now
            )
        )

        val memoryContext =
            memoryRetriever.buildContext(
                conversationId = conversationId,
                currentMessageId = messageId,
                userMessage = text,
                currentEmbedding = embedding
            )

        val prompt =
            promptBuilder.build(
                memoryContext,
                text
            )

        val response =
            openAIService.askAI(prompt)


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
