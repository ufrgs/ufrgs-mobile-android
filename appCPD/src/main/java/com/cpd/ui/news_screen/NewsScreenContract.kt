package com.cpd.ui.news_screen

import com.cpd.network.models.News

/**
 * Created by theolm on 19/05/17.
 */

interface NewsScreenContract {
    interface View {
        fun showNews(newsList: List<News>)
        fun showMessage(msg: String)
    }

    interface Presenter {
        fun fetchNews()
    }
}
