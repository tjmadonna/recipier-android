/*
 * YQLRemoteDataProvider.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/2/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.inelasticcollision.recipelink.BuildConfig;
import com.inelasticcollision.recipelink.data.remote.models.Response;
import com.inelasticcollision.recipelink.data.remote.models.Result;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExtractionRemoteDataProvider implements RemoteDataProvider {

    private final ExtractionService mExtractionService;

    @NonNull
    private static final Object lock = new Object();
    @Nullable
    private static volatile ExtractionRemoteDataProvider instance = null;

    private ExtractionRemoteDataProvider(ExtractionService extractionService) {
        mExtractionService = extractionService;
    }

    @NonNull
    public static ExtractionRemoteDataProvider getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(30, TimeUnit.SECONDS)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    // Inject api key into query parameters
                                    HttpUrl url = chain.request()
                                            .url()
                                            .newBuilder()
                                            .addQueryParameter("key", BuildConfig.REMOTE_SERVICE_API_KEY)
                                            .build();
                                    Request request = chain.request()
                                            .newBuilder()
                                            .url(url)
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BuildConfig.REMOTE_SERVICE_BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                    ExtractionService service = retrofit.create(ExtractionService.class);

                    instance = new ExtractionRemoteDataProvider(service);
                }
            }
        }

        return instance;
    }

    @Override
    public Observable<Result> getRecipeInformation(@NonNull String url) {
        return mExtractionService.extractRecipe(url)
                .map(new Function<Response, Result>() {
                    @Override
                    public Result apply(Response response) throws Exception {

                        String error = response.getError();

                        if (error != null) {
                            throw new Exception(error);
                        }

                        if (response.getMainImage().isEmpty() && response.getImages().size() < 1) {
                            throw new Exception("ExtractionService returned no images");
                        }

                        return new Result(response);
                    }
                });
    }

}
