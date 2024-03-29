package com.inelasticcollision.recipelink.data.usecase

import com.inelasticcollision.recipelink.data.cache.dao.RecipeDao
import com.inelasticcollision.recipelink.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class UpdateRecipe @Inject constructor(
    private val recipeDao: RecipeDao,
    dispatchers: UseCaseDispatchers
) : FlowUseCase<Unit, Recipe>(dispatchers) {

    override fun buildUseCaseFlow(params: Recipe?): Flow<Unit> {
        if (params == null) {
            throw IllegalArgumentException("Recipe cannot be null when attempting to update")
        }
        return flow {
            recipeDao.updateRecipe(params)
            emit(Unit)
        }.take(1)
    }
}