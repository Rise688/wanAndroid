package com.wh.wanandroid.app

import android.app.Application
import android.content.Context
import android.util.Log
import kotlin.properties.Delegates

class App: Application() {

    companion object {
        var context: Context  by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.d("appppp", "cont")
    }
}