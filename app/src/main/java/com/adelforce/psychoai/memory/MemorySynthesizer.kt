package com.adelforce.psychoai.memory

import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.MessageEntity

class MemorySynthesizer(
    private val openAIService: OpenAIService
) {

    suspend fun synthesize(
        previousMemory: String,
        newMessages: List<MessageEntity>
    ): String {

        val conversation = buildString {

            newMessages.forEach {

                append(it.role)
                append(": ")
                append(it.text)
                append("\n\n")
            }
        }

        val prompt =
            """
SYSTEM:

You maintain the long-term memory of PsychoAI.

Rewrite the user's long-term memory.

Keep only information likely to remain useful in future conversations.

Do not copy conversations.

Do not mention temporary events.

Return only the updated memory.


PREVIOUS MEMORY:

$previousMemory


NEW CONVERSATIONS:

$conversation


UPDATED MEMORY:
            """.trimIndent()

        return openAIService.askAI(prompt)
    }
}