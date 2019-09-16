package com.cpd.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Window
import android.view.WindowManager
import com.cpd.ufrgsmobile.R
import android.util.TypedValue



/**
 * Created by Theo on 16/06/17.
 */

object LayoutUtils {
    fun setStatusBarColor(activity: Activity, color: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }

    fun setStatusBarColor(activity: Activity, r: Int, g: Int, b: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.rgb(r, g, b)
        }
    }

    fun setNavbarTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            //TODO:Arrumar margem da root!
        }
    }

    fun setNavigationBarColor(activity: Activity, color: Int) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.navigationBarColor = color
        }
    }

    //must be called before setContentView
    fun setFullScreen(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun setupToolbar(activity: AppCompatActivity, toolbar: Toolbar, title: String?, isModal: Boolean) {

        activity.setSupportActionBar(toolbar)
        if (title != null) {
            activity.supportActionBar!!.title = title
            toolbar.setTitleTextColor(activity.resources.getColor(R.color.md_white_1000))
        } else {
            activity.supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        if (isModal) {
            toolbar.navigationIcon = ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener { activity.onBackPressed() }
        }
    }

    fun spToPx(sp: Int): Int {
        return (sp * Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}
