package com.adelforce.psychoai.ai

import android.util.Log
import com.adelforce.psychoai.BuildConfig


class OpenAIService {


    private val api =
        NetworkModule.openAIApi


    private val apiKey = BuildConfig.OPENAI_API_KEY
    init {
        println("API KEY LENGTH = ${apiKey.length}")
    }

    suspend fun createEmbedding(
        text: String
    ): List<Float> {

        val response =
            api.createEmbedding(

                authorization =
                    "Bearer ${BuildConfig.OPENAI_API_KEY}",

                request =
                    EmbeddingRequest(

                        model =
                            "text-embedding-3-small",

                        input = text
                    )
            )

        return response
            .data
            .first()
            .embedding
    }


    suspend fun askAI(
        message: String
    ): String {

        Log.d(
            "OpenAIService",
            "Sending prompt: $message"
        )

        println(
            "KEY LENGTH = ${BuildConfig.OPENAI_API_KEY.length}"
        )


        val response =
            api.createResponse(
                authorization =
                    "Bearer ${BuildConfig.OPENAI_API_KEY}",
                request =
                    ResponseRequest(
                        model = "gpt-4.1-mini-2025-04-14",
                        instructions = PsychoAIPrompt.systemInstruction,
                        input = message
                    )
            )


        println("OPENAI RESPONSE: $response")


        return response.output
            .first()
            .content
            .first()
            .text

    }

}