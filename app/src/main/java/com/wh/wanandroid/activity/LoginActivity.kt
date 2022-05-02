package com.wh.wanandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wh.wanandroid.ViewModel.LoginViewModel
import com.wh.wanandroid.activity.ui.theme.WanAndroidTheme


data class ButtonState(var text: String, var textColor: Color, var buttonColor: Color)

class LoginActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val now = remember { mutableStateOf(true) }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    if (now.value) Text("登录")
                                    else Text("注册")
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            this.finish()
                                        } //do something
                                    ) {
                                        Icon(Icons.Filled.ArrowBack, null)
                                    }
                                },

                                )

                        },
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(0.dp, 0.dp, 0.dp, 50.dp), Alignment.Center
                        ) {
                            if (now.value) Lon(now)
                            else Reg(now)

                        }
                    }
                }
            }
        }

    }

    @Composable
    fun Reg(now: MutableState<Boolean>) {
        var userName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordAgain by remember { mutableStateOf("") }
        var passwordHidden by remember { mutableStateOf(true) }
        // 获取按钮的状态
        val interactionState = remember { MutableInteractionSource() }
        // 使用 Kotlin 的解构方法
        val (text, textColor, buttonColor) = when {
            interactionState.collectIsPressedAsState().value ->
                ButtonState("登录", Color.White, Color.Gray)
            else -> ButtonState("登录", Color.White, Color.Blue)
        }
        Column(
            modifier = Modifier
                .padding(50.dp, 10.dp, 50.dp, 10.dp),
            horizontalAlignment = Alignment.End
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                ),
                value = userName,
                onValueChange = {
                    userName = it
                },
                singleLine = true,
                label = {
                    Text("用户名")
                },
                trailingIcon = {
                    IconButton(onClick = {

                    }) {
//                        Icon(Icons.Filled., null)
                    }
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                ),
                value = password,
                onValueChange = {
                    password = it
                },
                singleLine = true,
                label = {
                    Text("密码")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordHidden = !passwordHidden
                        }
                    ) {
                        Icon(Icons.Filled.Visibility, null)
                    }
                },
                visualTransformation = if (passwordHidden)
                    PasswordVisualTransformation() else VisualTransformation.None
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                ),

                value = passwordAgain,
                onValueChange = {
                    passwordAgain = it
                },
                singleLine = true,
                label = {
                    Text("再次输入密码")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordHidden = !passwordHidden
                        }
                    ) {
                        Icon(Icons.Filled.Visibility, null)
                    }
                },
                visualTransformation = if (passwordHidden)
                    PasswordVisualTransformation() else VisualTransformation.None
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(0.dp, 15.dp),
                interactionSource = interactionState,
                elevation = null,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = buttonColor,
                ),
                onClick = {

                }

            ) {
                Text(text = text, color = textColor)
            }
            Text(
                text = "已有账户？去登陆",
                style = TextStyle(Color.Blue),
                modifier = Modifier.clickable(
                    onClick = {
                        now.value = !now.value
                    },
                )
            )
        }
    }

    val lonModel = LoginViewModel()

    @Composable
    fun Lon(now: MutableState<Boolean>) {
        var userName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordHidden by remember { mutableStateOf(true) }
        // 获取按钮的状态
        val interactionState = remember { MutableInteractionSource() }
        // 使用 Kotlin 的解构方法
        val (text, textColor, buttonColor) = when {
            interactionState.collectIsPressedAsState().value ->
                ButtonState("登录", Color.White, Color.Gray)
            else -> ButtonState("登录", Color.White, Color.Blue)
        }
        Column(
            modifier = Modifier
                .padding(50.dp, 10.dp, 50.dp, 10.dp),
            horizontalAlignment = Alignment.End
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                ),
                value = userName,
                onValueChange = {
                    userName = it
                },
                singleLine = true,
                label = {
                    Text("用户名")
                },
                trailingIcon = {
                    IconButton(onClick = {

                    }) {
//                        Icon(Icons.Filled., null)
                    }
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                ),
                value = password,
                onValueChange = {
                    password = it
                },
                singleLine = true,
                label = {
                    Text("密码")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordHidden = !passwordHidden
                        }
                    ) {
                        Icon(Icons.Filled.Visibility, null)
                    }
                },
                visualTransformation = if (passwordHidden)
                    PasswordVisualTransformation() else VisualTransformation.None
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(0.dp, 15.dp),
                interactionSource = interactionState,
                elevation = null,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = buttonColor,
                ),
                onClick = {
                    if (userName.equals("") || password.equals("")) {
                        Toast.makeText(this@LoginActivity, "账号或密码不能为空", Toast.LENGTH_SHORT).show()
                    }else{
                        lonModel.login(userName, password)
                    }
                }
            ) {
                Text(text = text, color = textColor)
                if(!userName.equals("") && lonModel.logindata.value?.username.equals(userName)){
                    Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    this@LoginActivity.finish()
                }
                if(!lonModel.data.value.errorMsg.equals("")){
                    Toast.makeText(this@LoginActivity, lonModel.data.value.errorMsg, Toast.LENGTH_SHORT).show()
                    lonModel.data.value.errorMsg = ""
                }
            }
            Text(
                text = "注册",
                style = TextStyle(Color.Blue),
                modifier = Modifier.clickable(
                    onClick = {
                        now.value = !now.value
                    },
                )
            )
        }
    }

}

