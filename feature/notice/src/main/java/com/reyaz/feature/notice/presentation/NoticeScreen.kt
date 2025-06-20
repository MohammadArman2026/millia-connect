package com.reyaz.feature.notice.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun NoticeScreen (
    modifier: Modifier = Modifier,
    uiState: NoticeUiState
    ){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    val mContext = LocalContext.current

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        /*ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp
        ) {
            viewModel.tabConfigs.forEach {
                val index = it.key
                Tab(
                    text = {
                        Text(
                            text = it.value.tabName,
                            color = if (selectedTabIndex == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        isLoading  = true
                    }
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loader(
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else if (uiState.error != null) {
            Text("Error: ${uiState.error}")
        } else if (selectedTabIndex==8){
            // Use AndroidView to embed the WebView in Compose
            val webView = remember { WebView(mContext) }

            // Configure the WebView
            DisposableEffect(webView) {
                webView.webViewClient = WebViewClient()
                webView.settings.javaScriptEnabled = true
                webView.loadUrl(viewModel.tabConfigs[selectedTabIndex]?.url ?: "")

                onDispose {
                    webView.stopLoading()
                }
            }

            AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize())
        }else {
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(uiState.notices) { notice ->
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.url))
                                startActivity(mContext, intent, null)
                            },
                        text = notice.title
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        }*/
    }
}