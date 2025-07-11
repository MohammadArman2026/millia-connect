package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import constants.NavigationRoute
import com.reyaz.feature.result.presentation.ResultScreen
import com.reyaz.feature.result.presentation.ResultViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.resultNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    composable(
        route = constants.NavigationRoute.Result.route,
        deepLinks = listOf(
            navDeepLink { uriPattern = constants.NavigationRoute.Result.getDeepLink() }
        )
    ) {
        val viewModel: ResultViewModel = koinViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        ResultScreen(
            modifier = Modifier,
            uiState = uiState.value,
            onEvent = viewModel::onEvent,
            openPdf = {
                navController.navigate(constants.NavigationRoute.PdfViewer.createRoute(it))
            },
            onNavigateBack = {
                navController.popBackStack()
            },
            snackbarHostState = snackbarHostState
        )
    }

}