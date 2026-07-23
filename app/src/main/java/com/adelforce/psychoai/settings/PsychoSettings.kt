package com.adelforce.psychoai.settings


data class PsychoSettings(

    // 🧠 Recollection
    // 0 = forgetful, 1 = deep memory
    val recollectionLevel: Float = 0.5f,


    // 🔎 Insight depth
    // 0 = only strong matches
    // 1 = more subtle connections
    val insightLevel: Float = 0.5f,


    // 💬 Conversation flow
    // 0 = short sessions
    // 1 = longer continuity
    val conversationLevel: Float = 0.5f
)