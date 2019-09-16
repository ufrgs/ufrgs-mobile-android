package com.cpd.ui.ru_screen

import android.annotation.SuppressLint
import com.cpd.network.ApiBuilder
import com.cpd.network.UfrgsServices
import com.cpd.network.models.Ru
import com.cpd.ufrgsmobile.BuildConfig
import com.cpd.utils.CacheUtils
import com.cpd.utils.RuUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Theo on 02/06/17.
 */

class RuScreenPresenter(private val view: RuScreenContract.View) : RuScreenContract.Presenter {

    @SuppressLint("CheckResult")
    override fun getRuInformation() {
        val retrofit = ApiBuilder.retrofitBuilder(BuildConfig.API_BASE_URL)
        val service = retrofit.create(UfrgsServices::class.java)
        val observable = service.getRuMenu(BuildConfig.VERSION_CODE.toString())

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val list = ArrayList<Ru>()

                    try {

                        list.add(fixRuInfo(it.ru1, 1))
                        list.add(fixRuInfo(it.ru2, 2))
                        list.add(fixRuInfo(it.ru3, 3))
                        list.add(fixRuInfo(it.ru4, 4))
                        list.add(fixRuInfo(it.ru5, 5))
                        list.add(fixRuInfo(it.ru6, 6))
                        list.add(fixRuInfo(it.ru7, 7))

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    CacheUtils.cacheRu(list)

                    view.showRuInfo(list)

                }, {
                    view.showFailMessage()
                })
    }

    private fun fixRuInfo(ru: Ru, number: Int): Ru {
        var cardapio = ru.cardapio.replace("(\\s)+".toRegex(), "$1")

        // remove "RU X - " from RU name
        val name = ru.nome
        val parts = name.split(" - ")

        if (parts.count() > 1) {
            ru.nome = parts[1]

            if (ru.nome == "Agronomia") {
                ru.nome = "Agro"
            } else if (ru.nome == "Esef") {
                ru.nome = "Esefid"
            }
        }

        // trim whitespaces from menu
        if (Character.isWhitespace(cardapio[0]))
            cardapio = cardapio.substring(1)
        ru.cardapio = cardapio

        if (ru.cardapio == "") {
            ru.cardapio = "Cardápio não disponível"
        }

        ru.number = number

        return ru
    }
}
