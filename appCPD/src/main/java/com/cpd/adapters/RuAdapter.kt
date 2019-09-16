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

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cpd.network.models.Ru
import com.cpd.ufrgsmobile.R
import kotlinx.android.synthetic.main.item_boltim_ru.view.*
import kotlinx.android.synthetic.main.restaurant_card_item.view.*
import android.animation.ValueAnimator
import android.view.View.MeasureSpec
import android.widget.TextView
import com.cpd.utils.ScreenSizeUtils
import kotlinx.android.synthetic.main.fragment_restaurant.view.*
import android.widget.LinearLayout
import com.crashlytics.android.answers.FirebaseAnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics


/**
 * RU Adapter. Retrieve data from model to show.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class RuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val TYPE_RU = 0
    private val TYPE_BOLETIM = 1

    private var mContext: Context? = null
    private val mList = arrayListOf<Ru>()

    var onItemClickCompletion: ((Int)->Unit)? = null

    constructor(mContext: Context) {
        this.mContext = mContext
    }

    constructor(context: Context, list: List<Ru>) {
        mContext = context
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateWholeList(list: List<Ru>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 0) {
            TYPE_RU
        } else {
            TYPE_BOLETIM
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        if (i == TYPE_RU) {
            val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.restaurant_card_item, viewGroup, false)
            val holder = RuViewHolder(itemView, mContext!!)
            holder.setCompletion(this.onItemClickCompletion)

            return holder
        } else {
            val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_boltim_ru, viewGroup, false)
            return BoletimHolder(itemView, mContext!!)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        if (viewHolder is RuViewHolder) {
            val ru = mList[pos - 1]
            viewHolder.bindRu(ru)
        } else {
            (viewHolder as BoletimHolder).bindBoletim()
        }

    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    companion object {
        private val TAG = "RuAdapter"
    }

    class RuViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

        var isExpanded = false
        var ru = Ru()
        var onItemClickCompletion: ((Int)->Unit)? = null

        fun setCompletion(completion: ((Int) -> Unit)?) {
            onItemClickCompletion = completion
        }

        fun bindRu(ru: Ru) {
            this.ru = ru

            fillFields()

            if (ScreenSizeUtils.isLarge(context)) {
                configureLargeSize()
            }

            configureMenuInitialHeight()
        }

        private fun fillFields() {
            Glide.with(context).load(ru.image).into(itemView.restaurant_card_header_picture)

            itemView.restaurant_card_header_picture.visibility = View.VISIBLE
            itemView.restaurant_card_header_ru_name.text = ru.nome
            itemView.restaurant_card_header_title_view.setBackgroundColor(ru.color)
            itemView.restaurant_card_header_ru_number.text = "RU " + ru.number.toString()
            itemView.restaurant_card_content_menu.text = ru.cardapio
            itemView.restaurant_card_layout.setOnClickListener { toggleAnimation() }
        }

        private fun toggleAnimation() {
            isExpanded = !isExpanded
            animateMenu(ru.number)
        }

        fun configureMenuInitialHeight() {
            val params = itemView.restaurant_card_content_menu.layoutParams
            params.height = if (isExpanded) getRealHeight() else 0

            itemView.restaurant_card_content_menu.layoutParams = params
        }

        fun getRealHeight(): Int {
            val widthSpec = View.MeasureSpec.makeMeasureSpec(itemView.restaurant_card_header_layout.measuredWidth, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

            itemView.restaurant_card_content_menu.measure(widthSpec, heightSpec)

            return itemView.restaurant_card_content_menu.measuredHeight
        }

        fun animateMenu(index: Int) {
            if (isExpanded) {
                showMenu(index)
            } else {
                hideMenu()
            }
        }

        fun showMenu(index: Int) {
            val anim = ValueAnimator.ofInt(0, getRealHeight())

            anim.addUpdateListener { valueAnimator ->
                val h = valueAnimator.animatedValue as Int
                val layoutParams = itemView.restaurant_card_content_menu.layoutParams
                layoutParams.height = h

                itemView.restaurant_card_content_menu.setLayoutParams(layoutParams)

                if (onItemClickCompletion != null) {
                    onItemClickCompletion!!(index)
                }
            }

            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            firebaseAnalytics.logEvent("ru_menu_open_event", null)

            anim.duration = 300
            anim.start()
        }

        fun hideMenu() {
            val anim = ValueAnimator.ofInt(getRealHeight(), 0)
            anim.addUpdateListener { valueAnimator ->
                val h = valueAnimator.animatedValue as Int
                val layoutParams = itemView.restaurant_card_content_menu.layoutParams
                layoutParams.height = h
                itemView.restaurant_card_content_menu.setLayoutParams(layoutParams)
            }
            anim.duration = 300
            anim.start()
        }

        private fun configureLargeSize() {
            val multiplier = 1.35
            val imageMultiplier = 1.75
            val imageParams = itemView.restaurant_card_header_picture.layoutParams
            val titleViewParams = itemView.restaurant_card_header_title_view.layoutParams


            imageParams.height = (context.resources.getDimension(R.dimen.ru_card_header_height).toInt() * imageMultiplier).toInt()

            titleViewParams.height = (context.resources.getDimension(R.dimen.ru_card_header_height).toInt() * imageMultiplier).toInt()
            titleViewParams.width = (context.resources.getDimension(R.dimen.ru_card_header_height).toInt() * imageMultiplier).toInt()


            itemView.restaurant_card_header_picture.layoutParams = imageParams
            itemView.restaurant_card_header_title_view.layoutParams = titleViewParams
            itemView.restaurant_card_header_ru_number.textSize = (context.resources.getDimension(R.dimen.ru_number_font_size).toInt() * multiplier).toFloat()
            itemView.restaurant_card_header_ru_name.textSize = (context.resources.getDimension(R.dimen.ru_name_font_size).toInt() * multiplier).toFloat()
            itemView.restaurant_card_content_menu.textSize = (context.resources.getDimension(R.dimen.ru_menu_font_size).toInt() * multiplier).toFloat()

//            itemView.news_list_item_highlight_rounded_layout.layoutParams = baseParams
//            itemView.news_list_item_highlight_rounded_title.textSize = (context.resources.getDimension(R.dimen.news_list_item_highlight_rounded_title_font_size) * multiplier).toFloat()
//            itemView.news_list_item_highlight_rounded_content.textSize = (context.resources.getDimension(R.dimen.news_list_item_highlight_rounded_content_font_size) * 1.5).toFloat()
        }
    }

    class BoletimHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindBoletim() {
            Glide.with(context).load(R.drawable.dal).into(itemView.img_boltim)

            itemView.img_boltim.setOnClickListener {
                val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
                firebaseAnalytics.logEvent(context.getString(R.string.dal_click_firebase_event), null)

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ufrgs.br/laranjanacolher/"))
                context.startActivity(browserIntent)
            }
        }
    }
}

