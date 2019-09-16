package com.cpd.ui.library_screen

import br.ufrgs.ufrgsapi.library.UfrgsLibraryManager
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryRegister
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryUser
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import br.ufrgs.ufrgsapi.user_data.UfrgsUser
import br.ufrgs.ufrgsapi.user_data.UfrgsUserDataManager

/**
 * Created by Theo on 09/06/17.
 */

interface LibraryScreenContract {
    interface View {
        fun showUserInformation(user: UfrgsUser)
        fun showLoanList(ufrgsLibraryUser: UfrgsLibraryUser)
        fun showMessage(msg: String)
        fun showLoanExtraInfo(extraInfo: String)
        fun showAutoRenewInfo(register: UfrgsLibraryRegister)
        fun showAutoRenewStatus(register: UfrgsLibraryRegister)
        fun onLogoffReady()
        fun apiError()
    }

    interface Presenter {
        fun getUserInformation(ufrgsUserDataManager: UfrgsUserDataManager)
        fun getLoanList(ufrgsLibraryManager: UfrgsLibraryManager)
        fun getLoanExtraInfo(ufrgsLibraryUser: UfrgsLibraryUser)
        fun getAutoRenewInfo(ufrgsLibraryManager: UfrgsLibraryManager)
        fun changeAutoRenewStatus(ufrgsLibraryManager: UfrgsLibraryManager)
        fun renewAllBooks(ufrgsLibraryManager: UfrgsLibraryManager)
        fun autoRenewMessage(ufrgsLibraryManager: UfrgsLibraryManager)
        fun autoRenewMessageLogoff(ufrgsLibraryManager: UfrgsLibraryManager)
        fun exitMessage(ufrgsTokenManager: UfrgsTokenManager)
        fun logoff(ufrgsTokenManager: UfrgsTokenManager)
    }
}
