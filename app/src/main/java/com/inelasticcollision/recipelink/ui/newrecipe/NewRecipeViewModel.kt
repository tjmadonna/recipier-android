package com.inelasticcollision.recipelink.ui.newrecipe

import android.util.Log
import androidx.lifecycle.*
import com.inelasticcollision.recipelink.common.Result
import com.inelasticcollision.recipelink.data.local.RecipeLocalDataSource
import com.inelasticcollision.recipelink.data.models.Recipe
import com.inelasticcollision.recipelink.data.remote.RecipeRemoteDataSource
import com.inelasticcollision.recipelink.ui.newrecipe.state.NewRecipeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class RecipeLocalDataSourceImpl() : RecipeLocalDataSource {
    override fun getAllRecipes(): Flow<List<Recipe>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRecipesWithCollection(collection: String): Flow<List<Recipe>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRecipesBySearchTerm(searchTerm: String?): Flow<List<Recipe>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getRecipeById(id: String): Recipe {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveRecipe(recipe: Recipe): Result<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateRecipe(recipe: Recipe): Result<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteRecipe(id: String): Result<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class RecipeRemoteDataSourceImpl() : RecipeRemoteDataSource {
    override suspend fun getRecipe(url: String): Recipe {
        return Recipe(
                UUID.randomUUID().toString(),
                url,
                UUID.randomUUID().toString(),
                false,
                UUID.randomUUID().toString(),
                emptyList()
        )
    }
}

class NewRecipeViewModel(
        private val savedState: SavedStateHandle,
        private val url: String,
        private val recipeLocalDataSource: RecipeLocalDataSource,
        private val recipeRemoteDataSource: RecipeRemoteDataSource
) : ViewModel() {

    companion object {
        private const val LOADING_STATE = "loading_state"
        private const val TITLE_STATE = "title_state"
        private const val IMAGE_URL_STATE = "image_url_state"
        private const val FAVORITE_STATE = "favorite_state"
        private const val NOTES_STATE = "notes_state"
        private const val TAGS_STATE = "tags_state"
    }

    private val _state: MutableLiveData<NewRecipeState> = MutableLiveData()

    val state: LiveData<NewRecipeState>
        get() = _state

    init {
        when (savedState.get<Boolean>(LOADING_STATE)) {
            true -> getRemoteRecipeInfo()
            null -> getRemoteRecipeInfo()
            false -> {
                _state.value = getStateFromSavedStateHandle(false)
            }
        }
    }

    private fun getRemoteRecipeInfo() {
        _state.value = NewRecipeState.Loading
        savedState.set(LOADING_STATE, true)
        viewModelScope.launch {
            val recipe = recipeRemoteDataSource.getRecipe(url)
            savedState.set(LOADING_STATE, false)
            savedState.set(TITLE_STATE, recipe.title)
            savedState.set(IMAGE_URL_STATE, recipe.imageUrl)
            savedState.set(FAVORITE_STATE, recipe.isFavorite)
            savedState.set(NOTES_STATE, recipe.notes)
            savedState.set(TAGS_STATE, recipe.tags)
            _state.value = getStateFromSavedStateHandle(true)
        }
    }

    fun setState(state: NewRecipeState.SetRecipe) {
        if (state != _state.value) {
            savedState.set(TITLE_STATE, state.title)
            savedState.set(IMAGE_URL_STATE, state.imageUrl)
            savedState.set(FAVORITE_STATE, state.favorite)
            savedState.set(NOTES_STATE, state.notes)
            savedState.set(TAGS_STATE, state.tags)
            _state.value = getStateFromSavedStateHandle(false)
        }
    }

    private fun getStateFromSavedStateHandle(animate: Boolean = false): NewRecipeState.SetRecipe {
        return NewRecipeState.SetRecipe(
                savedState.get<String>(TITLE_STATE) ?: "",
                savedState.get<String>(IMAGE_URL_STATE) ?: "",
                savedState.get<Boolean>(FAVORITE_STATE) ?: false,
                savedState.get<String>(NOTES_STATE) ?: "",
                savedState.get<List<String>>(TAGS_STATE) ?: emptyList(),
                animate
        )
    }
}