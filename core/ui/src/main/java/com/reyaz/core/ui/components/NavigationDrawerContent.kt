package com.reyaz.core.ui.components

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.reyaz.core.common.utlis.openUrl
import com.reyaz.core.common.utlis.shareTextExternally
import com.reyaz.core.ui.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    val context = LocalContext.current
    var showAboutDeveloper by remember { mutableStateOf(false) }
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
    ModalDrawerSheet(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            // logo and name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.app_logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = "Millia\nConnect",
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp,
                    fontSize = 28.sp,
                )
            }
            HorizontalDivider()

            // about developer
            Column(
                modifier = Modifier
                    .clickable {
                        showAboutDeveloper = !showAboutDeveloper
                    }
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                // heading
                Row(
                    Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "About Developer",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = null,
                        modifier = if (showAboutDeveloper) Modifier.rotate(180f) else Modifier
                    )
                }
                // social
                AnimatedVisibility(showAboutDeveloper) {
                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .padding(top = 16.dp),
//                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sadique),
                            contentDescription = "sadique",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Sadique Reyaz",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            //Spacer(modifier = Modifier.weight(1f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
//                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                SocialButton(
                                    onClick = { context.openUrl("https://github.com/mdsadique47") },
                                    text = "linkedin"
                                )
                                SocialButton(
                                    onClick = { context.openUrl("mailto:mdsadique47@gmail.com") },
                                    text = "mdsadique47@gmail.com"
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider(modifier = Modifier)
            Text(
                text = "JMI Official Websites",
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            CustomModalLinkText(text = "jmi.coe", link = "https://jmicoe.in/")
            CustomModalLinkText(text = "jmi.ac.in", link = "https://jmi.ac.in/")
            CustomModalLinkText(
                text = "admissions.jmi.ac.in",
                link = "https://admission.jmi.ac.in/"
            )
            CustomModalLinkText(
                text = "Entrance Result",
                link = "https://admission.jmi.ac.in/EntranceResults/UniversityResult"
            )
            CustomModalLinkText(
                text = "MyJamia ( Fee payment, Wifi, hostel registration etc. )",
                link = "https://mj.jmi.ac.in/"
            )
            CustomModalLinkText(
                text = "Regular Student Login (Academic Info)",
                link = "http://jmiregular.ucanapply.com/universitysystem/student/"
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier)

            // share button
            NavigationDrawerItem(
                label = { Text("Share") },
                selected = false,
                icon = { Icon(Icons.Outlined.Share, contentDescription = null) },
                onClick = { context.shareTextExternally("Hey! Checkout this app: https://play.google.com/store/apps/details?id=${context.packageName}\n made for Jamians with \uD83D\uDC9A") }
            )

            // feedback
            NavigationDrawerItem(
                label = { Text("Help and feedback") },
                selected = false,
                icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { context.openUrl("https://forms.gle/s3a9mjcps5HmHgrt5") },
            )
            Spacer(Modifier.weight(1f))
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp),
                onClick = { scope.launch { drawerState.close() } }) {
                Icon(
                    modifier = Modifier
                        .rotate(180f)
                        .size(20.dp),
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "close drawer",

                    )
                Spacer(Modifier.width(8.dp))
                Text(text = "Close")
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SocialButton(onClick: () -> Unit, text: String) {
    TextButton(
        onClick = { onClick.invoke() }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun CustomModalLinkText(text: String, link: String) {
    val context = LocalContext.current
    TextButton(
        onClick = { context.openUrl(link) }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
//        Icon(ImageVector.vectorResource(R.drawable))
    }
}