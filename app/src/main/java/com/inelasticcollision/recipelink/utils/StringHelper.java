/*
 * StringHelper.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 6/30/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.utils;

import android.text.Html;

import java.net.URI;
import java.net.URISyntaxException;

public class StringHelper {

    private static final String[] URL_PREFIX = {"www.", "m."};

    public static String mapUrlToUserPresentableHostUrl(String url) {

        try {

            URI uri = new URI(url);

            String host = uri.getHost();

            if (host == null) {
                return "";
            }

            for (String prefix : URL_PREFIX) {
                if (host.startsWith(prefix)) {
                    host = host.substring(prefix.length());
                }
            }

            return host;

        } catch (URISyntaxException e) {
            return "";
        }

    }

    @SuppressWarnings("deprecation")
    public static String fromHtml(String html){
        String result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            result = Html.fromHtml(html).toString();
        }
        return result;
    }

    public static String removeWhitespace(String string) {
        return string.replaceAll("\\s+", "");
    }

}
