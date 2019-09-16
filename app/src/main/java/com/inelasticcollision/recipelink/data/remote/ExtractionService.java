/*
 * YQLService.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote;

import com.inelasticcollision.recipelink.data.remote.models.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExtractionService {

    @GET("api/v1/fullinfo")
    Observable<Response> extractRecipe(@Query("url") String url);

}
