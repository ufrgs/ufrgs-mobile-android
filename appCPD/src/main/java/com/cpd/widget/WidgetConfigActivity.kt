package com.cpd.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.cpd.ufrgsmobile.R
import com.cpd.utils.WidgetUtils
import kotlinx.android.synthetic.main.activity_widget_config.*

/**
 * Created by theolm on 02/01/17.
 */

class WidgetConfigActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mContext: Context? = null
    private var mAppWidgetId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)
        mContext = this


        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        val ruArray = resources.getStringArray(R.array.restaurantArray)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, ruArray)
        widget_listview.adapter = adapter
        widget_listview.onItemClickListener = this

    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

        WidgetUtils.saveWidgetRU(mContext!!, i)
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()

        val widgetUpdateIntent = Intent()
        widgetUpdateIntent.action = WidgetProvider.DATA_FETCHED
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        sendBroadcast(widgetUpdateIntent)
    }
}
