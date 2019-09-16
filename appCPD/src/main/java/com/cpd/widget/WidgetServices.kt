package com.cpd.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created by theolm on 19/07/16.
 */
class WidgetServices : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return WidgetViewFactory(this.applicationContext, intent)
    }
}
