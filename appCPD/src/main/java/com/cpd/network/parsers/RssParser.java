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

import com.cpd.vos.NewsVo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the RSS feed.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */

public class RssParser extends AsyncTask<Void, Void, Void> {

    private static final String TAG = RssParser.class.getSimpleName();

    private List<NewsVo> mNewsList;
    private RssResultReady mCallback;

    public void setCallback(RssResultReady r){
        mCallback = r;
    }

    @Override
    protected Void doInBackground(Void... params) {

        mNewsList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("http://www.ufrgs.br/ufrgs/noticias/noticias/RSS/index.xml")
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .timeout(7000)
                    .get();

            Elements items = doc.select("item");

            if ((items.size() == 15) && (items.first().children().size() == 9)){
                for(Element item : items){
                    NewsVo news = new NewsVo();
                    news.title = item.child(0).text();
                    news.articleUrl = item.attr("rdf:about");
                    news.imgLargeUrl = news.articleUrl + "/image_large";
                    news.imgThumbUrl = news.articleUrl + "/image_thumb";
                    news.shortDescription = item.child(2).text();
                    news.author = item.child(5).text();
                    news.date = item.child(7).text();
                    mNewsList.add(news);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Connection error " + e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // Execute callback
        if(mCallback != null)
            mCallback.onRssResultReady(mNewsList);

    }

    public interface RssResultReady{
        void onRssResultReady(List<NewsVo> newsList);
    }
}

