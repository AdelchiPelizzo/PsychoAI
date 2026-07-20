package com.adelforce.psychoai.ai


import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface OpenAIApi {


    @POST("v1/responses")
    suspend fun createResponse(

        @Header("Authorization")
        authorization: String,

        @Body
        request: ResponseRequest

    ): ResponseResponse

    @POST("v1/embeddings")
    suspend fun createEmbedding(

        @Header("Authorization")
        authorization: String,

        @Body
        request: EmbeddingRequest

    ): EmbeddingResponse

}