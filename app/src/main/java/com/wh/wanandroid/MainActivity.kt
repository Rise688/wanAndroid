package com.wh.wanandroid


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wh.wanandroid.ui.theme.WanAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {  WanAndroidTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                Greeting("Android")
            }
        } }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ScaffoldDemo()
//        WanAndroidTheme {
//            Greeting("Android")
//        }
//        SwipeRefresh(
//                state = rememberSwipeRefreshState(true),
//        onRefresh = {  },
//        ) {
//            LazyColumn {
//
//            }
//        }
    }

    @Composable
    fun ScaffoldDemo(){
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("首页", "广场", "公众号","体系","项目")
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("主页")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { } //do something
                        ) {
                            Icon(Icons.Filled.Menu, null)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { } //do something
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
                ){
                    Icon(Icons.Filled.ArrowUpward, null)
                }
            },
            bottomBar = {
                BottomNavigation {
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                when(index){
                                    0 -> Icon(Icons.Filled.Home, contentDescription = null)
                                    1 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    2 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    3 -> Icon(Icons.Filled.Favorite, contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                                   },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            }
        ) {

        }
    }

}
