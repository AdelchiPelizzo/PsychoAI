package com.adelforce.psychoai.settings


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()) {

    val settings by
    viewModel.settings.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(20.dp),
    ) {
        Button(
            onClick = onBack
        ) {
            Text("Back")
        }

        Text(
            text = "Settings",
            style =
                MaterialTheme.typography.headlineMedium,
        )

        SettingCard(
            title = "🧠 Recollection",
            description =
                "How much should PsychoAI remember?",
        ) {
            Slider(
                value =
                    settings.recollectionLevel,
                onValueChange =
                    {
                        viewModel.setRecollection(it)
                    },
            )

            SliderLabels(
                left = "Less",
                right = "More",
            )
        }

        SettingCard(
            title = "🔎 Insight Depth",
            description =
                "How deeply should PsychoAI connect ideas?",
        ) {
            Slider(
                value =
                    settings.insightLevel,
                onValueChange =
                    {
                        viewModel.setInsight(it)
                    },
            )

            SliderLabels(
                left = "Simple",
                right = "Deep",
            )
        }

        SettingCard(
            title = "💬 Conversation Flow",
            description =
                "How long should conversations remain connected?",
        ) {
            Slider(
                value =
                    settings.conversationLevel,
                onValueChange =
                    {
                        viewModel.setConversation(it)
                    },
            )

            SliderLabels(
                left = "Short",
                right = "Long",
            )
        }
    }
}

@Composable
private fun SettingCard(
    title: String,
    description: String,
    content:
    @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                style =
                    MaterialTheme.typography.titleLarge,
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp),
            )

            Text(
                text = description,
                style =
                    MaterialTheme.typography.bodyMedium,
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp),
            )

            content()
        }
    }
}

@Composable
private fun SliderLabels(
    left: String,
    right: String,
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
    ) {
        Text(left)

        Text(right)
    }
}