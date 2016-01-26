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
package com.cpd.network;

import android.content.Context;
import android.util.Log;

import com.cpd.network.parsers.LibraryParser;
import com.cpd.utils.DebugUtils;
import com.cpd.vos.LibraryUserVo;

import java.util.concurrent.ExecutionException;

/**
 * Handles library operations.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryLoader {

    private static final String TAG = LibraryLoader.class.getSimpleName();

    private Context mContext;

    public LibraryLoader(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Loads library user and its books.
     *
     * @param callback Thread callback.
     * @param asynchronous If the thread will run asynchronously or not.
     */
    public void loadUser(LibraryParser.LibraryUserReady callback, boolean asynchronous){
        LibraryParser.LibraryUserInfoParser userParser = new LibraryParser().
                new LibraryUserInfoParser(mContext, callback);

        if(asynchronous){
            userParser.execute();
        }
        else {
            try{
                userParser.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                if(DebugUtils.ERROR) Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * Renew all books. Call loadUser to get user.
     *
     * @param callback Thread callback.
     * @param asynchronous If the thread will run asynchronously or not.
     */
    public void renewBooks(final LibraryParser.LibraryRenewReady callback, final boolean asynchronous){
        loadUser(new LibraryParser.LibraryUserReady() {
            @Override
            public void onLibraryUserReady(LibraryUserVo user) {
                // With user we can renew.
                LibraryParser.LibraryRenew renewParser = new LibraryParser().new LibraryRenew(mContext, user, callback);

                if(asynchronous){
                    renewParser.execute();
                }
                else {
                    try{
                        renewParser.execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        if(DebugUtils.ERROR) Log.e(TAG, e.toString());
                    }
                }
            }

            @Override
            public void onLibraryUserError(Exception exception) {

            }
        }, asynchronous);

    }
}
