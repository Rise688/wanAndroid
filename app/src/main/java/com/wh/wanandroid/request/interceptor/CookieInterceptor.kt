package com.wh.wanandroid.request.interceptor


import okhttp3.Interceptor
import okhttp3.Response

const val DEFAULT_TIMEOUT: Long = 15
const val SAVE_USER_LOGIN_KEY = "user/login"
const val SAVE_USER_REGISTER_KEY = "user/register"

const val COLLECTIONS_WEBSITE = "lg/collect"
const val UNCOLLECTIONS_WEBSITE = "lg/uncollect"
const val ARTICLE_WEBSITE = "article"
const val TODO_WEBSITE = "lg/todo"
const val COIN_WEBSITE = "lg/coin"

const val SET_COOKIE_KEY = "set-cookie"
const val COOKIE_NAME = "Cookie"

const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50

class CookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val domain = request.url().host()
        // set-cookie maybe has multi, login to save cookie
        if ((requestUrl.contains(SAVE_USER_LOGIN_KEY)
                    || requestUrl.contains(SAVE_USER_REGISTER_KEY))
            && !response.headers(SET_COOKIE_KEY).isEmpty()) {
            val cookies = response.headers(SET_COOKIE_KEY)
            val cookie = encodeCookie(cookies)
            saveCookie(requestUrl, domain, cookie)
        }
        return response
    }
}
fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
        .map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        .forEach {
            it.filterNot { set.contains(it) }.forEach { set.add(it) }
        }
    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }
    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }
    return sb.toString()
}

fun saveCookie(url: String?, domain: String?, cookies: String) {
    url ?: return
    var spUrl: String by com.wh.wanandroid.utils.Preference(url, cookies)
    @Suppress("UNUSED_VALUE")
    spUrl = cookies
    domain ?: return
    var spDomain: String by com.wh.wanandroid.utils.Preference(domain, cookies)
    @Suppress("UNUSED_VALUE")
    spDomain = cookies
}