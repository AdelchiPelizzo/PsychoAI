package com.adelforce.psychoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.adelforce.psychoai.memory.MemoryScheduler
import com.adelforce.psychoai.ui.ChatScreen
import com.adelforce.psychoai.ui.MainPager
import com.adelforce.psychoai.ui.theme.PsychoAITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

//        MemoryScheduler.runNow(this)

//        MemoryScheduler.schedule(this)

        setContent {

            PsychoAITheme {

                LaunchedEffect(Unit) {
                    MemoryScheduler.schedule(this@MainActivity)
                }

                MainPager()

            }
        }
    }
}