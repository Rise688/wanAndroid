package com.wh.wanandroid.ViewModel

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
import retrofit2.http.Path

class WeChatViewModel {

    var wxName = mutableStateOf(listOf<WXChapterBean>())
    var wxArti = mutableStateOf(listOf<Article>())
    fun init(){
        requestWxName()
    }
    fun requestWxName(){
        RetrofitHelper.service.getWXChapters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<MutableList<WXChapterBean>>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<MutableList<WXChapterBean>>) {
                    wxName.value = wxName.value + t.data
                }
                override fun onError(t: Throwable) {}
            })
    }
    fun requestWxArticle(id: Int, page: Int){
        RetrofitHelper.service.getWXArticles(id,page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<ArticleResponseBody>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<ArticleResponseBody>) {
                    wxArti.value = wxArti.value + t.data.datas
                }
                override fun onError(t: Throwable) {}
            })
    }
}