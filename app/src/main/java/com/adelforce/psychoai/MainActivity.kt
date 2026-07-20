package com.adelforce.psychoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.adelforce.psychoai.memory.MemoryScheduler
import com.adelforce.psychoai.ui.ChatScreen
import com.adelforce.psychoai.ui.theme.PsychoAITheme
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import com.adelforce.psychoai.memory.MemoryUpdateWorker
import com.adelforce.psychoai.util.ConversationConfig
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        testMemoryUpdateWorker()

//        startMemoryWorker()

        // Start background memory update scheduler
        MemoryScheduler.schedule(this)

        setContent {

            PsychoAITheme {

                ChatScreen()


            }
        }
    }

    private fun testMemoryUpdateWorker() {

        val request =
            OneTimeWorkRequestBuilder<MemoryUpdateWorker>()
                .build()


        WorkManager.getInstance(this)
            .enqueue(request)

    }

    private fun startMemoryWorker() {


        // update immediately when app opens

        val startupRequest =
            OneTimeWorkRequestBuilder<MemoryUpdateWorker>()
                .build()


        WorkManager.getInstance(this)
            .enqueue(startupRequest)



        // schedule future updates

        val periodicRequest =
            PeriodicWorkRequestBuilder<MemoryUpdateWorker>(
                ConversationConfig.MEMORY_UPDATE_INTERVAL_MINUTES.toLong(),
                TimeUnit.MINUTES
            )
                .build()


        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "memory_update",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
    }
}