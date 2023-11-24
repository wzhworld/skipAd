package com.zh1.skipsplashad.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.zh1.skipsplashad.utils.LogTool
import com.zh1.skipsplashad.utils.ToastUtils

//https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650278067&idx=1&sn=a738ff575526ea5c019f05a4e2aeb797&chksm=886cf9dcbf1b70ca989ca14a097a0fcfca762ca3447349acc853b155610bd8cb3001aec1200c&scene=27
class MyAccessibilityService : AccessibilityService() {
    companion object {
        var instance: MyAccessibilityService? = null
        val isServiceEnable: Boolean get() = instance != null   // 判断无障碍服务是否可用
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // 如果查找包含跳过按钮的结点列表不为空，取第一个，然后输出
            var nodeInfo = getCurrentRootNode()?.findAccessibilityNodeInfosByText("跳过")
                .takeUnless { it.isNullOrEmpty() }?.get(0)
                nodeInfo?.let{
                    if (it.isClickable) {
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        ToastUtils.toast(this@MyAccessibilityService, "已为您跳过广告")
                    }
                }
        }
    }

    private fun getCurrentRootNode() = try {
        rootInActiveWindow
    } catch (e: Exception) {
        e.message?.let { LogTool.d(it) }
        null
    }

    override fun onInterrupt() {
        instance = null;
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this;
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null;
    }
}