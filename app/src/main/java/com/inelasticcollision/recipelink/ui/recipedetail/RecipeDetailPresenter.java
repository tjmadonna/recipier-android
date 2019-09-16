/*
 * RecipeDetailPresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/26/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipedetail;

import android.util.Log;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private final RecipeDetailContract.View mView;

    private final LocalDataProvider mDataProvider;

    private final int mRecipeId;

    private final CompositeDisposable mCompositeDisposable;

    public RecipeDetailPresenter(RecipeDetailContract.View view, LocalDataProvider dataProvider, int recipeId) {
        mView = view;
        mDataProvider = dataProvider;
        mRecipeId = recipeId;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void handleLoadRecipes() {

        mCompositeDisposable.clear();

        Disposable disposable = mDataProvider.loadRecipe(mRecipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Recipe>() {
                    @Override
                    public void onNext(Recipe recipe) {
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
                    public void onComplete() {

                    }
                });

        mCompositeDisposable.add(disposable);
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
        mCompositeDisposable.clear();

        Disposable disposable = mDataProvider.deleteRecipe(mRecipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
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
                    public void onComplete() {

                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onSubscribe() { }

    @Override
    public void onUnsubscribe() { mCompositeDisposable.clear(); }

}
