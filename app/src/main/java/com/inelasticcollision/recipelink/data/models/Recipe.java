/*
 * Recipe.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 5/28/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "recipes")
public class Recipe {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    private final String id;

    @NonNull
    @ColumnInfo(name = "last_modified")
    private final Date lastModified;

    @NonNull
    @ColumnInfo(name = "title")
    private final String title;

    @NonNull
    @ColumnInfo(name = "url")
    private final String url;

    @Nullable
    @ColumnInfo(name = "image_url")
    private final String imageUrl;

    @Nullable
    @ColumnInfo(name = "tags")
    private final List<String> tags;

    @Nullable
    @ColumnInfo(name = "notes")
    private final String notes;
    @ColumnInfo(name = "favorite")
    private boolean favorite;

    public Recipe(@NonNull String id,
                  @NonNull Date lastModified,
                  @NonNull String title,
                  @NonNull String url,
                  @Nullable String imageUrl,
                  boolean favorite,
                  @Nullable String notes,
                  @Nullable List<String> tags) {
        this.id = id;
        this.lastModified = lastModified;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
        this.notes = notes;
        this.tags = tags;
    }

    @Ignore
    public Recipe(@NonNull String title,
                  @NonNull String url,
                  @Nullable String imageUrl,
                  boolean favorite,
                  @Nullable String notes,
                  @Nullable List<String> tags) {
        this.id = UUID.randomUUID().toString();
        this.lastModified = new Date();
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
        this.notes = notes;
        this.tags = tags;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public Date getLastModified() {
        return lastModified;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    @NonNull
    public List<String> getTags() {
        if (tags == null) {
            return Collections.emptyList();
        }
        return tags;
    }

    @Ignore
    @Override
    public boolean equals(Object object) {
        return (object == this) || (object instanceof Recipe) && url.equals(((Recipe) object).getUrl());
    }
}
