package com.wh.wanandroid.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wh.wanandroid.bean.ArticleResponseBody

class LazyListItem {

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ArticleItem()
    }
    companion object {
        @Composable
        fun ArticleItem() {
            Column(Modifier.background(Color.White)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Card(){}
                        Card(){}
                        Text("111")
                    }
                    Text("")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("")
                    IconButton(onClick = {}) {
                        
                    }
                }
            }
        }
    }
}