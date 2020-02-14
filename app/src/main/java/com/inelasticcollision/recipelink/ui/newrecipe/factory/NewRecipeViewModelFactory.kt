package com.inelasticcollision.recipelink.ui.newrecipe.factory

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.inelasticcollision.recipelink.data.local.RecipeLocalDataSource
import com.inelasticcollision.recipelink.data.remote.RecipeRemoteDataSource
import com.inelasticcollision.recipelink.ui.newrecipe.NewRecipeViewModel

class NewRecipeViewModelFactory(
        private val url: String,
        private val localDataSource: RecipeLocalDataSource,
        private val remoteDataSource: RecipeRemoteDataSource,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return NewRecipeViewModel(handle, url,localDataSource, remoteDataSource) as T
    }

}