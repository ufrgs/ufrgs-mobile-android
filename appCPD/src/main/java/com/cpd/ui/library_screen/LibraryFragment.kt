package com.cpd.ui.library_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.ufrgs.ufrgsapi.library.UfrgsLibraryManager
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryLoan
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryRegister
import br.ufrgs.ufrgsapi.library.models.UfrgsLibraryUser
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import br.ufrgs.ufrgsapi.user_data.UfrgsUser
import br.ufrgs.ufrgsapi.user_data.UfrgsUserDataManager
import com.cpd.adapters.LibraryAdapter
import com.cpd.ufrgsmobile.R
import com.cpd.ui.main_screen.MainActivity
import com.cpd.utils.ScreenSizeUtils
import kotlinx.android.synthetic.main.fragment_library.view.*

/**
 * Created by theolm on 12/07/16.
 */
class LibraryFragment : Fragment(), LibraryScreenContract.View, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mUserManager: UfrgsUserDataManager
    private lateinit var mLibraryManager: UfrgsLibraryManager
    private var mUfrgsTokenManager = UfrgsTokenManager()

    private var mPresenter: LibraryScreenPresenter? = null
    private var mRegister: UfrgsLibraryRegister? = null //contem informação do autorenovar
    private val mLoans: List<UfrgsLibraryLoan>? = null
    private var mUserData: UfrgsUser? = null
    private var mLibraryUserData: UfrgsLibraryUser? = null
    private var mAdapter: LibraryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserManager = UfrgsUserDataManager(activity)
        mLibraryManager = UfrgsLibraryManager(activity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        mPresenter = LibraryScreenPresenter(activity!!, this)

        view.library_fragment_loading.visibility = View.VISIBLE
        view.library_fragment_swipe_layout.setColorSchemeResources(R.color.primaryColor)

        mPresenter!!.getUserInformation(mUserManager)
        mPresenter!!.getLoanList(mLibraryManager)
        mPresenter!!.getAutoRenewInfo(mLibraryManager)

        view.library_fragment_swipe_layout.setOnRefreshListener(this)

        val llm = LinearLayoutManager(activity)
        view.library_fragment_books_recycler.layoutManager = llm
        mAdapter = LibraryAdapter(activity!!)
        view.library_fragment_books_recycler.adapter = mAdapter
        view.library_fragment_books_recycler.isNestedScrollingEnabled = false

        //clicks

        view.library_fragment_owl_retry_btn.setOnClickListener {
            mUfrgsTokenManager = UfrgsTokenManager()
            mLibraryManager = UfrgsLibraryManager(activity)
            mUserManager = UfrgsUserDataManager(activity)
            mPresenter!!.getUserInformation(mUserManager)
            mPresenter!!.getLoanList(mLibraryManager)
            mPresenter!!.getAutoRenewInfo(mLibraryManager)
        }

        view.library_fragment_owl_exit.setOnClickListener {
            mPresenter!!.logoff(mUfrgsTokenManager)
        }

        if (ScreenSizeUtils.isLarge(context!!)) {
            configureLargeSize(view)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showLibraryMenu(true)
    }


    fun renewAllBooks() {
        mPresenter!!.renewAllBooks(mLibraryManager)
    }

    override fun showUserInformation(user: UfrgsUser) {
        try {
            view?.library_fragment_swipe_layout?.isRefreshing = false

            mUserData = user
            view?.library_fragment_user_info_name?.text = user.nomePessoa
            view?.library_fragment_loading?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showLoanList(ufrgsLibraryUser: UfrgsLibraryUser) {
        try {
            mLibraryUserData = ufrgsLibraryUser

            if (mLibraryUserData!!.loans.size > 0) {
                view?.library_fragment_books_recycler?.visibility = View.VISIBLE
                view?.library_fragment_owl_layout?.visibility = View.INVISIBLE
                mAdapter!!.updateBookList(mLibraryUserData!!.loans)
            } else {
                view?.library_fragment_books_recycler?.visibility = View.INVISIBLE
                view?.library_fragment_owl_layout?.visibility = View.VISIBLE
            }

            mPresenter!!.getLoanExtraInfo(mLibraryUserData!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showMessage(msg: String) {
        try {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showLoanExtraInfo(extraInfo: String) {
        view?.library_fragment_user_info_extra_info?.text = extraInfo
    }

    override fun showAutoRenewInfo(register: UfrgsLibraryRegister) {
        try {
            mRegister = register
            if (mRegister!!.isRenewActive) {
                view?.library_fragment_user_info_autorenew?.text = activity?.getString(R.string.autorenew_activated)
                (activity as MainActivity).changeMenuItemText(activity!!.getString(R.string.deactivate_autorenew))
            } else {
                view?.library_fragment_user_info_autorenew?.text = activity?.getString(R.string.autorenew_deactivated)
                (activity as MainActivity).changeMenuItemText(activity!!.getString(R.string.activate_autorenew))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showAutoRenewStatus(register: UfrgsLibraryRegister) {
        try {
            mRegister = register
            if (mRegister!!.isRenewActive) {
                view?.library_fragment_user_info_autorenew?.text = activity?.getString(R.string.autorenew_activated)
                (activity as MainActivity).changeMenuItemText(activity!!.getString(R.string.deactivate_autorenew))
            } else {
                view?.library_fragment_user_info_autorenew?.text = activity?.getString(R.string.autorenew_deactivated)
                (activity as MainActivity).changeMenuItemText(activity!!.getString(R.string.activate_autorenew))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onLogoffReady() {
        val rootFragment = parentFragment as LibraryRootFragment?
        rootFragment!!.configureNestedFragments()
    }

    override fun apiError() {
        try {
            view?.library_fragment_swipe_layout?.isRefreshing = false

            //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            view?.library_fragment_owl?.setImageDrawable(resources.getDrawable(R.drawable.owl_confused))
            view?.library_fragment_owl_msg?.text = activity?.getString(R.string.library_no_connection_error)
            view?.library_fragment_owl_retry_btn?.visibility = View.VISIBLE
            view?.library_fragment_owl_exit?.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun autorenewAction() {
        try {
            if (!mRegister!!.isRenewActive)
                mPresenter!!.autoRenewMessage(mLibraryManager)
            else
                mPresenter!!.autoRenewMessageLogoff(mLibraryManager)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun displayLogoffMessage() {
        mPresenter!!.exitMessage(mUfrgsTokenManager)
    }

    override fun onRefresh() {
        mPresenter!!.getUserInformation(mUserManager)
        mPresenter!!.getLoanList(mLibraryManager)
        mPresenter!!.getAutoRenewInfo(mLibraryManager)
    }

    private fun configureLargeSize(theView: View) {
        val multiplier = 1.75

        if (context != null) {
            theView.library_fragment_user_info_name.textSize = (context!!.resources.getDimension(R.dimen.library_username_font_size).toInt() * multiplier).toFloat()
            theView.library_fragment_user_info_autorenew.textSize = (context!!.resources.getDimension(R.dimen.library_extra_info_font_size).toInt() * multiplier).toFloat()
            theView.library_fragment_user_info_extra_info.textSize = (context!!.resources.getDimension(R.dimen.library_extra_info_font_size).toInt() * multiplier).toFloat()


            theView.library_fragment_owl_msg.textSize = (context!!.resources.getDimension(R.dimen.library_extra_info_font_size).toInt() * multiplier).toFloat()
        }
    }

    companion object {
        private val TAG = "LibraryFragment"
    }
}
