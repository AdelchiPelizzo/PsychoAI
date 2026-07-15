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
                repository.getOrCreateConversation()

            _conversationId.value = id

            println(
                "ACTIVE CONVERSATION ID = $id"
            )
        }

        viewModelScope.launch {
            _conversationId
                .filterNotNull()
                .collect { id ->
                    startScreenWipeMonitor(id)
                }
        }
    }

    private fun startScreenWipeMonitor(
        conversationId: Long
    ) {
        screenWipeJob?.cancel()

        screenWipeJob =
            viewModelScope.launch {

                while (isActive) {

                    delay(10000L)

                    val lastMessage =
                        messageDao.getLastMessage(
                            conversationId
                        )

                    if (lastMessage != null) {

                        val age =
                            System.currentTimeMillis() -
                                    lastMessage.timestamp

                        if (age >= ConversationConfig.chatScreenTimeoutMillis) {

                            println(
                                "SCREEN WIPE TRIGGERED"
                            )

                            _hideDatabaseMessages.value = true

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
                                            System.currentTimeMillis()
                                    )
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
                    "LOADING MESSAGES FOR CONVERSATION ID = $id"
                )

                messageDao
                    .getMessagesForConversation(id)
                    .onEach { messages ->

                        println(
                            "FLOW RECEIVED ${messages.size} MESSAGES FOR CONVERSATION $id"
                        )

                    }

            }
            .combine(
                _temporaryMessages
            ) { databaseMessages, temporaryMessages ->

                databaseMessages to temporaryMessages

            }
            .combine(
                _hideDatabaseMessages
            ) { pair, hideDatabaseMessages ->

                val databaseMessages =
                    pair.first

                val temporaryMessages =
                    pair.second

                val savedMessages =
                    databaseMessages.map { entity ->

                        Message(
                            id = entity.id,
                            text = entity.text,
                            role =
                                if (entity.role == "USER")
                                    Role.USER
                                else
                                    Role.ASSISTANT,
                            timestamp = entity.timestamp
                        )

                    }

                if (hideDatabaseMessages) {
                    temporaryMessages
                } else {
                    savedMessages + temporaryMessages
                }

            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    fun sendMessage(
        text: String
    ) {

        if (text.isBlank()) {
            return
        }

        if (_isLoading.value) {
            return
        }

        println(
            "CHECKING INTERNET"
        )

        val internetAvailable =
            NetworkUtils.isInternetAvailable(context)

        println(
            "INTERNET AVAILABLE = $internetAvailable"
        )

        if (!internetAvailable) {

            showTemporaryError(
                "No internet connection. Please check your network."
            )

            return
        }

        _isLoading.value = true

        viewModelScope.launch {

            try {

                println(
                    "BEFORE sendMessage()"
                )

                _hideDatabaseMessages.value = true

                _temporaryMessages.value =
                    emptyList()

                val conversationId =
                    repository.getOrCreateConversation()

                println(
                    "NEW ACTIVE ID = $conversationId"
                )

                // switch UI immediately
                _conversationId.value =
                    conversationId

                delay(50)

                repository.sendMessage(
                    text,
                    conversationId
                )

                println(
                    "CURRENT ID = $conversationId"
                )

                _hideDatabaseMessages.value =
                    false

                println(
                    "AFTER sendMessage()"
                )

            } catch (e: Exception) {

                println(
                    "CHAT ERROR: ${e.javaClass.name} - ${e.message}"
                )

                showTemporaryError(
                    "Something went wrong: ${e.message}"
                )

            } finally {

                _isLoading.value = false

            }
        }
    }

    private fun showTemporaryError(
        text: String
    ) {

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
                        System.currentTimeMillis()
                )
            )
    }

    override fun onCleared() {

        super.onCleared()

        screenWipeJob?.cancel()
    }
}