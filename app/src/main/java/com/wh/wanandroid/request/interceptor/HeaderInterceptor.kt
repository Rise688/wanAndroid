package com.wh.wanandroid.request.interceptor

import android.util.Log
import com.wh.wanandroid.app.Constant
import com.wh.wanandroid.utils.Preference
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    /**
     * token
     */
    private var token: String by Preference(Constant.TOKEN_KEY, "")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
        // .header("token", token)
        // .method(request.method(), request.body())

        val domain = request.url().host()
        val url = request.url().toString()
        if (domain.isNotEmpty() && (url.contains(COLLECTIONS_WEBSITE)
                    || url.contains(UNCOLLECTIONS_WEBSITE)
                    || url.contains(ARTICLE_WEBSITE)
                    || url.contains(TODO_WEBSITE)
                    || url.contains(COIN_WEBSITE))) {
            val spDomain: String by Preference(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                Log.d("cookie",cookie)
                // 将 Cookie 添加到请求头
                builder.addHeader(COOKIE_NAME, cookie)
            }
        }

        return chain.proceed(builder.build())
    }

}