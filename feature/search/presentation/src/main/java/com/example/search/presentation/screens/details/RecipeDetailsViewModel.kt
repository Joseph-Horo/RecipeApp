package com.example.search.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.NetworkResult
import com.example.common.utils.UiText
import com.example.search.domain.model.Recipe
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.use_cases.DeleteRecipe
import com.example.search.domain.use_cases.GetRecipeDetails
import com.example.search.domain.use_cases.InsertRecipe
import com.example.search.presentation.screens.recipe_list.RecipeList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetails: GetRecipeDetails,
    private val deleteRecipe: DeleteRecipe,
    private val insertRecipe: InsertRecipe
    ): ViewModel() {

    private val _uiState = MutableStateFlow(com.example.search.presentation.screens.details.RecipeDetails.UiState())
    val uiState: StateFlow<com.example.search.presentation.screens.details.RecipeDetails.UiState> get() = _uiState.asStateFlow()
    private val _navigation = Channel<com.example.search.presentation.screens.details.RecipeDetails.Navigation> ()
    val navigation: Flow<com.example.search.presentation.screens.details.RecipeDetails.Navigation> get() = _navigation.receiveAsFlow()

    fun onEvent(event: com.example.search.presentation.screens.details.RecipeDetails.Event){
        when(event){
            is com.example.search.presentation.screens.details.RecipeDetails.Event.FetchRecipeDetails -> recipeDetails(event.id)
            com.example.search.presentation.screens.details.RecipeDetails.Event.GoToRecipeListScreen -> viewModelScope.launch {
                _navigation.send(com.example.search.presentation.screens.details.RecipeDetails.Navigation.GoToRecipeListScreen)
            }

            is com.example.search.presentation.screens.details.RecipeDetails.Event.DeleteRecipe -> {
                deleteRecipe.invoke(event.recipeDetails.toRecipe())
                    .launchIn(viewModelScope)
            }
            is com.example.search.presentation.screens.details.RecipeDetails.Event.InsertRecipe -> {
                insertRecipe.invoke(event.recipeDetails.toRecipe())
                    .launchIn(viewModelScope)
            }

            is com.example.search.presentation.screens.details.RecipeDetails.Event.GoToMediaPlayer -> {
                viewModelScope.launch {
                    _navigation.send(
                        com.example.search.presentation.screens.details.RecipeDetails.Navigation.GoToMediaPlayer(
                            event.youtubeUrl))
                }
            }
        }
    }

    private fun recipeDetails(id:String) = getRecipeDetails.invoke(id)
        .onEach { result ->
            when(result){
                is NetworkResult.Error -> {
                    _uiState.update { com.example.search.presentation.screens.details.RecipeDetails.UiState(
                        error = UiText.RemoteString(result.message.toString())) }
                }
                is NetworkResult.Loading ->{
                    _uiState.update { com.example.search.presentation.screens.details.RecipeDetails.UiState(
                        isLoading = true) }

                }
                is NetworkResult.Success ->{
                    _uiState.update { com.example.search.presentation.screens.details.RecipeDetails.UiState(
                        data = result.data)
                    }

                }
            }
        }.launchIn(viewModelScope)
}
fun RecipeDetails.toRecipe():Recipe{
    return Recipe(
        idMeal,
        strArea,
        strMeal,
        strMealThumb,
        strCategory,
        strTags,
        strYoutube,
        strInstructions
    )
}

object RecipeDetails{
    data class UiState(
        val isLoading: Boolean = false,
        val error: UiText = UiText.Idle,
        val data: RecipeDetails? = null
    )

    sealed interface Navigation{
data object GoToRecipeListScreen: Navigation
        data class GoToMediaPlayer(val youtubeUrl: String): Navigation
    }

    sealed interface Event{
data class FetchRecipeDetails(val id: String): Event

        data class InsertRecipe(val recipeDetails: RecipeDetails): Event
        data class DeleteRecipe(val recipeDetails: RecipeDetails): Event
        data object GoToRecipeListScreen: Event
        data class GoToMediaPlayer(val youtubeUrl: String): Event
    }
}