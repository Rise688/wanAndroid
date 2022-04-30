package com.wh.wanandroid

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.just.agentweb.AgentWeb


class AgenWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agen_web)

        val intent = getIntent()
        val reqUrl = intent.extras?.getString("url")
        val title = intent.extras?.getString("title")
        val linearLayout = findViewById<LinearLayout>(R.id.layout_web)
        var mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(linearLayout!!, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(reqUrl)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                android.R.id.home -> this.finish()
        }
        return super.onOptionsItemSelected(item)
    }
}