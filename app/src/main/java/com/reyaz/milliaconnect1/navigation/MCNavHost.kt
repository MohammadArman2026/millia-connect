package com.reyaz.milliaconnect1.navigation

import androidx.compose.foundation.layout.fillMaxSize
import com.reyaz.core.navigation.NavigationRoute
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.reyaz.feature.portal.presentation.PortalScreen
import com.reyaz.feature.portal.presentation.PortalViewModel
import com.reyaz.milliaconnect1.navigation.graph.attendanceNavGraph

/**
 * Main navigation host for the Millia Connect App
 * Manages all navigation graphs and routing logic
 */
@Composable
fun MCNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    portalViewModel: PortalViewModel,
) {

    NavHost(
        navController = navController,
        startDestination =
//            NavigationRoute.AttendanceGraph.route,
            NavigationRoute.Portal.route,
        modifier = modifier.fillMaxSize()
    ) {
        // Attendance Feature Graph
        navigation(
            route = NavigationRoute.AttendanceGraph.route,
            startDestination = NavigationRoute.Schedule.route
        ){
            attendanceNavGraph(navController, snackbarHostState)
        }

        dialog(route = NavigationRoute.Portal.route){
            PortalScreen(viewModel = portalViewModel, dismissDialog = {
                navController.navigateUp()
            })
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

