package com.reyaz.milliaconnect1

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reyaz.core.navigation.isCurrentRoute
import com.reyaz.core.ui.components.BottomNavItem
import com.reyaz.core.ui.components.CustomBottomNavigationBar
import com.reyaz.core.ui.components.CustomCenterAlignedTopAppBar
import com.reyaz.core.ui.components.NavigationDrawerContent
import com.reyaz.feature.portal.presentation.PortalViewModel
import com.reyaz.milliaconnect1.navigation.MCNavHost
import com.reyaz.milliaconnect1.navigation.TopLevelDestinations
import com.reyaz.milliaconnect1.navigation.getIcon
import com.reyaz.milliaconnect1.ui.components.WifiIconComposable
import constants.NavigationRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilliaConnectApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route
    val currentDestination = TopLevelDestinations.ALL.find { it.route.route == currentRoute }
    // Determine if current destination is a top-level destination
    val isTopLevelDestination = TopLevelDestinations.ALL.any { it.route.route == currentRoute || currentRoute == NavigationRoute.Portal.route }

    val bottomNavItems = TopLevelDestinations.ALL.map {
        BottomNavItem(
            icon = it.getIcon(navController.isCurrentRoute(it.route)),
            route = it.route.route,
            title = it.titleResourceId
        )
    }
    var showSearchComponents by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val portalViewModel: PortalViewModel = koinViewModel()
    val portalUiState by portalViewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val actionIconItems = listOf<ActionIconItem>(
        ActionIconItem.IconButtonItem(
            actionRoute = NavigationRoute.PropertyFeed.route,
            icon = Icons.Default.Search,
            onClick = { showSearchComponents = !showSearchComponents },
            contentDescription = "Search Property"
        ),
        ActionIconItem.CustomComposableItem(
            actionRoute = NavigationRoute.Portal.route,
            content = {
                WifiIconComposable(
                    portalUiState = portalUiState,
                    navigateToPortal = {
                        navController.navigate(NavigationRoute.Portal.route)
                    }
                )
            }
        )
    )


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        },
        gesturesEnabled = false
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                CustomCenterAlignedTopAppBar(
                    title = when (currentRoute) {
                        NavigationRoute.Result.route -> "Entrance Result"
                        NavigationRoute.Schedule.route -> "Class Schedule"
//                        NavigationRoute.Notice.route -> "Millia Connect"
                        NavigationRoute.AttendanceHistory.route -> "Attendance Summary"
                        NavigationRoute.PropertyFeed.route -> "Rent Property"
                        NavigationRoute.CreatePost.route -> "List Your Property"
                        else -> /*currentDestination?.titleTextId ?:*/ stringResource(R.string.app_name)
                    },
                    navigationIcon = if (isTopLevelDestination) Icons.Default.Menu else Icons.Default.ArrowBackIosNew,
                    onNavigationClick = {
                        if (isTopLevelDestination) {
                            scope.launch {
                                drawerState.open()
                            }
                        } else {
                            navController.navigateUp()
                        }
                    },
                    actions = {
                        val actionsForRoute = actionIconItems.filter { it.route == currentRoute }

                        if (actionsForRoute.isEmpty()) {
                            if (isTopLevelDestination) {
                                WifiIconComposable(
                                    portalUiState = portalUiState,
                                    navigateToPortal = {
                                        navController.navigate(NavigationRoute.Portal.route)
                                    }
                                )
                            } else {
                                IconButton(onClick = { /* default search or noop */ }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                                }
                            }
                        } else {
                            actionsForRoute.forEach { action ->
                                when (action) {
                                    is ActionIconItem.IconButtonItem -> {
                                        IconButton(onClick = action.onClick) {
                                            Icon(
                                                imageVector = action.icon,
                                                contentDescription = action.contentDescription
                                            )
                                        }
                                    }

                                    is ActionIconItem.CustomComposableItem -> {
                                        action.content()
                                    }
                                }
                            }
                        }
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
                snackbarHostState = snackbarHostState,
                showSearchComponents = showSearchComponents,
            )
        }
    }
}

