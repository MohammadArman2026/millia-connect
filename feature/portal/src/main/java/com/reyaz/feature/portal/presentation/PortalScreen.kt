package com.reyaz.feature.portal.presentation


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.reyaz.feature.portal.presentation.components.CaptivePortalDialogContent

@Composable
fun PortalScreen(
    modifier: Modifier = Modifier,
    viewModel: PortalViewModel,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
            ,shape = RoundedCornerShape(16.dp),
        ) {
            CaptivePortalDialogContent(viewModel = viewModel)
        }
    }
}