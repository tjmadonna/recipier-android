package com.inelasticcollision.recipelink.data.local.converter;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {

    @NonNull
    @TypeConverter
    public static Date fromTimestamp(@NonNull Long value) {
        return new Date(value);
    }

    @NonNull
    @TypeConverter
    public static Long dateToTimestamp(@NonNull Date date) {
        return date.getTime();
    }

}
