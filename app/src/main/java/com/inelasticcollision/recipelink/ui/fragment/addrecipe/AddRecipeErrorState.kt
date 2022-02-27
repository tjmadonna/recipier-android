package com.inelasticcollision.recipelink.ui.fragment.addrecipe

sealed class AddRecipeErrorState {
    object NoRecipeUrlProvided : AddRecipeErrorState()
    object ErrorFetchRecipeInfo : AddRecipeErrorState()
    object ErrorSavingRecipe : AddRecipeErrorState()
}