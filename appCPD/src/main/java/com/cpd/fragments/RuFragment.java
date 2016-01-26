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
package com.cpd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpd.adapters.RuAdapter;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;

/**
 * RU Fragment.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class RuFragment extends Fragment {

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For larger devices (like tablets), it creates more columns.
        int columnsNumber = getActivity().getResources().getInteger(R.integer.restaurant_columns);
        if(mStaggeredGridLayoutManager == null) {
            mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(columnsNumber, StaggeredGridLayoutManager.VERTICAL);
        }
        else{
            mStaggeredGridLayoutManager.setSpanCount(columnsNumber);
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.restaurant_fragment_recycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        final RuAdapter ruAdapter = new RuAdapter(getActivity());
        recyclerView.setAdapter(ruAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.restaurant_fragment_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(AppTags.UFRGS_MOBILE, "RU Refresh");
                ruAdapter.updateAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

	    return view;
	  }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            // Texto da toolbar
            TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
            toolbarTitle.setText(R.string.restaurant);
        }
    }

}
