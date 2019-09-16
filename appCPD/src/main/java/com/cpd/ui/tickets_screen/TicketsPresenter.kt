package com.cpd.ui.tickets_screen

import android.content.Context
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.cpd.network.ApiBuilder
import com.cpd.network.UfrgsServices
import com.cpd.ufrgsmobile.R
import com.cpd.utils.CacheUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Theo on 08/09/17.
 */

class TicketsPresenter(private val mView: TicketsContract.View, private val context: Context) : TicketsContract.Presenter {

    override fun getTickets() {

        val token = ""

        val retrofit = ApiBuilder.retrofitBuilder("")
        val service = retrofit.create(UfrgsServices::class.java)
        val observable = service.getTickets(token)

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    try {
                        CacheUtils.cacheTickets(it.data.tiquetes)
                        mView.showTickets(it.data.tiquetes)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        val list = CacheUtils.getTickets()
                        val date = CacheUtils.getTicketsDate()
                        if (list != null) {
                            mView.showTickets(list)
                            mView.showMessage(context.getString(R.string.ticket_get_error_cache) + " " + date)
                        } else {
                            mView.showMessage(context.getString(R.string.ticket_get_error))
                        }
                    }
                }, {
                    val list = CacheUtils.getTickets()
                    val date = CacheUtils.getTicketsDate()
                    if (list != null) {
                        mView.showTickets(list)
                        mView.showMessage(context.getString(R.string.ticket_get_error_cache) + " " + date)
                    } else {
                        mView.showMessage(context.getString(R.string.ticket_get_error))
                    }
                })

    }

}
