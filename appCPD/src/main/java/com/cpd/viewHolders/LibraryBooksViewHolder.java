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
import android.widget.TextView;

import com.cpd.ufrgsmobile.R;

/**
 * Library RecyclerView holder.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryBooksViewHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView author;
    public TextView date;
    public TextView libCode;

    public LibraryBooksViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.book_list_item_title);
        author = (TextView) itemView.findViewById(R.id.book_list_item_author);
        date = (TextView) itemView.findViewById(R.id.book_list_item_date);
        libCode = (TextView) itemView.findViewById(R.id.book_list_item_library);

    }
}
