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
package com.cpd.network;

import android.util.Log;

import com.cpd.network.parsers.NewsParser;
import com.cpd.utils.DebugUtils;
import com.cpd.vos.NewsVo;

import java.util.concurrent.ExecutionException;

/**
 * Handles News loading
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class NewsLoader {
    private static final String TAG = NewsLoader.class.getSimpleName();

    NewsParser.NewsReady mCallback;

    /**
     * Handles the thread creation to a new RSS parser. Requires a callback to be
     * called when the task is done.
     */
    public NewsLoader(NewsParser.NewsReady mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * Run the new thread.
     * @param asynchronous If the thread will run asynchronously.
     */
    public void run(NewsVo newsVo, boolean asynchronous){
        NewsParser parser = new NewsParser(newsVo);
        parser.setCallback(mCallback);

        // Optimization: If the NewsVo has an articleText already,
        // it does not need to call the parser again.
        if(newsVo.articleText != null && !newsVo.articleText.isEmpty()){
            if(DebugUtils.DEBUG) Log.d(TAG, "Optimized! There is no need to recall the parser");
            return;
        }

        if(asynchronous) {
            parser.execute();
        }
        else {
            try {
                parser.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                if(DebugUtils.ERROR) Log.e(TAG, e.toString());
            }
        }
    }
}
