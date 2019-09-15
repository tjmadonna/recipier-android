package com.inelasticcollision.recipelink.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.helper.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.provider.RecipeLocalDataProvider;
import com.inelasticcollision.recipelink.data.remote.ExtractionRemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.ExtractionService;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.squareup.sqlbrite.BriteDatabase;

public class AppDependencyProvider {

    // Local

    private static BriteDatabase briteDatabase;

    private static LocalDataProvider localDataProvider;

    private static BriteDatabase provideBriteDatabase(@NonNull Context context) {
        if (briteDatabase == null) {
            briteDatabase = BriteDatabaseHelper.getInstance(context.getApplicationContext());
        }
        return briteDatabase;
    }

    public static LocalDataProvider provideLocalDataProvider(@NonNull Context context) {
        if (localDataProvider == null) {
            localDataProvider = new RecipeLocalDataProvider(provideBriteDatabase(context));
        }
        return localDataProvider;
    }

    // Remote

    private static ExtractionService extractionService;

    private static RemoteDataProvider remoteDataProvider;

    private static ExtractionService provideExtractionService() {
        if (extractionService == null) {
            extractionService = ExtractionRemoteDataProvider.buildExtractionService();
        }
        return extractionService;
    }

    public static RemoteDataProvider provideRemoteDataProvider() {
        if (remoteDataProvider == null) {
            remoteDataProvider = new ExtractionRemoteDataProvider(provideExtractionService());
        }
        return remoteDataProvider;
    }

}
