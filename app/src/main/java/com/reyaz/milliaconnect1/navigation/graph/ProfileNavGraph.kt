package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.composableRoute

/**
 * Profile feature navigation graph
 */
internal fun androidx.navigation.NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composableRoute(NavigationRoute.Profile) {
        // TODO: Replace with actual Profile screen composable
        /*ProfileScreen(
            onNavigateToEditProfile = {
                navController.navigate(NavigationRoute.EditProfile.route)
            },
            onNavigateToSettings = {
                navController.navigate(NavigationRoute.Settings.route)
            },
            onLogout = {
                navController.navigate(NavigationRoute.AuthGraph.route) {
                    popUpTo(NavigationRoute.MainGraph.route) { inclusive = true }
                }
            }
        )*/
    }

    composableRoute(NavigationRoute.EditProfile) {
        // TODO: Replace with actual EditProfile screen composable
        /*EditProfileScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }

    composableRoute(NavigationRoute.Settings) {
        // TODO: Replace with actual Settings screen composable
        /*SettingsScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }
}