/*
 * LocalDataProvider.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local.provider;

import android.content.ContentValues;

import androidx.annotation.Nullable;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.local.table.RecipeTable;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.squareup.sqlbrite2.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RecipeLocalDataProvider implements LocalDataProvider {

    private BriteDatabase mDatabase;

    public RecipeLocalDataProvider(BriteDatabase database) {
        mDatabase = database;
    }

    // Recipe queries

    @Override
    public Observable<List<Recipe>> loadAllRecipes() {
        return mDatabase
                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getAllRecipesQuery())
                .mapToOneOrDefault(RecipeTable.RECIPE_MAPPER, new ArrayList<Recipe>(0));

    }

    @Override
    public Observable<List<Recipe>> loadFavoriteRecipes() {
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
    public Observable<Recipe> loadRecipe(int id) {
        return mDatabase
                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getRecipeIdQuery(id))
                .mapToOne(RecipeTable.SINGLE_RECIPE_MAPPER);
    }

    // Recipe save

    @Override
    public Observable<Long> saveRecipe(Recipe recipe) {

        return Observable.just(saveRecipeHelper(recipe));

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
    public Observable<Boolean> deleteRecipe(final int id) {
        return Observable.just(mDatabase.delete(RecipeTable.TABLE_NAME, RecipeTable.getRecipeWhereClause(id)))
                .map(new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer) throws Exception {
                        return integer > 0;
                    }
                });
    }

}
