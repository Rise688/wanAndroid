package com.wh.wanandroid.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wh.wanandroid.SearchActivity
import com.wh.wanandroid.ViewModel.SystemViewModel
import com.wh.wanandroid.activity.ui.theme.WanAndroidTheme
import com.wh.wanandroid.activity.ui.theme.grayWhite
import com.wh.wanandroid.activity.ui.theme.trueWhite
import com.wh.wanandroid.bean.KnowledgeTreeBody
import com.wh.wanandroid.viewItem.LazyListItem
import kotlinx.coroutines.launch

class SyaArtiActivity : ComponentActivity() {

    val systemViewModel = SystemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = getIntent()
        val klbody = intent.getSerializableExtra("body")
        systemViewModel.sysArtiSum =
            (klbody as KnowledgeTreeBody).children.map { mutableStateOf(emptyList()) }
        systemViewModel.countList =
            (klbody as KnowledgeTreeBody).children.map { 0 }.toTypedArray()
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                elevation = 0.dp,
                                title = {
                                    Text(klbody.name)
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
                        Wx(klbody as KnowledgeTreeBody)
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Wx(body : KnowledgeTreeBody) {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            initialPage = 0, //总页数
            pageCount = body.children.size,
            //预加载的个数
            initialOffscreenLimit = 1,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val rowScrollState = rememberLazyListState()
            LaunchedEffect(pagerState.currentPage) {
                rowScrollState.scrollToItem(pagerState.currentPage)
            }
            LazyRow(
                state = rowScrollState,
                modifier = Modifier
                    .background(MaterialTheme.colors.primary),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                itemsIndexed(body.children) { i, Bean ->
                    //颜色
                    val color = if (pagerState.currentPage == i) trueWhite else grayWhite
                    Column(
                        Modifier.clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        },
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            Bean.name, color = color,
                            modifier = Modifier.padding(
                                top = 5.dp,
                                start = 15.dp,
                                end = 15.dp,
                                bottom = 15.dp
                            ),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            HorizontalPager(state = pagerState) { page ->
                val listState = rememberLazyListState()
                val refreshState = rememberSwipeRefreshState(isRefreshing = false)
                    LazyColumn(state = listState) {
                        systemViewModel.sysArtiSum.get(page).value.apply {
                            itemsIndexed(this) { idx, article ->
                                if (idx > 0) Divider(thickness = 1.dp)
                                LazyListItem.ArticleItem(article) {
                                    OnClickEvent(
                                        article.link,
                                        article.title
                                    )
                                }
                            }
                        }
                        //  加载更多
                        item {
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
                                        systemViewModel.requestSysAticle(
                                            page,
                                            body.children[page].id
                                        )
                                    }
                            }
                        }
                    }
            }
        }
    }
    fun KnowledgeTreeBody.forEachLeaf(onLeaf: (KnowledgeTreeBody) -> Unit) {
        if(children.isEmpty()) {
            onLeaf(this)
        } else children.forEach {
            it.forEachLeaf(onLeaf)
        }
    }
    fun OnClickEvent(url: String, title: String) {
        val intent = Intent(this, AgenWebActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        startActivity(intent)
    }
}

