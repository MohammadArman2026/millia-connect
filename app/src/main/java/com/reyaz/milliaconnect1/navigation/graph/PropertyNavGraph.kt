package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_detail_screen.PropertyDetailScreen
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListScreen
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListViewModel
import com.reyaz.feature.rent.presentation.property_post_screen.CreatePostScreen
import com.reyaz.feature.rent.presentation.property_post_screen.CreatePostViewModel
import constants.NavigationRoute
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.propertyNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    showSearchComponents: Boolean,
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
//            onPostClick ={
//                //on clicking it will take to the post screen where u are allowed to post
//                navController.navigate("route i dont know because my mind is fucked in understanding naviagation")
//            },
            onDetailClick={it->
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("property",it)
                navController.navigate( NavigationRoute.PropertyDetails("viewmodel.id  --> uistate se bhe le skte ho ya argument se").route)
            },

            onAddClick={
                navController.navigate(NavigationRoute.CreatePost.route)
            },
            showSearchComponents = showSearchComponents
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
    composable (
        route = NavigationRoute.CreatePost.route
    ){
        val viewModel: CreatePostViewModel = koinViewModel()
        CreatePostScreen(
            onPostClick = {context, launcher->
                viewModel.checkSignInAndPost(context, launcher)
            },
            viewModel = viewModel,
            navigateToPostScreen = {
                navController.popBackStack() //todo: uncomment and delete below line
//                navController.navigate(NavigationRoute.PropertyFeed.route)
            },
            createPostUiState = viewModel.createPostUiState.collectAsStateWithLifecycle().value
        )
    }

        composable(
            route = NavigationRoute.PropertyDetails("viewmodel.id  --> uistate se bhe le skte ho ya argument se").route,
        ){
            backStackEntry->
            val property = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Property>("property")
            /// detail screeen ka composle rhega
            //backstack entry m store krke object ko pass kr denge but for now lets hardcode it
            PropertyDetailScreen(property = property?:Property())
        }
}