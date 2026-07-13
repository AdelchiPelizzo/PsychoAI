package com.adelforce.psychoai.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adelforce.psychoai.data.local.MessageDao
import com.adelforce.psychoai.data.model.Message
import com.adelforce.psychoai.data.model.Role
import com.adelforce.psychoai.repository.ConversationRepository
import com.adelforce.psychoai.util.NetworkUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ChatViewModel(
    private val repository: ConversationRepository,
    private val messageDao: MessageDao,
    private val context: Context
) : ViewModel() {


    private val _conversationId =
        MutableStateFlow<Long?>(null)


    val conversationId =
        _conversationId.asStateFlow()



    init {

        println("CHAT VIEWMODEL CREATED")

        viewModelScope.launch {

            val id =
                repository.getActiveConversationId()


            _conversationId.value =
                id


            println(
                "ACTIVE CONVERSATION ID = $id"
            )

        }

    }



    private val _temporaryMessages =
        MutableStateFlow<List<Message>>(emptyList())



    val messages: StateFlow<List<Message>> =

        conversationId
            .filterNotNull()
            .flatMapLatest { id ->

                combine(

                    messageDao.getMessagesForConversation(id),

                    _temporaryMessages

                ) { databaseMessages, temporaryMessages ->


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




    fun sendMessage(text: String) {


        if (text.isBlank()) {
            return
        }


        if (_isLoading.value) {
            return
        }



        println("CHECKING INTERNET")


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


                repository.sendMessage(text)


                _temporaryMessages.value =
                    emptyList()


            }


            catch (e: Exception) {


                println(
                    "CHAT ERROR: ${e.javaClass.name} - ${e.message}"
                )



                val errorText =
                    when (e) {


                        is java.net.UnknownHostException ->

                            "No internet connection. Please check your network."



                        is java.net.SocketTimeoutException ->

                            "The AI service is taking too long to respond. Please try again."



                        is retrofit2.HttpException -> {


                            when(e.code()) {


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



                showTemporaryError(errorText)


            }


            finally {

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

                    id = System.currentTimeMillis(),

                    text = text,

                    role = Role.ASSISTANT,

                    timestamp = System.currentTimeMillis()

                )

            )

    }

}