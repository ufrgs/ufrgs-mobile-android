package com.cpd.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

import com.cpd.ufrgsmobile.R

/**
 * Created by theolm on 15/07/16.
 */
class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val N = appWidgetIds.size

        for (i in 0 until N) {
            val appWidgetId = appWidgetIds[i]


            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
            remoteViews.setTextViewText(R.id.widget_title, "RU1 - Centro")
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

            val serviceIntent = Intent(context, WidgetRemoteService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            context.startService(serviceIntent)

        }
    }

    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        val svcIntent = Intent(context, WidgetServices::class.java)
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        svcIntent.data = Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME))
        remoteViews.setRemoteAdapter(R.id.widget_lv, svcIntent)
        return remoteViews
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: ")
        if (intent.action == DATA_FETCHED) {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val remoteViews = updateWidgetListView(context, appWidgetId)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

    }

    companion object {
        private val TAG = "WidgetProvider"
        val DATA_FETCHED = "com.cpd.appcpd.DATA_FETCHED"
    }


}
