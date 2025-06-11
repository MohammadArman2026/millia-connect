package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.composableRoute

/**
 * Attendance feature navigation graph
 */
internal fun androidx.navigation.NavGraphBuilder.attendanceNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composableRoute(NavigationRoute.Attendance) {
        // TODO: Replace with actual Attendance screen composable
       /* AttendanceScreen(
            onNavigateToHistory = {
                navController.navigate(NavigationRoute.AttendanceHistory.route)
            },
            onNavigateToMarkAttendance = {
                navController.navigate(NavigationRoute.MarkAttendance.route)
            },
            onNavigateToDetails = { attendanceId ->
                navController.navigate(NavigationRoute.AttendanceDetails.createRoute(attendanceId))
            }
        )*/
    }

    composableRoute(NavigationRoute.AttendanceHistory) {
       /* // TODO: Replace with actual AttendanceHistory screen composable
        AttendanceHistoryScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToDetails = { attendanceId ->
                navController.navigate(NavigationRoute.AttendanceDetails.createRoute(attendanceId))
            }
        )*/
    }

    composable(NavigationRoute.AttendanceDetails.route) { backStackEntry ->
        val attendanceId = backStackEntry.arguments?.getString("attendanceId") ?: ""
        // TODO: Replace with actual AttendanceDetails screen composable
        /*AttendanceDetailsScreen(
            attendanceId = attendanceId,
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }

    composableRoute(NavigationRoute.MarkAttendance) {
        // TODO: Replace with actual MarkAttendance screen composable
        /*MarkAttendanceScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onAttendanceMarked = {
                navController.popBackStack()
            }
        )*/
    }
}