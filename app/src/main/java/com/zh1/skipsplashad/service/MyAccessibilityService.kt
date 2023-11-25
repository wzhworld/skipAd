package com.zh1.skipsplashad.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.zh1.skipsplashad.utils.LogTool

//https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650278067&idx=1&sn=a738ff575526ea5c019f05a4e2aeb797&chksm=886cf9dcbf1b70ca989ca14a097a0fcfca762ca3447349acc853b155610bd8cb3001aec1200c&scene=27
class MyAccessibilityService : AccessibilityService() {
    companion object {
        var instance: MyAccessibilityService? = null
        val isServiceEnable: Boolean get() = instance != null   // 判断无障碍服务是否可用
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // 如果查找包含跳过按钮的结点列表不为空
            var listOfRect = handleNode(getCurrentRootNode())
            listOfRect ?: return
            for (rect in listOfRect) {
                click(this@MyAccessibilityService, rect)
            }
        }
    }

    fun handleNode(node: AccessibilityNodeInfo?): List<Rect>? {
        val nodes = node?.findAccessibilityNodeInfosByText("跳过")
        if (null == nodes || nodes.isEmpty()){
            return null
        }
        LogTool.d("TextNodeHandler node$nodes");
        //
        val listOfRect = nodes.map {
            val rect = Rect()
            it.getBoundsInScreen(rect)
            rect
        }
        nodes.forEach { it.recycle() }

        return listOfRect
    }

    private fun click(accessibilityService: AccessibilityService, rect: Rect) {
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        path.lineTo(rect.centerX().toFloat(), rect.centerY().toFloat())

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 1))
            .build()

        accessibilityService.dispatchGesture(
            gesture,
            object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                }
            },
            null
        )
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