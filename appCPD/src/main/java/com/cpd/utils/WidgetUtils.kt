package com.cpd.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

/**
 * Created by theolm on 02/01/17.
 */

object WidgetUtils {

    private val WIDGET_PREFS = "WidgetPreferences"
    private val RU_TAG = "SavedRuReference"

    fun saveWidgetRU(context: Context, i: Int) {
        val editor = context.getSharedPreferences(WIDGET_PREFS, MODE_PRIVATE).edit()
        editor.putInt(RU_TAG, i)
        editor.commit()
    }

    fun getWidgetRU(context: Context): Int {
        val prefs = context.getSharedPreferences(WIDGET_PREFS, MODE_PRIVATE)
        return prefs.getInt(RU_TAG, 0)
    }
}
