/*
 * Result.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/13/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("mainImage")
    private String mainImage;

    @SerializedName("images")
    private List<String> images;

    @SerializedName("error")
    private String error;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getMainImage() {
        return mainImage;
    }

    public List<String> getImages() {
        return images;
    }

    public String getError() {
        return error;
    }

}
