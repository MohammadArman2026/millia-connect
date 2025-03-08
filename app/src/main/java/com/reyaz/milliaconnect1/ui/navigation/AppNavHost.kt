package com.reyaz.milliaconnect1.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.milliaconnect1.ui.screen.WebViewScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   /* Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.millia_connect_logo),
                        "logo",
                        modifier = Modifier.size(36.dp)
                    )*/
                    Text("Millia Connect", fontWeight = FontWeight.Bold)
                }
            })
        }
    )
    { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Route.Home.name
        ) {
            composable(Route.Home.name) {
                WebViewScreen()
            }
            composable(Route.Setting.name) {
//            val detailsViewModel: MyViewModel = getViewModel()
//            DetailsScreen(detailsViewModel)
            }
        }
    }
}

sealed class Route(val name: String) {
    data object Home : Route("home")
    data object Setting : Route("setting")
}
