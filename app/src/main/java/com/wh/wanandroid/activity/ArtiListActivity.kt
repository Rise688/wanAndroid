package com.wh.wanandroid.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wh.wanandroid.History
import com.wh.wanandroid.ViewModel.ArtiListViewModel
import com.wh.wanandroid.activity.ui.theme.WanAndroidTheme
import com.wh.wanandroid.bean.ArticleResponseBody
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.collectModel
import com.wh.wanandroid.request.RetrofitHelper
import com.wh.wanandroid.squareModel
import com.wh.wanandroid.viewItem.LazyListItem
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ArtiListActivity : ComponentActivity() {

    val artiModel = ArtiListViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = getIntent()
        val key = intent.extras?.getString("key")
        key?.apply { artiModel.init(this) }
        artiModel.mToast.observe(this) { showToast(it) }
        setContent {
            WanAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    key?.apply { Text(this) }
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            this.finish()
                                        }
                                    ) {
                                        Icon(Icons.Filled.ArrowBack, null)
                                    }
                                },
                            )
                        },
                    ){
                        ArtiList()
                    }
                }
            }
        }
    }

    fun showToast(mes : String){
        Toast.makeText(this,mes, Toast.LENGTH_SHORT).show()
    }

    val onClickEvent : (String,String) -> Unit =  { url, title ->
        val intent = Intent(this, AgenWebActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    val collectEvent : (Boolean, Int) -> Unit = { col, id ->
        if(col){
            collectModel.cancelColArti(id)
        }else{
            collectModel.addColArti(id)
        }
    }
    @Composable
    fun ArtiList(){
            val artiState = artiModel.Arti
            val listState = rememberLazyListState()
            val refreshing by artiModel.isRefreshing
            val refreshState = rememberSwipeRefreshState(refreshing)
            SwipeRefresh(state = refreshState, onRefresh = { artiModel.fresh() })
            {
                LazyColumn(state = listState) {
                    artiState.value.apply {
                        itemsIndexed(this) { idx, article ->
                            if (idx > 0) Divider(thickness = 1.dp)
                            LazyListItem.ArticleItem(article,collectEvent,onClickEvent)
                        }
                    }
                    item {
//                Text("加载更多")
                        val layoutInfo = listState.layoutInfo
                        val shouldLoadMore = remember {
                            derivedStateOf {
                                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                                    ?: return@derivedStateOf true
                                lastVisibleItem.index == layoutInfo.totalItemsCount - 1
                            }
                        }
                        LaunchedEffect(shouldLoadMore) {
                            snapshotFlow { shouldLoadMore.value }
                                .collect {
                                    artiModel.queryKeyAtri(artiModel.pageCount)
                                }
                        }
                    }
                }
            }
    }

}



