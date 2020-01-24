/*
 * RecipesPresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipes;

import androidx.annotation.Nullable;
import android.util.Log;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class RecipesPresenter implements RecipesContract.Presenter {

    private final RecipesContract.View mView;

    private final LocalDataProvider mDataProvider;

    private final CompositeDisposable mCompositeDisposable;

    RecipesPresenter(RecipesContract.View view, LocalDataProvider dataProvider) {
        mView = view;
        mDataProvider = dataProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onUnsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void handleLoadRecipes(int sortType, @Nullable String search) {

        mCompositeDisposable.clear();

        Observable<List<Recipe>> recipeObservable = null;

        switch (sortType) {

            case RecipesSortType.ALL:

                Log.d("Presenter", "Getting all recipes");

                recipeObservable = mDataProvider.getAllRecipes();

                break;

            case RecipesSortType.FAVORITE:

                Log.d("Presenter", "Getting favorite recipes");

                recipeObservable = mDataProvider.getFavoriteRecipes();

                break;

            case RecipesSortType.SEARCH:

                Log.d("Presenter", "Getting recipes for search type " + search);

                recipeObservable = mDataProvider.searchRecipes(search);

                break;

        }

        if (recipeObservable != null) {

            Disposable disposable = recipeObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<Recipe>>() {
                        @Override
                        public void onNext(List<Recipe> recipes) {
                            if (recipes.size() < 1) {
                                mView.showRecipes(recipes);
                                mView.showEmptyContentMessage();
                                return;
                            }
                            mView.showRecipes(recipes);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("RecipesPresenter", e.getMessage(), e);
                            mView.showEmptyContentMessage();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            mCompositeDisposable.add(disposable);
        }
    }

    @Override
    public void handleLearnHowButtonClicked() { mView.showLearnHowWebPage(); }

}
