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
package com.cpd.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpd.exceptions.OverloadedSystemException;
import com.cpd.network.LibraryLoader;
import com.cpd.network.parsers.LibraryParser;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;
import com.cpd.utils.DebugUtils;
import com.cpd.vos.LibraryUserVo;


/**
 * Login screen for library users.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryLoginFragment extends Fragment {

    public static final String TAG = LibraryLoginFragment.class.getSimpleName();

    private LibraryLoader mLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_login, container, false);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = pref.getString(getString(R.string.pref_library_username), null);
        String password = pref.getString(getString(R.string.pref_library_password), null);

        loginAction(username, password);

        return view;
    }

    private void loginAction(String userLogin, String userPassword) {

        if(userLogin == null || userPassword == null){
            openErrorFragment(null);
            return;
        }

        //TODO: Check how SABi works with login to other types of input (as passport) to handle this user in a more effective way.
        if( userLogin.length() < 8 && userLogin.length() > 0 ){
            String tempuser = "";
            int i = 8 - userLogin.length();
            while(i > 0) {
                tempuser = tempuser + "0";
                i--;
            }
            tempuser = tempuser + userLogin;
            userLogin = tempuser;
        }

        SharedPreferences settings = getActivity().getSharedPreferences(AppTags.LIBRARY_LOGIN_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(AppTags.LIBRARY_PARSER_USER, userLogin);
        editor.putString(AppTags.LIBRARY_PARSER_PASSWORD, userPassword);
        editor.putBoolean(AppTags.LIBRARY_HAS_USER, true);
        editor.commit();

        if(DebugUtils.DEBUG) Log.d(TAG, "Login: " + userLogin + " Password: " + userPassword);

        mLoader = new LibraryLoader(getContext());
        mLoader.loadUser(new LibraryParser.LibraryUserReady() {
            @Override
            public void onLibraryUserReady(LibraryUserVo user) {
                openLibraryFragment(user);
                return;
            }

            @Override
            public void onLibraryUserError(Exception exception) {
                openErrorFragment(exception);
            }
        }, true);

    }

    private void openLibraryFragment(LibraryUserVo user) {
        // Change fragment
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        LibraryFragment fragment = new LibraryFragment();
        fragment.setUserInfo(user);
        trans.replace(R.id.root_biblio_layout, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
    }

    private void openErrorFragment(Exception exception) {
        LibraryFragmentError fragmentError = new LibraryFragmentError();
        if (exception != null){
            if(exception instanceof OverloadedSystemException){
                fragmentError.setError(LibraryFragmentError.ERROR_OVERLOAD_SYSTEM);
            }
        }

        // Change fragment
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_biblio_layout, fragmentError);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
    }

}
