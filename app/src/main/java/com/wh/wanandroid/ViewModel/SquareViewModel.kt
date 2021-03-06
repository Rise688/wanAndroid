package com.wh.wanandroid.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.ArticleResponseBody
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SquareViewModel : ViewModel(){

    val mToast = MutableLiveData<String>()
    var isRefreshing = mutableStateOf(false)
    var sqArti = mutableStateOf(listOf<Article>())
    var pageCount = 0
    fun init(){
        requestSquareArtcle(pageCount)
    }
    fun fresh(){
        isRefreshing.value = true
        pageCount = 0
        requestSquareArtcle(0)
    }
    fun requestSquareArtcle(num : Int){
        RetrofitHelper.service.getSquareList(num)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<ArticleResponseBody>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<ArticleResponseBody>) {
                    if(num == 0) {
                        sqArti.value = t.data.datas
                    }else{
                        sqArti.value = sqArti.value + t.data.datas
                    }
                    if(isRefreshing.value){
                        isRefreshing.value = false
                        mToast.postValue("已刷新")
                    }
                }
                override fun onError(t: Throwable) {}
            })
        pageCount++
    }
}