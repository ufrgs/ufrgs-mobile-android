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
package com.cpd.network.parsers;

import android.content.Context;
import android.os.AsyncTask;

import com.cpd.exceptions.ConnectionErrorExecption;
import com.cpd.exceptions.InvalidLoginInfoException;
import com.cpd.exceptions.OverloadedSystemException;
import com.cpd.network.parsers.connectors.LibraryConnector;
import com.cpd.vos.LibraryUserVo;

/**
 * Retrieves library information.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryParser {

    /**
     * Get user information, as well as user books list.
     */
    public class LibraryUserInfoParser extends AsyncTask<Void, Void, Void>{

        private Context mContext;
        private LibraryUserReady mCallback;

        private LibraryUserVo mUser;

        public LibraryUserInfoParser(Context context, LibraryUserReady callback) {
            this.mContext = context;
            this.mCallback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LibraryConnector connector = new LibraryConnector(mContext);

            // Get user and books info
            try {
                mUser = connector.getUserInfo();
                connector.getBooksList(mUser);

                if(mUser.loans != mUser.bookList.size()){
                    throw new ConnectionErrorExecption();
                }
            } catch (InvalidLoginInfoException | ConnectionErrorExecption | OverloadedSystemException e) {
                // User login error
                mUser = null;
                if(mCallback != null){
                    mCallback.onLibraryUserError(e);
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCallback != null && mUser != null)
                mCallback.onLibraryUserReady(mUser);
        }
    }

    /**
     * Renew all books for a user. It DOES NOT update the given LibraryUserVo.
     */
    public class LibraryRenew extends AsyncTask<Void, Void, Void>{

        Context mContext;
        LibraryUserVo mUser;
        LibraryRenewReady mCallback;
        String response;

        public LibraryRenew(Context mContext, LibraryUserVo mUser, LibraryRenewReady callback) {
            this.mContext = mContext;
            this.mUser = mUser;
            this.mCallback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                LibraryConnector connector = new LibraryConnector(mContext);
                response = connector.renewAll(mUser);
            } catch (ConnectionErrorExecption connectionErrorExecption) {
                if(mCallback != null){
                    mCallback.onLibraryRenewError(connectionErrorExecption);
                    mCallback = null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCallback != null)
                mCallback.onLibraryRenewReady(response);
        }
    }



    public interface LibraryUserReady {
        void onLibraryUserReady(LibraryUserVo user);
        void onLibraryUserError(Exception exception);
    }

    public interface LibraryRenewReady {
        void onLibraryRenewReady(String response);
        void onLibraryRenewError(Exception exception);
    }

}
