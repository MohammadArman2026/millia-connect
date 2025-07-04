package com.reyaz.milliaconnect1

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.isCurrentRoute
import com.reyaz.core.ui.components.BottomNavItem
import com.reyaz.core.ui.components.CustomBottomNavigationBar
import com.reyaz.core.ui.components.CustomCenterAlignedTopAppBar
import com.reyaz.feature.portal.presentation.PortalViewModel
import com.reyaz.milliaconnect1.navigation.MCNavHost
import com.reyaz.milliaconnect1.navigation.TopLevelDestinations
import com.reyaz.milliaconnect1.navigation.getIcon
import com.reyaz.milliaconnect1.ui.screen.components.WifiIconComposable
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilliaConnectApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route
    val currentDestination = TopLevelDestinations.ALL.find { it.route.route == currentRoute }
    // Determine if current destination is a top-level destination
    val isTopLevelDestination = TopLevelDestinations.ALL.any { it.route.route == currentRoute }

    val bottomNavItems = TopLevelDestinations.ALL.map {
        BottomNavItem(
            icon = it.getIcon(navController.isCurrentRoute(it.route)),
            route = it.route.route,
            title = it.titleResourceId
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val portalViewModel: PortalViewModel = koinViewModel()
    val portalUiState by portalViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            CustomCenterAlignedTopAppBar(
                title = when (currentRoute) {
                    NavigationRoute.Result.route -> "Entrance Result"
                    NavigationRoute.Schedule.route -> "Class Schedule"
                    NavigationRoute.Notice.route -> "Millia Connect"
                    NavigationRoute.AttendanceHistory.route -> "Attendance Summary"
                    else -> /*currentDestination?.titleTextId ?:*/ stringResource(R.string.app_name)
                },
                navigationIcon = if (isTopLevelDestination) Icons.Default.Menu else Icons.Default.ArrowBackIosNew,
                onNavigationClick = {
                    if (isTopLevelDestination) {
                        // Handle menu click (e.g., open drawer)
                    } else {
                        navController.navigateUp()
                    }
                },
                actions = {
                    // Add common actions like search and notifications
                    /*IconButton(onClick = {
                        //navController.navigate(AppDestinations.Notifications.route)
                    }) {
                        Icon(
                            Icons.Default.NotificationsActive, contentDescription = "Notifications",
                        )
                    }*/

                    WifiIconComposable(
                        portalUiState = portalUiState,
                        navigateToPortal = { navController.navigate(NavigationRoute.Portal.route) })
                }
            )
        },
        bottomBar = {
            if (isTopLevelDestination) {
                CustomBottomNavigationBar(
                    items = bottomNavItems,
                    selectedRoute = currentRoute ?: NavigationRoute.ResultGraph.route,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination and save state
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        MCNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            portalViewModel = portalViewModel,
            snackbarHostState = snackbarHostState
        )
    }
}

