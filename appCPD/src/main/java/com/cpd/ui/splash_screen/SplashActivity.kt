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
package com.cpd.ui.splash_screen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.cpd.ufrgsmobile.R
import com.cpd.ui.main_screen.MainActivity
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import java.util.*

/**
 * Splash screen activity launched when the app starts.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class SplashActivity : AppCompatActivity() {

    private var mShort: String? = null
    private var mLong: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash_screen)

        if (intent.extras != null) {
            mShort = intent.extras!!.getString("short")
            mLong = intent.extras!!.getString("long")
        } else {
            Log.d(TAG, "onCreate: sem extras")
        }
    }

    override fun onStart() {
        super.onStart()

        requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER
        }

        openMainActivity()
    }

    /**
     * Give opens the MainActivity after a certain time.
     */
    private fun openMainActivity() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                finish()

                val intent = Intent()
                intent.setClass(this@SplashActivity, MainActivity::class.java)
                if (mShort != null && mLong != null) {
                    intent.putExtra("short", mShort)
                    intent.putExtra("long", mLong)
                }
                startActivity(intent)
            }
        }, SPLASH_TIME_MS.toLong())
    }

    companion object {
        private val TAG = "SplashActivity"

        val SPLASH_TIME_MS = 1000
    }
}
