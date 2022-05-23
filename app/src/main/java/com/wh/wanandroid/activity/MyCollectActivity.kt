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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wh.wanandroid.ViewModel.CollectViewModel
import com.wh.wanandroid.ViewModel.MycolViewModel
import com.wh.wanandroid.activity.ui.theme.WanAndroidTheme
import com.wh.wanandroid.viewItem.LazyListItem

class MyCollectActivity : ComponentActivity() {


    val myColmodelc = MycolViewModel()
    val collectModel = CollectViewModel

    fun showToast(mes : String){
        Toast.makeText(this,mes, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectModel.mToast.observe(this) { showToast(it) }
        setContent {
            WanAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                     Text("我的收藏")
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
                        MyCollectList()
                    }

                }
            }
        }
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
    fun MyCollectList() {
        val artiState = myColmodelc.Arti
        val listState = rememberLazyListState()
        val refreshing by myColmodelc.isRefreshing
        val refreshState = rememberSwipeRefreshState(refreshing)
        SwipeRefresh(state = refreshState, onRefresh = { myColmodelc.fresh() })
        {
            LazyColumn(state = listState) {
                artiState.value.apply {
                    itemsIndexed(this) { idx, article ->
                        if (idx > 0) Divider(thickness = 1.dp)
                        LazyListItem.CollectArtiItem(article,collectEvent,onClickEvent)
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
                                myColmodelc.getColArtiList(myColmodelc.pageCount)
                            }
                    }
                }
            }
        }
    }

}

