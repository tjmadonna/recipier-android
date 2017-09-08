/*
 * CollectionLocalDataProvider.java
 * RecipeLink
 *
 * Created by Tyler Madonna on 6/8/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.data.local;

import com.inelasticcollision.recipelink.data.models.Collection;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class CollectionLocalDataProvider {

    private BriteDatabase mDatabase;

    public CollectionLocalDataProvider(BriteDatabase database) {
        mDatabase = database;
    }

    // Collection queries

    public Observable<List<Collection>> loadCollections() {
        return mDatabase
                .createQuery(CollectionTable.TABLE_NAME, CollectionTable.getAllCollectionsQuery())
                .mapToOneOrDefault(CollectionTable.COLLECTION_MAPPER, new ArrayList<Collection>(0));
    }

    public Observable<Integer> deleteAllCollections() {
        return Observable.just(mDatabase
                .delete(CollectionTable.TABLE_NAME, null));

    }

}
