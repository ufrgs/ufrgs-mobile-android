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
package com.cpd.ui.ru_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpd.adapters.RuAdapter
import com.cpd.adapters.RuClosedAdapter
import com.cpd.adapters.RuWarningAdapter
import com.cpd.extensions.px
import com.cpd.network.models.Ru
import com.cpd.ufrgsmobile.R
import com.cpd.utils.CacheUtils
import com.cpd.utils.DateUtils
import com.cpd.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.fragment_restaurant.view.*
import org.jetbrains.anko.toast

/**
 * RU Fragment.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class RuFragment : Fragment(), RuScreenContract.View {

    private var mAdapter: RuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("ON CREATE")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant, container, false)

        view.restaurant_fragment_recycler.setHasFixedSize(true)

        println("ON CREATE VIEW")

        mAdapter = RuAdapter(activity!!, listOf<Ru>())
        mAdapter!!.onItemClickCompletion = lambda@ {
            view.restaurant_fragment_recycler.scrollToPosition(it)
        }

        view.restaurant_fragment_swipe_layout.setProgressViewOffset(false, 0, 100.px)
        view.restaurant_fragment_swipe_layout.setColorSchemeResources(R.color.primaryColor)
        view.restaurant_fragment_swipe_layout.setOnRefreshListener {
            getRuInformation()
        }

        view.restaurant_fragment_recycler.adapter = RuWarningAdapter(activity!!, RuWarningAdapter.LOADING)
        view?.restaurant_fragment_recycler?.layoutManager = LinearLayoutManager(context)

        getRuInformation()

        return view
    }

    private fun getRuInformation() {
        showLoading()

        val presenter = RuScreenPresenter(this)
        presenter.getRuInformation()
    }

    // -- Show result of loading

    override fun showRuInfo(list: List<Ru>) {

        hideLoading()

        view?.restaurant_fragment_swipe_layout?.isRefreshing = false

        view?.restaurant_fragment_recycler?.adapter = mAdapter!!
        view?.restaurant_fragment_recycler?.layoutManager = LinearLayoutManager(context)

        mAdapter!!.updateWholeList(list)

    }

    override fun showFailMessage() {

        hideLoading()

        if (DateUtils.todayIsWeekend()) {
            showRuClosedAlert()
            return
        }

        if (!NetworkUtils.isConnectedToNetwork(this.context!!)) {
            showNoInternetAlert()
            return
        }

        showMenuUnavailableAlert(getString(R.string.menu_is_unavailable))
    }

    override fun showMessage(msg: String) {
        view?.restaurant_fragment_swipe_layout?.isRefreshing = false
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showNoInternetAlert() {
        if (CacheUtils.getRuList().isEmpty()) {
            showMenuUnavailableAlert(getString(R.string.connection_error_message))
        } else {
            showRuInfo(CacheUtils.getRuList())
            context!!.toast(R.string.connection_error_message)
        }
    }

    private fun showRuClosedAlert() {
        view?.restaurant_fragment_swipe_layout?.isRefreshing = false

        view?.restaurant_fragment_recycler?.adapter = RuClosedAdapter(activity!!)
        view?.restaurant_fragment_recycler?.layoutManager = LinearLayoutManager(context)
    }

    private fun showMenuUnavailableAlert(message: String) {
        view?.restaurant_fragment_swipe_layout?.isRefreshing = false

        val adapter = RuWarningAdapter(activity!!, RuWarningAdapter.MENU_UNAVAILABLE)
        adapter.message = message

        view?.restaurant_fragment_recycler?.adapter = adapter
        view?.restaurant_fragment_recycler?.layoutManager = LinearLayoutManager(context)
    }

    private fun showLoading() {
        if (restaurant_fragment_progress_bar != null) {
            if (!(view?.restaurant_fragment_swipe_layout?.isRefreshing)!!) {
                restaurant_fragment_progress_bar!!.visibility = View.VISIBLE
            }
        }
    }

    private fun hideLoading() {
        if (restaurant_fragment_progress_bar != null) {
            restaurant_fragment_progress_bar!!.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): RuFragment {
            return RuFragment()
        }
    }
}
