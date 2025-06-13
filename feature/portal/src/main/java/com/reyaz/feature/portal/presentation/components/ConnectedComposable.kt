package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.R

@Composable
internal fun ConnectedComposable(
    modifier: Modifier = Modifier,
    onDisconnect: () -> Unit,
    ) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.connectedimage),
            "connected icon",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Text(
            text = "You're connected to \nJamia Wifi.",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp),
            color = if (isSystemInDarkTheme()) Color(0xFFDFFFA1) else Color(0xFF008000)
        )
        OutlinedButton(
            modifier = Modifier
                .height(48.dp)
                .padding(4.dp, 12.dp, 4.dp, 0.dp)
                .fillMaxWidth(),
            onClick = { onDisconnect() }) {
            Text("Logout")
        }
    }
}