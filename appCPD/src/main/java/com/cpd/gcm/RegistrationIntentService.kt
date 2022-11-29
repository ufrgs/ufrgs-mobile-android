package com.cpd.gcm

import android.app.IntentService
import android.content.Intent
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.util.Log
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.cpd.ufrgsmobile.R
import com.google.android.gms.gcm.GcmPubSub
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID
import java.io.IOException


/**
 * Created by theolm on 10/08/16.
 */
class RegistrationIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        try {
            val instanceID = InstanceID.getInstance(this)
            val token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)

            Log.i(TAG, "GCM Registration Token: $token")

            sendRegistrationToServer(token)
            subscribeTopics(token)
            sharedPreferences.edit().putBoolean("sentTokenToServer", true).apply()
            sharedPreferences.edit().putString("gcmtoken", token).apply()

        } catch (e: Exception) {
            Log.d(TAG, "Failed to complete token refresh", e)
            sharedPreferences.edit().putBoolean("sentTokenToServer", false).apply()
        }

        val registrationComplete = Intent("registrationComplete")
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "sendRegistrationToServer: $token")
        val context = applicationContext
        val android_id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val ufrgsTokenManager = UfrgsTokenManager()

        if (ufrgsTokenManager.hasToken(context)) {
            ufrgsTokenManager.registerGcmToken(context, token, android_id)
        }

    }

    @Throws(IOException::class)
    private fun subscribeTopics(token: String) {
        val pubSub = GcmPubSub.getInstance(this)
        for (topic in TOPICS) {
            pubSub.subscribe(token, "/topics/$topic", null)
        }
    }

    companion object {
        private val TAG = "RegIntentService"
        private val TOPICS = arrayOf("global")
    }
}
