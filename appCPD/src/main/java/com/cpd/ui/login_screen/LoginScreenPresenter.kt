package com.cpd.ui.login_screen

import android.content.Context
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import br.ufrgs.ufrgsapi.library.UfrgsLibraryManager
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryRegister
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager

/**
 * Created by Theo on 02/06/17.
 */

class LoginScreenPresenter(private val context: Context, private val view: LoginScreenContract.View) : LoginScreenContract.Presenter {

    override fun registerUser() {
        UfrgsLibraryManager(context).registerUser(object : UfrgsLibraryManager.RegisterUser {
            override fun onRegisterUser(register: UfrgsLibraryRegister) {
                view.onUserRegister()
            }

            override fun onError(error: String) {
                Log.d(TAG, "onError: ")
            }
        })
    }

    override fun registerGcmToken(tokenManager: UfrgsTokenManager) {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = sharedPreferences.getString("gcmtoken", null)

        if (token != null) {
            val android_id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            if (tokenManager.hasToken(context)) {
                tokenManager.registerGcmToken(context, token, android_id)
            }
        }
    }

    companion object {
        private val TAG = "LoginScreenPresenter"
    }
}
