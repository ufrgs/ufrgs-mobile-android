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
package com.cpd.network.parsers;

import android.os.AsyncTask;
import android.util.Log;

import com.cpd.utils.DebugUtils;
import com.cpd.vos.NewsVo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Loads the information about one news article.
 * It uses the same object that RssParser. Therefore, you can check if the
 * NewsVo.articleText is not null and avoid recalling this parser.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class NewsParser extends AsyncTask<Void, Void, Void>{

    private static final String TAG = NewsParser.class.getSimpleName();

    private NewsVo mNewsVo;
    private NewsReady mCallback;

    public NewsParser(NewsVo mNewsVo) {
        this.mNewsVo = mNewsVo;
    }

    public void setCallback(NewsReady n){
        mCallback = n;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Document doc = Jsoup.connect(mNewsVo.articleUrl)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .timeout(5000)
                    .get();

            // Convert all links to absolute path
            Elements select = doc.select("a");
            for (Element e : select){
                // baseUri will be used by absUrl
                String absUrl = e.absUrl("href");
                e.attr("href", absUrl);
            }

            // Image caption
            Elements captionElements = doc.select(".newsImageContainer img");
            Element imageCaption = captionElements.first();

            if(imageCaption != null) {
                mNewsVo.imageCaption = imageCaption.attr("alt");
            }
            else {
                mNewsVo.imageCaption = "";
            }

            Elements classText = doc.select("div[class=texto]");

            if(classText.isEmpty()) {
                mNewsVo.articleText = null;
            }else{
                mNewsVo.articleText = classText.html();
            }

        } catch (IOException e) {
            if(DebugUtils.ERROR) Log.e(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mCallback != null)
            mCallback.OnNewsReady(mNewsVo);
    }

    public interface NewsReady{
        void OnNewsReady(NewsVo newsArticle);
    }
}
