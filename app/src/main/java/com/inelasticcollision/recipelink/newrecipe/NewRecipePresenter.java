/*
 * NewRecipePresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.newrecipe;

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

class NewRecipePresenter implements NewRecipeContract.Presenter {

    private final NewRecipeContract.View mView;

    private final LocalDataProvider mLocalDataProvider;

    private final RemoteDataProvider mRemoteDataProvider;

    private final CompositeSubscription mSubscriptions;

    private NewRecipeSavedState mState;

    NewRecipePresenter(NewRecipeContract.View view, LocalDataProvider localDataProvider, RemoteDataProvider remoteDataProvider, NewRecipeSavedState state) {
        mView = view;
        mLocalDataProvider = localDataProvider;
        mRemoteDataProvider = remoteDataProvider;
        mSubscriptions = new CompositeSubscription();
        restoreState(view, state);
    }

    private void restoreState(NewRecipeContract.View view, NewRecipeSavedState state) {
        mState = state;

        if (state.needsExtracting) {
            handleExtractRecipe(state.title, state.url);
        } else {
            view.showMainImage(mState.selectedImage);
        }

    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onUnsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void handleExtractRecipe(final String title, final String url) {

        mState.needsExtracting = true;

        mView.showMainLoadingIndicator(true);

        Subscription subscription = mRemoteDataProvider.getRecipeInformation(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {

                    @Override
                    public void onNext(Result result) {

                        mState.images = result.getImages();

                        String imageUrl = result.getImages().get(0);

                        mState.selectedImage = imageUrl;

                        mView.showMainImage(imageUrl);

                        mView.showTitle(result.getTitle());

                        mView.showUrl(url);

                        mState.needsExtracting = false;

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("NewRecipePresenter", e.getMessage(), e);

                        mView.showExtractionErrorMessage();

                        mView.showMainLoadingIndicator(false);

                        mState.needsExtracting = false;

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
            mView.showExtractionErrorMessage();
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

        Recipe recipe = new Recipe(title, url, mState.selectedImage , mState.favorite ? 1 : 0, keywords, notes);

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

                        mView.showDatabaseErrorMessage();

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("NewRecipePresenter", e.getMessage(), e);

                        mView.showDatabaseErrorMessage();

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
    public NewRecipeSavedState handleSavedState() {
        return mState;
    }

}
