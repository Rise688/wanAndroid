package com.wh.wanandroid.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.bean.HttpResultNullable
import com.wh.wanandroid.bean.WXChapterBean
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field

object CollectViewModel : ViewModel(){
    val mToast = MutableLiveData<String>()
    fun addColArti(id : Int){
        RetrofitHelper.service.addCollectArticle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResultNullable<Any>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResultNullable<Any>) {
                    mToast.postValue("收藏成功")
                }
                override fun onError(t: Throwable) {
                }
            })
    }
    fun cancelColArti(id : Int){
        RetrofitHelper.service.cancelCollectArticle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResultNullable<Any>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResultNullable<Any>) {
                    mToast.postValue("已取消收藏")
                }
                override fun onError(t: Throwable) {
                }
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