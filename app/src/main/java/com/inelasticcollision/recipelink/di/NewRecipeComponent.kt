package com.inelasticcollision.recipelink.di

import androidx.lifecycle.ViewModelProvider
import com.inelasticcollision.recipelink.ui.newrecipe.NewRecipeActivity
import com.inelasticcollision.recipelink.ui.newrecipe.NewRecipeViewModel
import com.inelasticcollision.recipelink.ui.newrecipe.RecipeLocalDataSourceImpl
import com.inelasticcollision.recipelink.ui.newrecipe.RecipeRemoteDataSourceImpl
import com.inelasticcollision.recipelink.ui.newrecipe.factory.NewRecipeViewModelFactory

interface NewRecipeComponent {

    val viewModel: NewRecipeViewModel

}

class NewRecipeComponentImpl(
        private val activity: NewRecipeActivity,
        private val url: String
) : NewRecipeComponent {

    override val viewModel: NewRecipeViewModel by lazy {
        val factory = NewRecipeViewModelFactory(
                url,
                RecipeLocalDataSourceImpl(),
                RecipeRemoteDataSourceImpl(),
                activity
        )

        return@lazy ViewModelProvider(activity, factory).get(NewRecipeViewModel::class.java)
    }

}