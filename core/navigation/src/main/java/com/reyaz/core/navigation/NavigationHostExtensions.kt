package com.reyaz.core.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlin.math.round

/**
 * Extension function to safely navigate to a route
 * Prevents duplicate navigation to the same destination
 */
fun NavController.navigateSafely(
    route: String,
    navOptions: NavOptions? = null
) {
    if (currentDestination?.route != route) {
        navigate(route, navOptions)
    }
}

/**
 * Extension function to navigate to a NavigationRoute
 */
fun NavController.navigateTo(
    navigationRoute: NavigationRoute,
    navOptions: NavOptions? = null
) {
    navigateSafely(navigationRoute.route, navOptions)
}

/**
 * Extension function to navigate with single top and pop up to behavior
 * Common pattern for bottom navigation
 */
fun NavController.navigateToBottomNavRoute(route: NavigationRoute) {
    navigate(route.route) {
        // Pop up to the start destination of the graph to avoid building up a large stack
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

/**
 * Extension function to check if current destination matches the route
 */
fun NavController.isCurrentRoute(route: NavigationRoute): Boolean {
    return currentDestination?.route == route.route
}


/**
 * Extension function to add nested navigation graph
 */
fun NavGraphBuilder.nestedGraph(
    startDestination: NavigationRoute,
    route: NavigationRoute,
    builder: NavGraphBuilder.() -> Unit
) {
    navigation(
        startDestination = startDestination.route,
        route = route.route,
        builder = builder
    )
}

/**
 * Extension function to add composable with NavigationRoute
 */
fun NavGraphBuilder.composableRoute(
    navigationRoute: NavigationRoute,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = navigationRoute.route,
        content = content
    )
}


/**
 * Extension function to get route arguments from NavBackStackEntry
 */
fun NavBackStackEntry.getRouteArgument(key: String): String? {
    return arguments?.getString(key)
}

/**
 * Extension function to get required route argument
 */
fun NavBackStackEntry.getRequiredRouteArgument(key: String): String {
    return arguments?.getString(key)
        ?: throw IllegalArgumentException("Required argument '$key' not found")
}

/**
 * Extension function to check if back stack has specific route
 */
fun NavController.hasRoute(route: NavigationRoute): Boolean {
    return try {
        getBackStackEntry(route.route)
        true
    } catch (e: IllegalArgumentException){
        false
    }
}

/**
 * Extension function to clear back stack and navigate
 */
fun NavController.clearBackStackAndNavigate(route: NavigationRoute) {
    navigate(route.route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Extension function for conditional navigation
 * Useful for navigation based on authentication state
 */
fun NavController.navigateConditionally(
    condition: Boolean,
    trueRoute: NavigationRoute,
    falseRoute: NavigationRoute
) {
    val targetRoute = if (condition) trueRoute else falseRoute
    clearBackStackAndNavigate(targetRoute)
}

/**
 * Extension function to pop back stack to specific route
 */
fun NavController.popBackToRoute(
    route: NavigationRoute,
    inclusive: Boolean = false
): Boolean {
    return popBackStack(route.route, inclusive)
}