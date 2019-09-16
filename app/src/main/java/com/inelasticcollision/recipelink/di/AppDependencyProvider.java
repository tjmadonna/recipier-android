package com.inelasticcollision.recipelink.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.helper.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.provider.RecipeLocalDataProvider;
import com.inelasticcollision.recipelink.data.remote.ExtractionRemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.ExtractionServiceHelper;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;

public class AppDependencyProvider {

    // Local

    private static LocalDataProvider localDataProvider;

    public static LocalDataProvider provideLocalDataProvider(@NonNull Context context) {
        if (localDataProvider == null) {
            localDataProvider = new RecipeLocalDataProvider(
                    BriteDatabaseHelper.getInstance(context.getApplicationContext())
            );
        }
        return localDataProvider;
    }

    // Remote

    private static RemoteDataProvider remoteDataProvider;

    public static RemoteDataProvider provideRemoteDataProvider() {
        if (remoteDataProvider == null) {
            remoteDataProvider = new ExtractionRemoteDataProvider(
                    ExtractionServiceHelper.getInstance()
            );
        }
        return remoteDataProvider;
    }

}
