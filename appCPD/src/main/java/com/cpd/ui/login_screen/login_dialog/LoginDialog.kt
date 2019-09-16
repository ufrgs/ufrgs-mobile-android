package com.cpd.ui.login_screen.login_dialog

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import br.ufrgs.ufrgsapi.token.UfrgsToken
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.cpd.ufrgsmobile.R
import com.cpd.utils.LayoutUtils
import kotlinx.android.synthetic.main.login_dialog.*

/**
 * Created by theolm on 14/10/16.
 */

class LoginDialog : AppCompatActivity() {

    private var mUfrgsTokenManager = UfrgsTokenManager()
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_dialog)
        LayoutUtils.setStatusBarColor(this, "#7d2623")

        // Loading dialog
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setMessage(getString(R.string.loading))
        mProgressDialog!!.setCancelable(false)

        login_background.setOnClickListener { onBackPressed() }

        login_button.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(login_layout_password.windowToken, 0)

            val u = login_card.text.toString().isEmpty()
            val p = login_password.text.toString().isEmpty()

            if (u) {
                login_layout_card.isErrorEnabled = true
                login_layout_card.error = getString(R.string.user_needed)
            }
            if (p) {
                login_layout_password.isErrorEnabled = true
                login_layout_password.error = getString(R.string.password_needed)
            }

            if (!u && !p)
                getTokenFromAPI(login_card.text.toString(), login_password.text.toString())
        }

        login_password.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login_button.performClick()
                return@OnEditorActionListener true
            }
            false
        })

        login_forgot_password_text_view.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(FORGOT_PASSWORD_URL))
            startActivity(browserIntent)
        }

    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.supportFinishAfterTransition()
        } else {
            finish()
        }
        overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out)
    }

    private fun getTokenFromAPI(user: String, password: String) {
        mProgressDialog!!.show()
        mUfrgsTokenManager!!.requestNewToken(this, user, password, object : UfrgsTokenManager.OnTokenListener {

            override fun onTokenReady(token: UfrgsToken) {
                mProgressDialog!!.dismiss()

                Toast.makeText(this@LoginDialog, R.string.welcome, Toast.LENGTH_SHORT).show()

                val bundle = Bundle()
                bundle.putSerializable("token", token)
                val intent = Intent()
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out)

            }

            override fun onError(error: String) {
                mProgressDialog!!.dismiss()

                if (error.contains("Unable to resolve host")) {
                    login_failed_view_message_label.text = resources.getString(R.string.generic_login_failed)
                } else {
                    login_failed_view_message_label.text = resources.getString(R.string.wrong_card_or_password)
                }

                login_failed_view.visibility = View.VISIBLE
            }
        })
    }

    companion object {
        private val FORGOT_PASSWORD_URL = "https://www1.ufrgs.br/trocasenhas/esqueciSenha/"
    }

}
