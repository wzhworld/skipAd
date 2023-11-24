package com.zh1.skipsplashad.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastUtils {
    fun toast(ctx: Context, content: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show()
        }
    }
}