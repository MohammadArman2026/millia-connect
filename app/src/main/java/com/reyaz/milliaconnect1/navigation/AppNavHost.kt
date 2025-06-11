package com.reyaz.milliaconnect1.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import com.reyaz.core.navigation.NavigationRoute
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.reyaz.core.navigation.nestedGraph
import com.reyaz.milliaconnect1.navigation.graph.authNavGraph
import com.reyaz.milliaconnect1.navigation.graph.mainNavGraph

/**
 * Main navigation host for the Millia Connect App
 * Manages all navigation graphs and routing logic
 */
@Composable
fun NewAppNavHost(
    navController: NavHostController,
    startDestination: NavigationRoute = NavigationRoute.AuthGraph,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(
        navController = navController,
        startDestination = /*startDestination.route*/ NavigationRoute.Login.route,
        modifier = modifier.fillMaxSize().safeContentPadding()
    ) {
        // Authentication Graph
        composable(NavigationRoute.Login.route){
            Column {
                Text("hellow")
            }
        }
        nestedGraph(
            startDestination = NavigationRoute.Login,
            route = NavigationRoute.AuthGraph
        ) {
            authNavGraph(navController)
        }

        // Main App Graph (requires authentication)
        nestedGraph(
            startDestination = NavigationRoute.Home,
            route = NavigationRoute.MainGraph
        ) {
            //mainNavGraph(navController, snackbarHostState)
        }
    }
}

