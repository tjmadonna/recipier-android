/*
 * MigrationActivity.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 8/12/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.inelasticcollision.recipelink.R;
import com.inelasticcollision.recipelink.data.local.BriteDatabaseHelper;
import com.inelasticcollision.recipelink.data.local.CollectionLocalDataProvider;
import com.inelasticcollision.recipelink.data.local.DatabaseMigration;
import com.inelasticcollision.recipelink.data.local.RecipeLocalDataProvider;
import com.inelasticcollision.recipelink.data.local.RelationshipLocalDataProvider;
import com.inelasticcollision.recipelink.data.preference.PreferenceProvider;
import com.inelasticcollision.recipelink.data.preference.SharedPreferenceProvider;
import com.squareup.sqlbrite.BriteDatabase;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MigrationActivity extends AppCompatActivity {

    private boolean mMigrating = false;

    private CompositeSubscription mSubscription;

    public static Intent createIntent(Context context) {
        return new Intent(context, MigrationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_migration);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mSubscription = new CompositeSubscription();

        migrate();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mSubscription.clear();

    }

    private void migrate() {

        if (!mMigrating) {

            BriteDatabase database = BriteDatabaseHelper.getInstance(this);

            RecipeLocalDataProvider recipeProvider = new RecipeLocalDataProvider(database);

            CollectionLocalDataProvider collectionProvider = new CollectionLocalDataProvider(database);

            RelationshipLocalDataProvider relationshipProvider = new RelationshipLocalDataProvider(database);

            PreferenceProvider preferenceProvider = new SharedPreferenceProvider(PreferenceManager.getDefaultSharedPreferences(this));

            final DatabaseMigration migration = new DatabaseMigration(recipeProvider, collectionProvider, relationshipProvider, preferenceProvider);

            mMigrating = true;

            Subscription subscription = migration.migrate()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {

                        @Override
                        public void onNext(Boolean aBoolean) {

                            if (aBoolean) {
                                finishActivity();
                                return;
                            }

                            mMigrating = false;

                            migration.migrate();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("MigrationActivity", e.getMessage(), e);
                            mMigrating = false;
                            migration.migrate();
                        }

                        @Override
                        public void onCompleted() {

                        }

                    });

            mSubscription.add(subscription);

        }
    }

    private void finishActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();

            }
        }, 1000);

    }

}
