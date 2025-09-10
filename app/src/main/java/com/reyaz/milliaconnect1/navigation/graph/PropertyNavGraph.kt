package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListScreen
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListViewModel
import constants.NavigationRoute
import com.reyaz.feature.result.presentation.ResultScreen
import com.reyaz.feature.result.presentation.ResultViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.propertyNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    composable(
        route = NavigationRoute.PropertyFeed.route,
        deepLinks = listOf(
            navDeepLink { uriPattern = NavigationRoute.Result.getDeepLink() }
        )
    ) {
        val viewModel: PropertyListViewModel = koinViewModel()

        PropertyListScreen(
            modifier = Modifier,
            viewModel = viewModel,
            // sare navigation wale lambda jisme navcontroller use hoga wo yhi se pass krna
            // aur jo function viewmodel me hai like button click pe execute krna h wo bhi yhi se pass krna
            /*
            openPdf = {
                navController.navigate(NavigationRoute.PdfViewer.createRoute(it))
            },
            onNavigateBack = {
                navController.popBackStack()
            },

            fo example:
            onClick = {username->
            viewmodel.onSigninClick(username)
            }
             */
        )
    }

        composable(
            route = NavigationRoute.PropertyDetails("viewmodel.id  --> uistate se bhe le skte ho ya argument se").route,
        ){
            /// detail screeen ka composle rhega
        }
}