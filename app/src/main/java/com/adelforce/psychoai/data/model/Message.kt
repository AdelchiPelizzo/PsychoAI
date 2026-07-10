package com.adelforce.psychoai.data.model

data class Message(

    val id: Long,

    val role: Role,

    val text: String,

    val timestamp: Long

)