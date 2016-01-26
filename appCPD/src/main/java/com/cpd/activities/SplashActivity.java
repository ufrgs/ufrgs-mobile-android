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
package com.cpd.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cpd.ufrgsmobile.R;
import com.cpd.utils.ConnectionUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Splash screen activity launched when the app starts.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    public static final int SPLASH_TIME_MS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }

        if(ConnectionUtils.isConnectingToInternet(this)){
            openMainActivity();
        }
        else {
            closeAppNoConnection();
        }
    }

    /**
     * Give opens the MainActivity after a certain time.
     */
    private void openMainActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();

                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, SPLASH_TIME_MS);
    }

    /**
     * If there is no network connection, the app shows a message and closes.
     */
    private void closeAppNoConnection(){

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Closes app
                        finish();
                        moveTaskToBack(true);
                    }
                })
                .setTitle(R.string.no_connection)
                .setMessage(R.string.no_connection_message)
                .create();

        alertDialog.show();

    }

}
