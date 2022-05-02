package com.wh.wanandroid.ViewModel

import androidx.compose.runtime.mutableStateOf
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.ArticleResponseBody
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SquareViewModel {

    var sqArti = mutableStateOf(listOf<Article>())
    var pageCount = 0
    fun init(){
        requestSquareArtcle(pageCount)
    }
    fun fresh(){
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
                }
                override fun onError(t: Throwable) {}
            })
        pageCount++
    }
}