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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.adelforce.psychoai.ui.components.MessageBubble
import com.adelforce.psychoai.ai.OpenAIService
import com.adelforce.psychoai.repository.ConversationRepository
import com.adelforce.psychoai.data.local.DatabaseProvider
import com.adelforce.psychoai.memory.ThemeExtractor
import com.adelforce.psychoai.memory.ThemeRepository
import com.adelforce.psychoai.memory.MemoryRetriever
import com.adelforce.psychoai.memory.MemorySynthesizer
import com.adelforce.psychoai.memory.UserMemoryManager
import com.adelforce.psychoai.memory.search.LinearSearchEngine
import com.adelforce.psychoai.prompt.PromptBuilder
import com.adelforce.psychoai.memory.EmbeddingCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun ChatScreen() {

    val context = LocalContext.current

    val database =
        remember {
            DatabaseProvider.getDatabase(context)
        }

    val messageDao =
        database.messageDao()

    val embeddingCache =
        remember {
            EmbeddingCache(
                embeddingDao = database.messageEmbeddingDao()
            )
        }

    val openAIService =
        remember {
            OpenAIService()
        }

    val userMemoryManager =
        remember {
            UserMemoryManager(
                synthesizer = MemorySynthesizer(
                    openAIService = openAIService
                ),
                messageDao = messageDao,
                userMemoryDao = database.userMemoryDao()
            )
        }

    val repository =
        remember {

            ConversationRepository(
                context = context,

                openAIService = openAIService,

                messageEmbeddingDao =
                    database.messageEmbeddingDao(),

                messageDao = messageDao,

                conversationDao =
                    database.conversationDao(),

                themeExtractor =
                    ThemeExtractor(),

                themeRepository =
                    ThemeRepository(
                        themeDao = database.themeDao(),
                        messageThemeDao =
                            database.messageThemeDao()
                    ),

                messageThemeDao =
                    database.messageThemeDao(),

                memoryRetriever = MemoryRetriever(
                    messageDao = database.messageDao(),
                    themeDao = database.themeDao(),
                    searchEngine = LinearSearchEngine(
                        embeddingDao = database.messageEmbeddingDao(),
                        messageDao = database.messageDao()
                    )
                ),

                promptBuilder =
                    PromptBuilder(
                        userMemoryDao =
                            database.userMemoryDao()
                    ),

                userMemoryManager = userMemoryManager
            )
        }

    LaunchedEffect(Unit) {

        withContext(Dispatchers.IO) {
            embeddingCache.load()
            userMemoryManager.initializeMemoryIfNeeded()
        }
    }

    val factory =
        ChatViewModelFactory(
            repository,
            messageDao,
            context
        )

    val viewModel: ChatViewModel =
        viewModel(
            factory = factory
        )

    val isLoading by viewModel.isLoading.collectAsState()

    val conversationId by viewModel.conversationId.collectAsState()

    val listState =
        rememberLazyListState()

    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(conversationId) {
        listState.scrollToItem(0)
    }

    LaunchedEffect(messages.size) {

        if (messages.isNotEmpty()) {

            kotlinx.coroutines.delay(300)

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

            // HEADER

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


            // MESSAGES

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

                    key(conversationId) {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState
                        ) {

                            if (isLoading) {

                                item {

                                    Text(
                                        text = "PsychoAI is thinking..."
                                    )
                                }
                            }


                            items(messages) { message ->

                                MessageBubble(
                                    message = message
                                )
                            }
                        }
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // INPUT

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
                        Text(
                            stringResource(R.string.enter_thought)
                        )
                    }
                )


                IconButton(
                    onClick = {

                        viewModel.sendMessage(
                            message.text
                        )

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