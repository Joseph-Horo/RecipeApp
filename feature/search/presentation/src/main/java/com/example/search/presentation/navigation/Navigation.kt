package com.example.search.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.common.navigation.FeatureAPi
import com.example.common.navigation.NavigationRoute
import com.example.common.navigation.NavigationSubGraphRoute
import com.example.search.presentation.screens.details.RecipeDetails
import com.example.search.presentation.screens.details.RecipeDetailsScreen
import com.example.search.presentation.screens.details.RecipeDetailsViewModel
import com.example.search.presentation.screens.favorite.FavoriteScreen
import com.example.search.presentation.screens.favorite.FavoriteViewModel
import com.example.search.presentation.screens.recipe_list.RecipeList
import com.example.search.presentation.screens.recipe_list.RecipeListScreen
import com.example.search.presentation.screens.recipe_list.RecipeListViewModel

interface SearchFeatureApi: FeatureAPi

class SearchFeatureApiImpl: SearchFeatureApi{
    override fun registerGraph(
        navGraphBuilder: androidx.navigation.NavGraphBuilder,
        navHostController: androidx.navigation.NavHostController
    ) {
navGraphBuilder.navigation(
    route = NavigationSubGraphRoute.Search.route,
    startDestination = NavigationRoute.RecipeList.route
){
composable(route = NavigationRoute.RecipeList.route) {
val viewModel = hiltViewModel<RecipeListViewModel>()
    RecipeListScreen(viewModel = viewModel, navHostController = navHostController){mealId->
viewModel.onEvent(RecipeList.Event.GoToRecipeDetails(mealId))
    }
}
    composable(route = NavigationRoute.RecipeDetails.route) {
        val viewModel = hiltViewModel<RecipeDetailsViewModel>()
        val mealId = it.arguments?.getString("id")
        LaunchedEffect(key1 = mealId){
            mealId?.let {
                viewModel.onEvent(RecipeDetails.Event.FetchRecipeDetails(it))
            }
        }

RecipeDetailsScreen(
    viewModel = viewModel,
    onNavigationClick = {
                        viewModel.onEvent(RecipeDetails.Event.GoToRecipeListScreen)
    },
    onFavoriteClick = {
                      viewModel.onEvent(RecipeDetails.Event.InsertRecipe(it))
    },
    onDelete = {
               viewModel.onEvent(RecipeDetails.Event.DeleteRecipe(it))
    }, navHostController = navHostController
)
    }
    composable(NavigationRoute.FavoriteScreen.route){
        val viewModel = hiltViewModel<FavoriteViewModel>()
        FavoriteScreen(
            navHostController = navHostController,
            viewModel = viewModel,
            onClick = { mealId ->
                viewModel.onEvent(FavoriteScreen.Event.GoToDetails(mealId))
            }
        )
    }
}

    }
}