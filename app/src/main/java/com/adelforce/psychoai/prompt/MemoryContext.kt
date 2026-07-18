package com.adelforce.psychoai.prompt

import com.adelforce.psychoai.data.model.Message
import com.adelforce.psychoai.data.model.ThemeMemory


data class MemoryContext(

    val recurringThemes: List<ThemeMemory>,

    val recentMessages: List<Message>,

    val relevantMemories: List<String>

)