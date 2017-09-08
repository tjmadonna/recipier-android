/*
 * Result.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 7/13/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.remote.models;

import java.util.List;

public class Result {

    private String title;

    private String url;

    private List<String> images;

    public Result(Response response) {

        this.title = response.getTitle();

        this.url = response.getUrl();

        String mainImage = response.getMainImage();

        List<String> images = response.getImages();

        if (images.contains(mainImage)) {
            images.remove(mainImage);
        }

        images.add(0, mainImage);

        this.images = images;

    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getImages() {
        return images;
    }

}
