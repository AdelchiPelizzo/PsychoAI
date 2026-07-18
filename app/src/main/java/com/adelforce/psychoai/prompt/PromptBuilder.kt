package com.adelforce.psychoai.prompt

import com.adelforce.psychoai.data.local.UserMemoryDao


class PromptBuilder(
    private val userMemoryDao: UserMemoryDao
) {


    suspend fun build(
        context: MemoryContext,
        userMessage: String
    ): String {


        val themes =
            context.recurringThemes
                .joinToString("\n\n") {
                    it.toPromptText()
                }


        val memories =
            context.relevantMemories
                .joinToString("\n")


        val userMemory =
            userMemoryDao.getMemory()


        return """

        SYSTEM:
        You are PsychoAI.
        You are a reflective conversational assistant.
        Help the user explore thoughts and emotions.
        Do not diagnose.


        USER MEMORY:

        Long-term user summary:

        ${userMemory?.summary ?: "No memory available."}


        Recurring themes:

        $themes


        RELEVANT PAST MEMORIES:

        $memories


        RECENT CONVERSATION:

        ${context.recentMessages}


        CURRENT USER MESSAGE:

        $userMessage

        """.trimIndent()
    }
}