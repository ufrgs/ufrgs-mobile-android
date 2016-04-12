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
package com.cpd.receivers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;

/**
 * Receiver to set library auto-renew when Android starts.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class BootUpReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoRenewOn = pref.getBoolean(context.getString(R.string.pref_library_autorenew), false);
        if(autoRenewOn) {
            LibraryRenewAlarmBroadcastReceiver alarm = new LibraryRenewAlarmBroadcastReceiver();
            alarm.setAlarm(context);
        }

    }

}
