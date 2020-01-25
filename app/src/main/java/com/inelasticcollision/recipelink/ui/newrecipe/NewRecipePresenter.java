/*
 * NewRecipePresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.newrecipe;

import android.util.Log;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;
import com.inelasticcollision.recipelink.data.remote.RemoteDataProvider;
import com.inelasticcollision.recipelink.data.remote.models.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class NewRecipePresenter implements NewRecipeContract.Presenter {

    private final NewRecipeContract.View mView;

    private final LocalDataProvider mLocalDataProvider;

    private final RemoteDataProvider mRemoteDataProvider;

    private final CompositeDisposable mCompositeDisposable;

    private NewRecipeSavedState mState;

    NewRecipePresenter(NewRecipeContract.View view, LocalDataProvider localDataProvider, RemoteDataProvider remoteDataProvider, NewRecipeSavedState state) {
        mView = view;
        mLocalDataProvider = localDataProvider;
        mRemoteDataProvider = remoteDataProvider;
        mCompositeDisposable = new CompositeDisposable();
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
        mCompositeDisposable.clear();
    }

    @Override
    public void handleExtractRecipe(final String title, final String url) {

        mState.needsExtracting = true;

        mView.showMainLoadingIndicator(true);

        Disposable disposable = mRemoteDataProvider.getRecipeInformation(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Result>() {
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
                    public void onComplete() {
                    }
                });

        mCompositeDisposable.add(disposable);
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
    public void handleSaveRecipe(String title, String url, String notes, List<String> tags) {

        if (title == null || title.isEmpty()) {
            return;
        }

        if (url == null || url.isEmpty()) {
            return;
        }

        if (notes != null && notes.isEmpty()) {
            notes = null;
        }

        Recipe recipe = new Recipe(title, url, mState.selectedImage, mState.favorite, notes, tags);

        Disposable disposable = mLocalDataProvider.saveRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mView.finishView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NewRecipePresenter", e.getMessage(), e);
                        mView.showDatabaseErrorMessage();
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void handleCloseClick() {
        mView.showCloseDialog();
    }

    @Override
    public void handleCloseView() {
        mView.finishView();
    }

    @Override
    public NewRecipeSavedState handleSavedState() {
        return mState;
    }

}
