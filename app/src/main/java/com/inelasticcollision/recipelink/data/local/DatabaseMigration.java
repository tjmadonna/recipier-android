/*
 * DatabaseMigration.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/17/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.inelasticcollision.recipelink.data.models.Collection;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.models.Relationship;
import com.inelasticcollision.recipelink.data.preference.PreferenceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class DatabaseMigration {

    private final PreferenceProvider mPreferenceProvider;

    private final RecipeLocalDataProvider mRecipeLocalDataProvider;

    private final CollectionLocalDataProvider mCollectionLocalDataProvider;

    private final RelationshipLocalDataProvider mRelationshipLocalDataProvider;

    public DatabaseMigration(RecipeLocalDataProvider recipeLocalDataProvider, CollectionLocalDataProvider collectionLocalDataProvider, RelationshipLocalDataProvider relationshipLocalDataProvider, PreferenceProvider preferenceProvider) {
        mRecipeLocalDataProvider = recipeLocalDataProvider;
        mCollectionLocalDataProvider = collectionLocalDataProvider;
        mRelationshipLocalDataProvider = relationshipLocalDataProvider;
        mPreferenceProvider = preferenceProvider;
    }

    public Observable<Boolean> migrate() {

        return getRecipes();

    }

    private Observable<Boolean> getRecipes() {

        return mRecipeLocalDataProvider.loadAllRecipes()
                .map(new Func1<List<Recipe>, HashMap<Integer, Recipe>>() {
                    @Override
                    public HashMap<Integer, Recipe> call(List<Recipe> recipes) {
                        HashMap<Integer, Recipe> map = new HashMap<>();

                        for (Recipe recipe : recipes) {
                            map.put(recipe.getId(), recipe);
                        }

                        return map;
                    }
                })
                .flatMap(new Func1<HashMap<Integer, Recipe>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(HashMap<Integer, Recipe> integerRecipeHashMap) {
                        return getCollections(integerRecipeHashMap);
                    }
                });

    }

    private Observable<Boolean> getCollections(final HashMap<Integer, Recipe> recipeMap) {

        return mCollectionLocalDataProvider.loadCollections()
                .map(new Func1<List<Collection>, HashMap<Integer, String>>() {
                    @Override
                    public HashMap<Integer, String> call(List<Collection> collections) {
                        HashMap<Integer, String> map = new HashMap<>();

                        for (Collection collection : collections) {
                            map.put(collection.getId(), collection.getTitle());
                        }

                        return map;
                    }
                })
                .flatMap(new Func1<HashMap<Integer, String>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(HashMap<Integer, String> integerCollectionHashMap) {
                        return getRelationships(recipeMap, integerCollectionHashMap);
                    }
                });

    }

    private Observable<Boolean> getRelationships(final HashMap<Integer, Recipe> recipeMap, final HashMap<Integer, String> collectionMap) {

        return mRelationshipLocalDataProvider.loadRelationships()
                .map(new Func1<List<Relationship>, List<Recipe>>() {
                    @Override
                    public List<Recipe> call(List<Relationship> relationships) {

                        for (Relationship relationship : relationships) {

                            int recipeId = relationship.getRecipeId();

                            int collectionId = relationship.getCollectionId();

                            Recipe recipe = recipeMap.get(recipeId);

                            String collectionTitle = collectionMap.get(collectionId);

                            if (recipe != null && collectionTitle != null) {

                                ArrayList<String> keywords = new ArrayList<>(recipe.getKeywords());

                                keywords.add(collectionTitle);

                                recipe.setKeywords(keywords);

                            }

                        }

                        return new ArrayList<>(recipeMap.values());

                    }
                })
                .map(new Func1<List<Recipe>, Integer>() {
                    @Override
                    public Integer call(List<Recipe> recipes) {
                        return mRecipeLocalDataProvider.bulkSaveRecipe(recipes);
                    }
                })
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {

                        if (integer == recipeMap.size()) {

                            mCollectionLocalDataProvider.deleteAllCollections();

                            mRelationshipLocalDataProvider.deleteAllRelationships();

                            mPreferenceProvider.setMigrateToVersion12Database();

                            return mPreferenceProvider.setMigrateToVersion12Database();

                        }

                        return false;

                    }
                });

    }

}
