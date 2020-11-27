package com.inelasticcollision.recipelink.ui.fragment.searchrecipe

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.data.usecase.SearchRecipes
import com.inelasticcollision.recipelink.data.usecase.UseCaseObserver

class SearchRecipeViewModel @ViewModelInject constructor(
    private val searchRecipes: SearchRecipes,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val SEARCH_TERM_SAVED_STATE = "searchTermSavedState"
    }

    private val _state = MutableLiveData<SearchRecipeState>()

    val state: LiveData<SearchRecipeState>
        get() = _state

    var searchTermSavedState: String?
        get() = savedStateHandle.get(SEARCH_TERM_SAVED_STATE)
        set(value) = savedStateHandle.set(SEARCH_TERM_SAVED_STATE, value)

    init {
        setSearchTerm(searchTermSavedState)
    }

    fun setIntent(intent: SearchRecipeIntent) {
        when (intent) {
            is SearchRecipeIntent.Search -> setSearchTerm(intent.text)
        }
    }

    private fun setSearchTerm(searchTerm: String?) {
        searchTermSavedState = searchTerm
        searchRecipes.cancel()
        searchRecipes.execute(SearchRecipesObserver(), searchTerm)
    }

    override fun onCleared() {
        // Clean up
        searchRecipes.dispose()
        super.onCleared()
    }

    // Use case observers

    private inner class SearchRecipesObserver : UseCaseObserver<List<Recipe>> {

        override fun onSuccess(value: List<Recipe>) {
            if (value.isNotEmpty()) {
                _state.value = SearchRecipeState.Data(value)
            } else {
                _state.value = SearchRecipeState.NoData
            }
        }

        override fun onError(error: Throwable) {
            _state.value = SearchRecipeState.Error
        }
    }
}