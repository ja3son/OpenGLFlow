package com.ja3son.openglflow.application

import android.app.Application
import com.ja3son.gllib.sensor.SensorController
import com.ja3son.gllib.util.OBJUtils
import com.ja3son.gllib.util.ShaderUtils

class StartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ShaderUtils.register(applicationContext)
        OBJUtils.register(applicationContext)
        SensorController.register(applicationContext)
    }
}