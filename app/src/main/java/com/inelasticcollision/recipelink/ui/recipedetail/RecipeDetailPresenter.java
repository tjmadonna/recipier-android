/*
 * RecipeDetailPresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/26/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipedetail;

import androidx.annotation.Nullable;
import android.util.Log;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;


import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private final RecipeDetailContract.View mView;

    private final LocalDataProvider mDataProvider;

    private final int mRecipeId;

    private final CompositeSubscription mSubscriptions;

    public RecipeDetailPresenter(RecipeDetailContract.View view, LocalDataProvider dataProvider, int recipeId) {
        mView = view;
        mDataProvider = dataProvider;
        mRecipeId = recipeId;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void handleLoadRecipes() {

        mSubscriptions.clear();

        Subscription subscription = mDataProvider.loadRecipe(mRecipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Recipe>() {

                    @Override
                    public void onNext(@Nullable Recipe recipe) {

                        if (recipe == null) {
                            mView.showErrorMessage();
                            return;
                        }

                        mView.showImageLoadingIndicator(true);

                        mView.showMainImage(recipe.getImageUrl());

                        mView.showFavoriteIcon(recipe.isFavorite());

                        mView.showTitle(recipe.getTitle());

                        mView.showUrl(recipe.getUrl());

                        if (recipe.getNotes().isEmpty()) {
                            mView.showEmptyNotes();
                        } else {
                            mView.showNotes(recipe.getNotes());
                        }

                        if (recipe.getKeywords().isEmpty()) {
                            mView.showEmptyKeywords();
                        } else {
                            mView.showKeywords(recipe.getKeywords());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("RecipeDetailPresenter", e.getMessage(), e);

                        mView.showErrorMessage();

                    }

                    @Override
                    public void onCompleted() {

                    }

                });

        mSubscriptions.add(subscription);

    }

    @Override
    public void handleEditRecipeClick() {
        mView.showEditRecipeActivity(mRecipeId);
    }

    @Override
    public void handleDeleteRecipeClick() {
        mView.showDeleteRecipeDialog();
    }

    @Override
    public void handleDeleteRecipe() {
        mSubscriptions.clear();

        Subscription subscription = mDataProvider.deleteRecipe(mRecipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.finishView();
                        } else {
                            Log.e("RecipeDetailFragment", "Recipe couldn't be deleted");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RecipeDetailFragment", e.getMessage(), e);
                    }

                    @Override
                    public void onCompleted() {

                    }

                });

        mSubscriptions.add(subscription);

    }

    @Override
    public void onSubscribe() { }

    @Override
    public void onUnsubscribe() { mSubscriptions.clear(); }

}
