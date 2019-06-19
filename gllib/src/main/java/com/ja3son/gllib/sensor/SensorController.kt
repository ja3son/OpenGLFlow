package com.ja3son.gllib.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

object SensorController {

    private lateinit var sensorManager: SensorManager

    fun register(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun getSensorByType(sensorType: Int): Sensor? {
        return sensorManager.getDefaultSensor(sensorType)
    }

    fun registerSensor(listener: SensorEventListener, sensor: Sensor, sensorRate: Int) {
        sensorManager.registerListener(listener, sensor, sensorRate)
    }
}