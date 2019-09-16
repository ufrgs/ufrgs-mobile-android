package com.cpd.utils

import android.content.Context
import android.content.res.Configuration

object ScreenSizeUtils {

    fun isLarge(context: Context): Boolean {
        var screenLayout = context.resources.configuration.screenLayout
        screenLayout = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return true;
        }
        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            return true;
        }
        return false;
    }

}