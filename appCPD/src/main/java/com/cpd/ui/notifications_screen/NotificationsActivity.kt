package com.cpd.ui.notifications_screen;

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.core.view.MenuItemCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.appcompat.widget.Toolbar
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import com.cpd.network.models.Notifications
import com.cpd.ufrgsmobile.R
import com.cpd.utils.AppTags
import com.cpd.utils.TrackerUtils
import kotlinx.android.synthetic.main.activity_notifications.*
import org.jetbrains.anko.startActivity


/**
 * Displays full notification.
 * Created by Hermes Tessaro on 16/10/18.
 */
class NotificationsActivity : AppCompatActivity() {

    private val mNotificationsVo by lazy { intent.getSerializableExtra(AppTags.NOTIFICATIONS_VO) as Notifications }


    private val defaultShareIntent: Intent
        get() {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + mNotificationsVo.titulo)
            return intent
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        setSupportActionBar(notifications_toolbar as Toolbar)

        requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER
        }

        TrackerUtils.clickNewsArticle(mNotificationsVo.titulo)

        notifications_activity_title.text = mNotificationsVo.titulo
        notifications_activity_text.text = Html.fromHtml(mNotificationsVo.texto)
        notifications_activity_text.movementMethod = LinkMovementMethod.getInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.news_activity_menu, menu)

        val item = menu.findItem(R.id.action_share)

        val shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        shareActionProvider.setShareIntent(defaultShareIntent)
        shareActionProvider.setOnShareTargetSelectedListener { source, intent ->
            val shareTarget = intent.component!!.packageName
            TrackerUtils.shareNewsArticle(mNotificationsVo.titulo, shareTarget)

            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    companion object {


        fun start(context: Context, notification: Notifications) {
            context.startActivity<NotificationsActivity>(AppTags.NOTIFICATIONS_VO to notification)
        }
    }
}
