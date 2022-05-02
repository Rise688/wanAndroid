package com.wh.wanandroid.ViewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.wh.wanandroid.bean.*
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field

class LoginViewModel {

    var data = mutableStateOf(BaseBean())
    var logindata : MutableState<LoginData?> = mutableStateOf(null)

    fun login(username: String, password: String) {
        RetrofitHelper.service.loginWanAndroid(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResultNullable<LoginData>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResultNullable<LoginData>) {
                    Log.d("wwww",data.value.errorCode.toString())
                    data.value.errorCode = t.errorCode
                    data.value.errorMsg = t.errorMsg
                    if(t.data != null){
                        logindata.value = t.data
                    }
                }
                override fun onError(t: Throwable) {
                    Log.d("wwww","err")
                }
            })
    }
}