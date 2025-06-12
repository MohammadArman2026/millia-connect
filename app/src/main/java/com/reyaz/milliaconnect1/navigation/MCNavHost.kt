package com.reyaz.milliaconnect1.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.fillMaxSize
import com.reyaz.core.navigation.NavigationRoute
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.reyaz.milliaconnect1.navigation.graph.mainNavGraph

/**
 * Main navigation host for the Millia Connect App
 * Manages all navigation graphs and routing logic
 */
@Composable
fun MCNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.MainGraph.route,
        modifier = modifier.fillMaxSize()
    ) {
        /*nestedGraph(
            startDestination = NavigationRoute.Login,
            route = NavigationRoute.AuthGraph
        ) {
            authNavGraph(navController)
        }*/

        // Main App Graph (requires authentication)
       /* navigation(
            startDestination = NavigationRoute.AttendanceGraph.route,
            route = NavigationRoute.MainGraph.route
        ){
            mainNavGraph(navController, snackbarHostState)
        }*/

        navigation(
            startDestination = NavigationRoute.AttendanceGraph.route,
            route = NavigationRoute.MainGraph.route
        ){
            mainNavGraph(navController, snackbarHostState)
        }
    }
}

fun NavGraphBuilder.composableRoute(
    route: NavigationRoute,
    content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(route = route.route, content = content)
}

