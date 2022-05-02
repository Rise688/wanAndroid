package com.wh.wanandroid.ViewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.ArticleResponseBody
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.bean.WXChapterBean
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeChatViewModel {

    var wxName = mutableStateOf(listOf<WXChapterBean>())
    var wxArtiSum = emptyList<MutableState<List<Article>>>()
    var countList = arrayOf<Int>()
    fun init(){
        requestWxName()
    }
    fun fresh(){
        init()
    }
    fun requestWxName(){
        RetrofitHelper.service.getWXChapters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<MutableList<WXChapterBean>>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<MutableList<WXChapterBean>>) {
                    wxName.value = t.data
                    countList = t.data.map { 0 }.toTypedArray()
                    wxArtiSum = t.data.map { mutableStateOf(emptyList()) }
                }
                override fun onError(t: Throwable) {}
            })
    }
    fun requestWxArticle(idx: Int){
        val id = wxName.value[idx].id
        val page = countList[idx]++
        RetrofitHelper.service.getWXArticles(id, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<ArticleResponseBody>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<ArticleResponseBody>) {
                    wxArtiSum[idx].value = wxArtiSum[idx].value + t.data.datas
//                    Log.d("ggg", "$idx -> ${wxArtiSum[idx].value.size}: $id")
                }
                override fun onError(t: Throwable) {
//                    Log.d("ggg", "$idx -> $id")
                }
            })
    }
}