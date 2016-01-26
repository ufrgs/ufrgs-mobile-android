/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

/**
 * Holds methods to convert measures
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class MeasureUtils {

    /**
     * A method to find the status bar height, if the API is equal or greater than a api level.
     * @param context Application context
     * @param minApiLevel Minimum API level to receive a value.
     *                    If the API is lower than this value, it will receive 0.
     * @return  Status par height, in pixels
     */
    public static int getStatusBarHeightApiLevel(Context context, int minApiLevel) {
        if(Build.VERSION.SDK_INT >= minApiLevel){
            return getStatusBarHeight(context);
        }
        return 0;
    }

    private static int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * A method to find the navigation bar height, if the API is equal or greater than a api level.
     * @param context Application context
     * @param minApiLevel Minimum API level to receive a value.
     *                    If the API is lower than this value, it will receive 0.
     * @return  Status par height, in pixels
     */
    public static int getNavigationBarHeightApiLevel(Context context, int minApiLevel) {
        if(Build.VERSION.SDK_INT >= minApiLevel){
            return getNavigationBarHeight(context);
        }
        return 0;
    }

    private static int getNavigationBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Convert a value in DP (Density-independent Points) to pixels
     * @param dp Value in DP
     * @param context App context
     * @return Value in pixels
     */
    public static int convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) Math.ceil(dp * (metrics.densityDpi / 160f));
        return px;

    }

    /**
     * Convert a value in pixels to DP (Density-independent Points)
     * @param px Value in pixels
     * @param context App context
     * @return Value in DP
     */
    public static int convertPxToDp(float px, Context context){

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) Math.ceil(dp);

    }

    /**
     * Computes the distance between two coordinates. It does not takes into account
     * the shape of the earth, so the points must be close enough to ignore this error.
     * Based on http://www.movable-type.co.uk/scripts/latlong.html
     * @return Distance in meters
     */
    public static double coordinateDistance(double latA, double lngA, double latB, double lngB){
        final int radius = 6371000; // Meters

        double omegaA = Math.toRadians(latA);
        double omegaB = Math.toRadians(latB);

        double deltaOmega = Math.toRadians(latB - latA);
        double deltaAlpha = Math.toRadians(lngB - lngA);

        double a = Math.sin(deltaOmega/2) * Math.sin(deltaOmega/2) +
                    Math.cos(omegaA) * Math.cos(omegaB) *
                    Math.sin(deltaAlpha/2) * Math.sin(deltaAlpha/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radius * c;

    }
}
