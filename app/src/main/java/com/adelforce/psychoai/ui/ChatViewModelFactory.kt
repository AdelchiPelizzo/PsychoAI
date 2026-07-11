package com.adelforce.psychoai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ChatViewModelFactory : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {

            return ChatViewModel() as T

        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}