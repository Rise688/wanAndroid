package com.wh.wanandroid.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wh.wanandroid.bean.Article
import com.wh.wanandroid.bean.WXChapterBean
import com.wh.wanandroid.view.CardHelper.Companion.CardBlue
import com.wh.wanandroid.view.CardHelper.Companion.CardRed

class LazyListItem {

    companion object {
        @Composable
        fun ArticleItem(article: Article,clickEvent:()->Unit) {
            Column(
                Modifier.background(Color.White).fillMaxWidth().padding(8.dp, 3.dp)
                    .clickable(onClick = clickEvent),
            ) {
                Row(
                    modifier = Modifier.height(24.dp)
                        .fillMaxWidth().padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        if(article.top == "1") {
                            CardRed(Modifier,"置顶")
                        }
                        Box(Modifier.width(4.dp))
                        if(article.fresh){
                            CardRed(Modifier,"新")
                        }else if(article.tags.isNotEmpty()){
                            CardBlue(Modifier,"本站发布")
                        }
                        Box(Modifier.width(4.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray,
                            text = article.author,
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Text(
                        article.niceDate,
                        color = Color.Gray,
                        style = MaterialTheme.typography.body2
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                ){
                    Text(
                        text = article.title,
                        style = TextStyle(
                            fontWeight = FontWeight.W400, //设置字体粗细
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        article.chapterName +"/"+ article.superChapterName,
                        color = Color.Gray,
                        style = MaterialTheme.typography.body2
                    )
                    IconButton(onClick = {
                    }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Filled.Favorite, null)
                    }
                }
            }
        }
        @Composable
        fun WxChapterItem(wxChapterBean: WXChapterBean,clickEvent:()->Unit){

        }
    }
}