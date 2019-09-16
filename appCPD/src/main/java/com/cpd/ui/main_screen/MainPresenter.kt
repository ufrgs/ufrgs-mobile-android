package com.cpd.ui.main_screen

import android.annotation.SuppressLint
import com.cpd.network.ApiBuilder
import com.cpd.network.UfrgsServices
import com.cpd.ufrgsmobile.BuildConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {
    @SuppressLint("CheckResult")
    override fun fetchNotification() {
        val retrofit = ApiBuilder.retrofitBuilder(BuildConfig.API_BASE_URL)
        val service = retrofit.create(UfrgsServices::class.java)
        val observable = service.getNotifications(BuildConfig.VERSION_CODE.toString())

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            try {
                                if (it != null && it.isNotEmpty()){
                                    view.onNotificationReady(it.first())
                                } else {
                                    view.onNotificationReady(null)
                                }
                            } catch (e : Exception) {
                                view.onNotificationReady(null)
                            }
                        },
                        {
                            view.onNotificationReady(null)
                        })
    }
}