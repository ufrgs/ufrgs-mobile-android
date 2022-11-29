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
package com.cpd

import androidx.multidex.MultiDexApplication
import br.ufrgs.ufrgsapi.UfrgsAPI
import st.lowlevel.storo.StoroBuilder

/**
 * Executes before any activity.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        // Fabric.with(this, Answers(), Crashlytics())

        StoroBuilder.configure(500000)  // maximum size to allocate in bytes
                .setDefaultCacheDirectory(this)
                .initialize()

        UfrgsAPI.initialize(SET_API_TO_PRODUCTION, CLIENT_ID, CLIENT_SECRET, LIBRARY_SCOPE, GRANT_TYPE)

    }

    companion object {

        private val BASE_URL = ""
        private val CLIENT_ID = ""
        private val CLIENT_SECRET = ""
        private val LIBRARY_SCOPE = ""
        private val GRANT_TYPE = ""
        private val SET_API_TO_PRODUCTION = true
    }

}
