/*
 * RecipesPresenter.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.inelasticcollision.recipelink.data.local.LocalDataProvider;
import com.inelasticcollision.recipelink.data.models.Recipe;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class RecipesPresenter implements RecipesContract.Presenter {

    private final RecipesContract.View mView;

    private final LocalDataProvider mDataProvider;

    private final CompositeSubscription mSubscriptions;

    RecipesPresenter(RecipesContract.View view, LocalDataProvider dataProvider) {
        mView = view;
        mDataProvider = dataProvider;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onUnsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void handleLoadRecipes(int sortType, @Nullable String search) {

        mSubscriptions.clear();

        Observable<List<Recipe>> recipeObservable = null;

        switch (sortType) {

            case RecipesSortType.ALL:

                Log.d("Presenter", "Getting all recipes");

                recipeObservable = mDataProvider.loadAllRecipes();

                break;

            case RecipesSortType.FAVORITE:

                Log.d("Presenter", "Getting favorite recipes");

                recipeObservable = mDataProvider.loadFavoriteRecipes();

                break;

            case RecipesSortType.SEARCH:

                Log.d("Presenter", "Getting recipes for search type " + search);

                recipeObservable = mDataProvider.searchRecipes(search);

                break;

        }

        if (recipeObservable != null) {

            Subscription subscription = recipeObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Recipe>>() {

                        @Override
                        public void onNext(@NonNull List<Recipe> recipes) {

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
                        public void onCompleted() {

                        }

                    });

            mSubscriptions.add(subscription);

        }

    }

    @Override
    public void handleLearnHowButtonClicked() { mView.showLearnHowWebPage(); }

}
