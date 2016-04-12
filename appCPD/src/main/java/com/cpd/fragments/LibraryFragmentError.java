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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpd.activities.MainPreferencesActivity;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;

/**
 * @author Alan Wink
 */
public class LibraryFragmentError extends Fragment {

    private static final int ERROR_UNKNOWN = 0;
    public static final int ERROR_OVERLOAD_SYSTEM = 1;

    private Button mLoginButton;
    private Button mSettingsButton;

    private ImageView mErrorImage;
    private TextView mErrorMessage;

    private int mError = ERROR_UNKNOWN;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_error, container, false);

        mLoginButton = (Button) view.findViewById(R.id.library_fragment_error_login_button);
        mSettingsButton = (Button) view.findViewById(R.id.library_fragment_error_settings_button);
        mErrorImage = (ImageView) view.findViewById(R.id.library_fragment_error_owl);
        mErrorMessage = (TextView) view.findViewById(R.id.library_fragment_error_text);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAdded()) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.root_biblio_layout, new LibraryLoginFragment());
                    transaction.commit();
                }
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainPreferencesActivity.class);
                startActivityForResult(i, AppTags.UFRGS_OPTIONS);
            }
        });

        // Set based on error
        switch (mError){
            case ERROR_OVERLOAD_SYSTEM:
                overloadError();
        }

        return view;

    }

    private void overloadError() {
        mErrorImage.setImageResource(R.drawable.owl_crying);
        mErrorMessage.setText(R.string.overloaded_system_message);
    }

    public void setError(int error) {
        mError = error;
    }
}
