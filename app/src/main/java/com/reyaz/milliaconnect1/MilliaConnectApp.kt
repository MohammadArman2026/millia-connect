package com.reyaz.milliaconnect1

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.isCurrentRoute
import com.reyaz.core.ui.components.BottomNavItem
import com.reyaz.core.ui.components.CustomBottomNavigationBar
import com.reyaz.core.ui.components.CustomCenterAlignedTopAppBar
import com.reyaz.milliaconnect1.navigation.MCNavHost
import com.reyaz.milliaconnect1.navigation.TopLevelDestinations
import com.reyaz.milliaconnect1.navigation.getIcon

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

    Scaffold(
        modifier = Modifier,
        topBar = {
            CustomCenterAlignedTopAppBar(
                title = when (currentRoute) {
                    NavigationRoute.Profile.route -> "Class Schedule"
                    else -> /*currentDestination?.titleTextId ?:*/ stringResource(R.string.app_name)
                },
                navigationIcon = if (!!isTopLevelDestination) Icons.Default.Menu else Icons.Default.ArrowBackIosNew,
                onNavigationClick = {
                    if (isTopLevelDestination) {
                        // Handle menu click (e.g., open drawer)
                    } else {
                        navController.navigateUp()
                    }
                },
                actions = {
                    // Add common actions like search and notifications
                    IconButton(onClick = {
                        //navController.navigate(AppDestinations.Notifications.route)
                    }) {
                        Icon(
                            Icons.Default.NotificationsActive, contentDescription = "Notifications",
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(NavigationRoute.Portal.route)
                    }) {
                        Icon(
                            Icons.Default.Wifi, contentDescription = "Wifi",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (isTopLevelDestination) {
                CustomBottomNavigationBar(
                    items = bottomNavItems,
                    selectedRoute = currentRoute ?: NavigationRoute.Home.route,
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
        }
    ) { innerPadding ->
        MCNavHost(modifier = Modifier.padding(innerPadding), navController = navController)
    }
}