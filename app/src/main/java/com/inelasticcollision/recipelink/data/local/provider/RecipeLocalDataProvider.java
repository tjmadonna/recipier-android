/*
 * LocalDataProvider.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;

import androidx.annotation.Nullable;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.table.RecipeTable;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.squareup.sqlbrite2.BriteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RecipeLocalDataProvider implements LocalDataProvider {

    private BriteDatabase mDatabase;

    public RecipeLocalDataProvider(BriteDatabase database) {
        mDatabase = database;
    }

    // Recipe queries

    @Override
    public Observable<List<Recipe>> getAllRecipes() {
        return mDatabase
                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getAllRecipesQuery())
                .mapToOneOrDefault(RecipeTable.RECIPE_MAPPER, new ArrayList<Recipe>(0));

    }

    @Override
    public Observable<List<Recipe>> getFavoriteRecipes() {
        return mDatabase
                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getFavoriteRecipesQuery())
                .mapToOneOrDefault(RecipeTable.RECIPE_MAPPER, new ArrayList<Recipe>(0));
    }

    @Override
    public Observable<List<Recipe>> searchRecipes(@Nullable String searchTerm) {

        if (searchTerm == null) {
            searchTerm = "";
        }

        searchTerm = searchTerm.replace(RecipeTable.KEYWORDS_DELIMITER, "");

        return mDatabase
                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getRecipesSearchQuery(searchTerm))
                .mapToOneOrDefault(RecipeTable.RECIPE_MAPPER, new ArrayList<Recipe>(0));

    }

    @Override
    public Single<Recipe> loadRecipe(int id) {
        return mDatabase
                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getRecipeIdQuery(id))
                .mapToOne(RecipeTable.SINGLE_RECIPE_MAPPER)
                .singleOrError();
    }

    // Recipe save

    @Override
    public Completable saveRecipe(final Recipe recipe) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {

                if (saveRecipeHelper(recipe) > 0) {
                    return Completable.complete();
                } else {
                    return Completable.error(new SQLiteException("Unable to save recipes"));
                }
            }
        });
    }

    private Long saveRecipeHelper(Recipe recipe) {

        ContentValues contentValues = RecipeTable.getRecipeAsContentValues(recipe);

        switch (recipe.getId()) {

            case Recipe.ID_NONE:

                return mDatabase.insert(RecipeTable.TABLE_NAME, contentValues);

            default:

                return (long) mDatabase.update(RecipeTable.TABLE_NAME, contentValues, RecipeTable.getRecipeWhereClause(recipe.getId()));

        }

    }

    public int bulkSaveRecipe(List<Recipe> recipes) {

        final BriteDatabase.Transaction transaction = mDatabase.newTransaction();

        int insertCount = 0;

        try {

            for (Recipe recipe : recipes) {

                insertCount += saveRecipeHelper(recipe);

            }

            transaction.markSuccessful();
        } finally {

            transaction.end();

        }

        return insertCount;
    }

    // Recipe delete

    @Override
    public Completable deleteRecipe(final int id) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
            if (mDatabase.delete(RecipeTable.TABLE_NAME, RecipeTable.getRecipeWhereClause(id)) > 0) {
                return Completable.complete();
            } else {
                return Completable.error(new SQLiteException("Unable to delete recipe"));
            }
            }
        });
    }

}
