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
package com.cpd.adapters

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.text.Html
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cpd.network.models.News
import com.cpd.ufrgsmobile.R
import kotlinx.android.synthetic.main.activity_tickets.view.*
import kotlinx.android.synthetic.main.news_list_item.view.*
import kotlinx.android.synthetic.main.news_list_item_highlight.view.*
import kotlinx.android.synthetic.main.news_list_item_highlight_rounded.view.*
import java.util.*
import android.text.style.BackgroundColorSpan
import android.text.SpannableString
import androidx.core.graphics.ColorUtils
import androidx.annotation.ColorInt
import com.cpd.extensions.PaddingBackgroundColorSpan
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.cpd.network.models.NewsImage
import kotlinx.android.synthetic.main.news_list_item_card.view.*
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
import com.cpd.utils.ScreenSizeUtils


/**
 * News list adapter
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class NewsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val TYPE_HIGHLIGHT_ROUNDED  = 0
    private val TYPE_NORMAL = 1

    private var mNewsList = arrayListOf<News>()
    private var mContext: Context? = null

    constructor(mContext: Context) {
        this.mContext = mContext
        this.mNewsList = ArrayList()
    }

    constructor(context: Context, list: List<News>) {
        mContext = context
        mNewsList.addAll(list)
    }

    fun updateList(newsList: List<News>) {
        mNewsList.clear()
        mNewsList.addAll(newsList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HIGHLIGHT_ROUNDED
        }
        return TYPE_NORMAL
    }

    fun getItemAtPosition(pos: Int): News? {
        return if (mNewsList.size >= pos)
            mNewsList[pos]
        else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_HIGHLIGHT_ROUNDED) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list_item_highlight_rounded, parent, false)
            return NewsHighlightRoundedViewHolder(view, mContext!!)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false)
            return NewsViewHolder(view, mContext!!)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val news = mNewsList[position]

        if (holder is NewsHighlightRoundedViewHolder) {
            holder.bindView(news)
        } else {
            (holder as NewsViewHolder).bindView(news)
        }

    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    class NewsViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindView(news : News) {
            itemView.news_list_item_title.text = news.titulo

            val params = itemView.news_list_item_layout.layoutParams

            if (news.image == null) {
                params.height = context.resources.getDimension(R.dimen.news_list_item_no_image_height).toInt()

                itemView.news_list_item_layout.layoutParams = params
                itemView.news_list_item_thumb_image.visibility = View.GONE
                itemView.news_list_item_card_layout.visibility = View.GONE
            } else {
                params.height = context.resources.getDimension(R.dimen.news_list_item_w_image_height).toInt()

                itemView.news_list_item_layout.layoutParams = params
                itemView.news_list_item_thumb_image.visibility = View.VISIBLE
                itemView.news_list_item_card_layout.visibility = View.VISIBLE
                Glide.with(context).load(news.image!!.url).into(itemView.news_list_item_thumb_image)
            }

            // on a large screen device ...
            if (ScreenSizeUtils.isLarge(context)) {
                configureLargeSize()
            }

        }

        private fun configureLargeSize() {
            val multiplier = 1.5
            val baseParams = itemView.news_list_item_layout.layoutParams
            val cardParams = itemView.news_list_item_card_layout.layoutParams
            val textParams = itemView.news_list_item_title.layoutParams

            baseParams.height = (context.resources.getDimension(R.dimen.news_list_item_w_image_height).toInt() * multiplier).toInt()

            cardParams.height = (context.resources.getDimension(R.dimen.news_list_item_card_height).toInt() * multiplier).toInt()
            cardParams.width = (context.resources.getDimension(R.dimen.news_list_item_card_width).toInt() * multiplier).toInt()


            itemView.news_list_item_layout.layoutParams = baseParams
            itemView.news_list_item_card_layout.layoutParams = cardParams
            itemView.news_list_item_title.textSize = (context.resources.getDimension(R.dimen.news_list_item_font_size) * multiplier).toFloat()
        }
    }

    class NewsHighlightRoundedViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindView(news : News) {

            // on a large screen device ...
            if (ScreenSizeUtils.isLarge(context)) {
                configureLargeSize()
            }

            val image = news.image

            if (image != null) {
                configureHighlightedTitle(news.titulo)
            } else {
                itemView.news_list_item_highlight_rounded_title.text = news.titulo
            }

            configureSubtitle(news.chamada)
            configureImage(image)
            configureDegrade(image != null)

        }

        private fun configureHighlightedTitle(text: String) {
            @ColorInt
            val padding = 16
            val alphaColor = ColorUtils.setAlphaComponent(Color.BLACK, 140)
            val str = SpannableString(text)
            val highlight = PaddingBackgroundColorSpan(alphaColor, padding)

            str.setSpan(highlight,0, text.length, 0)
            itemView.news_list_item_highlight_rounded_title.setShadowLayer(padding.toFloat() /* radius */, 0.toFloat(), 0.toFloat(), 0 /* transparent */)
            itemView.news_list_item_highlight_rounded_title.setPadding(padding, padding, padding, padding)

            itemView.news_list_item_highlight_rounded_title.text = str

            // set margin left of subtitle
            val params = itemView.news_list_item_highlight_rounded_content.layoutParams as LinearLayout.LayoutParams
            params.setMargins(20,0,0,0)

            itemView.news_list_item_highlight_rounded_content.layoutParams = params
        }

        private fun configureSubtitle(subtitle: String) {

            itemView.news_list_item_highlight_rounded_content.text = subtitle

            if (subtitle == "") {
                itemView.news_list_item_highlight_rounded_content.visibility = View.GONE
            } else {
                itemView.news_list_item_highlight_rounded_content.visibility = View.VISIBLE
            }
        }

        private fun configureImage(image: NewsImage?) {

            if (image != null) {
                itemView.news_list_item_highlight_rounded_thumb_image.visibility = View.VISIBLE
                Glide.with(context).load(image.url).into(itemView.news_list_item_highlight_rounded_thumb_image)
            } else {
                itemView.news_list_item_highlight_rounded_thumb_image.setBackgroundColor(Color.parseColor("#E34540"))
                itemView.news_list_item_highlight_rounded_thumb_image.setImageResource(android.R.color.transparent)
            }

        }

        private fun configureDegrade(hasImage: Boolean) {
            if (hasImage) {
                Glide.with(context).load(R.drawable.black_degrade).into(itemView.news_list_item_highlight_rounded_degrade)

                itemView.news_list_item_highlight_rounded_degrade.alpha = 0.7f
                itemView.news_list_item_highlight_rounded_degrade.visibility = View.VISIBLE
            } else {
                itemView.news_list_item_highlight_rounded_degrade.visibility = View.GONE
            }
        }

        private fun configureLargeSize() {
            val multiplier = 1.75
            val baseParams = itemView.news_list_item_highlight_rounded_layout.layoutParams

            baseParams.height = (context.resources.getDimension(R.dimen.news_list_item_highlight_rounded_height).toInt() * multiplier).toInt()

            println(baseParams.height)

            itemView.news_list_item_highlight_rounded_layout.layoutParams = baseParams
            itemView.news_list_item_highlight_rounded_title.textSize = (context.resources.getDimension(R.dimen.news_list_item_highlight_rounded_title_font_size) * multiplier).toFloat()
            itemView.news_list_item_highlight_rounded_content.textSize = (context.resources.getDimension(R.dimen.news_list_item_highlight_rounded_content_font_size) * 1.5).toFloat()
        }
    }

}