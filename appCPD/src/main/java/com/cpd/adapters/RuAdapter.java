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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpd.network.RuLoader;
import com.cpd.network.parsers.RuParser;
import com.cpd.utils.AppTags;
import com.cpd.ufrgsmobile.R;
import com.cpd.viewHolders.RuViewHolder;
import com.cpd.vos.RuVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * RU Adapter. Retrieve data from model to show.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class RuAdapter extends RecyclerView.Adapter {

    private static final String TAG = "RuAdapter";

    private List<RuVo> mRestaurantList;
    private Context mContext;

    private RuLoader mLoader;

    public RuAdapter(Context context) {
        mContext = context;
        mLoader = new RuLoader(new RuParser.RuReady() {
            @Override
            public void onRuReady(List<RuVo> restaurantList) {
                mRestaurantList = filterRestaurants(restaurantList);
                reloadAdapter();
            }
        });
        mLoader.run(true);
    }

    private List<RuVo> filterRestaurants(List<RuVo> restaurantList) {
        if(restaurantList == null)
            return null;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Set<String> resSet = pref.getStringSet(mContext.getString(R.string.pref_restaurant_list), null);
        if(resSet == null){
            Log.w(TAG, "filterRestaurants: Empty restaurant preferences" );
            return restaurantList;
        }

        List<RuVo> restaurantFiltered = new ArrayList<>();
        for(String id: resSet){
            for(RuVo ru: restaurantList){
                if(ru.id == Integer.valueOf(id)){
                    restaurantFiltered.add(ru);
                }
            }
        }
        // Set is unordered.
        Collections.sort(restaurantFiltered);
        return restaurantFiltered;
    }

    private void reloadAdapter() {
        checkForErrors();
        // Called from a another thread. notifyDataSetChanged() only works on the UI thread.
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private void checkForErrors() {
        if(mRestaurantList == null) {
            createPageNotFoundError();
            return;
        }
        if(mRestaurantList.isEmpty()) {
            if(isPreferenceListEmpty()){
                createEmptySettingsError();
            }
            else {
                createNoMenuError();
            }
        }
    }


    /**
     * Called to re-download.
     */
    public void updateAdapter(){
        mLoader.run(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_card_item, viewGroup, false);

        return new RuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        RuVo ru = mRestaurantList.get(pos);

        RuViewHolder ruHolder = (RuViewHolder) viewHolder;
        Picasso.with(mContext).load(getImageRu(ru)).into(ruHolder.image);

        // Se o card for reciclado, ele pode ter a config de um card de erro
        ruHolder.image.setVisibility(View.VISIBLE);
        ruHolder.place.setTextColor(Color.WHITE);

        if(ru.error) {
            Log.e(AppTags.UFRGS_MOBILE, "Ru erro");
            ruHolder.image.setVisibility(View.INVISIBLE);
            ruHolder.place.setTextColor(Color.BLACK);
        }

        ruHolder.place.setText(ru.place);
        ruHolder.menu.setText(ru.menu);

    }

    @Override
    public int getItemCount() {
        if(mRestaurantList != null) {
            return mRestaurantList.size();
        }else{
            return 0;
        }
    }

    private int getImageRu(RuVo ru) {
        switch (ru.id) {
            case 1:
                return R.drawable.ru1;
            case 2:
                return R.drawable.ru2;
            case 3:
                return R.drawable.ru3;
            case 4:
                return R.drawable.ru4;
            case 5:
                return R.drawable.ru5;
            case 6:
                return R.drawable.ru6;
            default:
                return R.drawable.ru1; //TODO: Error image
        }
    }

    /**
     * Creates a list with one card to show the page was not found.
     */
    private void createPageNotFoundError(){
        RuVo ru = new RuVo();

        ru.place = mContext.getString(R.string.error);
        ru.menu = mContext.getString(R.string.page_not_found);
        ru.error = true;

        List<RuVo> ruList = new ArrayList<>();
        ruList.add(ru);

        mRestaurantList = ruList;
    }

    /**
     * Creates a list with one card to show there is no menu
     */

    private void createNoMenuError(){
        RuVo ru = new RuVo();

        ru.place = mContext.getString(R.string.menu_unavailable);
        ru.menu = mContext.getString(R.string.menu_not_ready_yet_message);
        ru.error = true;

        List<RuVo> ruList = new ArrayList<>();
        ruList.add(ru);

        mRestaurantList = ruList;
    }


    private void createEmptySettingsError() {
        RuVo ru = new RuVo();

        ru.place = mContext.getString(R.string.no_restaurant_to_show);
        ru.menu = mContext.getString(R.string.no_restaurant_to_show_message);
        ru.error = true;

        List<RuVo> ruList = new ArrayList<>();
        ruList.add(ru);

        mRestaurantList = ruList;
    }

    public boolean isPreferenceListEmpty() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Set<String> resSet = pref.getStringSet(mContext.getString(R.string.pref_restaurant_list), null);
        if(resSet == null){
            Log.w(TAG, "filterRestaurants: Empty restaurant preferences" );
            return true;
        }
        if(resSet.isEmpty()){
            return true;
        }
        return false;
    }
}

