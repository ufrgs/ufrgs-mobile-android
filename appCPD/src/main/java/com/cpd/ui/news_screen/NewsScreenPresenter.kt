package com.cpd.ui.news_screen

import com.cpd.network.ApiBuilder
import com.cpd.network.UfrgsServices
import com.cpd.ufrgsmobile.BuildConfig
import com.cpd.utils.CacheUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by theolm on 19/05/17.
 */

class NewsScreenPresenter(private val view: NewsScreenContract.View) : NewsScreenContract.Presenter {

    override fun fetchNews() {
        val retrofit = ApiBuilder.retrofitBuilder(BuildConfig.API_BASE_URL)
        val service = retrofit.create(UfrgsServices::class.java)
        val observable = service.getNews(BuildConfig.VERSION_CODE.toString())

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            CacheUtils.cacheNews(it)
                            view.showNews(it)
                        },
                        {
                            view.showMessage("Não foi possivel adquirir as informações. Por favor tente mais tarde.")
                        })

    }


}
