/*
 * NewRecipePresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.editrecipe;

import android.util.Log;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.models.Result;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class EditRecipePresenter implements EditRecipeContract.Presenter {

    private final EditRecipeContract.View mView;

    private final LocalDataProvider mLocalDataProvider;

    private final RemoteDataProvider mRemoteDataProvider;

    private final CompositeSubscription mSubscriptions;

    private EditRecipeSavedState mState;

    EditRecipePresenter(EditRecipeContract.View view, LocalDataProvider localDataProvider, RemoteDataProvider remoteDataProvider, EditRecipeSavedState state) {
        mView = view;
        mLocalDataProvider = localDataProvider;
        mRemoteDataProvider = remoteDataProvider;
        mSubscriptions = new CompositeSubscription();
        restoreState(view, state);
    }

    private void restoreState(EditRecipeContract.View view, EditRecipeSavedState state) {
        mState = state;

        if (state.needsRecipeQuery) {
            handleLoadRecipe();
            return;
        }

        if (state.needsImageExtracting) {
            handleExtractImages(state.url);
        }

        view.showMainImage(mState.selectedImage);

        mView.showFab(mState.favorite);

    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onUnsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void handleLoadRecipe() {

        mSubscriptions.clear();

        Subscription subscription = mLocalDataProvider.loadRecipe(mState.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Recipe>() {

                    @Override
                    public void onNext(Recipe recipe) {

                        handleExtractImages(recipe.getUrl());

                        String imageUrl = recipe.getImageUrl();

                        mState.dateAdded = recipe.getDateAdded();

                        mState.selectedImage = recipe.getImageUrl();

                        mState.favorite = recipe.isFavorite();

                        mView.showMainImage(imageUrl);

                        mView.showTitle(recipe.getTitle());

                        mView.showUrl(recipe.getUrl());

                        mView.showFab(recipe.isFavorite());

                        mView.showNotes(recipe.getNotes());

                        mView.showKeywords(recipe.getKeywords());

                        mState.needsRecipeQuery = false;

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("EditRecipePresenter", e.getMessage(), e);

                        mView.showErrorMessage(e.getMessage());

                    }

                    @Override
                    public void onCompleted() {

                    }

                });

        mSubscriptions.add(subscription);

    }

    @Override
    public void handleExtractImages(final String url) {

        if (!mState.needsImageExtracting) {
            return;
        }

        mView.showImageLoadingIndicator(true);

        Subscription subscription = mRemoteDataProvider.getRecipeInformation(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {

                    @Override
                    public void onNext(Result result) {

                        mState.images = result.getImages();

                        mState.needsImageExtracting = false;

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("EditRecipePresenter", e.getMessage(), e);

                        mView.showErrorMessage(e.getMessage());

                        mState.needsImageExtracting = false;

                    }

                    @Override
                    public void onCompleted() {

                    }

                });

        mSubscriptions.add(subscription);

    }

    @Override
    public void handleChangeImage() {

        if (mState.images == null || mState.images.isEmpty()) {
            mView.showErrorMessage();
            return;
        }

        mView.showImagePicker(mState.images);

    }

    @Override
    public void handleImagePickerClick(String image) {

        mView.showImageLoadingIndicator(true);

        mState.selectedImage = image;

        mView.showMainImage(image);

    }

    @Override
    public void handleFabClick() {

        mState.favorite = !mState.favorite;

        mView.showFab(mState.favorite);

    }

    @Override
    public void handleSaveRecipe(String title, String url, String notes, List<String> keywords) {

        if (title == null || title.isEmpty()) {
            return;
        }

        if (url == null || url.isEmpty()) {
            return;
        }

        if (notes == null) {
            notes = "";
        }

        if (keywords == null) {
            keywords = new ArrayList<>();
        }

        Recipe recipe = new Recipe(mState.id, mState.dateAdded, title, url, mState.selectedImage , mState.favorite ? 1 : 0, keywords, notes);

        Subscription subscription = mLocalDataProvider.saveRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {

                    @Override
                    public void onNext(Long aLong) {

                        if (aLong > 0) {
                            mView.finishView();
                            return;
                        }

                        mView.showErrorMessage();

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("NewRecipePresenter", e.getMessage(), e);

                        mView.showErrorMessage(e.getMessage());

                    }

                    @Override
                    public void onCompleted() {

                    }

                });

        mSubscriptions.add(subscription);

    }

    @Override
    public void handleCloseClick() { mView.showCloseDialog(); }

    @Override
    public void handleCloseView() { mView.finishView(); }

    @Override
    public EditRecipeSavedState handleSavedState() {
        return mState;
    }

}
