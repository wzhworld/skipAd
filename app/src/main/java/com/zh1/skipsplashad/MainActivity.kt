package com.zh1.skipsplashad

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zh1.skipsplashad.service.MyAccessibilityService
import com.zhizhi.skipsplashad.R
import java.util.*

//相关逻辑介绍：
//https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650278067&idx=1&sn=a738ff575526ea5c019f05a4e2aeb797&chksm=886cf9dcbf1b70ca989ca14a097a0fcfca762ca3447349acc853b155610bd8cb3001aec1200c&scene=27
class MainActivity : AppCompatActivity() {
    private lateinit var mServiceStatusTv: TextView
    private lateinit var mToOpenBt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_layout)
        mServiceStatusTv = findViewById<TextView>(R.id.status_tv)
        mToOpenBt = findViewById<Button>(R.id.open_btn).apply {
            setOnClickListener {
                alert(this@MainActivity)
            }
        }
    }

    private fun alert(ctx: Context) {
        var dialog = AlertDialog.Builder(ctx)
            .setTitle("启用无障碍服务")
            .setMessage("无障碍服务 >  打开「SkipSplashAd」")
            .setNegativeButton("再想想") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("去开启") { dialog, which ->
                openAccessibility()
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        if (MyAccessibilityService.isServiceEnable) {
            mServiceStatusTv.text = "跳过广告服务状态：已开启"
        } else {
            mServiceStatusTv.text = "跳过广告服务状态：未开启"
        }
    }

    private fun openAccessibility() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        )
        intent.component = ComponentName(
            "com.android.settings",
            "com.android.settings.Settings\$AccessibilitySettingsActivity"
        )
        startActivity(intent)
    }
}