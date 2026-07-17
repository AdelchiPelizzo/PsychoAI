package com.adelforce.psychoai.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.model.Message
import com.adelforce.psychoai.data.model.Role
import com.adelforce.psychoai.repository.ConversationRepository
import com.adelforce.psychoai.util.ConversationConfig
import com.adelforce.psychoai.util.NetworkUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ConversationRepository,
    private val messageDao: MessageDao,
    private val context: Context,
) : ViewModel() {
    private var screenWipeJob: Job? = null

    private val _conversationId =
        MutableStateFlow<Long?>(null)

    val conversationId =
        _conversationId.asStateFlow()

    private val _temporaryMessages =
        MutableStateFlow<List<Message>>(emptyList())

    private val _hideDatabaseMessages =
        MutableStateFlow(false)

    init {

        println("CHAT VIEWMODEL CREATED")

        viewModelScope.launch {

            val id =
                repository.getCurrentConversationId()

            if (id != null) {

                val lastMessage =
                    messageDao.getLastMessage(id)

                if (lastMessage != null) {

                    val age =
                        System.currentTimeMillis() -
                                lastMessage.timestamp


                    if (age < ConversationConfig.chatScreenTimeoutMillis) {

                        println(
                            "CONTINUING ACTIVE CONVERSATION $id"
                        )

                        _conversationId.value = id

                        startScreenWipeMonitor(id)

                    } else {

                        println(
                            "STALE CONVERSATION ON APP RELOAD - KEEPING SCREEN BLANK"
                        )

                        /*
                           Important:
                           - Do not set conversationId
                           - Do not load messages
                           - Do not show welcome message
                           - Do not start monitor

                           The next user message will call
                           repository.getOrCreateConversation()
                        */

                        _hideDatabaseMessages.value = true
                    }

                } else {

                    println(
                        "EMPTY ACTIVE CONVERSATION $id"
                    )

                    _conversationId.value = id

                    startScreenWipeMonitor(id)
                }

            } else {

                println(
                    "NO ACTIVE CONVERSATION"
                )

            }
        }
    }

    private fun startScreenWipeMonitor(conversationId: Long) {
        screenWipeJob?.cancel()

        screenWipeJob =
            viewModelScope.launch {
                while (isActive) {
                    delay(10000L)

                    val lastMessage =
                        messageDao.getLastMessage(
                            conversationId,
                        )

                    if (lastMessage != null) {
                        val age =
                            System.currentTimeMillis() -
                                    lastMessage.timestamp

                        if (age >= ConversationConfig.chatScreenTimeoutMillis) {
                            println(
                                "SCREEN WIPE TRIGGERED",
                            )

                            _hideDatabaseMessages.value =
                                true

                            _temporaryMessages.value =
                                listOf(
                                    Message(
                                        id =
                                            System.currentTimeMillis(),
                                        text =
                                            "Welcome back. It has been a while.\nWhat happened?",
                                        role =
                                            Role.ASSISTANT,
                                        timestamp =
                                            System.currentTimeMillis(),
                                    ),
                                )

                            break
                        }
                    }
                }
            }
    }

    val messages: StateFlow<List<Message>> =

        _conversationId
            .filterNotNull()
            .flatMapLatest { id ->

                println(
                    "LOADING MESSAGES FOR CONVERSATION ID = $id",
                )

                messageDao
                    .getMessagesForConversation(id)
                    .onEach { messages ->

                        println(
                            "FLOW RECEIVED ${messages.size} MESSAGES FOR CONVERSATION $id",
                        )
                    }
            }.combine(
                _temporaryMessages,
            ) { databaseMessages, temporaryMessages ->

                databaseMessages to temporaryMessages
            }.combine(
                _hideDatabaseMessages,
            ) { pair, hideDatabaseMessages ->

                val databaseMessages =
                    pair.first

                val temporaryMessages =
                    pair.second

                val savedMessages =
                    databaseMessages.map { entity ->

                        Message(
                            id =
                                entity.id,
                            text =
                                entity.text,
                            role =
                                if (entity.role == "USER") {
                                    Role.USER
                                } else {
                                    Role.ASSISTANT
                                },
                            timestamp =
                                entity.timestamp,
                        )
                    }

                if (hideDatabaseMessages) {

                    temporaryMessages
                } else {

                    savedMessages + temporaryMessages
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList(),
            )

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    fun sendMessage(text: String) {
        if (text.isBlank()) {
            return
        }

        if (_isLoading.value) {
            return
        }

        val internetAvailable =
            NetworkUtils.isInternetAvailable(context)

        if (!internetAvailable) {
            showTemporaryError(
                "No internet connection. Please check your network.",
            )

            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                println(
                    "BEFORE sendMessage()",
                )

                _temporaryMessages.value =
                    listOf(
                        Message(
                            id =
                                System.currentTimeMillis(),
                            text =
                                text,
                            role =
                                Role.USER,
                            timestamp =
                                System.currentTimeMillis(),
                        ),
                    )

                _hideDatabaseMessages.value =
                    true

                /*
                    This is now the ONLY place
                    where a conversation can be created.
                 */

                val conversationId =
                    repository.getOrCreateConversation()

                println(
                    "ACTIVE CONVERSATION ID = $conversationId",
                )

                _conversationId.value =
                    conversationId

                startScreenWipeMonitor(conversationId)

                repository.sendMessage(
                    text,
                    conversationId,
                )

                delay(100)

                _hideDatabaseMessages.value =
                    false

                _conversationId.value =
                    conversationId

                _temporaryMessages.value =
                    emptyList()

                println(
                    "AFTER sendMessage()",
                )
            } catch (e: Exception) {
                println(
                    "CHAT ERROR: ${e.javaClass.name} - ${e.message}",
                )

                showTemporaryError(
                    "Something went wrong: ${e.message}",
                )
            } finally {
                _isLoading.value =
                    false
            }
        }
    }

    private fun showTemporaryError(text: String) {
        _temporaryMessages.value =
            listOf(
                Message(
                    id =
                        System.currentTimeMillis(),
                    text =
                        text,
                    role =
                        Role.ASSISTANT,
                    timestamp =
                        System.currentTimeMillis(),
                ),
            )
    }

    override fun onCleared() {
        super.onCleared()

        screenWipeJob?.cancel()
    }
}