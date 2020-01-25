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
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

class EditRecipePresenter implements EditRecipeContract.Presenter {

    private final EditRecipeContract.View mView;

    private final LocalDataProvider mLocalDataProvider;

    private final RemoteDataProvider mRemoteDataProvider;

    private final CompositeDisposable mCompositeDisposable;

    private EditRecipeSavedState mState;

    EditRecipePresenter(EditRecipeContract.View view, LocalDataProvider localDataProvider, RemoteDataProvider remoteDataProvider, EditRecipeSavedState state) {
        mView = view;
        mLocalDataProvider = localDataProvider;
        mRemoteDataProvider = remoteDataProvider;
        mCompositeDisposable = new CompositeDisposable();
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
        mCompositeDisposable.clear();
    }

    @Override
    public void handleLoadRecipe() {

        mCompositeDisposable.clear();

        Disposable disposable = mLocalDataProvider.getRecipeById(mState.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Recipe>() {
                    @Override
                    public void onSuccess(Recipe recipe) {
                        handleExtractImages(recipe.getUrl());
                        String imageUrl = recipe.getImageUrl();
                        mState.selectedImage = recipe.getImageUrl();
                        mState.favorite = recipe.isFavorite();
                        mView.showMainImage(imageUrl);
                        mView.showTitle(recipe.getTitle());
                        mView.showUrl(recipe.getUrl());
                        mView.showFab(recipe.isFavorite());
                        mView.showNotes(recipe.getNotes());
                        mView.showKeywords(recipe.getTags());
                        mState.needsRecipeQuery = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("EditRecipePresenter", e.getMessage(), e);
                        mView.showErrorMessage(e.getMessage());
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void handleExtractImages(final String url) {

        if (!mState.needsImageExtracting) {
            return;
        }

        mView.showImageLoadingIndicator(true);

        Disposable disposable = mRemoteDataProvider.getRecipeInformation(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Result>() {
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
                    public void onComplete() {

                    }
                });

        mCompositeDisposable.add(disposable);
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

        Recipe recipe = new Recipe(mState.id, new Date(), title, url, mState.selectedImage, mState.favorite, notes, tags);

        Disposable disposable = mLocalDataProvider.updateRecipe(recipe)
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
                        mView.showErrorMessage();
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
    public EditRecipeSavedState handleSavedState() {
        return mState;
    }
}
