package com.cpd.network

import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import com.cpd.network.models.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by theolm on 15/07/16.
 */
interface UfrgsServices {

    @GET("getnoticias")
    fun getNews(@Query("v") appVersion: String): Observable<List<News>>

    @GET("getCardapioRU")
    fun getRuMenu(@Query("v") appVersion: String): Observable<RuResponse>

    @GET("v1/r-u-tiquetes/disponiveis")

    fun getTickets(@Header("Authorization") authorizationHeader: String): Observable<ApiAnswer<TicketsResponse>>

    @GET("getnotificacoes")
    fun getNotifications(@Query("v") appVersion: String): Observable<List<Notifications>>

}
