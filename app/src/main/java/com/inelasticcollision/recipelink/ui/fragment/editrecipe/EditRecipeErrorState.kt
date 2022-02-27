package com.inelasticcollision.recipelink.ui.fragment.editrecipe

sealed class EditRecipeErrorState {
    object RecipeNotFound : EditRecipeErrorState()
    object ErrorGettingRecipe : EditRecipeErrorState()
    object ErrorFetchRecipeInfo : EditRecipeErrorState()
    object ErrorSavingRecipe : EditRecipeErrorState()
}