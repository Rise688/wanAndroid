package com.wh.wanandroid


import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.wh.wanandroid.ViewModel.HomeViewModel
import com.wh.wanandroid.ViewModel.SquareViewModel
import com.wh.wanandroid.ViewModel.WeChatViewModel
import com.wh.wanandroid.request.requestPic
import com.wh.wanandroid.ui.theme.*
import com.wh.wanandroid.view.LazyListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Message(var image: ImageVector, var itemText: String)

val messages = mutableListOf<Message>(
    Message(Icons.Outlined.Favorite, "我的收藏"),
    Message(Icons.Outlined.Share, "我的分享"),
    Message(Icons.Outlined.PendingActions, "TODO清单"),
    Message(Icons.Outlined.NightsStay, "夜间模式"),
//    Message(Icons.Outlined.Settings,"设置"),
    Message(Icons.Outlined.PowerSettingsNew, "退出登录")
)
val homeModel = HomeViewModel()
var articlesState = homeModel.arti
var bannerDate = homeModel.bann

class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
        homeModel.init()
        wxChapterViewModel.init()
//        wxChapterViewModel.requestWxArticle(408,0)
    }

    @Composable
    fun MainView() {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("首页", "广场", "公众号", "体系", "项目")
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    title = {
                        Text("主页")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Menu, null)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Icon(Icons.Filled.Search, null)
                        }
                    }
                )
            },
            floatingActionButton = {
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colors.secondary, shape = CircleShape),
                    onClick = { }
                ) {
                    Icon(Icons.Filled.ArrowUpward, null)
                }
            },
            bottomBar = {
                BottomNavigation {
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Home, contentDescription = null)
                                    1 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    2 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    3 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                            }
                        )
                    }
                }
            },
            drawerContent = {
                DrawerContent(scaffoldState, scope)
            },
            drawerShape = customShape(),
            drawerElevation = 100.dp,
        ) {
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                when (selectedItem) {
                    0 -> Home()
                    1 -> Square()
                    2 -> Wx()
                    3 -> SystemView()
                    else -> Project()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DrawerContent(
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {

        Box {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
//                Image(
//                    painter = Icons.Filled.People,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(65.dp)
//                        .border(BorderStroke(1.dp, Color.Gray), CircleShape)
//                )
                Spacer(Modifier.padding(vertical = 8.dp))
                Text(
                    text = "游客12345",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Column {
            messages.forEach { message ->
                MessageRow(message) { clickEvent(message.itemText) }
            }
        }
        // 如果 drawer 已经展开了，那么点击返回键收起而不是直接退出 app
        BackHandler(enabled = scaffoldState.drawerState.isOpen) {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }

    @Composable
    fun MessageRow(message: Message, clickEvent: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = clickEvent)
                .padding(10.dp, 20.dp),
        ) {
            Icon(
                imageVector = message.image,
                contentDescription = null
            )
            Text(text = message.itemText)
        }
    }

    val clickEvent: (String) -> Unit = { tag ->
        when (tag) {
            "我的收藏" -> {
                Toast.makeText(this@MainActivity, "我的收藏", Toast.LENGTH_SHORT).show()
            }
            "我的分享" -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            "TODO清单" -> {
                val intent = Intent(this, AgenWebActivity::class.java)
                intent.putExtra("url","https://www.zhihu.com/")
                startActivity(intent)
            }
            "夜间模式" -> {}
            "退出登录" -> {}
        }
    }

    fun OnClickEvent(url : String,title : String){
        val intent = Intent(this, AgenWebActivity::class.java)
        intent.putExtra("url",url)
        intent.putExtra("title",title)
        startActivity(intent)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Home() {
        var articlePageCount = 0
        val pagerState = rememberPagerState(initialPage = 0, infiniteLoop = true,//总页数
            pageCount = bannerDate.value.size,
            //预加载的个数
            initialOffscreenLimit = 1,)//初始页面
        val imageState = bannerDate.value.map { banner ->
            requestPic.loadImage(
                context = LocalContext.current,
                url = banner.imagePath,
            )
        }
        val listState = rememberLazyListState()
        LazyColumn(state = listState){
            item{
                Box(contentAlignment = Alignment.BottomEnd) {
                    HorizontalPager(
                        state = pagerState,
                    ) { page ->
                        imageState[page].value?.asImageBitmap()?.let { bitmap ->
                            Image(
                                bitmap = bitmap,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                    }
                }
                //   自动滚动
                if (pagerState.pageCount > 0) {
                    LaunchedEffect(pagerState.currentPage) {
                        if (pagerState.pageCount > 0) {
                            delay(2000)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            }
            articlesState.value.apply {
                itemsIndexed(this) { idx, article ->
                    if (idx > 0) Divider(thickness = 1.dp)
                    LazyListItem.ArticleItem(article) { OnClickEvent(article.link, article.title) }
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
                            homeModel.requestArticle(++articlePageCount)
                        }
                }
            }
        }
    }

    @Composable
    fun Square() {
        val squareModel = SquareViewModel()
        val sqArtiState = squareModel.sqArti
        squareModel.init()
        var articlePageCount = 0
        val listState = rememberLazyListState()
        LazyColumn(state = listState){
            sqArtiState.value.apply {
                itemsIndexed(this) { idx, article ->
                    if (idx > 0) Divider(thickness = 1.dp)
                    LazyListItem.ArticleItem(article) { OnClickEvent(article.link, article.title) }
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
                            squareModel.requestSquareArtcle(++articlePageCount)
                        }
                }
            }
        }
    }

    val wxChapterViewModel = WeChatViewModel()
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Wx() {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            initialPage = 0, //总页数
            pageCount = wxChapterViewModel.wxArtiSum.size,
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
                    .background(MaterialTheme.colors.primary)
                    ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                itemsIndexed(wxChapterViewModel.wxName.value) { i, wxCBean ->
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
                        Text(wxCBean.name, color = color,
                            modifier = Modifier.padding(top = 5.dp,start = 15.dp,end = 15.dp,bottom = 15.dp),
                            fontSize = 14.sp)
                    }
                }
            }
            HorizontalPager(state = pagerState) { page ->
                val listState = rememberLazyListState()
//                wxChapterViewModel.requestWxArticle(page)
                LazyColumn(state = listState){
                    wxChapterViewModel.wxArtiSum.get(page).value.apply {
                        itemsIndexed(this) { idx, article ->
                            if (idx > 0) Divider(thickness = 1.dp)
                            LazyListItem.ArticleItem(article) { OnClickEvent(article.link, article.title) }
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
                                    wxChapterViewModel.requestWxArticle(page)
                                }
                        }
                    }
                }
            }
        }
    }
    @Composable
    fun SystemView() {

    }

    @Composable
    fun Project() {

    }
}
