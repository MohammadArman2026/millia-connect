package com.reyaz.core.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
 fun TranslucentLoader( modifier: Modifier = Modifier, message: String? = null, strokeWidth: Dp = 5.dp) {
    Column(
        modifier = modifier
//            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f), shape = RoundedCornerShape(16.dp))
            .clickable(enabled = false, role = Role.Button, onClick = {}),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier,
            strokeWidth = strokeWidth
        )
        message?.let {
            Text(message, modifier = Modifier.padding(top = 16.dp))
        }
    }
}