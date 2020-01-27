package com.inelasticcollision.recipelink.data.local.migration.helper;

import android.content.ContentValues;

import static com.inelasticcollision.recipelink.data.local.migration.helper.TestVersion2RecipeTable.*;

public class TestVersion2Recipe {

    private final ContentValues contentValues;

    TestVersion2Recipe(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    ContentValues getContentValues() {
        return contentValues;
    }

    public long getLastModified() {
        return contentValues.getAsLong(COLUMN_DATE_UPDATED);
    }

    public String getTitle() {
        return contentValues.getAsString(COLUMN_TITLE);
    }

    public String getUrl() {
        return contentValues.getAsString(COLUMN_URL);
    }

    public String getImageUrl() {
        String imageUrl = contentValues.getAsString(COLUMN_IMAGE_URL);
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        return imageUrl;
    }

    public int isFavorite() {
        return contentValues.getAsInteger(COLUMN_FAVORITE);
    }

    public String getNotes() {
        String notes = contentValues.getAsString(COLUMN_NOTES);
        if (notes == null || notes.isEmpty()) {
            return null;
        }
        return notes;
    }

    public String getTags() {
        String tags = contentValues.getAsString(COLUMN_KEYWORDS);
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return tags;
    }
}
