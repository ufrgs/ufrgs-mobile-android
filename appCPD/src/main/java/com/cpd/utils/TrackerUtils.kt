package com.cpd.utils

import android.util.Log

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.crashlytics.android.answers.ShareEvent

/**
 * Tracking user interactions.
 *
 * @author Alan Wink
 */
object TrackerUtils {

    private val TAG = "TrackerUtils"

    fun openSettings() {
        Log.d(TAG, "openSettings() called with: " + "")
        Answers.getInstance().logCustom(
                CustomEvent("Open settings")
        )
    }

    fun usernameEdit() {
        Log.d(TAG, "usernameEdit() called with: " + "")
        Answers.getInstance().logCustom(
                CustomEvent("Username edit")
        )
    }

    fun passwordEdit() {
        Log.d(TAG, "passwordEdit() called with: " + "")
        Answers.getInstance().logCustom(
                CustomEvent("Password edit")
        )
    }

    fun autoRenewSwitch() {
        Log.d(TAG, "autoRenewSwitch() called with: " + "")
        Answers.getInstance().logCustom(
                CustomEvent("Autorenew switch")
        )
    }

    fun restaurantListEdit() {
        Log.d(TAG, "restaurantListEdit() called with: " + "")
        Answers.getInstance().logCustom(
                CustomEvent("Restaurant list edit")
        )
    }

    fun clickNewsArticle(articleTitle: String) {
        Log.d(TAG, "clickNewsArticle() called with: articleTitle = [$articleTitle]")
        Answers.getInstance().logCustom(
                CustomEvent("Click news article")
                        .putCustomAttribute("articleTitle", articleTitle)
        )
    }

    fun shareNewsArticle(articleTitle: String, shareIntent: String) {
        Log.d(TAG, "shareNewsArticle() called with: articleTitle = [$articleTitle], shareIntent = [$shareIntent]")
        Answers.getInstance().logShare(
                ShareEvent()
                        .putMethod(shareIntent)
                        .putContentName(articleTitle)
        )
    }

    fun openArticleImage(articleTitle: String) {
        Log.d(TAG, "openArticleImage() called with: articleTitle = [$articleTitle]")
        Answers.getInstance().logCustom(
                CustomEvent("Open article image")
                        .putCustomAttribute("articleTitle", articleTitle)
        )
    }

    fun registerEnter() {
        Log.d(TAG, "registerEnter() called with: " + "")
        Answers.getInstance().logCustom(
                CustomEvent("Register enter")
        )
    }

}
