package com.inelasticcollision.recipelink.data.local.migration.helper;

import android.provider.BaseColumns;

final class TestVersion2RecipeTable implements BaseColumns {

    // Table name
    static final String TABLE_NAME = "recipe";

    // Columns
    static final String COLUMN_DATE_ADDED = "date_added";
    static final String COLUMN_DATE_UPDATED = "date_updated";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_URL = "link";
    static final String COLUMN_IMAGE_URL = "photo_url";
    static final String COLUMN_FAVORITE = "favorite";
    static final String COLUMN_CATEGORY = "category";
    static final String COLUMN_KEYWORDS = "keywords";
    static final String COLUMN_NOTES = "notes";

}
