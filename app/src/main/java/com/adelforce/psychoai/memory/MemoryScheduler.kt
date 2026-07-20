package com.adelforce.psychoai.memory

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.adelforce.psychoai.util.ConversationConfig
import java.util.concurrent.TimeUnit


object MemoryScheduler {


    fun schedule(
        context: Context
    ) {

        val request =
            PeriodicWorkRequestBuilder<MemoryUpdateWorker>(
                ConversationConfig.MEMORY_UPDATE_INTERVAL_MINUTES.toLong(),
                TimeUnit.MINUTES
            )
                .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(

                "memory_update",

                ExistingPeriodicWorkPolicy.KEEP,

                request
            )
    }
}