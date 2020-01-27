package com.inelasticcollision.recipelink.data.local.migration.helper;

import android.content.ContentValues;

import com.inelasticcollision.recipelink.DataFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestVersion2SQLiteDataHelper {

    private static final Comparator<TestVersion2Recipe> TITLE_COMPARATOR = new Comparator<TestVersion2Recipe>() {
        @Override
        public int compare(TestVersion2Recipe o1, TestVersion2Recipe o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    public static List<TestVersion2Recipe> generateRandomContentSortedByTitle(int size) {
        ArrayList<TestVersion2Recipe> values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ContentValues value = new ContentValues(9);
            value.put("date_added", DataFactory.randomLong());
            value.put("date_updated", DataFactory.randomLong());
            value.put("title", DataFactory.randomString());
            value.put("link", DataFactory.randomString());
            value.put("photo_url", DataFactory.randomBoolean() ? DataFactory.randomString() : "");
            value.put("favorite", DataFactory.randomBoolean() ? 1 : 0);
            value.put("category", DataFactory.randomBoolean() ? DataFactory.randomString() : "");
            value.put("keywords", DataFactory.randomBoolean() ? DataFactory.randomString() : "");
            value.put("notes", DataFactory.randomBoolean() ? DataFactory.randomString() : "");
            values.add(new TestVersion2Recipe(value));
        }
        Collections.sort(values, TITLE_COMPARATOR);
        return values;
    }

}
