package com.adelforce.psychoai.ui

import com.adelforce.psychoai.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.adelforce.psychoai.ui.components.MessageBubble
import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.repository.ConversationRepository
import com.adelforce.psychoai.ui.ChatViewModelFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ChatScreen() {

    val factory = ChatViewModelFactory()

    val viewModel: ChatViewModel = viewModel(
        factory = factory
    )

    val listState = rememberLazyListState()
    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(messages.size) {

        if (messages.isNotEmpty()) {

            listState.animateScrollToItem(
                messages.size - 1
            )

        }

    }

    var message by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x923B4CEC),
                        Color(0xE6C9446E)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = stringResource(R.string.chat_title),
                        style = MaterialTheme.typography.bodyMedium
                    )

                }


                Spacer(
                    modifier = Modifier.width(16.dp)
                )


                Image(
                    painter = painterResource(R.drawable.psyche_logo),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )

            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // Conversation area

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {

                        items(messages) { message ->

                            MessageBubble(
                                message = message
                            )

                        }

                    }

                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // Input area

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(stringResource(R.string.enter_thought))
                    }
                )


                IconButton(
                    onClick = {

                        viewModel.sendMessage(message.text)

                        message = TextFieldValue("")

                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send"
                    )

                }
            }
        }

    }

}