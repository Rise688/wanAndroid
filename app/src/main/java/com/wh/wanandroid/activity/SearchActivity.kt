package com.wh.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.*
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.wh.wanandroid.bean.HotSearchBean
import com.wh.wanandroid.bean.HttpResult
import com.wh.wanandroid.request.RetrofitHelper
import com.wh.wanandroid.activity.ui.theme.WanAndroidTheme
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity
data class History(
    @PrimaryKey val history: String,
    @ColumnInfo(name = "count") val count: Int = 0,
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            this, HistoryDatabase::class.java, "searchHistory").build()
        val historyDao = db.historyDao()
        val historys = mutableStateOf(listOf<History>())
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
                // A surface container using the 'background' color from the theme
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
                                                historyDao.insertAll(History(key,1))
                                                historys.value = historyDao.getAll()
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Filled.Search, null)
                                    }
                                }
                            )
                        },
                    ){
                        Column() {
                            FlowRow(
                                modifier = Modifier.padding(20.dp),
                                mainAxisSize = SizeMode.Expand,
                            ) {
                                hotSearch.value.forEach {
                                        hotSearchBean -> RowItem(hotSearchBean)
                                }
                            }
                            Text("历史记录")
                            LazyColumn{
                                historys.value.forEach {
                                        history ->  item{
                                    Text(history.history)
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
        modifier = Modifier
            .padding(15.dp) // 外边距
            .clickable {
            },
        elevation = 10.dp // 设置阴影
    ) {
        Text(text = hotItem.name)
    }
}


