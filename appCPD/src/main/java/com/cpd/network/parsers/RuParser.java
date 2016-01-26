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
import android.text.TextUtils;
import android.util.Log;

import com.cpd.utils.DebugUtils;
import com.cpd.vos.RuVo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RU parser.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class RuParser extends AsyncTask<Void, Void, Void>{

    private static final String TAG = RuParser.class.getSimpleName();
    private static final String RU_URL = "http://www.ufrgs.br/ufrgs/ru";
    //private static final String RU_URL = "https://web.archive.org/web/20150417002551/http://www.ufrgs.br/ufrgs/ru";

    private RuReady mCallback;

    public void setCallback(RuReady ruReady) {
        mCallback = ruReady;
    }

    /**
     * Fetches the page and build a List<RuVo>. Needs to be run outside the main thread, since
     * it uses the network. The call will receive a list with all the RuVo. If the list is null,
     * we couldn't get the page. If the list is empty. Probably there is no menu yet.
     */
    @Override
    protected Void doInBackground(Void... voids) {
        Document doc;
        try {
            doc = Jsoup.connect(RU_URL)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException e) {
            if(DebugUtils.ERROR) Log.e(TAG, e.getMessage());
            if(mCallback != null)
                mCallback.onRuReady(null);
            return null;
        }

        List<RuVo> restaurantList = new ArrayList<>();
        Elements elements = doc.select(".ru"); // Each RU page block has class="ru"

        for(Element elem : elements){
            RuVo ru = new RuVo();
            String nameId = elem.id();
            String place = elem.select("h3").html().split(" - ")[1]; // Example: Takes "Esef" from "<h3>RU5 - Esef</h3>"
            String menu = TextUtils.join("\n", elem.select("div.area").html().split("<br>")); // Changes <br> to '\n'

            ru.id = getId(nameId);
            ru.nameId = nameId;
            ru.place = place;
            ru.menu = menu;

            restaurantList.add(ru);
        }

        if(restaurantList.isEmpty()){
            if(DebugUtils.ERROR) Log.e(TAG, "No menu on page");
        }

        if(mCallback != null)
            mCallback.onRuReady(restaurantList);

        return null;
    }

    private int getId(String nameId) {
        return Integer.valueOf(nameId.substring(2)); // RU2 - 2
    }

    public interface RuReady{
        void onRuReady(List<RuVo> restaurantList);
    }

}
