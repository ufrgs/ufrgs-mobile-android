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
package com.cpd.utils

import android.content.Context
import android.os.Build

/**
 * Holds methods to convert measures
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
object MeasureUtils {

    /**
     * A method to find the status bar height, if the API is equal or greater than a api level.
     * @param context Application context
     * @param minApiLevel Minimum API level to receive a value.
     * If the API is lower than this value, it will receive 0.
     * @return  Status par height, in pixels
     */
    fun getStatusBarHeightApiLevel(context: Context, minApiLevel: Int): Int {
        return if (Build.VERSION.SDK_INT >= minApiLevel) {
            getStatusBarHeight(context)
        } else 0
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * A method to find the navigation bar height, if the API is equal or greater than a api level.
     * @param context Application context
     * @param minApiLevel Minimum API level to receive a value.
     * If the API is lower than this value, it will receive 0.
     * @return  Status par height, in pixels
     */
    fun getNavigationBarHeightApiLevel(context: Context, minApiLevel: Int): Int {
        return if (Build.VERSION.SDK_INT >= minApiLevel) {
            getNavigationBarHeight(context)
        } else 0
    }

    private fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Convert a value in DP (Density-independent Points) to pixels
     * @param dp Value in DP
     * @param context App context
     * @return Value in pixels
     */
    fun convertDpToPixel(dp: Float, context: Context): Int {

        val resources = context.resources
        val metrics = resources.displayMetrics
        return Math.ceil((dp * (metrics.densityDpi / 160f)).toDouble()).toInt()

    }

    /**
     * Convert a value in pixels to DP (Density-independent Points)
     * @param px Value in pixels
     * @param context App context
     * @return Value in DP
     */
    fun convertPxToDp(px: Float, context: Context): Int {

        val resources = context.resources
        val metrics = resources.displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return Math.ceil(dp.toDouble()).toInt()

    }

    /**
     * Computes the distance between two coordinates. It does not takes into account
     * the shape of the earth, so the points must be close enough to ignore this error.
     * Based on http://www.movable-type.co.uk/scripts/latlong.html
     * @return Distance in meters
     */
    fun coordinateDistance(latA: Double, lngA: Double, latB: Double, lngB: Double): Double {
        val radius = 6371000 // Meters

        val omegaA = Math.toRadians(latA)
        val omegaB = Math.toRadians(latB)

        val deltaOmega = Math.toRadians(latB - latA)
        val deltaAlpha = Math.toRadians(lngB - lngA)

        val a = Math.sin(deltaOmega / 2) * Math.sin(deltaOmega / 2) + Math.cos(omegaA) * Math.cos(omegaB) *
                Math.sin(deltaAlpha / 2) * Math.sin(deltaAlpha / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return radius * c

    }
}
