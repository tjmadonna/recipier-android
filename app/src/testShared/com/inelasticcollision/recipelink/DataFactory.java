package com.inelasticcollision.recipelink;

import java.util.Random;
import java.util.UUID;

public final class DataFactory {

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt() {
        return new Random().nextInt();
    }

    public static long randomLong() {
        return new Random().nextInt();
    }

    public static boolean randomBoolean() {
        return new Random().nextFloat() > 0.5;
    }

}
