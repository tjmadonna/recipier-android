/*
 * SharedPreferenceProvider.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 8/12/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.preference;

import android.content.SharedPreferences;
import android.util.Log;

import com.inelasticcollision.recipelink.BuildConfig;

public class SharedPreferenceProvider implements PreferenceProvider {

    private static final String SHOULD_MIGRATE_DATABASE_VERION_12 = "migrate_database_version_12";

    private static final String APP_VERSION_KEY = "app_version";

    private final SharedPreferences mSharedPreferences;

    public SharedPreferenceProvider(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public boolean shouldMigrateToVersion12Database() {

        int appVersion = mSharedPreferences.getInt(APP_VERSION_KEY, BuildConfig.VERSION_CODE);

        boolean shouldMigrate = mSharedPreferences.getBoolean(SHOULD_MIGRATE_DATABASE_VERION_12, true);

        Log.d("PreferenceProvider", "================App version is " + appVersion + " and should migrate is " + shouldMigrate);

        return appVersion == 12 && shouldMigrate;

    }

    @Override
    public boolean setMigrateToVersion12Database() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(SHOULD_MIGRATE_DATABASE_VERION_12, false);
        return editor.commit();
    }

}
