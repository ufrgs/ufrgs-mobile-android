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
package com.cpd.ui.news_screen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cpd.adapters.NewsAdapter
import com.cpd.network.models.News
import com.cpd.ufrgsmobile.R
import com.cpd.ui.news_screen.news_detail.NewsOpenActivity
import com.cpd.utils.AppTags
import com.cpd.utils.CacheUtils
import com.cpd.utils.ItemClickSupport
import kotlinx.android.synthetic.main.activity_news_open.*
import kotlinx.android.synthetic.main.activity_news_open_image.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.news_list_item_highlight_rounded.view.*
import android.util.Pair as UtilPair
import androidx.core.util.Pair
import kotlinx.android.synthetic.main.news_list_item.view.*

/**
 * Fragment that holds the news list.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class NewsFragment : Fragment(), NewsScreenContract.View, ItemClickSupport.OnItemClickListener {

    private var mAdapter: NewsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        view.news_fragment_recycler.setHasFixedSize(true)
        view.news_fragment_recycler.layoutManager = createLayoutManager()

        mAdapter = NewsAdapter(activity!!, CacheUtils.getNewsList())
        view.news_fragment_recycler.adapter = mAdapter

        view.news_fragment_swipe_layout.setColorSchemeResources(R.color.primaryColor)
        view.news_fragment_swipe_layout.setOnRefreshListener { getNews() }

        getNews()

        ItemClickSupport.addTo(view.news_fragment_recycler).setOnItemClickListener(this)

        return view
    }

    private fun getNews() {
        val presenter = NewsScreenPresenter(this)
        presenter.fetchNews()
    }

    override fun showNews(newsList: List<News>) {
        view?.news_fragment_swipe_layout?.isRefreshing = false
        mAdapter!!.updateList(newsList)
    }

    override fun showMessage(msg: String) {
        try {
            view?.news_fragment_swipe_layout?.isRefreshing = false
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
        val newsVo = mAdapter!!.getItemAtPosition(position)
        if (newsVo != null) {
            val i = Intent(activity, NewsOpenActivity::class.java)
            i.putExtra(AppTags.NEWS_VO, newsVo)

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                var theView: View? = null

                // if a normal list item was clicked
                if (v.news_list_item_thumb_image != null) {
                    theView = v.news_list_item_thumb_image
                }

                // if some view was found
                if (theView!= null) {
                    val pairImage = Pair.create<View, String>(theView, R.string.transition_name_news_image.toString())

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.activity!!, pairImage)

                    startActivity(i, options.toBundle())
                    return
                }
            }

            startActivity(i)
        }

    }

    // For larger devices (like tablets), it creates more columns.
    private fun createLayoutManager(): StaggeredGridLayoutManager {

        val columnsNumber = activity!!.resources.getInteger(R.integer.news_columns)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(columnsNumber, StaggeredGridLayoutManager.VERTICAL)

        staggeredGridLayoutManager!!.spanCount = columnsNumber

        return staggeredGridLayoutManager
    }

    companion object {
        private val TAG = NewsFragment::class.java.simpleName

        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }
}
