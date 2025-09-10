package constants

/**
 * Sealed class defining all navigation routes in the application.
 * This centralizes route definitions to prevent duplication and ensure type safety.
 */

sealed class NavigationRoute(val route: String) {

    // Nested Graph Routes
    data object AttendanceGraph : NavigationRoute("attendance_graph")
    data object ResultGraph : NavigationRoute("result_graph")
    data object Notice : NavigationRoute("notice_graph")
    data object PropertyGraph : NavigationRoute("property_graph")


    // Attendance Feature Routes
    data object Schedule : NavigationRoute("attendance/schedule")
    data object AttendanceHistory : NavigationRoute("attendance/history")

    // miscellaneous
    data object PdfViewer : NavigationRoute("pdf_viewer?path={path}") {
        fun createRoute(path: String) = "pdf_viewer?path=$path"
    }

    data object Result : NavigationRoute("result"){
        fun getDeepLink() = "$DEEPLINK_BASE/$route"
    }
    data object Portal : NavigationRoute("portal"){
        fun getDeepLink() = "$DEEPLINK_BASE/$route"
    }


//    data object AttendanceDetails : NavigationRoute("attendance/details/{attendanceId}") {
//        fun createRoute(attendanceId: String) = "attendance/details/$attendanceId"
//    }
//    data object MarkAttendance : NavigationRoute("attendance/mark")


    // Authentication Routes
//    data object Login : NavigationRoute("login")
//    data object Register : NavigationRoute("register")
//    data object ForgotPassword : NavigationRoute("forgot_password")


    // Academic Feature Routes
//    data object Courses : NavigationRoute("academics/courses")
//    data object CourseDetails : NavigationRoute("academics/courses/{courseId}") {
//        fun createRoute(courseId: String) = "academics/courses/$courseId"
//    }
//    data object Assignments : NavigationRoute("academics/assignments")
//    data object AssignmentDetails : NavigationRoute("academics/assignments/{assignmentId}") {
//        fun createRoute(assignmentId: String) = "academics/assignments/$assignmentId"
//    }
//    data object Grades : NavigationRoute("academics/grades")

    // Profile Feature Routes
//    data object EditProfile : NavigationRoute("profile/edit")
//    data object Settings : NavigationRoute("profile/settings")
//    data object Notifications : NavigationRoute("profile/notifications")
//    data object About : NavigationRoute("profile/about")

    // Property Feature Routes
    data object PropertyFeed : NavigationRoute("property/feed")
    data class PropertyDetails(val propertyId: String) : NavigationRoute("property/details"){
        fun createRoute(propertyId: String) = "property/details/$propertyId"
    }
    companion object {

        const val DEEPLINK_BASE = "reyaz://milliaconnect"
        /**
         * Returns all main bottom navigation routes
         */
//        val bottomNavigationRoutes = listOf(
//            Home,
//            NoticeGraph,
//            Academics,
//            Profile
//        )

        /**
         * Returns all authentication routes
         */
//        val authRoutes = listOf(
//            Login,
//            Register,
//            ForgotPassword
//        )
    }
}

/**
 * Extension function to get route arguments
 */
//fun NavigationRoute.getArguments(): List<String> {
//    return when (this) {
//        is NavigationRoute.AttendanceDetails -> listOf("attendanceId")
//        is NavigationRoute.CourseDetails -> listOf("courseId")
//        is NavigationRoute.AssignmentDetails -> listOf("assignmentId")
//        else -> emptyList()
//    }
//}