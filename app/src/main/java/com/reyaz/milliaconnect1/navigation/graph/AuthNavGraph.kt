package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gargoylesoftware.htmlunit.javascript.host.Screen
import com.reyaz.core.navigation.NavigationRoute
import com.reyaz.core.navigation.composableRoute

/**
 * Authentication navigation graph
 * Contains login, register, and forgot password screens
 */
internal fun androidx.navigation.NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    composable(NavigationRoute.Login.route) {
        Column() {
            Text("Login Screen")

        }
    }

   /* composableRoute(NavigationRoute.Register) {
        *//*RegisterScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onRegisterSuccess = {
                navController.navigate(NavigationRoute.MainGraph.route) {
                    popUpTo(NavigationRoute.AuthGraph.route) { inclusive = true }
                }
            }
        )*//*
    }

    composableRoute(NavigationRoute.ForgotPassword) {
        // TODO: Replace with actual ForgotPassword screen composable
        *//*ForgotPasswordScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onPasswordResetSent = {
                navController.popBackStack()
            }
        )*//*
    }*/
}