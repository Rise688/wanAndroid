package com.wh.wanandroid.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.ArticleResponseBody
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.bean.KnowledgeTreeBody
import com.wh.wanandroid.request.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Query

class SystemViewModel {

    val sysTree = mutableStateOf(listOf<KnowledgeTreeBody>())
    var sysArtiSum = emptyList<MutableState<List<Article>>>()
    var countList = arrayOf<Int>()
    fun init(){
        requestSysTree()
    }

    fun requestSysTree(){
        RetrofitHelper.service.getKnowledgeTree()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<List<KnowledgeTreeBody>>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<List<KnowledgeTreeBody>>) {
                    sysTree.value = t.data
                }
                override fun onError(t: Throwable) {}
            })
    }
    fun requestSysAticle(idx: Int, cid: Int){
        val page = countList[idx]++
        RetrofitHelper.service.getKnowledgeList(page,cid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<ArticleResponseBody>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<ArticleResponseBody>) {
                    sysArtiSum[idx].value = sysArtiSum[idx].value + t.data.datas
                }
                override fun onError(t: Throwable) {}
            })
    }
}