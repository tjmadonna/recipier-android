/*
 * RemoteDataProvider.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote;

import com.inelasticcollision.recipelink.data.remote.models.Result;

import io.reactivex.Observable;

public interface RemoteDataProvider {

    Observable<Result> getRecipeInformation(String url);

}
