package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.nestedGraph

/**
 * Main application navigation graph
 * Contains all authenticated screens and nested feature graphs
 */
internal fun androidx.navigation.NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {

    // Attendance Feature Graph
    navigation(
        startDestination = NavigationRoute.Schedule.route,
        route = NavigationRoute.AttendanceGraph.route
    ) {
        attendanceNavGraph(navController, snackbarHostState)
    }


    // Academic Feature Graph
    nestedGraph(
        startDestination = NavigationRoute.Academics,
        route = NavigationRoute.AcademicGraph
    ) {
        //academicNavGraph(navController, snackbarHostState)
    }

    // Profile Feature Graph
    nestedGraph(
        startDestination = NavigationRoute.Profile,
        route = NavigationRoute.ProfileGraph
    ) {
        //profileNavGraph(navController, snackbarHostState)
    }
}