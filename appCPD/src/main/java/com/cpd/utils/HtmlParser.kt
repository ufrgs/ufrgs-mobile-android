package com.example.htmlparsertest

import org.jsoup.Jsoup

class HtmlParser() {

    companion object {

        fun completeRelativeLinks(htmlStr: String): String {

            val className = "internal-link"
            val urlPrefix = "https://ufrgs.br/ufrgs/noticias/"

            val document = Jsoup.parse(htmlStr)
            val links = document.getElementsByClass(className)

            for (l in links) {
                val fullUrl = urlPrefix + l.attr("href")
                l.attr("href", fullUrl)
            }

            return document.html()

        }

    }

}