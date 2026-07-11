package com.adelforce.psychoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.adelforce.psychoai.ui.ChatScreen
import com.adelforce.psychoai.ui.theme.PsychoAITheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            PsychoAITheme {

                ChatScreen()


            }
        }
    }
}