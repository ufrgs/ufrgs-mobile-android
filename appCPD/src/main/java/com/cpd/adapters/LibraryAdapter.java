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
package com.cpd.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpd.ufrgsmobile.R;
import com.cpd.viewHolders.LibraryBooksViewHolder;
import com.cpd.vos.LibraryBookVo;
import com.cpd.vos.LibraryUserVo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Library books adapter.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryAdapter extends RecyclerView.Adapter {

    Context mContext;
    LibraryUserVo mLibraryUser;

    public LibraryAdapter(Context context, LibraryUserVo user) {
        notifyDataSetChanged();
        mContext = context;
        mLibraryUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_list_item, viewGroup, false);

        return new LibraryBooksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        LibraryBookVo book = mLibraryUser.bookList.get(pos);

        LibraryBooksViewHolder libraryBookHolder = (LibraryBooksViewHolder) viewHolder;

        libraryBookHolder.name.setText(book.name);
        libraryBookHolder.author.setText(book.author);

        if(book.returnDate != null) {
            libraryBookHolder.date.setVisibility(View.VISIBLE);
            libraryBookHolder.date.setText(formatDate(book.returnDate));
        }
        else{
            libraryBookHolder.date.setVisibility(View.GONE);
        }

        if(book.libraryCode != null) {
            libraryBookHolder.libCode.setVisibility(View.VISIBLE);
            libraryBookHolder.libCode.setText(formatLibraryCode(book.libraryCode));
        }
        else{
            libraryBookHolder.libCode.setVisibility(View.GONE);
        }
    }

    private String formatLibraryCode(String libraryCode) {
        return mContext.getString(R.string.library) + " " + libraryCode;
    }

    private String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat(mContext.getString(R.string.date_format));
        return df.format(date);
    }

    @Override
    public int getItemCount() {
        if(mLibraryUser != null && mLibraryUser.bookList != null) {
            return mLibraryUser.bookList.size();
        }else{
            return 0;
        }
    }

    public void updateUser(LibraryUserVo user){
        mLibraryUser = user;
        notifyDataSetChanged();
    }

    private void emptyList() {

        mLibraryUser.bookList = new ArrayList<>();

        LibraryBookVo emptyBook = new LibraryBookVo();

        emptyBook.name = mContext.getString(R.string.no_books);
        emptyBook.author = mContext.getString(R.string.no_loan_books_found);

        mLibraryUser.bookList.add(emptyBook);

    }

}

