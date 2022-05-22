package com.wh.wanandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.*
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.wh.wanandroid.activity.AgenWebActivity
import com.wh.wanandroid.activity.ArtiListActivity
import com.wh.wanandroid.bean.HotSearchBean
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.request.RetrofitHelper
import com.wh.wanandroid.activity.ui.theme.WanAndroidTheme
import com.wh.wanandroid.activity.ui.theme.grayWhite
import com.wh.wanandroid.activity.ui.theme.hhh
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity
data class History(
    @PrimaryKey val history: String
)
@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): MutableList<History>

    @Insert
    fun insertAll(vararg historys: History)

    @Delete
    fun delete(history: History)

    @Query("delete from history")
    fun deleteAll()
}
@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

var hotSearch = mutableStateOf(listOf<HotSearchBean>())

class SearchActivity : ComponentActivity() {

    val coroutineScope = CoroutineScope(Dispatchers.IO)
    lateinit var historyDao : HistoryDao
    val historys = mutableStateOf(listOf<History>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            this, HistoryDatabase::class.java, "searchHistory").build()
        historyDao = db.historyDao()
        coroutineScope.launch {
            historys.value = historyDao.getAll()
        }
        RetrofitHelper.service.getHotSearchData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<HttpResult<List<HotSearchBean>>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: HttpResult<List<HotSearchBean>>) {
                    hotSearch.value = t.data
                }
                override fun onError(t: Throwable) {}
            })

        setContent {
            WanAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var key by remember{ mutableStateOf("") }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    TextField(
                                        value = key,
                                        onValueChange = {
                                            key = it
                                        },
                                        singleLine = true,
                                        placeholder = {
                                            Text("发现更多干货")
                                        },
                                    )
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
                                actions = {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                for (history in historys.value) {
                                                    if(history.history.equals(key)){
                                                        historyDao.delete(History(key))
                                                    }
                                                }
                                                historyDao.insertAll(History(key))
                                                historys.value = historyDao.getAll()
                                            }
                                            openActivity(key)
                                        }
                                    ) {
                                        Icon(Icons.Filled.Search, null)
                                    }
                                }
                            )
                        },
                    ){
                        Column() {
                            Text("搜索热词",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(15.dp,10.dp,0.dp,0.dp))
                            FlowRow(
                                modifier = Modifier.padding(10.dp),
                                mainAxisSize = SizeMode.Expand,
                            ) {
                                hotSearch.value.forEach {
                                        hotSearchBean -> RowItem(hotSearchBean)
                                }
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                            ){
                                Text("历史记录",
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(15.dp,0.dp,0.dp,10.dp))

                            }
                            LazyColumn{
                                historys.value.asReversed().forEach() { history ->
                                    item{
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(15.dp, 0.dp)
                                            .clickable(onClick = {
                                                openActivity(history.history)
                                            }),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ){
                                            Text(history.history,
                                                fontSize = 16.sp,
                                                modifier = Modifier
                                                    .padding(10.dp,5.dp))
                                            IconButton(
                                                onClick = {
                                                    coroutineScope.launch {
                                                        historyDao.delete(history)
                                                        historys.value = historyDao.getAll()
                                                    }
                                                }
                                            ){
                                                Icon(Icons.Filled.Close, null)
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
    @Composable
    fun RowItem(hotItem : HotSearchBean) {
        Card(
            backgroundColor = hhh,
            modifier = Modifier
                .padding(6.dp) // 外边距
                .clickable {
                    coroutineScope.launch {
                        for (history in historys.value) {
                            if(history.history.equals(hotItem.name)){
                                historyDao.delete(History(hotItem.name))
                            }
                        }
                        historyDao.insertAll(History(hotItem.name))
                        historys.value = historyDao.getAll()
                    }
                    openActivity(hotItem.name)
                },
            elevation = 0.dp // 设置阴影
        ) {
            Text(text = hotItem.name,
                Modifier
                    .padding(8.dp))
        }
    }
    fun openActivity(key : String){
        val intent = Intent(this@SearchActivity, ArtiListActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)
    }
}




