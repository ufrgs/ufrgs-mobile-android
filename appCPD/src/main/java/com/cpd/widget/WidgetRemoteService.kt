package com.cpd.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.cpd.vos.RuVo
import java.util.*

/**
 * Created by theolm on 20/07/16.
 */
class WidgetRemoteService : Service() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        getRuInformation()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getRuInformation() {

        //TODO?
        //        UfrgsServices service = mClient.create(UfrgsServices.class);
        //        Call<ResponseBody> call = service.getRuMenu(String.valueOf(BuildConfig.VERSION_CODE));
        //        call.enqueue(new Callback<ResponseBody>() {
        //            @Override
        //            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        //                try {
        //
        //                    Gson gson = new Gson();
        //                    RuApiReturn ru = gson.fromJson(response.body().string(), RuApiReturn.class);
        //
        //                    mRuList.clear();
        //
        //                    mRuList.add(ru.getRu1());
        //                    mRuList.add(ru.getRu2());
        //                    mRuList.add(ru.getRu3());
        //                    mRuList.add(ru.getRu4());
        //                    mRuList.add(ru.getRu5());
        //                    mRuList.add(ru.getRu6());
        //
        //
        //                } catch (JsonSyntaxException e){
        //                    mRuList = RuUtils.getListSemCardapio();
        //                } catch (Exception e1){
        //                    mRuList = RuUtils.getListError();
        //                }
        //
        //                populateWidget();
        //            }
        //
        //            @Override
        //            public void onFailure(Call<ResponseBody> call, Throwable t) {
        //                Log.d(TAG, "onFailure: deu erro");
        //                mRuList = RuUtils.getListError();
        //                populateWidget();
        //            }
        //        });
    }

    private fun populateWidget() {
        Log.d(TAG, "populateWidget: ")

        val widgetUpdateIntent = Intent()
        widgetUpdateIntent.action = WidgetProvider.DATA_FETCHED
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        sendBroadcast(widgetUpdateIntent)

        this.stopSelf()
    }

    companion object {
        private val TAG = "WidgetRemoteService"
        var mRuList: List<RuVo> = ArrayList()
    }

}
