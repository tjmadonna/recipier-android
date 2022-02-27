package com.inelasticcollision.recipelink.data.usecase

import com.inelasticcollision.recipelink.data.cache.dao.RecipeDao
import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteRecipes @Inject constructor(
    private val recipeDao: RecipeDao,
    dispatchers: UseCaseDispatchers
) : FlowUseCase<List<Recipe>, Nothing?>(dispatchers) {

    override fun buildUseCaseFlow(params: Nothing?): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipes()
    }
}