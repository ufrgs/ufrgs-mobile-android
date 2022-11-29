package com.cpd.gcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.cpd.ufrgsmobile.R
import com.cpd.ui.main_screen.MainActivity
import com.cpd.utils.AppTags
import com.google.android.gms.gcm.GcmListenerService

/**
 * Created by theolm on 10/08/16.
 */
class UfrgsGcmListenerService : GcmListenerService() {

    override fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data!!.getString("message")
        val shotMsg = data.getString("short")
        val longMsg = data.getString("long")
        sendNotification(shotMsg, longMsg)
    }

    private fun sendNotification(shortMessage: String?, longMessage: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(AppTags.PUSH_EXTRAS, longMessage)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Renovação Automática")
                .setContentText(shortMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private val TAG = "UfrgsGcmListenerService"
    }

}
