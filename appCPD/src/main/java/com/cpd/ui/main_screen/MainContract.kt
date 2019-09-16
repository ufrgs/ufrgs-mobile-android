package com.cpd.ui.main_screen

import com.cpd.network.models.Notifications

interface MainContract {
    interface View {
        fun onNotificationReady(notification: Notifications?)
        fun showMessage(message: String)
    }

    interface Presenter {
        fun fetchNotification()
    }
}