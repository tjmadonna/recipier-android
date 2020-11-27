package com.inelasticcollision.recipelink.data.usecase

import com.inelasticcollision.recipelink.data.model.RecipeInfo
import com.inelasticcollision.recipelink.data.network.RecipeNetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetRecipeInfo @Inject constructor(
        private val networkService: RecipeNetworkService,
        dispatchers: UseCaseDispatchers
) : FlowUseCase<RecipeInfo, String>(dispatchers) {

    override fun buildUseCaseFlow(params: String?): Flow<RecipeInfo> {
        if (params == null) {
            throw IllegalArgumentException("Url cannot be null when attempting to get recipe info")
        }
        return flow {
            val recipeInfo = networkService.getRecipeInfo(params)
            emit(recipeInfo)
        }.take(1)
    }
}