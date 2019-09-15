/*
 * DatabaseMigrationTest.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.preference.PreferenceManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.inelasticcollision.recipelink.data.local.helper.DatabaseHelper;
import com.inelasticcollision.recipelink.data.local.provider.RecipeLocalDataProvider;
import com.inelasticcollision.recipelink.data.local.table.legacy.CollectionTable;
import com.inelasticcollision.recipelink.data.local.table.RecipeTable;
import com.inelasticcollision.recipelink.data.local.table.legacy.RelationshipTable;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.preference.PreferenceProvider;
import com.inelasticcollision.recipelink.data.preference.SharedPreferenceProvider;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

@RunWith(AndroidJUnit4.class)
public class DatabaseMigrationTest {

    private DatabaseMigration mMigration;

    private BriteDatabase mBriteDatabase;

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getTargetContext();

        DatabaseHelper helper = new DatabaseHelper(context);

        mBriteDatabase = new SqlBrite.Builder()
                .build()
                .wrapDatabaseHelper(helper, Schedulers.immediate());

        mBriteDatabase.delete(RecipeTable.TABLE_NAME, null);
        mBriteDatabase.delete(CollectionTable.TABLE_NAME, null);
        mBriteDatabase.delete(RelationshipTable.TABLE_NAME, null);

        RecipeLocalDataProvider recipeDataProvider = new RecipeLocalDataProvider(mBriteDatabase);

        CollectionLocalDataProvider collectionProvider = new CollectionLocalDataProvider(mBriteDatabase);

        RelationshipLocalDataProvider relationshipProvider = new RelationshipLocalDataProvider(mBriteDatabase);

        PreferenceProvider preferenceProvider = new SharedPreferenceProvider(PreferenceManager.getDefaultSharedPreferences(context));

        mMigration = new DatabaseMigration(recipeDataProvider, collectionProvider, relationshipProvider, preferenceProvider);

        populationDatabase();

    }

    @After
    public void tearDown() {
        mBriteDatabase.delete(RecipeTable.TABLE_NAME, null);
        mBriteDatabase.delete(CollectionTable.TABLE_NAME, null);
        mBriteDatabase.delete(RelationshipTable.TABLE_NAME, null);
    }

    @Test
    public void migrationTest() {

        TestSubscriber<List<Recipe>> testSubscriber = new TestSubscriber<> ();

        mMigration
                .migrate()
                .flatMap(new Func1<Boolean, Observable<List<Recipe>>>() {
                    @Override
                    public Observable<List<Recipe>> call(Boolean aBoolean) {

                        Assert.assertTrue(aBoolean);

                        return mBriteDatabase
                                .createQuery(RecipeTable.TABLE_NAME, RecipeTable.getAllRecipesQuery())
                                .mapToOne(RecipeTable.RECIPE_MAPPER);
                    }
                })
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();

        List<Recipe> recipes = testSubscriber.getOnNextEvents().get(0);

        Assert.assertEquals(Arrays.asList("Collection 1", "Collection 2", "Collection 3"), recipes.get(0).getKeywords());

        Assert.assertEquals(Arrays.asList("Collection 2", "Collection 4"), recipes.get(1).getKeywords());

        Assert.assertEquals(Collections.emptyList(), recipes.get(2).getKeywords());

        Assert.assertEquals(Arrays.asList("Keywords", "Collection 1", "Collection 5", "Collection 6"), recipes.get(3).getKeywords());

    }

    private void populationDatabase() {

        Recipe recipe1 = new Recipe("Recipe 1", "Url 1", "Image url 1", 1, null, "Note 1");

        Recipe recipe2 = new Recipe("Recipe 2", "Url 2", "Image url 2", 1, null, "Note 2");

        Recipe recipe3 = new Recipe("Recipe 3", "Url 3", "Image url 3", 1, null, "Note 3");

        Recipe recipe4 = new Recipe("Recipe 4", "Url 4", "Image url 4", 1, Arrays.asList("Keywords"), "Note 4");

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2, recipe3, recipe4);

        ContentValues[] recipeValues = DatabaseTestHelper.getContentValuesFromRecipeList(recipes);

        long recipeId1 = mBriteDatabase.insert(RecipeTable.TABLE_NAME, recipeValues[0]);

        long recipeId2 = mBriteDatabase.insert(RecipeTable.TABLE_NAME, recipeValues[1]);

        long recipeId3 = mBriteDatabase.insert(RecipeTable.TABLE_NAME, recipeValues[2]);

        long recipeId4 = mBriteDatabase.insert(RecipeTable.TABLE_NAME, recipeValues[3]);



        Collection collection1 = new Collection("Collection 1");

        Collection collection2 = new Collection("Collection 2");

        Collection collection3 = new Collection("Collection 3");

        Collection collection4 = new Collection("Collection 4");

        Collection collection5 = new Collection("Collection 5");

        Collection collection6 = new Collection("Collection 6");

        List<Collection> collections = Arrays.asList(collection1, collection2, collection3, collection4, collection5, collection6);

        ContentValues[] collectionValues = DatabaseTestHelper.getContentValuesFromCollectionList(collections);

        long collectionId1 = mBriteDatabase.insert(CollectionTable.TABLE_NAME, collectionValues[0]);

        long collectionId2 = mBriteDatabase.insert(CollectionTable.TABLE_NAME, collectionValues[1]);

        long collectionId3 = mBriteDatabase.insert(CollectionTable.TABLE_NAME, collectionValues[2]);

        long collectionId4 = mBriteDatabase.insert(CollectionTable.TABLE_NAME, collectionValues[3]);

        long collectionId5 = mBriteDatabase.insert(CollectionTable.TABLE_NAME, collectionValues[4]);

        long collectionId6 = mBriteDatabase.insert(CollectionTable.TABLE_NAME, collectionValues[5]);



        Relationship relationship1 = new Relationship((int)recipeId1, (int)collectionId1);

        Relationship relationship2 = new Relationship((int)recipeId1, (int)collectionId2);

        Relationship relationship3 = new Relationship((int)recipeId1, (int)collectionId3);

        Relationship relationship4 = new Relationship((int)recipeId2, (int)collectionId2);

        Relationship relationship5 = new Relationship((int)recipeId2, (int)collectionId4);

        Relationship relationship6 = new Relationship((int)recipeId4, (int)collectionId1);

        Relationship relationship7 = new Relationship((int)recipeId4, (int)collectionId5);

        Relationship relationship8 = new Relationship((int)recipeId4, (int)collectionId6);



        List<Relationship> relationships = Arrays.asList(relationship1, relationship2, relationship3, relationship4, relationship5, relationship6, relationship7, relationship8);

        ContentValues[] relationshipValues = DatabaseTestHelper.getContentValuesFromRelationship(relationships);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[0]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[1]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[2]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[3]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[4]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[5]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[6]);

        mBriteDatabase.insert(RelationshipTable.TABLE_NAME, relationshipValues[7]);

    }


}
