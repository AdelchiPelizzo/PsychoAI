package com.adelforce.psychoai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.model.Message
import com.adelforce.psychoai.data.model.Role
import com.adelforce.psychoai.repository.ConversationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ChatViewModel : ViewModel() {


    private val openAIService =
        OpenAIService()


    private val repository =
        ConversationRepository(
            openAIService
        )


    private val _messages =
        MutableStateFlow<List<Message>>(emptyList())


    val messages: StateFlow<List<Message>> =
        _messages



    fun sendMessage(text: String) {


        if (text.isBlank()) return



        val userMessage =
            Message(
                id = System.currentTimeMillis(),
                text = text,
                role = Role.USER,
                timestamp = System.currentTimeMillis()
            )


        _messages.value =
            _messages.value + userMessage



        viewModelScope.launch {


            val response =
                repository.sendMessage(text)



            val assistantMessage =
                Message(
                    id = System.currentTimeMillis(),
                    text = response,
                    role = Role.ASSISTANT,
                    timestamp = System.currentTimeMillis()
                )


            _messages.value =
                _messages.value + assistantMessage

        }

    }

}