package com.cpd.ui.login_screen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.cpd.ufrgsmobile.R
import com.cpd.ui.library_screen.LibraryRootFragment
import com.cpd.ui.login_screen.login_dialog.LoginDialog
import com.cpd.ui.main_screen.MainActivity
import kotlinx.android.synthetic.main.fragment_login_library.view.*

/**
 * Created by theolm on 12/07/16.
 */
class LoginFragment : Fragment(), LoginScreenContract.View {

    private var mUfrgsTokenManager = UfrgsTokenManager()
    private var mPreseter: LoginScreenPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login_library, container, false)
        mPreseter = LoginScreenPresenter(activity!!, this)

        view.library_login_button.setOnClickListener {
            val i = Intent(activity, LoginDialog::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view.library_login_button, "login_dialog")
                startActivityForResult(i, LOGIN_REQUEST_CODE, options.toBundle())
            } else {
                startActivityForResult(i, LOGIN_REQUEST_CODE)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showLibraryMenu(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            val token = mUfrgsTokenManager.getToken(activity)
            if (token != null) {
                registerUser()
            }
        }
    }


    private fun registerUser() {
        mPreseter!!.registerUser()
    }

    override fun onUserRegister() {
        mPreseter!!.registerGcmToken(mUfrgsTokenManager)
        val rootFragment = parentFragment as LibraryRootFragment?
        rootFragment!!.configureNestedFragments()
    }

    companion object {
        private val TAG = "LoginFragment"
        val LOGIN_REQUEST_CODE = 13
    }
}
