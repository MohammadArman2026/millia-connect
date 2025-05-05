package com.reyaz.milliaconnect1.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.reyaz.milliaconnect1.R
import com.reyaz.milliaconnect1.ui.screen.components.FeedBackContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: VMLogin = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoggedIn) {
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.connectedimage),
                    "connected icon",
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clip(CircleShape)
                )
                Text(
                    text = "You're connected to \nJamia Wifi.",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp),
                    color = if(isSystemInDarkTheme()) Color(0xFF89AC46) else Color(0xFF008000)
                )
                if(uiState.isMobileDataOn)
                    Text(
                        text = "Please turn you Mobile Data Off to make Wifi your primary connection.",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                Spacer(Modifier.weight(1f))

                // disconnect btn
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { viewModel.logout() }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.wifi_off),
                            "wifi off"
                        )
                        Text("Disconnect")
                    }
                }
            } else {
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.millia_connect_logo),
                    "logo",
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("Student Id") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible }
                        ) {
                            val icon = if(isPasswordVisible)
                                ImageVector.vectorResource(id = R.drawable.visibility_off_24)
                            else
                                ImageVector.vectorResource(id = R.drawable.visibility_on_24)

                            Icon(
                                imageVector = icon,
                                contentDescription = "password visibility button",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.handleLogin()
                        }
                    )
                )

                //switch
                Row(
                    modifier = Modifier
//                    .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = (Arrangement.End)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = if (uiState.autoConnect) "Your device will automatically connect when session expired." else "Auto Connect",
                        textAlign = TextAlign.End,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 16.sp
                    )
                    Spacer(Modifier.width(16.dp))
                    Switch(
                        checked = uiState.autoConnect,
                        onCheckedChange = { viewModel.updateAutoConnect(it, context) },
                        colors = SwitchDefaults.colors()
                    )
                }
                // login btn
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.handleLogin()
                    },
                    modifier = Modifier
                    .padding(top = 16.dp)
                        .fillMaxWidth(),
                    enabled = uiState.loginEnabled
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.wifi_on),
                            "wifi on"
                        )
                        Text("Get Internet Access")
                    }
                }

//                Spacer(Modifier.weight(1.2f))
            }
            FeedBackContent(modifier = Modifier.padding(top = 12.dp, start = 24.dp, end = 24.dp))

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        uiState.loadingMessage?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .clickable(enabled = false, role = Role.Button, onClick = {}),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                )
                Text(it, modifier = Modifier.padding(top = 16.dp))
            }
        }
        if (uiState.showDialog) {
            val onPressedCallback = { (context as? Activity)?.finish() }
            BackHandler { onPressedCallback()/* Exit the app when back button is pressed */ }
            Dialog(
                onDismissRequest = { /* viewModel.dismissNoWifiDialog() */ }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    DialogContent()
                }
            }
        }
    }
}

@Composable
fun DialogContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
//            .padding(24.dp)
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //close button
            Icon(
                Icons.Default.Clear,
                "close",
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .clickable {
                        (context as Activity).finish()
                    }
                    .background(color = MaterialTheme.colorScheme.error)

            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Not Connected",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(Modifier.weight(1.1f))
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

        Text(
            text = "Please connect to JMI-Wifi and try again!", textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
        )
    }

}

/*enum class DialogContent(val title: String, val imageId: Int, val subtitle: String) {
    LOGGED_IN(
        "Already Connected",
        R.drawable.connectedimagesmall,
        "You'll be automatically connected to Wi-Fi once your session expires."
    ),
    NO_WIFI("No Wifi", R.drawable.nowifi, "Please connect JMI Wifi and try again.")
}*/
