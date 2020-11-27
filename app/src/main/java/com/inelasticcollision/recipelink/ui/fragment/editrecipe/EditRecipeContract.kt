package com.inelasticcollision.recipelink.ui.fragment.editrecipe

data class EditRecipeViewData(
    val title: String?,
    val selectedImageUrl: String?,
    val imageUrls: List<String>?,
    val favorite: Boolean,
    val notes: String?,
    val tags: List<String>?
)

sealed class EditRecipeState {
    data class Data(val viewData: EditRecipeViewData) : EditRecipeState()
    object Error : EditRecipeState()
    object Saved : EditRecipeState()
}

sealed class EditRecipeIntent {
    data class ChangeTitle(val title: String?) : EditRecipeIntent()
    data class ChangeNotes(val notes: String?) : EditRecipeIntent()
    data class ChangeTags(val tags: List<String>?) : EditRecipeIntent()
    data class ChangeImage(val imageUrl: String) : EditRecipeIntent()
    object ToggleFavorite : EditRecipeIntent()
    object SaveRecipe : EditRecipeIntent()
}