package com.cpd.network

import com.cpd.ufrgsmobile.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by theolm on 05/02/2018.
 */
object ApiBuilder {

    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
    }

    fun retrofitBuilder(baseUrl : String) : Retrofit {
        return Retrofit.Builder()
//                .baseUrl(BuildConfig.API_BASE_URL)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}