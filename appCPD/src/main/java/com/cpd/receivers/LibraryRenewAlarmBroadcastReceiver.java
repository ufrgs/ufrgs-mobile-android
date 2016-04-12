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

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.cpd.activities.MainActivity;
import com.cpd.network.LibraryLoader;
import com.cpd.utils.AppTags;
import com.cpd.ufrgsmobile.R;

import java.util.Calendar;

/**
 * Handles requests to renew books during day for library auto-renew.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryRenewAlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "LibraryRenewAlarmBroadcastReceiver";
    private static final int DAILY_RETRIES = 10;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AppTags.UFRGS_MOBILE);
        //Acquire the lock
        wl.acquire();

        prefs = context.getSharedPreferences(AppTags.LIBRARY_LOGIN_PREF, Context.MODE_PRIVATE);
        editor = prefs.edit();
        int value = prefs.getInt(AppTags.ALARM_COUNTER, 0);
        value = value + 1;
        editor.putInt(AppTags.ALARM_COUNTER, value);
        editor.commit();

        LibraryLoader loader = new LibraryLoader(context);
        loader.renewBooks(null, true);

        if(value == DAILY_RETRIES){
            launchNotification(context, context.getString(R.string.we_tried_to_renew_your_items_notification_title), context.getString(R.string.verify_return_date_notification_message));
            editor.putInt(AppTags.ALARM_COUNTER, 0);
            editor.commit();
        }

        wl.release();

    }

    public void setAlarm(Context context) {
        Log.d(TAG, "setAlarm() called with: " + "context = [" + context + "]");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LibraryRenewAlarmBroadcastReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY / DAILY_RETRIES, pi);
    }

    public void cancelAlarm(Context context) {
        Log.d(TAG, "cancelAlarm() called with: " + "context = [" + context + "]");
        Intent intent = new Intent(context, LibraryRenewAlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void launchNotification(Context context, String title, String message) {

        // Extender for Android Wear
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setBackground(BitmapFactory.decodeResource(context.getResources(), R.drawable.library_wear_bg));

        // Build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setLights(ContextCompat.getColor(context, R.color.pureRed), 2000, 2000);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.extend(wearableExtender);

        //TODO: criar extra no intent pra tratar abertura da pag da biblio!
        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        int idNotify = 1;
        mNotificationManager.notify(idNotify, notificationBuilder.build());


    }
}
