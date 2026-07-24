package com.adelforce.psychoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.adelforce.psychoai.memory.MemoryScheduler
import com.adelforce.psychoai.ui.MainPager
import com.adelforce.psychoai.ui.theme.PsychoAITheme
import com.adelforce.psychoai.settings.SettingsDataStore
import com.adelforce.psychoai.settings.SettingsMapper
import com.adelforce.psychoai.util.ConversationConfig
import kotlinx.coroutines.flow.first


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContent {

            PsychoAITheme {


                LaunchedEffect(Unit) {

                    val psychoSettings =
                        SettingsDataStore(this@MainActivity)
                            .settingsFlow
                            .first()


                    val conversationSettings =
                        SettingsMapper.map(
                            psychoSettings
                        )


                    ConversationConfig.apply(
                        conversationSettings
                    )


                    MemoryScheduler.schedule(
                        this@MainActivity
                    )
                }


                MainPager()

            }
        }
    }
}