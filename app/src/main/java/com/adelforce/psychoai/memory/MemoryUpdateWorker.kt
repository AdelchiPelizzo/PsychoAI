package com.adelforce.psychoai.memory

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.data.local.DatabaseProvider

class MemoryUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result =
        try {
            println(
                "MEMORY WORKER STARTED",
            )

            val database =
                DatabaseProvider.getDatabase(
                    applicationContext,
                )

            val memoryManager =
                UserMemoryManager(
                    synthesizer =
                        MemorySynthesizer(
                            openAIService =
                                OpenAIService(),
                        ),
                    messageDao =
                        database.messageDao(),
                    userMemoryDao =
                        database.userMemoryDao(),
                )

            if (memoryManager.shouldUpdateMemory()) {
                println(
                    "MEMORY UPDATE REQUIRED",
                )

                memoryManager.updateMemory()
            } else {
                println(
                    "MEMORY UPDATE SKIPPED",
                )
            }

            Result.success()
        } catch (e: Exception) {
            println(
                "MEMORY WORKER FAILED: ${e.message}",
            )

            Result.retry()
        }
}
