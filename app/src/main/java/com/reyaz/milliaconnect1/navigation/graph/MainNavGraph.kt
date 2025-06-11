package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.composableRoute
import com.reyaz.core.navigation.nestedGraph

/**
 * Main application navigation graph
 * Contains all authenticated screens and nested feature graphs
 */
internal fun androidx.navigation.NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    // Home Screen
    composableRoute(NavigationRoute.Home) {
        // TODO: Replace with actual Home screen composable
        /*HomeScreen(
            onNavigateToAttendance = {
                navController.navigate(NavigationRoute.Attendance.route)
            },
            onNavigateToAcademics = {
                navController.navigate(NavigationRoute.Academics.route)
            }
        )*/
    }

    // Attendance Feature Graph
    nestedGraph(
        startDestination = NavigationRoute.Attendance,
        route = NavigationRoute.AttendanceGraph
    ) {
        attendanceNavGraph(navController, snackbarHostState)
    }

    // Academic Feature Graph
    nestedGraph(
        startDestination = NavigationRoute.Academics,
        route = NavigationRoute.AcademicGraph
    ) {
        academicNavGraph(navController, snackbarHostState)
    }

    // Profile Feature Graph
    nestedGraph(
        startDestination = NavigationRoute.Profile,
        route = NavigationRoute.ProfileGraph
    ) {
        profileNavGraph(navController, snackbarHostState)
    }
}