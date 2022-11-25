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
package com.cpd.ui.main_screen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.cpd.gcm.RegistrationIntentService
import com.cpd.network.models.Notifications
import com.cpd.ufrgsmobile.R
import com.cpd.ui.library_screen.LibraryRootFragment
import com.cpd.ui.login_screen.login_dialog.LoginDialog
import com.cpd.ui.news_screen.NewsFragment
import com.cpd.ui.notifications_screen.NotificationsActivity
import com.cpd.ui.ru_screen.RuFragment
import com.cpd.ui.tickets_screen.TicketsActivity
import com.cpd.utils.AppRegister
import com.cpd.utils.AppTags
import com.cpd.utils.LayoutUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import androidx.appcompat.app.AlertDialog
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityOptionsCompat

/**
 * App's main activity. Configures the tabs to be loaded with the fragments.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class MainActivity : AppCompatActivity(), MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {

    var menu: Menu? = null

    private val presenter = MainPresenter(this)

    private val libraryRootFragment = LibraryRootFragment.newInstance()
    private val ruFragment = RuFragment.newInstance()
    private val newsFragment = NewsFragment.newInstance()

    private val mTokenManager = UfrgsTokenManager()

    private var notification: Notifications? = null
    private var navBarSelectedId = R.id.tab_news


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LayoutUtils.setupToolbar(this, main_toolbar as Toolbar, null, false)

        // tenta pegar o id do item que estava selecionado
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("navBarSelectedId")) {
                navBarSelectedId = savedInstanceState.getInt("navBarSelectedId")
            }
        }

        if (intent.getStringExtra("long") != null) {
            val message = intent.getStringExtra("long") ?: ""
            alert(message, "Resultado da Renovação") {
                yesButton { d -> d.dismiss() }
            }.show()
        }

        requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER
        }

        bottomNavigation.setOnNavigationItemSelectedListener(this)
        bottomNavigation.selectedItemId = navBarSelectedId
        AppRegister.register(this)

        ticket_button.setOnClickListener {

            val intent: Intent = if (mTokenManager.hasToken(this)) {
                Intent(this, TicketsActivity::class.java)
            } else {
                Intent(this, LoginDialog::class.java)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val bundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.my_fade_in, R.anim.my_fade_out).toBundle()
                startActivity(intent, bundle)
            } else {
                startActivity(intent)
            }
        }

        val pref = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        // se abriu o app pela primeira vez
        if (pref.getBoolean("firstRun", true)) {

            val editor = pref.edit()
            editor.putBoolean("firstRun", false)
            editor.commit()

            // mostra política de privacidade
            showPrivacyPolicy()
        }

    }

    public override fun onResume() {
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)

        val pushMsg = getIntent().getStringExtra(AppTags.PUSH_EXTRAS)
        getIntent().removeExtra(AppTags.PUSH_EXTRAS)
        if (pushMsg != null)
            alert(pushMsg, "Resultado da Renovação") {
                yesButton { d -> d.dismiss() }
            }.show()

        presenter.fetchNotification()

        super.onResume()
    }

    override fun onNotificationReady(notification: Notifications?) {
        this.notification = notification
        menu?.findItem(R.id.notification)?.isVisible = notification != null
    }

    override fun showMessage(message: String) {
        toast(message)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.my_fade_in, R.anim.my_fade_out)

        when (item.itemId) {
            R.id.tab_news -> {
                transaction.replace(R.id.contentContainer, newsFragment)
                showLibraryMenu(false)
                showTicketsMenu(false)
            }
            R.id.tab_ru -> {
                transaction.replace(R.id.contentContainer, ruFragment)
                showLibraryMenu(false)
                showTicketsMenu(true)
            }
            R.id.tab_library -> {
                transaction.replace(R.id.contentContainer, libraryRootFragment)
                showTicketsMenu(false)
            }
        }

        transaction.commit()
        navBarSelectedId = item.itemId
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        showLibraryMenu(false)
        showTicketsMenu(false)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.renew_now -> {
                libraryRootFragment.renewBooks()
                true
            }
            R.id.auto_renew -> {
                libraryRootFragment.autorenewBooks()
                true
            }
            R.id.exit -> {
                libraryRootFragment.logoff()
                super.onOptionsItemSelected(item)
            }

            R.id.notification -> {
                if (notification != null) {
                    NotificationsActivity.start(this, notification!!)
                } else {
                    menu?.findItem(R.id.notification)?.isVisible = false
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("navBarSelectedId", navBarSelectedId)
    }

    fun showLibraryMenu(showMenu: Boolean) {
        if (menu == null)
            return

        menu!!.setGroupVisible(R.id.library_menu_group, showMenu)
    }

    private fun showTicketsMenu(showMenu: Boolean) {
        if (showMenu)
            ticket_button!!.visibility = View.VISIBLE
        else
            ticket_button!!.visibility = View.GONE
    }

    fun changeMenuItemText(text: String) {
        val autoRenewItem = menu!!.findItem(R.id.auto_renew)
        autoRenewItem.title = text
    }

    private fun showPrivacyPolicy() {
        val textView = layoutInflater.inflate(R.layout.privacy_policy_textview, null) as TextView
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(R.string.privacy_policy_message)

        val dialog = AlertDialog.Builder(this)
                .setPositiveButton(R.string.privacy_policy_response, null)
                .setCancelable(false)
                .setView(textView)
                .create()

        dialog.show()
    }

    companion object {
        private val TAG = "MainActivity"
    }

}
