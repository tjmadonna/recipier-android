package com.inelasticcollision.recipelink.data.local.converter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringListTypeConverter {

    private static final String DELIMITER = "|";

    @NonNull
    @TypeConverter
    public static List<String> fromString(@Nullable String string) {
        if (string == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(string.split("\\" + DELIMITER));
    }

    @Nullable
    @TypeConverter
    public static String stringListToString(@NonNull List<String> stringList) {
        if (stringList.isEmpty()) {
            return null;
        }

        return TextUtils.join(DELIMITER, stringList);
    }

}
