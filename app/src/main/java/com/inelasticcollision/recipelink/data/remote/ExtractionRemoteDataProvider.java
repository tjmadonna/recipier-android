/*
 * YQLRemoteDataProvider.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/2/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote;

import com.inelasticcollision.recipelink.data.remote.models.Response;
import com.inelasticcollision.recipelink.data.remote.models.Result;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

public class ExtractionRemoteDataProvider implements RemoteDataProvider {

    private final ExtractionService mExtractionService;

    public static ExtractionService buildExtractionService() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://recipe-details.herokuapp.com/recipelink/api/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ExtractionService.class);

    }


    public ExtractionRemoteDataProvider(ExtractionService extractionService) {
        mExtractionService = extractionService;
    }

    @Override
    public Observable<Result> getRecipeInformation(String url) {
        return mExtractionService.extractRecipe(url)
                .flatMap(new Func1<Response, Observable<Result>>() {
                    @Override
                    public Observable<Result> call(Response response) {

                        String error = response.getError();

                        if (error != null) {
                            return Observable.error(new Exception(error));
                        }

                        if (response.getMainImage().isEmpty() && response.getImages().size() < 1) {
                            return Observable.error(new Exception("ExtractionService returned no images"));
                        }

                        return Observable.just(new Result(response));

                    }
                });
    }

}
