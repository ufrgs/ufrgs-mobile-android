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
package com.cpd.ui.news_screen.news_image

import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cpd.network.models.News
import com.cpd.ufrgsmobile.R
import com.cpd.utils.AppTags
import kotlinx.android.synthetic.main.activity_news_open_image.*
import java.util.*
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher


/**
 * Opens the image to zoom and scroll.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class NewsOpenImageActivity : AppCompatActivity() {

//    @BindView(R.id.news_open_image) internal var mImageView: ImageView? = null
//    @BindView(R.id.news_open_caption) internal var mLegenda: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_open_image)

        requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER
        }

        val newsVo = intent.getSerializableExtra(AppTags.NEWS_VO) as News

        try {
            val imagemVo = newsVo.imagem as LinkedHashMap<String, String>
            news_open_caption.text = imagemVo["legenda"]
        } catch (e: Exception) {
            e.printStackTrace()
            news_open_caption.text = ""
        }

        // if exists url for large image
        if (newsVo.imageLarge != null) {
            // if it fails to load large image, try loading the normal one
            System.out.println("Wil try to load large image!")
            loadImage(newsVo.imageLarge!!) lambda@{
                System.out.println("Failed to load large image!")
                loadImage(newsVo.image.url, null)
            }

        } else {
            loadImage(newsVo.image.url, null)
        }


        // Responsible for zoom and pinch
        val photoView = findViewById(R.id.news_open_image) as PhotoView
        val attacher: PhotoViewAttacher = PhotoViewAttacher(photoView)

    }

    private fun loadImage(url: String, failHandler: (()->Unit)?) {

        news_open_progress_bar.visibility = View.VISIBLE

        // load the image
        Glide.with(this)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        news_open_progress_bar.visibility = View.GONE

                        if (failHandler != null) {

                            if (failHandler != null) {
                                val handler = Handler()
                                handler.post(failHandler)
                            }

                        }

                        return false
                    }


                    override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        news_open_progress_bar.visibility = View.GONE
                        return false
                    }

                })
                .into(news_open_image)
    }


}
