package com.inelasticcollision.recipelink.di

import com.inelasticcollision.recipelink.data.usecase.UseCaseDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUseCaseDispatchers(): UseCaseDispatchers {
        return UseCaseDispatchers(
                executionDispatcher = Dispatchers.IO,
                postExecutionDispatcher = Dispatchers.Main
        )
    }
}