package com.adelforce.psychoai.ai

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit


object NetworkModule {

    private const val BASE_URL =
        "https://api.openai.com/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .client(client)
            .build()

    val openAIApi: OpenAIApi =
        retrofit.create(
            OpenAIApi::class.java
        )

}