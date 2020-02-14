package com.inelasticcollision.recipelink.ui.newrecipe.state

sealed class NewRecipeState {

    object Loading : NewRecipeState()

    data class SetRecipe(
            val title: String,
            val imageUrl: String,
            val favorite: Boolean,
            val notes: String,
            val tags: List<String>,
            val animate: Boolean
    ) : NewRecipeState()

    object Finish : NewRecipeState()
}

