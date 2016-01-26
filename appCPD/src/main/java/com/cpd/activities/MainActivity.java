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
package com.cpd.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cpd.adapters.ViewPagerAdapter;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;

/**
 * App's main activity. Configures the tabs to be loaded with the fragments.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class MainActivity extends AppCompatActivity {

	public static final String TAG = MainActivity.class.getSimpleName();

	private ViewPagerAdapter mPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(getResources().getBoolean(R.bool.portrait_only)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
		else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		}

		configureToolbar();
		configurePagerTabs();

    }

	private void configurePagerTabs() {

		mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
		pager.setAdapter(mPagerAdapter);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
		tabLayout.setupWithViewPager(pager);

		// Set Tab Icons
        for(int i = 0; i < mPagerAdapter.getCount(); i++){
            tabLayout.getTabAt(i).setIcon(mPagerAdapter.getIconAt(i));
        }

	}

	private void configureToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_app_settings:
				Intent i = new Intent(this, MainPreferencesActivity.class);
				startActivityForResult(i, AppTags.UFRGS_OPTIONS);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppTags.UFRGS_OPTIONS){
			mPagerAdapter.notifyDataSetChanged();
		}
	}
}
