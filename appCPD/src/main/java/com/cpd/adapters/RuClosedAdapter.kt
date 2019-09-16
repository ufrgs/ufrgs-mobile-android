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


class RuClosedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var mContext: Context? = null

    constructor(mContext: Context) {
        this.mContext = mContext
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.restaurant_closed_layout, viewGroup, false)
        return RuClosedViewHolder(itemView, mContext!!)

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        (viewHolder as RuClosedViewHolder).bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

    companion object {
        private val TAG = "RuClosedAdapter"
    }

    class RuClosedViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun bind() {}

    }

}

