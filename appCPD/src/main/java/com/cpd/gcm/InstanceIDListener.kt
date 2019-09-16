package com.cpd.gcm

import android.content.Intent

import com.google.android.gms.iid.InstanceIDListenerService

/**
 * Created by theolm on 10/08/16.
 */
class InstanceIDListener : InstanceIDListenerService() {
    override fun onTokenRefresh() {
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
    }
}
