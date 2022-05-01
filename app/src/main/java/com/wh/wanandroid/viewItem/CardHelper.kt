package com.wh.wanandroid.viewItem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wh.wanandroid.activity.ui.theme.Blue
import com.wh.wanandroid.activity.ui.theme.Red

class CardHelper {
    companion object {
        @Composable
        fun CardColored(modifier: Modifier = Modifier, s : String, color: Color){
            Card(
                modifier,
                border = BorderStroke(1.dp, color = color)
            ){
                Text(s, modifier = Modifier.padding(4.dp, 2.dp),
                    color = color,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        letterSpacing = 0.4.sp
                ),
                )
            }
        }
        @Composable
        fun CardRed(modifier: Modifier, s : String){
            CardColored(modifier, s, Red)
        }
        @Composable
        fun CardBlue(modifier: Modifier, s : String){
            CardColored(modifier, s, Blue)
        }
    }
}