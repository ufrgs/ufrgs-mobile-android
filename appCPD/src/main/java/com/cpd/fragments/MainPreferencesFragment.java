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
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.cpd.activities.MainPreferencesActivity;
import com.cpd.receivers.LibraryRenewAlarmBroadcastReceiver;
import com.cpd.ufrgsmobile.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MainPreferencesFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setAutoRenewEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFields();
    }

    private void setAutoRenewEvent(){
        final CheckBoxPreference autorenewPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_library_autorenew));
        autorenewPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Returns true if the value must change

                boolean willTurnOnAutorenew = newValue.toString().equals("true");

                final LibraryRenewAlarmBroadcastReceiver alarmBroadcastReceiver = new LibraryRenewAlarmBroadcastReceiver();

                if(willTurnOnAutorenew){
                    LibraryAutoRenewDialogFragment autoRenewDialogFragment = new LibraryAutoRenewDialogFragment();
                    autoRenewDialogFragment.setRenewDialogResponse(new LibraryAutoRenewDialogFragment.RenewDialogResponse() {
                        @Override
                        public void onAutoRenewAccepted() {
                            autorenewPreference.setChecked(true);
                            alarmBroadcastReceiver.setAlarm(getActivity());
                        }

                        @Override
                        public void onAutoRenewDenied() {
                            autorenewPreference.setChecked(false);
                        }
                    });
                    autoRenewDialogFragment.show(MainPreferencesActivity.mFragmentManager, "libTag");
                    return false;
                }
                else{
                    // Cancel autorenew
                    alarmBroadcastReceiver.cancelAlarm(getActivity());
                    return true;
                }
            }
        });

    }

    private void updateFields() {
        updateUsername();
        updatePassword();
        updateRestaurantList();
    }

    private void updateUsername() {
        EditTextPreference libUsername = (EditTextPreference) findPreference(getString(R.string.pref_library_username));
        if(libUsername != null && libUsername.getText() != null) {
            libUsername.setSummary(libUsername.getText());
        }
    }

    private void updatePassword() {
        EditTextPreference libPassword = (EditTextPreference) findPreference(getString(R.string.pref_library_password));
        if(libPassword != null && libPassword.getText() != null) {
            String passDot = "•";
            int n = libPassword.getText().length();
            String passwordMask = new String(new char[n]).replace("\0", passDot);
            libPassword.setSummary(passwordMask);
        }
    }

    private void updateRestaurantList(){
        MultiSelectListPreference restaurantPrefList = (MultiSelectListPreference) findPreference(getString(R.string.pref_restaurant_list));
        List<Integer> restaurantSelectedIdList = new ArrayList<>();
        if(restaurantPrefList.getValues().isEmpty()){
            restaurantPrefList.setSummary("");
            return;
        }
        for(String s : restaurantPrefList.getValues()){
            restaurantSelectedIdList.add(restaurantPrefList.findIndexOfValue(s));
        }

        Collections.sort(restaurantSelectedIdList);

        String restaurantSummary = "";
        String separator = ", ";
        for(int i : restaurantSelectedIdList){
            restaurantSummary = restaurantSummary + restaurantPrefList.getEntries()[i] + separator;
        }

        restaurantSummary = restaurantSummary.substring(0, restaurantSummary.length() - separator.length());
        restaurantPrefList.setSummary(restaurantSummary);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(isAdded()) {
            // We cannot update a fragment that is not attached to a activity
            updateFields();
        }
    }
}
