package com.reyaz.milliaconnect1.navigation

import android.util.Config.PROFILE
import com.reyaz.core.navigation.NavigationRoute
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a top-level destination in the app.
 * Used for bottom navigation bar items and main navigation structure.
 */
data class AppTopLevelDestination(
    val route: NavigationRoute,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val titleResourceId: String,
    val iconContentDescription: String? = null,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

/**
 * Companion object containing all top-level destinations for the app
 */
object TopLevelDestinations {

    val SCHEDULE = AppTopLevelDestination(
        route = NavigationRoute.Schedule,
        selectedIcon = Icons.Filled.Timer,
        unselectedIcon = Icons.Outlined.Timer,
        titleResourceId = "Schedule",
        iconContentDescription = "Schedule tab"
    )

    val NOTICE = AppTopLevelDestination(
        route = NavigationRoute.Notice,
        selectedIcon = Icons.Filled.Newspaper,
        unselectedIcon = Icons.Outlined.Newspaper,
        titleResourceId = "Notice",
        iconContentDescription = "Notice tab"
    )

    val RESULT = AppTopLevelDestination(
        route = NavigationRoute.Academics,
        selectedIcon = Icons.Filled.School,
        unselectedIcon = Icons.Outlined.School,
        titleResourceId = "Result",
        iconContentDescription = "Result tab"
    )

    /**
     * List of all top-level destinations in the order they should appear
     */
    val ALL = listOf(SCHEDULE, NOTICE, RESULT)
}

/**
 * Extension functions for AppTopLevelDestination
 */

/**
 * Returns the appropriate icon based on selection state
 */
fun AppTopLevelDestination.getIcon(isSelected: Boolean): ImageVector {
    return if (isSelected) selectedIcon else unselectedIcon
}

/**
 * Returns whether this destination should show a badge
 */
fun AppTopLevelDestination.shouldShowBadge(): Boolean {
    return hasNews || (badgeCount != null && badgeCount > 0)
}

/**
 * Returns the badge text to display
 */
fun AppTopLevelDestination.getBadgeText(): String? {
    return when {
        badgeCount != null && badgeCount > 0 -> {
            if (badgeCount > 99) "99+" else badgeCount.toString()
        }
        hasNews -> "•"
        else -> null
    }
}

/**
 * Extension function to find a top-level destination by route
 */
fun List<AppTopLevelDestination>.findByRoute(route: NavigationRoute): AppTopLevelDestination? {
    return find { it.route == route }
}

/**
 * Extension function to check if a route is a top-level destination
 */
/*fun NavigationRoute.isTopLevelDestination(): Boolean {
    return TopLevelDestinations.ALL.any { it.route == this }
}*/

/**
 * Extension function to get the top-level destination for a route
 */
fun NavigationRoute.toTopLevelDestination(): AppTopLevelDestination? {
    return TopLevelDestinations.ALL.find { it.route == this }
}

/**
 * Builder class for creating custom top-level destinations
 */
class TopLevelDestinationBuilder {
    private var route: NavigationRoute? = null
    private var selectedIcon: ImageVector? = null
    private var unselectedIcon: ImageVector? = null
    private var titleResourceId: String? = null
    private var iconContentDescription: String? = null
    private var hasNews: Boolean = false
    private var badgeCount: Int? = null

    fun route(route: NavigationRoute) = apply { this.route = route }
    fun selectedIcon(icon: ImageVector) = apply { this.selectedIcon = icon }
    fun unselectedIcon(icon: ImageVector) = apply { this.unselectedIcon = icon }
    fun title(title: String) = apply { this.titleResourceId = title }
    fun contentDescription(description: String) = apply { this.iconContentDescription = description }
    fun hasNews(hasNews: Boolean) = apply { this.hasNews = hasNews }
    fun badgeCount(count: Int?) = apply { this.badgeCount = count }

    fun build(): AppTopLevelDestination {
        return AppTopLevelDestination(
            route = route ?: throw IllegalStateException("Route must be set"),
            selectedIcon = selectedIcon ?: throw IllegalStateException("Selected icon must be set"),
            unselectedIcon = unselectedIcon ?: throw IllegalStateException("Unselected icon must be set"),
            titleResourceId = titleResourceId ?: throw IllegalStateException("Title must be set"),
            iconContentDescription = iconContentDescription,
            hasNews = hasNews,
            badgeCount = badgeCount
        )
    }
}

/**
 * DSL function for creating top-level destinations
 */
fun topLevelDestination(builder: TopLevelDestinationBuilder.() -> Unit): AppTopLevelDestination {
    return TopLevelDestinationBuilder().apply(builder).build()
}