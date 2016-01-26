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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpd.network.NewsLoader;
import com.cpd.network.parsers.NewsParser;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;
import com.cpd.utils.ConnectionUtils;
import com.cpd.utils.DebugUtils;
import com.cpd.vos.NewsVo;
import com.squareup.picasso.Picasso;

/**
 * Shows one full article.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class NewsOpenActivity extends AppCompatActivity{

	private static final String TAG = NewsOpenActivity.class.getSimpleName();

	private static int mConnectionTries = 0;

	private NewsVo mNewsVo;
    private NewsLoader mLoader;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_open);

		if(getResources().getBoolean(R.bool.portrait_only)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
		else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		}

		Intent intent = getIntent();
		mNewsVo = (NewsVo) intent.getSerializableExtra(AppTags.NEWS_VO);

		final TextView articleTextView = (TextView) findViewById(R.id.news_activity_text);

        mLoader = new NewsLoader(new NewsParser.NewsReady() {
			@Override
			public void OnNewsReady(NewsVo newsArticle) {
				mConnectionTries++;
				/* Avoid infinite loop */
				if (mConnectionTries <= ConnectionUtils.MAX_NETWORK_TENTATIVES) {
					if (newsArticle.articleText == null) {
						mLoader.run(mNewsVo, true);
					} else {
						// Interpret HTML tags
						mConnectionTries = 0;
						articleTextView.setText(Html.fromHtml(newsArticle.articleText));
						articleTextView.setMovementMethod(LinkMovementMethod.getInstance());
						if (DebugUtils.DEBUG)
							Log.d(TAG, "Opened article: " + newsArticle.title);
					}
				}
				else {
					if(DebugUtils.ERROR) Log.e(TAG, "Not connecting to news!");
				}
			}
		});
        mLoader.run(mNewsVo, true);

        // The RssParser already gives the title and image. Therefore, we can load this content
        // while the NewsParser is running.
		Toolbar toolbar = (Toolbar) findViewById(R.id.news_open_toolbar);
		setSupportActionBar(toolbar);

		ImageView imageView = (ImageView) findViewById(R.id.news_activity_picture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageActivity();
            }
        });
		Picasso.with(this).load(mNewsVo.imgLargeUrl).into(imageView);

		TextView titleView = (TextView) findViewById(R.id.news_activity_title);
		titleView.setText(mNewsVo.title);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.news_activity_menu, menu);

		MenuItem item = menu.findItem(R.id.action_share);

		ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareActionProvider.setShareIntent(getDefaultShareIntent());
 
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
//	        case R.id.action_exit:
//	        	finish();
//	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    private void openImageActivity(){
        Intent intent = new Intent(this, NewsOpenImageActivity.class);
        intent.putExtra(AppTags.NEWS_VO, mNewsVo);
        startActivity(intent);
    }

    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + mNewsVo.title);
        intent.putExtra(Intent.EXTRA_TEXT, mNewsVo.articleUrl);
        return intent;
    }

	

}
