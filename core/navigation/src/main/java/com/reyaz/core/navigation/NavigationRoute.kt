package com.reyaz.core.navigation

/**
 * Sealed class defining all navigation routes in the application.
 * This centralizes route definitions to prevent duplication and ensure type safety.
 */

sealed class NavigationRoute(val route: String) {

    // Authentication Routes
    data object Login : NavigationRoute("login")
    data object Register : NavigationRoute("register")
    data object ForgotPassword : NavigationRoute("forgot_password")

    // Main Bottom Navigation Routes
    data object Home : NavigationRoute("home")
    data object Attendance : NavigationRoute("attendance")
    data object Academics : NavigationRoute("academics")
    data object Profile : NavigationRoute("profile")

    // Attendance Feature Routes
    data object AttendanceHistory : NavigationRoute("attendance/history")
    data object AttendanceDetails : NavigationRoute("attendance/details/{attendanceId}") {
        fun createRoute(attendanceId: String) = "attendance/details/$attendanceId"
    }
    data object MarkAttendance : NavigationRoute("attendance/mark")

    // Academic Feature Routes
    data object Courses : NavigationRoute("academics/courses")
    data object CourseDetails : NavigationRoute("academics/courses/{courseId}") {
        fun createRoute(courseId: String) = "academics/courses/$courseId"
    }
    data object Assignments : NavigationRoute("academics/assignments")
    data object AssignmentDetails : NavigationRoute("academics/assignments/{assignmentId}") {
        fun createRoute(assignmentId: String) = "academics/assignments/$assignmentId"
    }
    data object Grades : NavigationRoute("academics/grades")
    data object Schedule : NavigationRoute("academics/schedule")

    // Profile Feature Routes
    data object EditProfile : NavigationRoute("profile/edit")
    data object Settings : NavigationRoute("profile/settings")
    data object Notifications : NavigationRoute("profile/notifications")
    data object About : NavigationRoute("profile/about")
    data object Portal : NavigationRoute("portal")


    // Nested Graph Routes
    data object AuthGraph : NavigationRoute("auth_graph")
    data object MainGraph : NavigationRoute("main_graph")
    data object AttendanceGraph : NavigationRoute("attendance_graph")
    data object AcademicGraph : NavigationRoute("academic_graph")
    data object ProfileGraph : NavigationRoute("profile_graph")

    companion object {
        /**
         * Returns all main bottom navigation routes
         */
        val bottomNavigationRoutes = listOf(
            Home,
            Attendance,
            Academics,
            Profile
        )

        /**
         * Returns all authentication routes
         */
        val authRoutes = listOf(
            Login,
            Register,
            ForgotPassword
        )
    }
}

/**
 * Extension function to get route arguments
 */
fun NavigationRoute.getArguments(): List<String> {
    return when (this) {
        is NavigationRoute.AttendanceDetails -> listOf("attendanceId")
        is NavigationRoute.CourseDetails -> listOf("courseId")
        is NavigationRoute.AssignmentDetails -> listOf("assignmentId")
        else -> emptyList()
    }
}