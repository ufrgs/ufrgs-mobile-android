package com.cpd.ui.login_screen

import br.ufrgs.ufrgsapi.token.UfrgsTokenManager

/**
 * Created by Theo on 02/06/17.
 */

interface LoginScreenContract {
    interface View {
        fun onUserRegister()
    }

    interface Presenter {
        fun registerUser()
        fun registerGcmToken(tokenManager: UfrgsTokenManager)
    }
}
