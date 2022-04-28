package com.wh.wanandroid.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wh.wanandroid.bean.ArticleResponseBody


class BaseLazyList {
    companion object {
        @Composable
        fun MessageList() {
            Box(Modifier.background(Color.Gray)) {
                LazyColumn(
                    modifier = Modifier.border(5.dp, color = Color.Blue),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    items(20) {
                        Text("LazyColumn")
                    }
                }
            }
        }
    }
}