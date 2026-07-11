package com.adelforce.psychoai.repository

import com.adelforce.psychoai.ai.OpenAIService


class ConversationRepository(
    private val openAIService: OpenAIService
) {


    suspend fun sendMessage(
        text: String
    ): String {

        return openAIService.askAI(text)

    }

}