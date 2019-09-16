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

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ExtractionRemoteDataProvider implements RemoteDataProvider {

    private final ExtractionService mExtractionService;

    public ExtractionRemoteDataProvider(ExtractionService extractionService) {
        mExtractionService = extractionService;
    }

    @Override
    public Observable<Result> getRecipeInformation(String url) {
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
