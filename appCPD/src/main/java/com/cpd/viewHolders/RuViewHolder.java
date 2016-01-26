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
package com.cpd.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpd.ufrgsmobile.R;

/**
 * RU Cards View Holder.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class RuViewHolder extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView place;
    public TextView menu;


    public RuViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.restaurant_card_header_picture);
        place = (TextView) itemView.findViewById(R.id.restaurant_card_header_location_title);
        menu = (TextView) itemView.findViewById(R.id.restaurant_card_content_menu);
    }
}
