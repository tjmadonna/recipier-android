/*
 * YQLService.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote;

import com.inelasticcollision.recipelink.data.remote.models.Response;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ExtractionService {

    @GET("details?key=aVmGgN9pLir9e2mxdRZnHsqanZut3moCnTmz28bm")
    Observable<Response> extractRecipe(@Query("url") String url);

}
