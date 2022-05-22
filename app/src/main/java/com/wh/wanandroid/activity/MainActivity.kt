package com.wh.wanandroid


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wh.wanandroid.ViewModel.*
import com.wh.wanandroid.activity.AgenWebActivity
import com.wh.wanandroid.activity.SyaArtiActivity
import com.wh.wanandroid.bean.KnowledgeTreeBody
import com.wh.wanandroid.request.requestPic
import com.wh.wanandroid.activity.ui.theme.*
import com.wh.wanandroid.app.App
import com.wh.wanandroid.utils.Preference
import com.wh.wanandroid.viewItem.LazyListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Message(var image: ImageVector, var itemText: String)

val messages = mutableListOf<Message>(
    Message(Icons.Outlined.Favorite, "我的收藏"),
    Message(Icons.Outlined.Share, "我的分享"),
    Message(Icons.Outlined.PendingActions, "TODO清单"),
    Message(Icons.Outlined.NightsStay, "夜间模式"),
    Message(Icons.Outlined.PowerSettingsNew, "退出登录")
)
val collectModel = CollectViewModel
val mainModel = MainViewModel()
val homeModel = HomeViewModel()
var bannerDate = homeModel.bann
val openDialog = mutableStateOf(false)
val squareModel = SquareViewModel()
val wxChapterViewModel = WeChatViewModel()

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                    SetALert()
                }
            }
        }
        App.context = applicationContext
        homeModel.init()
        wxChapterViewModel.init()
        homeModel.mToast.observe(this) { showToast(it) }
        collectModel.mToast.observe(this) { showToast(it) }
        squareModel.mToast.observe(this) { showToast(it) }
        wxChapterViewModel.mToast.observe(this) { showToast(it) }
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
                        Text(items[selectedItem])
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
//            floatingActionButton = {
//                IconButton(
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(CircleShape)
//                        .border(1.5.dp, MaterialTheme.colors.secondary, shape = CircleShape),
//                    onClick = { }
//                ) {
//                    Icon(Icons.Filled.ArrowUpward, null)
//                }
//            },
            bottomBar = {
                BottomNavigation {
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Home, contentDescription = null)
                                    1 -> Icon(Icons.Filled.TravelExplore, contentDescription = null)
                                    2 -> Icon(painterResource(R.drawable.ic_wechat),modifier = Modifier.size(24.dp),contentDescription = null)
                                    3 -> Icon(Icons.Filled.AccountTree, contentDescription = null)
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
//            homeModel.mToast.observeAsState().value?.apply{
//                if(this != "") {
//                    showToast(this)
//                    homeModel.mToast.postValue("")
//                }
//                Log.d("frgggg","333333")
//            }
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
                intent.putExtra("url", "https://www.zhihu.com/")
                startActivity(intent)
            }
            "夜间模式" -> {}
            "退出登录" -> {
                openDialog.value = true
            }
        }
    }

    @Composable
    fun SetALert(){
        val scope = rememberCoroutineScope()
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // 当用户点击对话框以外的地方或者按下系统返回键将会执行的代码
                    openDialog.value = false
                },
                title = {
                    Text(
                        text = "是否退出登录？",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.h6
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mainModel.outlogin()
                            scope.launch {
                                Preference.clearPreference()
                            }
                            Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()
                            openDialog.value = false
                        },
                    ) {
                        Text(
                            "确认",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.button
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text(
                            "取消",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            )
        }
    }

    fun showToast(mes : String){
        Toast.makeText(this,mes,Toast.LENGTH_SHORT).show()
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

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Home() {
        val pagerState = rememberPagerState(
            initialPage = 0, infiniteLoop = true,//总页数
            pageCount = bannerDate.value.size,
            //预加载的个数
            initialOffscreenLimit = 1,
        )//初始页面
        val imageState = bannerDate.value.map { banner ->
            requestPic.loadImage(
                context = LocalContext.current,
                url = banner.imagePath,
            )
        }
        val refreshing by homeModel.isRefreshing
        val refreshState = rememberSwipeRefreshState(refreshing)
        val listState = rememberLazyListState()
        val listData by homeModel.mHomeArticleData.observeAsState()
        SwipeRefresh(
            state = refreshState, onRefresh = { homeModel.fresh() }
        ) {
            var currentPageIndex = 0
            var executeChangePage by remember { mutableStateOf(false) }
            LazyColumn(state = listState) {
                item {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.pointerInput(pagerState.currentPage) {
                                awaitPointerEventScope {
                                    while (true) {
                                        //PointerEventPass.Initial - 本控件优先处理手势，处理后再交给子组件
                                        val event = awaitPointerEvent(PointerEventPass.Initial)
                                        //获取到第一根按下的手指
                                        val dragEvent = event.changes.firstOrNull()
                                        when {
                                            //当前移动手势是否已被消费
                                            dragEvent!!.positionChangeConsumed() -> {
                                                return@awaitPointerEventScope
                                            }
                                            //是否已经按下(忽略按下手势已消费标记)
                                            dragEvent.changedToDownIgnoreConsumed() -> {
                                                //记录下当前的页面索引值
                                                currentPageIndex = pagerState.currentPage
                                            }
                                            //是否已经抬起(忽略按下手势已消费标记)
                                            dragEvent.changedToUpIgnoreConsumed() -> {
                                                //当pageCount大于1，且手指抬起时如果页面没有改变，就手动触发动画
                                                if (currentPageIndex == pagerState.currentPage && pagerState.pageCount > 1) {
                                                    executeChangePage = !executeChangePage
                                                }
                                            }
                                        }
                                    }
                                }
                            }
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
                        LaunchedEffect(pagerState.currentPage, executeChangePage) {
                            if (pagerState.pageCount > 0) {
                                delay(2000)
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                }
                listData?.also {
                    itemsIndexed(it) { idx, article ->
                        if (idx > 0) Divider(thickness = 1.dp)
                        LazyListItem.ArticleItem(article,collectEvent,onClickEvent)
                        LaunchedEffect(it.size) {
                            if(it.size - idx < 2) homeModel.requestArticle(homeModel.pageCount)
                        }
                    }
                }
                item {
        //             Text("加载更多")
                }
            }
        }
    }

    @Composable
    fun Square() {
        val sqArtiState = squareModel.sqArti
        squareModel.init()
        val listState = rememberLazyListState()
        val refreshing by squareModel.isRefreshing
        val refreshState = rememberSwipeRefreshState(refreshing)
        SwipeRefresh(state = refreshState, onRefresh = { squareModel.fresh() })
        {
            LazyColumn(state = listState) {
                sqArtiState.value.apply {
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
                                squareModel.requestSquareArtcle(squareModel.pageCount)
                            }
                    }
                }
            }
        }
    }


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
        var curPageId = 0
        val refreshing by wxChapterViewModel.isRefreshing
        val refreshState = rememberSwipeRefreshState(refreshing)
        SwipeRefresh(state = refreshState, onRefresh = { wxChapterViewModel.fresh(curPageId) })
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val rowScrollState = rememberLazyListState()
                LaunchedEffect(pagerState.currentPage) {
                    curPageId = pagerState.currentPage
                    rowScrollState.scrollToItem(pagerState.currentPage)
                }
                LazyRow(
                    state = rowScrollState,
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    itemsIndexed(wxChapterViewModel.wxName.value) { i, wxCBean ->
                        //颜色
                        val color = if (pagerState.currentPage == i) trueWhite else grayWhite
                        Column(
                            Modifier.clickable {
                                scope.launch {
                                    curPageId = i
                                    pagerState.animateScrollToPage(i)
                                }
                            },
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                wxCBean.name, color = color,
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
                    LazyColumn(state = listState) {
                        wxChapterViewModel.wxArtiSum.get(page).value.apply {
                            itemsIndexed(this) { idx, article ->
                                if (idx > 0) Divider(thickness = 1.dp)
                                LazyListItem.ArticleItem(article,collectEvent,onClickEvent)
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
    }

    @Composable
    fun SystemView() {
        val intent = Intent(this, SyaArtiActivity::class.java)
        val sysModel = SystemViewModel()
        sysModel.init()
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
            itemsIndexed(sysModel.sysTree.value) { idx, klBody ->
                Column(
                    Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .clickable(onClick = {
                            intent.putExtra("body", klBody)
                            startActivity(intent)
                        }),
                ){
                if (idx > 0) Divider(thickness = 1.dp)
                Text(
                    text = klBody.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp
                    ),
                    modifier = Modifier.padding(15.dp,10.dp)
                )
                var sys: String = ""
                klBody.forEachLeaf{ leaf ->
                    sys += "   ${leaf.name}"
                }
                Text(sys.trim(),
                    modifier = Modifier.padding(15.dp,0.dp,50.dp,10.dp),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray)
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

    @Composable
    fun Project() {

    }

}