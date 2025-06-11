package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.composableRoute

/**
 * Academic feature navigation graph
 */
internal fun androidx.navigation.NavGraphBuilder.academicNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composableRoute(NavigationRoute.Academics) {
        // TODO: Replace with actual Academics screen composable
        Text("Academics Screen")
        /*AcademicsScreen(
            onNavigateToCourses = {
                navController.navigate(NavigationRoute.Courses.route)
            },
            onNavigateToAssignments = {
                navController.navigate(NavigationRoute.Assignments.route)
            },
            onNavigateToGrades = {
                navController.navigate(NavigationRoute.Grades.route)
            },
            onNavigateToSchedule = {
                navController.navigate(NavigationRoute.Schedule.route)
            }
        )*/
    }

    composableRoute(NavigationRoute.Courses) {
        // TODO: Replace with actual Courses screen composable
       /* CoursesScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToCourseDetails = { courseId ->
                navController.navigate(NavigationRoute.CourseDetails.createRoute(courseId))
            }
        )*/
    }

    composable(NavigationRoute.CourseDetails.route) { backStackEntry ->
        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
        // TODO: Replace with actual CourseDetails screen composable
       /* CourseDetailsScreen(
            courseId = courseId,
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }

    composableRoute(NavigationRoute.Assignments) {
        // TODO: Replace with actual Assignments screen composable
       /* AssignmentsScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToAssignmentDetails = { assignmentId ->
                navController.navigate(NavigationRoute.AssignmentDetails.createRoute(assignmentId))
            }
        )*/
    }

    composable(NavigationRoute.AssignmentDetails.route) { backStackEntry ->
        val assignmentId = backStackEntry.arguments?.getString("assignmentId") ?: ""
        // TODO: Replace with actual AssignmentDetails screen composable
        /*AssignmentDetailsScreen(
            assignmentId = assignmentId,
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }

    composableRoute(NavigationRoute.Grades) {
        // TODO: Replace with actual Grades screen composable
       /* GradesScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }

    composableRoute(NavigationRoute.Schedule) {
        // TODO: Replace with actual Schedule screen composable
       /* ScheduleScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )*/
    }
}