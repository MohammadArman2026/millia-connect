package com.reyaz.milliaconnect.ui.screen.components

import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
 fun FeedBackContent(
     modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    Row(        modifier = modifier.alpha(0.7f),
    ) {
    Text(
        text = "Facing any issue? ",
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
//        fontSize = 16.sp,
    )
//        Spacer(Modifier.weight(1f))


        Text(
        text = "Submit Feedback",
//        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
//        fontSize = 16.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            val url = "https://forms.gle/8g3R2J2G5ohAkhVK6"
            try {
                uriHandler.openUri(url)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No app found", Toast.LENGTH_SHORT).show()
            }
        },
        color = MaterialTheme.colorScheme.primary
    )
}

}