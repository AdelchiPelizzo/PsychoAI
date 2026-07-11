package com.adelforce.psychoai.ai

import kotlinx.serialization.Serializable


@Serializable
data class ResponseRequest(
    val model: String,
    val input: String
)


@Serializable
data class ResponseResponse(
    val output: List<OutputItem>
)


@Serializable
data class OutputItem(
    val content: List<ContentItem>
)


@Serializable
data class ContentItem(
    val text: String
)