package com.wh.wanandroid.ViewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import com.wh.wanandroid.app.Constant
import com.wh.wanandroid.bean.*
import com.wh.wanandroid.request.RetrofitHelper
import com.wh.wanandroid.utils.Preference
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.http.Field

class LoginViewModel {

    companion object{
        var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    }
    var data = mutableStateOf(BaseBean())
    var logindata : MutableState<LoginData?> = mutableStateOf(null)
    private val mScope = MainScope()
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
                        mScope.launch {
                            isLogin = true
                        }
                    }
                }
                override fun onError(t: Throwable) {
                    Log.d("wwww","err")
                }
            })
    }
}