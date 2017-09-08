/*
 * Recipe.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.models;

import java.util.List;

public class Recipe {

    public static final int ID_NONE = -1;

    public static final int DATE_SAVED_NEVER = -1;

    private int id;

    private long dateAdded;

    private String title;

    private String url;

    private String imageUrl;

    private boolean favorite;

    private String notes;

    private List<String> keywords;

    public Recipe(String title, String url, String imageUrl, int favorite, List<String> keywords, String notes) {
        this.id = ID_NONE;
        this.dateAdded = DATE_SAVED_NEVER;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.favorite = (favorite == 1);
        this.keywords = keywords;
        this.notes = notes;
    }

    public Recipe(int id, long dateAdded, String title, String url, String imageUrl, int favorite, List<String> keywords, String notes) {
        this.id = id;
        this.dateAdded = dateAdded;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.favorite = (favorite == 1);
        this.keywords = keywords;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public int getFavoriteAsInt() {
        return favorite ? 1 : 0;
    }

    public String getNotes() {
        return notes;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object object) {

        return (object == this) || (object instanceof Recipe) && url.equals(((Recipe) object).getUrl());

    }

}
