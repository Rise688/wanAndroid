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
import retrofit2.http.Field
import retrofit2.http.Path

class ArtiListViewModel : ViewModel() {
    val mToast = MutableLiveData<String>()
    var isRefreshing = mutableStateOf(false)
    var Arti = mutableStateOf(listOf<Article>())
    var pageCount = 0
    var Key = ""
    fun init(key: String){
        Key = key
        queryKeyAtri(pageCount)
    }
    fun fresh(){
        isRefreshing.value = true
        pageCount = 0
        queryKeyAtri(0)
    }
    fun queryKeyAtri(page: Int){
        RetrofitHelper.service.queryBySearchKey(page, Key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<ArticleResponseBody>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<ArticleResponseBody>) {
                    if(page == 0) {
                        Arti.value = t.data.datas
                    }else{
                        Arti.value = Arti.value + t.data.datas
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