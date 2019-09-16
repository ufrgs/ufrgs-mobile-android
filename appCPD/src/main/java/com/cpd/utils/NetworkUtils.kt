package com.cpd.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils {

    companion object {
        fun isConnectedToNetwork(context: Context) : Boolean {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            var isConnected = false

            if (connectivityManager != null) {
                val activeNetwork = (connectivityManager as ConnectivityManager).activeNetworkInfo
                isConnected = (activeNetwork != null) && (activeNetwork.isConnected)
            }

            return isConnected
        }
    }

}