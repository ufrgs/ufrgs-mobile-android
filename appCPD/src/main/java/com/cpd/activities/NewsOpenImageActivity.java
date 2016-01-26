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
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;
import com.cpd.vos.NewsVo;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Opens the image to zoom and scroll.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class NewsOpenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_open_image);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }

        Intent intent = getIntent();
        NewsVo newsVo = (NewsVo) intent.getSerializableExtra(AppTags.NEWS_VO);

        ImageView imageView = (ImageView) findViewById(R.id.news_open_image);
        TextView legenda = (TextView) findViewById(R.id.news_open_caption);

        Picasso.with(this).load(newsVo.imgLargeUrl).into(imageView);
        legenda.setText(newsVo.imageCaption);


        // Responsible for zoom and pinch
        PhotoViewAttacher attacher;
        attacher = new PhotoViewAttacher(imageView);

    }

}
