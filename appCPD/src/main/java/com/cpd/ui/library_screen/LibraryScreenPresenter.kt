package com.cpd.ui.library_screen

import android.app.AlertDialog
import android.content.Context
import android.provider.Settings
import br.ufrgs.ufrgsapi.library.UfrgsLibraryManager
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryRegister
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryRenew
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryUser
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import br.ufrgs.ufrgsapi.user_data.UfrgsUser
import br.ufrgs.ufrgsapi.user_data.UfrgsUserDataManager
import com.cpd.ufrgsmobile.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

/**
 * Created by Theo on 09/06/17.
 */

class LibraryScreenPresenter(private val context: Context, private val view: LibraryScreenContract.View) : LibraryScreenContract.Presenter {

    override fun getUserInformation(ufrgsUserDataManager: UfrgsUserDataManager) {
        ufrgsUserDataManager.getData(object : UfrgsUserDataManager.OnDataCallback {
            override fun onDataReady(user: UfrgsUser) {
                view.showUserInformation(user)
            }

            override fun onError(error: String) {
                view.apiError()
            }
        })
    }

    override fun getLoanList(ufrgsLibraryManager: UfrgsLibraryManager) {
        ufrgsLibraryManager.getLibraryUser(object : UfrgsLibraryManager.LibraryCallback {
            override fun onLibraryUserReady(user: UfrgsLibraryUser) {
                view.showLoanList(user)
            }

            override fun onError(error: String) {
                view.showMessage(error)
            }
        })
    }

    override fun getLoanExtraInfo(ufrgsLibraryUser: UfrgsLibraryUser) {
        var extraInfo: String

        if (ufrgsLibraryUser.loans.size == 0) {
            extraInfo = context.getString(R.string.no_loan) + "\n"
        } else if (ufrgsLibraryUser.loans.size == 1) {
            extraInfo = "1 " + context.getString(R.string.loan_word) + "\n"
        } else {
            extraInfo = ufrgsLibraryUser.loans.size.toString() + " " + context.getString(R.string.loans_word) + "\n"
        }

        if (ufrgsLibraryUser.debtTotal == -1f) {
            extraInfo = extraInfo + context.getString(R.string.no_debt)
        } else {
            extraInfo = extraInfo + context.getString(R.string.debt_word) + ": R$" + String.format("%.2f", ufrgsLibraryUser.debtTotal).replace("-", "")
        }

        view.showLoanExtraInfo(extraInfo)
    }

    override fun getAutoRenewInfo(ufrgsLibraryManager: UfrgsLibraryManager) {
        ufrgsLibraryManager.getUserRegister(object : UfrgsLibraryManager.GetUserRegister {
            override fun onGetRegisterUser(register: UfrgsLibraryRegister) {
                view.showAutoRenewInfo(register)
            }

            override fun onError(error: String) {
                //TODO: Tratar erro
            }
        })
    }

    override fun changeAutoRenewStatus(ufrgsLibraryManager: UfrgsLibraryManager) {
        ufrgsLibraryManager.changeRenewStatus(object : UfrgsLibraryManager.ChangeRenewStatus {
            override fun onChangeRenewStatus(register: UfrgsLibraryRegister) {
                view.showAutoRenewStatus(register)
            }

            override fun onError(error: String) {
                view.showMessage(context.getString(R.string.library_no_connection_error))
            }
        })
    }

    override fun renewAllBooks(ufrgsLibraryManager: UfrgsLibraryManager) {
        ufrgsLibraryManager.renewLibrary(object : UfrgsLibraryManager.LibraryRenew {
            override fun onLibraryRenew(renew: List<UfrgsLibraryRenew>) {
                var message = ""
                if (renew.size > 0) {
                    if (renew.size == 1 && renew[0].title == null) {
                        message = renew[0].status
                    } else {

                        var i = 1

                        for (x in renew) {
                            message = message + i.toString() + "- " + x.title + ": " + x.status + "\n"
                            i++
                        }
                    }
                    //atualiza dados
                    getLoanList(ufrgsLibraryManager)

                } else {
                    message = context.getString(R.string.no_loan)
                }

                context.alert(message, "Resultado da Renovação") {
                    yesButton { d -> d.dismiss()}
                }.show()
            }

            override fun onError(error: String) {

            }
        })
    }

    override fun autoRenewMessage(ufrgsLibraryManager: UfrgsLibraryManager) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("UFRGS Mobile")

        builder.setMessage(context.getString(R.string.library_loan_message))
        builder.setPositiveButton(context.getString(R.string.yes)) { dialogInterface, i ->
            changeAutoRenewStatus(ufrgsLibraryManager)
            dialogInterface.dismiss()
        }.setNegativeButton(context.getString(R.string.no)) { dialogInterface, i -> dialogInterface.dismiss() }

        builder.create().show()
    }

    override fun autoRenewMessageLogoff(ufrgsLibraryManager: UfrgsLibraryManager) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Desativar autorrenovação")

        builder.setMessage(context.getString(R.string.library_loan_message_logoff))
        builder.setPositiveButton(context.getString(R.string.yes)) { dialogInterface, i ->
            changeAutoRenewStatus(ufrgsLibraryManager)
            dialogInterface.dismiss()
        }.setNegativeButton(context.getString(R.string.no)) { dialogInterface, i -> dialogInterface.dismiss() }

        builder.create().show()
    }

    override fun exitMessage(ufrgsTokenManager: UfrgsTokenManager) {
        val builder = AlertDialog.Builder(context)

        builder.setMessage(context.getString(R.string.library_exit_message))
        builder.setPositiveButton(context.getString(R.string.yes)) { dialogInterface, i ->
            try {
                logoff(ufrgsTokenManager)
                dialogInterface.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.setNegativeButton(context.getString(R.string.no)) { dialogInterface, i -> dialogInterface.dismiss() }

        builder.create().show()
    }

    override fun logoff(ufrgsTokenManager: UfrgsTokenManager) {
        val android_id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        ufrgsTokenManager.logoutFromAPI(context, android_id) { isSuccessful ->
            if (isSuccessful) {
                view.onLogoffReady()
            }
        }
    }
}
