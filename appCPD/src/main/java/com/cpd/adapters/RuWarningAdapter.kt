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
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cpd.ufrgsmobile.R
import kotlinx.android.synthetic.main.loading_spinner_layout.view.*
import kotlinx.android.synthetic.main.menu_unavailable_layout.view.*


class RuWarningAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var mContext: Context? = null
    var message: String = ""
    var type: Int = RuWarningAdapter.MENU_UNAVAILABLE

    constructor(mContext: Context, type: Int) {
        this.mContext = mContext
        this.type = type
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        if (type == RuWarningAdapter.LOADING) {
            val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.loading_spinner_layout, viewGroup, false)
            return LoadingViewHolder(itemView, mContext!!)
        } else {
            val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.menu_unavailable_layout, viewGroup, false)
            return MenuUnavailableViewHolder(itemView, mContext!!)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        if (type == RuWarningAdapter.LOADING) {
            (viewHolder as LoadingViewHolder).bind()
        } else {
            (viewHolder as MenuUnavailableViewHolder).bind(this.message)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    companion object {
        private val TAG = "RuWarningAdapter"

        val MENU_UNAVAILABLE = 0
        val LOADING = 1
    }

    class MenuUnavailableViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: String) {
            itemView.menu_unavailable_textView.text = message
        }
    }

    class LoadingViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            itemView.loading_spinner_progress_bar.visibility = View.VISIBLE
        }
    }

}
