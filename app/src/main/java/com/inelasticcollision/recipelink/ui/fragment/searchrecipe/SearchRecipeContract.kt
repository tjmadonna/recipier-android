package com.inelasticcollision.recipelink.ui.fragment.searchrecipe

import com.inelasticcollision.recipelink.data.model.Recipe

sealed class SearchRecipeState {
    data class Data(val recipes: List<Recipe>) : SearchRecipeState()
    object NoData : SearchRecipeState()
    object Error : SearchRecipeState()
}

sealed class SearchRecipeIntent {
    data class Search(val text: String?) : SearchRecipeIntent()
}