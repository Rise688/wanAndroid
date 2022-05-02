package com.wh.wanandroid.ViewModel

import android.util.Log
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.bean.HttpResultNullable
import com.wh.wanandroid.bean.LoginData
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel {

    fun outlogin() {
        RetrofitHelper.service.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<Any>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<Any>) {

                }
                override fun onError(t: Throwable) {}
            })
    }
}