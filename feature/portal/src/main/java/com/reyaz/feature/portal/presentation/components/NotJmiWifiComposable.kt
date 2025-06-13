package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
 fun NotJmiWifiComposable(onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Wi-Fi Not Connected",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            )
            Text(
                text = "Please connect JMI Wifi and try again.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
        OutlinedButton(
            modifier = Modifier
                .height(48.dp)
                .padding(4.dp, 12.dp, 4.dp, 0.dp)
                .fillMaxWidth(),
            onClick = { onRetry() }) {
            Text("Retry")
        }
    }
}