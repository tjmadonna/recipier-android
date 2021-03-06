package com.inelasticcollision.recipelink.di

import com.inelasticcollision.recipelink.BuildConfig
import com.inelasticcollision.recipelink.data.network.RecipeNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRecipeNetworkService(retrofit: Retrofit): RecipeNetworkService {
        return retrofit.create(RecipeNetworkService::class.java)
    }

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NETWORK_SERVICE_BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .addHeader("x-api-key", BuildConfig.NETWORK_SERVICE_API_KEY)
                    .build()

                chain.proceed(newRequest)
            }
            .build()
    }
}