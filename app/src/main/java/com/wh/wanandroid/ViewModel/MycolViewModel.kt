package com.wh.wanandroid.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.BaseListResponseBody
import com.wh.wanandroid.bean.CollectionArticle
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MycolViewModel : ViewModel() {
    val mToast = MutableLiveData<String>()
    var isRefreshing = mutableStateOf(false)
    var Arti = mutableStateOf(listOf<CollectionArticle>())
    var pageCount = 0

    fun init(){
        getColArtiList(pageCount)
    }
    fun fresh(){
        isRefreshing.value = true
        pageCount = 0
        getColArtiList(0)
    }
    fun getColArtiList(page: Int){
        RetrofitHelper.service.getCollectList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<BaseListResponseBody<CollectionArticle>>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<BaseListResponseBody<CollectionArticle>>) {
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
    }

}