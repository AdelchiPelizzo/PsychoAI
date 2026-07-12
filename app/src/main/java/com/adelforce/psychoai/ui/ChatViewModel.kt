package com.adelforce.psychoai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adelforce.psychoai.data.model.Message
import com.adelforce.psychoai.data.model.Role
import com.adelforce.psychoai.repository.ConversationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import com.adelforce.psychoai.util.NetworkUtils

class ChatViewModel(
    private val repository: ConversationRepository,
    private val context: Context
) : ViewModel() {

    private val _messages =
        MutableStateFlow<List<Message>>(emptyList())


    val messages: StateFlow<List<Message>> =
        _messages



    fun sendMessage(text: String) {

        if (text.isBlank()) {
            return
        }

        println("CHECKING INTERNET")

        val internetAvailable =
            NetworkUtils.isInternetAvailable(context)

        println("INTERNET AVAILABLE = $internetAvailable")


        if (!internetAvailable) {

            val errorMessage =
                Message(
                    id = System.currentTimeMillis(),
                    text = "No internet connection. Please check your network.",
                    role = Role.ASSISTANT,
                    timestamp = System.currentTimeMillis()
                )


            _messages.value =
                _messages.value + errorMessage


            return
        }


        val userMessage = Message(
            id = System.currentTimeMillis(),
            text = text,
            role = Role.USER,
            timestamp = System.currentTimeMillis()
        )


        _messages.value =
            _messages.value + userMessage



        viewModelScope.launch {

            try {

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


            } catch (e: Exception) {

                println(
                    "CHAT ERROR: ${e.javaClass.name} - ${e.message}"
                )

                println("ERROR FROM OPENAI: ${e.javaClass.name} ${e.message}")


                val errorText =
                    when (e) {

                        is java.net.UnknownHostException ->
                            "No internet connection. Please check your network."

                        is java.net.SocketTimeoutException ->
                            "The AI service is taking too long to respond. Please try again."

                        is retrofit2.HttpException -> {

                            when (e.code()) {

                                401 ->
                                    "Authentication error. Please check the API key."

                                429 ->
                                    "Too many requests. Please try again later."

                                else ->
                                    "Server error (${e.code()}). Please try again."

                            }
                        }


                        else ->
                            "Something went wrong: ${e.message}"

                    }


                val errorMessage =
                    Message(
                        id = System.currentTimeMillis(),
                        text = errorText,
                        role = Role.ASSISTANT,
                        timestamp = System.currentTimeMillis()
                    )


                _messages.value =
                    _messages.value + errorMessage

            }
        }
    }

}