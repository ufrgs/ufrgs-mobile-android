package com.cpd.utils;

import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.ShareEvent;

/**
 * Tracking user interactions.
 *
 * @author Alan Wink
 */
public class TrackerUtils {

    private static final String TAG = "TrackerUtils";

    public static void openSettings(){
        Log.d(TAG, "openSettings() called with: " + "");
        Answers.getInstance().logCustom(
                new CustomEvent("Open settings")
        );
    }

    public static void usernameEdit(){
        Log.d(TAG, "usernameEdit() called with: " + "");
        Answers.getInstance().logCustom(
                new CustomEvent("Username edit")
        );
    }

    public static void passwordEdit(){
        Log.d(TAG, "passwordEdit() called with: " + "");
        Answers.getInstance().logCustom(
                new CustomEvent("Password edit")
        );
    }

    public static void autoRenewSwitch(){
        Log.d(TAG, "autoRenewSwitch() called with: " + "");
        Answers.getInstance().logCustom(
                new CustomEvent("Autorenew switch")
        );
    }

    public static void restaurantListEdit(){
        Log.d(TAG, "restaurantListEdit() called with: " + "");
        Answers.getInstance().logCustom(
                new CustomEvent("Restaurant list edit")
        );
    }

    public static void clickNewsArticle(String articleTitle){
        Log.d(TAG, "clickNewsArticle() called with: " + "articleTitle = [" + articleTitle + "]");
        Answers.getInstance().logCustom(
                new CustomEvent("Click news article")
                        .putCustomAttribute("articleTitle", articleTitle)
        );
    }

    public static void shareNewsArticle(String articleTitle, String shareIntent){
        Log.d(TAG, "shareNewsArticle() called with: " + "articleTitle = [" + articleTitle + "], shareIntent = [" + shareIntent + "]");
        Answers.getInstance().logShare(
                new ShareEvent()
                    .putMethod(shareIntent)
                    .putContentName(articleTitle)
        );
    }

    public static void openArticleImage(String articleTitle){
        Log.d(TAG, "openArticleImage() called with: " + "articleTitle = [" + articleTitle + "]");
        Answers.getInstance().logCustom(
                new CustomEvent("Open article image")
                        .putCustomAttribute("articleTitle", articleTitle)
        );
    }

    public static void registerEnter(){
        Log.d(TAG, "registerEnter() called with: " + "");
        Answers.getInstance().logCustom(
                new CustomEvent("Register enter")
        );
    }

}
