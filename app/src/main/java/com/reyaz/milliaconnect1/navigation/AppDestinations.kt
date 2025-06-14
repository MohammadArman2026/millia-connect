package com.reyaz.milliaconnect1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import com.reyaz.core.navigation.NavigationRoute

/**
 * Represents a top-level destination in the app with bottom navigation support
 */
/*data class AppTopLevelDestination(
    val route: NavigationRoute,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val titleResourceId: String,
    val hasNews: Boolean = false,
          val iconContentDescription: String = ""
)*/

/**
 * Object containing all app destinations and navigation-related utilities
 */
object AppDestinations {

    /**
     * Top-level destinations for bottom navigation
     */
    val topLevelDestinations = listOf(
        AppTopLevelDestination(
            route = NavigationRoute.Home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            titleResourceId = "Home"
        ),
        AppTopLevelDestination(
            route = NavigationRoute.NoticeGraph,
            selectedIcon = Icons.Filled.CalendarToday,
            unselectedIcon = Icons.Outlined.CalendarToday,
            titleResourceId = "Attendance"
        ),
        AppTopLevelDestination(
            route = NavigationRoute.Academics,
            selectedIcon = Icons.Filled.School,
            unselectedIcon = Icons.Outlined.School,
            titleResourceId = "Academics"
        ),
        AppTopLevelDestination(
            route = NavigationRoute.Profile,
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            titleResourceId = "Profile"
        )
    )

    /**
     * Authentication flow destinations
     */
    val authDestinations = listOf(
        NavigationRoute.Login,
        NavigationRoute.Register,
        NavigationRoute.ForgotPassword
    )

    /**
     * Main app destinations (require authentication)
     */
    val mainDestinations = listOf(
        NavigationRoute.Home,
        NavigationRoute.NoticeGraph,
        NavigationRoute.Academics,
        NavigationRoute.Profile
    )

    /**
     * Attendance feature destinations
     */
    val attendanceDestinations = listOf(
        NavigationRoute.NoticeGraph,
        NavigationRoute.AttendanceHistory,
        NavigationRoute.AttendanceDetails,
        NavigationRoute.MarkAttendance
    )

    /**
     * Academic feature destinations
     */
    val academicDestinations = listOf(
        NavigationRoute.Academics,
        NavigationRoute.Courses,
        NavigationRoute.CourseDetails,
        NavigationRoute.Assignments,
        NavigationRoute.AssignmentDetails,
        NavigationRoute.Grades,
        NavigationRoute.Schedule
    )

    /**
     * Profile feature destinations
     */
    val profileDestinations = listOf(
        NavigationRoute.Profile,
        NavigationRoute.EditProfile,
        NavigationRoute.Settings,
        NavigationRoute.Notifications,
        NavigationRoute.About
    )
}

/**
 * Extension function to check if a route is a top-level destination
 */
fun NavigationRoute.isTopLevelDestination(): Boolean {
    return AppDestinations.topLevelDestinations.any { it.route == this }
}

/**
 * Extension function to get the top-level destination for a route
 */
fun NavigationRoute.getTopLevelDestination(): AppTopLevelDestination? {
    return AppDestinations.topLevelDestinations.find { it.route == this }
}

/**
 * Extension function to check if a route requires authentication
 */
fun NavigationRoute.requiresAuthentication(): Boolean {
    return this !in AppDestinations.authDestinations
}

/**
 * Extension function to get the parent route for nested destinations
 */
fun NavigationRoute.getParentRoute(): NavigationRoute? {
    return when (this) {
        // Attendance nested routes
        NavigationRoute.AttendanceHistory,
        NavigationRoute.AttendanceDetails,
        NavigationRoute.MarkAttendance -> NavigationRoute.NoticeGraph

        // Academic nested routes
        NavigationRoute.Courses,
        NavigationRoute.CourseDetails,
        NavigationRoute.Assignments,
        NavigationRoute.AssignmentDetails,
        NavigationRoute.Grades,
        NavigationRoute.Schedule -> NavigationRoute.Academics

        // Profile nested routes
        NavigationRoute.EditProfile,
        NavigationRoute.Settings,
        NavigationRoute.Notifications,
        NavigationRoute.About -> NavigationRoute.Profile

        else -> null
    }
}

/**
 * Extension function to check if route should show bottom navigation
 */
fun NavigationRoute.shouldShowBottomNavigation(): Boolean {
    return this.isTopLevelDestination() || this.getParentRoute()?.isTopLevelDestination() == true
}

/**
 * Extension function to get the appropriate app bar title for a route
 */
fun NavigationRoute.getAppBarTitle(): String {
    return when (this) {
        NavigationRoute.Home -> "Dashboard"
        NavigationRoute.NoticeGraph -> "Attendance"
        NavigationRoute.AttendanceHistory -> "Attendance History"
        NavigationRoute.AttendanceDetails -> "Attendance Details"
        NavigationRoute.MarkAttendance -> "Mark Attendance"
        NavigationRoute.Academics -> "Academics"
        NavigationRoute.Courses -> "Courses"
        NavigationRoute.CourseDetails -> "Course Details"
        NavigationRoute.Assignments -> "Assignments"
        NavigationRoute.AssignmentDetails -> "Assignment Details"
        NavigationRoute.Grades -> "Grades"
        NavigationRoute.Schedule -> "Schedule"
        NavigationRoute.Profile -> "Profile"
        NavigationRoute.EditProfile -> "Edit Profile"
        NavigationRoute.Settings -> "Settings"
        NavigationRoute.Notifications -> "Notifications"
        NavigationRoute.About -> "About"
        NavigationRoute.Login -> "Login"
        NavigationRoute.Register -> "Register"
        NavigationRoute.ForgotPassword -> "Reset Password"
        else -> "University App"
    }
}