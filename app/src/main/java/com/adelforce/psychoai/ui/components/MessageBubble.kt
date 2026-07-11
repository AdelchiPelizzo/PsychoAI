package com.adelforce.psychoai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adelforce.psychoai.data.model.Message
import com.adelforce.psychoai.data.model.Role


@Composable
fun MessageBubble(
    message: Message
) {

    val isUser = message.role == Role.USER


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser)
            Arrangement.End
        else
            Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    if (isUser)
                        androidx.compose.ui.graphics.Color(0xFFE3F2FD)
                    else
                        androidx.compose.ui.graphics.Color(0xFFF1F1F1)
            )
        ) {

            Text(
                text = message.text,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
            )

        }

    }
}