package com.inelasticcollision.recipelink.data.local.converter;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class StringListTypeConverter {

    private static final String DELIMITER = "|";

    @Nullable
    @TypeConverter
    public static List<String> fromString(@Nullable String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        return Arrays.asList(string.split("\\" + DELIMITER));
    }

    @Nullable
    @TypeConverter
    public static String stringListToString(@Nullable List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return null;
        }

        return TextUtils.join(DELIMITER, stringList);
    }

}
