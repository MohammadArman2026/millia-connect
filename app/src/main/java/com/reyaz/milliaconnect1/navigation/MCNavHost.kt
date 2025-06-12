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
import com.reyaz.core.navigation.nestedGraph
import com.reyaz.milliaconnect1.navigation.graph.attendanceNavGraph
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

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.AttendanceGraph.route,
        modifier = modifier.fillMaxSize()
    ) {
        // Attendance Feature Graph
        navigation(
            route = NavigationRoute.AttendanceGraph.route,
            startDestination = NavigationRoute.Schedule.route
        ){
            attendanceNavGraph(navController, snackbarHostState)
        }

        // Notice Graph
        /*navigation(
            startDestination = NavigationRoute.Notice.route,
            route = NavigationRoute.AllNotice.route
        ) {
            attendanceNavGraph(navController, snackbarHostState)
        }*/

        // Side Navigation Graph
        /*navigation(
            route = NavigationRoute.AcademicGraph.route,
            startDestination = NavigationRoute.Academics.route,
        ) {
            //academicNavGraph(navController, snackbarHostState)
        }*/
    }
}

