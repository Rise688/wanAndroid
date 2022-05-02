package com.wh.wanandroid.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wh.wanandroid.bannerDate
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.ArticleResponseBody
import com.wh.wanandroid.bean.Banner
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.homeModel
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    var arti = mutableStateOf(listOf<Article>())
    var bann = mutableStateOf(listOf<Banner>())
    var pageCount = 0
    fun init(){
        requestArticle(pageCount)
        requestBanner()
    }
    fun fresh(){
        pageCount = 0
        requestArticle(0)
    }
    fun requestArticle(num : Int){
        RetrofitHelper.service.getArticles(num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<ArticleResponseBody>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<ArticleResponseBody>) {
                    if(num == 0){
                        arti.value = t.data.datas
                    }else{
                        arti.value = arti.value + t.data.datas
                    }
                }
                override fun onError(t: Throwable) {}
            })
        pageCount++
    }
    fun requestBanner(){
        RetrofitHelper.service.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<List<Banner>>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<List<Banner>>) {
                    Log.d("gggg", t.toString())
                    bannerDate.value = t.data
                }

                override fun onError(t: Throwable) {
                    Log.d("gggg", t.toString())
                }
            })
    }
}
