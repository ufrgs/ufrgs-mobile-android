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
package com.cpd.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cpd.activities.NewsOpenActivity;
import com.cpd.network.RssLoader;
import com.cpd.network.parsers.RssParser;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;
import com.cpd.utils.ConnectionUtils;
import com.cpd.utils.DebugUtils;
import com.cpd.viewHolders.NewsViewHolder;
import com.cpd.vos.NewsVo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * News list adapter
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();

    private static int mConnectionTries = 0;

	private List<NewsVo> mNewsList;
    private Context mContext;

    private RssLoader mRssLoader;

    public NewsAdapter(Context context) {
        mContext = context;

        mRssLoader = new RssLoader(new RssParser.RssResultReady() {
            @Override
            public void onRssResultReady(List<NewsVo> newsList) {
                mConnectionTries++;
                if (mConnectionTries <= ConnectionUtils.MAX_NETWORK_TENTATIVES) {
                    if (newsList == null || newsList.isEmpty()) {
                        if (DebugUtils.DEBUG) Log.d(TAG, "No RSS, calling parser again");
                        mRssLoader.run(true);
                        return;
                    } else {
                        mConnectionTries = 0;
                        removeProgressBar();
                    }

                    // When the RSS results are ready
                    mNewsList = newsList;
                    notifyDataSetChanged();
                }
                else{
                    if(DebugUtils.ERROR) Log.e(TAG, "Error connecting to RSS!");
                }
            }
        });
        mRssLoader.run(true);

    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);

        final NewsViewHolder viewHolder = new NewsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            /**
             * Click on item opens the news.
             */
            @Override
            public void onClick(View view) {
                int clickPos = viewHolder.getAdapterPosition();
                if(DebugUtils.DEBUG) Log.d(TAG, "Click position: " + clickPos);
                openArticle(clickPos);
            }
        });
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        NewsVo news = mNewsList.get(position);

        holder.postTitleLabel.setText(news.title);
        holder.postDateLabel.setText(formatDate(news.date));
        final ImageView imageView = holder.postThumb;

        if(DebugUtils.IMAGE_DEBUG) Picasso.with(mContext).setIndicatorsEnabled(true);

        Picasso.with(mContext).load(news.imgThumbUrl).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                imageView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mNewsList != null)
            return mNewsList.size();
        else
            return 0;
    }


    /**
     * Convert the date format.
     * @param input Date in the old format. In our case, a date in the format
     *              yyyy-MM-dd'T'HH:mm:ss'Z' (see SimpleDateFormat).
     * @return The string with the desired format.
     */
    public String formatDate(String input){
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat  newFormat = new SimpleDateFormat("dd/MM/yy - HH:mm");
        String formatedDate = "";

        try {
            Date date = format.parse(input);
            formatedDate = newFormat.format(date);
        } catch (ParseException e) {
            if(DebugUtils.ERROR) Log.e(TAG, "Unable to convert date from news");
            e.printStackTrace();
        }

        return formatedDate;
    }

    public void updateList() {
        mRssLoader.run(true);
    }

    public void removeProgressBar(){
        View progressBar = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.news_fragment_progress_bar);
        if(progressBar != null) progressBar.setVisibility(View.GONE);
    }

    /**
     * Opens the news article. Tablet compatbility: if there is a view to draw the article, show it. If not, open a new
     activity to show it.
     * @param pos Adapter position of the article to be shown.
     */
    private void openArticle(int pos){

        //View newsView = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.noticia_tablet);

        //if(newsView ==  null) { // Not a tablet
            loadArticleActivity(mNewsList.get(pos));
        //}
        //else{ // Tablet
        //    loadArticleView(newsView, mNewsList.get(pos));
        //}
    }

    private void loadArticleActivity(NewsVo newsVo) {
        Intent i = new Intent(mContext, NewsOpenActivity.class);
        i.putExtra(AppTags.NEWS_VO, newsVo);
        mContext.startActivity(i);
    }

    private void loadArticleView(View newsView, final NewsVo newsVo) {
        //TODO: Reopen on tablet
        loadArticleActivity(newsVo);
    }

}
