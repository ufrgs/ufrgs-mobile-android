/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Connection utilities
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class ConnectionUtils {

    /** Maximum number of tentatives to retrieve something from network */
    public static final int MAX_NETWORK_TENTATIVES = 10;

    /**
     * Check if user is connected to internet
     * @param context Application context
     * @return If the user is connected to internet
     */
    public static boolean isConnectingToInternet(Context context){
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

}

