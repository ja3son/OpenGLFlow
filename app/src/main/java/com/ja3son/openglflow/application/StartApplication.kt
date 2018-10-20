package com.ja3son.openglflow.application

import android.app.Application
import com.ja3son.gllib.util.ShaderUtils

class StartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ShaderUtils.register(applicationContext)
    }
}