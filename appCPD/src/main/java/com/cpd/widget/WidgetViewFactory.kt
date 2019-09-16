package com.cpd.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.cpd.ufrgsmobile.R
import com.cpd.utils.WidgetUtils
import com.cpd.vos.RuVo
import java.util.*

/**
 * Created by theolm on 19/07/16.
 */
class WidgetViewFactory(val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private val appWidgetId: Int = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    private var mRuList: List<RuVo> = ArrayList()

    init {
        populateListItem()
    }

    override fun onCreate() {
        //getRuInformation();
    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return if (mRuList.size > 0)
            1
        else
            0
    }

    override fun getViewAt(i: Int): RemoteViews {

        val ruIndex = WidgetUtils.getWidgetRU(context!!)
        changeWidgetTitle(ruIndex)

        val ru = mRuList[ruIndex]

        val fake = "- Cogumelos Matsutake\n- Zillion Dollar Lobster Frittata\n- Wagyu Ribeye Steak\n- Almas Caviar\n*Opção vegetariana\n- Yubari King Melons"

        val remoteViewRow = RemoteViews(context.packageName, R.layout.widget_row)
        remoteViewRow.setTextViewText(R.id.widget_content_menu, ru.cardapio.replace("(\\s)+".toRegex(), "$1"))
        return remoteViewRow
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun populateListItem() {
        if (WidgetRemoteService.mRuList != null)
            mRuList = ArrayList(WidgetRemoteService.mRuList)
    }

    private fun changeWidgetTitle(ruIndex: Int) {

        val ruName = context!!.resources.getStringArray(R.array.restaurantArray)
        val ruValue = context.resources.getStringArray(R.array.restaurantValues)

        val remoteView = RemoteViews(context.packageName, R.layout.widget_layout)
        remoteView.setTextViewText(R.id.widget_title, "RU" + ruValue[ruIndex] + " - " + ruName[ruIndex])

        val picture = getRuImage(ruIndex)

        remoteView.setImageViewResource(R.id.widget_picture, picture)

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteView)
    }

    private fun getRuImage(ruIndex: Int): Int {
        when (ruIndex) {
            0 -> return R.drawable.ru1
            1 -> return R.drawable.ru2
            2 -> return R.drawable.ru3
            3 -> return R.drawable.ru4
            4 -> return R.drawable.ru5
            5 -> return R.drawable.ru6
            else -> return R.drawable.ru1
        }
    }
}
