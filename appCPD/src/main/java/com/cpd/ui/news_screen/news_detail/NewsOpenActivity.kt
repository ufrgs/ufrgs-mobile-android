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
package com.cpd.ui.news_screen.news_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.view.MenuItemCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.appcompat.widget.Toolbar
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide
import com.cpd.network.models.News
import com.cpd.ufrgsmobile.R
import com.cpd.ui.news_screen.news_image.NewsOpenImageActivity
import com.cpd.utils.AppTags
import com.cpd.utils.ScreenSizeUtils
import com.cpd.utils.TrackerUtils
import com.example.htmlparsertest.HtmlParser
import kotlinx.android.synthetic.main.activity_news_open.*
import kotlinx.android.synthetic.main.activity_tickets.view.*
import kotlinx.android.synthetic.main.restaurant_card_item.view.*
import java.util.*

/**
 * Shows one full article.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class NewsOpenActivity : AppCompatActivity() {

    private var mNewsVo: News? = null

    private val shareActionProvider = ShareActionProvider(this)

    private val defaultShareIntent: Intent
        get() {
            val intent = Intent(Intent.ACTION_SEND)

            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + mNewsVo!!.titulo)

            val url = if (mNewsVo!!.imageThumb != null) {
                mNewsVo!!.imageThumb.split("/image_thumb").dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } else if (mNewsVo!!.imageLarge != null) {
                mNewsVo!!.imageLarge.split("/image_large").dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } else {
                mNewsVo!!.image.url.split("/@@images").dropLast(1)[0]
            }

            intent.putExtra(Intent.EXTRA_TEXT, url)

            return intent
        }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_open)

        requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER
        }

        mNewsVo = intent.getSerializableExtra(AppTags.NEWS_VO) as News

        TrackerUtils.clickNewsArticle(mNewsVo!!.titulo)

        configureButtons()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            news_activity_picture.transitionName = R.string.transition_name_news_image.toString()
            news_activity_return_button.transitionName = "abc"
        }

        if (mNewsVo!!.image != null) {
            Glide.with(this).load(mNewsVo!!.image!!.url).into(news_activity_picture)
            val imagemVo = mNewsVo!!.imagem as LinkedHashMap<String, String>

            news_activity_picture.contentDescription = imagemVo["legenda"]

        } else {
            val layoutParams = news_activity_picture.layoutParams
            layoutParams.height = resources.getDimension(R.dimen.news_open_buttons_view_height).toInt()

            news_activity_picture.layoutParams = layoutParams
            news_activity_picture.setBackgroundColor(Color.TRANSPARENT)

            news_activity_zoom_button.visibility = View.GONE

            news_activity_return_button.setBackgroundResource(R.drawable.ic_back_black)
            news_activity_share_button.setBackgroundResource(R.drawable.ic_share_black)
        }

        news_activity_picture.visibility = View.VISIBLE

        // completa os internal-links (Galeria de imagens)
        val htmlStr = HtmlParser.completeRelativeLinks(mNewsVo!!.texto)

        news_activity_date.text = mNewsVo!!.data
        news_activity_title.text = mNewsVo!!.titulo
        news_activity_text.text = Html.fromHtml(htmlStr).trim()
        news_activity_text.movementMethod = LinkMovementMethod.getInstance()

        if (ScreenSizeUtils.isLarge(this)) {
            configureLargeSize()
        }

    }

    private fun openImageActivity() {
        TrackerUtils.openArticleImage(mNewsVo!!.titulo)
        val intent = Intent(this, NewsOpenImageActivity::class.java)
        intent.putExtra(AppTags.NEWS_VO, mNewsVo)
        startActivity(intent)
    }

    private fun configureButtons() {
        // set buttons actions
        news_activity_return_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        news_activity_zoom_button.setOnClickListener {
            openImageActivity()
        }
        news_activity_share_button.setOnClickListener {
            startActivity(defaultShareIntent)

            val shareTarget = intent.component!!.packageName
            TrackerUtils.shareNewsArticle(mNewsVo!!.titulo, shareTarget)
        }

    }

    private fun configureLargeSize() {
        val multiplier = 1.5

        val imageParams = news_activity_picture.layoutParams

        imageParams.height = (resources.getDimension(R.dimen.news_open_image_height).toInt() * multiplier).toInt()

        news_activity_picture.layoutParams = imageParams
        news_activity_title.textSize = (resources.getDimension(R.dimen.news_open_title_font_size) * multiplier).toFloat()
        news_activity_date.textSize = (resources.getDimension(R.dimen.news_open_date_font_size) * multiplier).toFloat()
        news_activity_text.textSize = (resources.getDimension(R.dimen.news_open_content_font_size) * multiplier).toFloat()
    }

    companion object {
        private val TAG = NewsOpenActivity::class.java.simpleName
        private val mConnectionTries = 0
    }


}
