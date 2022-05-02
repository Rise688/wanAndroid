package com.wh.wanandroid.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.bean.WXChapterBean
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field

class CollectViewMoedl {

    fun addColArti(id : Int){
        RetrofitHelper.service.addCollectArticle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<Any>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<Any>) {
                    Log.d("qqq",t.toString())
                }
                override fun onError(t: Throwable) {}
            })
    }
    fun cancelColArti(id : Int){
        RetrofitHelper.service.cancelCollectArticle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<Any>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<Any>) {
                    Log.d("qqq",t.toString())
                }
                override fun onError(t: Throwable) {}
            })
    }
    fun addOutColArti(title: String, author: String, link: String){
        RetrofitHelper.service.addCoolectOutsideArticle(title, author, link)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<Any>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<Any>) {
                    Log.d("qaq",t.toString())
                }
                override fun onError(t: Throwable) {}
            })
    }
}