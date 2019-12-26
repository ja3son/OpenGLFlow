package com.ja3son.gllib.demo.senior.computer

import android.opengl.Matrix


internal object Constant {
    const val WATER_WIDTH = 127
    const val WATER_HEIGHT = 127
    const val WATER_UNIT_SIZE = 1.5f

    const val tx = WATER_WIDTH * WATER_UNIT_SIZE / 2.0f
    const val ty = 0f
    const val tz = WATER_HEIGHT * WATER_UNIT_SIZE / 2.0f

    var cx = 0f
    var cy = 0f
    var cz = 0f

    var upx = 0f
    var upy = 0f
    var upz = 0f

    var direction = 0f

    var yj = 45f

    var fsjl = Math.sqrt(WATER_WIDTH * WATER_UNIT_SIZE * (WATER_WIDTH * WATER_UNIT_SIZE) / 4 +
            WATER_HEIGHT * WATER_UNIT_SIZE * (WATER_HEIGHT * WATER_UNIT_SIZE) / 4.toDouble()).toFloat() * 0.7f

    fun calCamera() {
        val cameraV = floatArrayOf(0f, 0f, fsjl, 1f)
        val m = FloatArray(16)
        Matrix.setIdentityM(m, 0)
        Matrix.rotateM(m, 0, direction, 0f, 1f, 0f)
        Matrix.rotateM(m, 0, -yj, 1f, 0f, 0f)
        val cameraVResult = FloatArray(4)
        Matrix.multiplyMV(cameraVResult, 0, m, 0, cameraV, 0)
        val up = floatArrayOf(0f, 1f, 0f, 1f)
        val upResult = FloatArray(4)
        Matrix.multiplyMV(upResult, 0, m, 0, up, 0)
        cx = tx + cameraVResult[0]
        cy = ty + cameraVResult[1]
        cz = tz + cameraVResult[2]
        upx = upResult[0]
        upy = upResult[1]
        upz = upResult[2]
    }
}