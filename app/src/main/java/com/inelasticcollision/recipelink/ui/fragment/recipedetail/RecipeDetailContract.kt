package com.inelasticcollision.recipelink.ui.fragment.recipedetail

import com.inelasticcollision.recipelink.data.model.Recipe

sealed class RecipeDetailState {
    data class Data(val recipe: Recipe) : RecipeDetailState()
    object Deleted : RecipeDetailState()
    object Error : RecipeDetailState()
}

sealed class RecipeDetailIntent {
    object ToggleFavorite : RecipeDetailIntent()
    object DeleteRecipe : RecipeDetailIntent()
}