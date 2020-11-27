package com.inelasticcollision.recipelink.ui.fragment.addrecipe

data class AddRecipeViewData(
    val title: String?,
    val selectedImageUrl: String?,
    val imageUrls: List<String>?,
    val favorite: Boolean,
    val notes: String?,
    val tags: List<String>?
)

sealed class AddRecipeState {
    object Loading : AddRecipeState()
    data class Data(val viewData: AddRecipeViewData) : AddRecipeState()
    object Error : AddRecipeState()
    object Saved : AddRecipeState()
}

sealed class AddRecipeIntent {
    data class ChangeTitle(val title: String?) : AddRecipeIntent()
    data class ChangeNotes(val notes: String?) : AddRecipeIntent()
    data class ChangeTags(val tags: List<String>?) : AddRecipeIntent()
    data class ChangeImage(val imageUrl: String) : AddRecipeIntent()
    object ToggleFavorite : AddRecipeIntent()
    object SaveRecipe : AddRecipeIntent()
}