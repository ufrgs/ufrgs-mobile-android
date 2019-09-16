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
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryLoan
import com.bumptech.glide.Glide
import com.cpd.ufrgsmobile.R
import kotlinx.android.synthetic.main.book_list_item.view.*

/**
 * Library books adapter.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class LibraryAdapter(private val mContext: Context) : RecyclerView.Adapter<LibraryAdapter.LibraryBooksViewHolder>() {

    private val mList = arrayListOf<UfrgsLibraryLoan>()

    fun updateBookList(list: List<UfrgsLibraryLoan>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LibraryBooksViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.book_list_item, viewGroup, false)
        return LibraryBooksViewHolder(itemView, mContext)
    }

    override fun onBindViewHolder(viewHolder: LibraryBooksViewHolder, pos: Int) {
        val book = mList[pos]
        viewHolder.bindView(book)
    }

    override fun getItemCount(): Int = mList.size

    class LibraryBooksViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindView(book : UfrgsLibraryLoan){


            itemView.book_title.text = book.title
            itemView.book_author.text = book.author
            itemView.book_year.text = book.year
            itemView.book_date.text = book.returnDate

            if (book.isbnIssn != null) {
                val isbn = book.isbnIssn.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val url = "http://images.amazon.com/images/P/$isbn.01.LZZZZ.jpg"
                Glide.with(context).load(url).into(itemView.book_image)
            }
        }
    }

}


