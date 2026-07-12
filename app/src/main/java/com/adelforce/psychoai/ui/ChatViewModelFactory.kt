package com.adelforce.psychoai.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adelforce.psychoai.repository.ConversationRepository


class ChatViewModelFactory(
    private val repository: ConversationRepository,
    private val context: Context
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {

            return ChatViewModel(
                repository,
                context
            ) as T

        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}