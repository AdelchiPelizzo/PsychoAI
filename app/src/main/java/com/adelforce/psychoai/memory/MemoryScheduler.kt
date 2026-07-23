package com.adelforce.psychoai.memory

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.adelforce.psychoai.util.ConversationConfig
import java.util.concurrent.TimeUnit


object MemoryScheduler {


    private const val PERIODIC_WORK_NAME = "memory_update_periodic"
    private const val IMMEDIATE_WORK_NAME = "memory_update_now"
    private const val WORK_NAME = "memory_update"

    fun schedule(context: Context) {

        val request =
            PeriodicWorkRequestBuilder<MemoryUpdateWorker>(
                ConversationConfig.MEMORY_UPDATE_INTERVAL_MINUTES.toLong(),
                TimeUnit.MINUTES
            )
                .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    fun runNow(context: Context) {

        val request =
            OneTimeWorkRequestBuilder<MemoryUpdateWorker>()
                .build()


        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                IMMEDIATE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}