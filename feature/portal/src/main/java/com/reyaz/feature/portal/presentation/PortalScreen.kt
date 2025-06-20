package com.reyaz.feature.portal.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.reyaz.feature.portal.presentation.components.CaptivePortalDialogContent
import com.reyaz.core.ui.components.TranslucentLoader

@Composable
fun PortalScreen(
    modifier: Modifier = Modifier,
    viewModel: PortalViewModel,
    dismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = {dismissDialog() }) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
            ) {
                HorizontalDivider(
                    thickness = 1.5.dp,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.outline
                )
                CaptivePortalDialogContent(viewModel = viewModel)
            }
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { dismissDialog() }
                    .padding(8.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error),
                imageVector = Icons.Default.Clear, contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onError
            )
            Text(
                text = "Wifi Login",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(4.dp),
                fontWeight = FontWeight.SemiBold
            )
            if (viewModel.uiState.collectAsState().value.isLoading)
                TranslucentLoader(modifier = modifier.matchParentSize())
        }
    }
}