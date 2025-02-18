package com.reyaz.milliaconnect.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.milliaconnect.ui.screen.WebViewScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Wifi Auto Connect") }) }
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
