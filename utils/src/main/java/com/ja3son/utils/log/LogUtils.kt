package com.ja3son.utils.log

import android.util.Log

object LogUtils {
    val TAG: String = "ja333son_gl"

    fun eLog(log: String) {
        Log.e(TAG, log)
    }

    fun dLog(log: String) {
        Log.d(TAG, log)
    }
}