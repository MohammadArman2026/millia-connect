package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.feature.attendance.schedule.presentation.ScheduleScreen

/**
 * Attendance feature navigation graph
 */
internal fun NavGraphBuilder.
        attendanceNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {

    // Schedule Screen
    composable(route = NavigationRoute.Schedule.route){
        ScheduleScreen()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text("Coming Soon...")
        }
    }
}


