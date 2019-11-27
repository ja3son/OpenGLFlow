package com.ja3son.gllib.demo.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLES30
import android.opengl.Matrix
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.sensor.SensorController
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class SensorRenderer : BaseRenderer() {

    var SPAN_X = 0.0f
    var SPAN_Y = 0.0f

    var OFFSET_X = 0.0f
    var OFFSET_Y = 0.0f

    var acceRotateMatrix = FloatArray(16)
    val tempRotateMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        MatrixState.setInitStack()
        entities = arrayListOf()
        entities.add(SensorEntity("ch_n.obj", 0))

        Matrix.setRotateM(acceRotateMatrix, 0, 10f, 0f, 1f, 0f)

        val sensor = SensorController.getSensorByType(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) {
            SensorController.registerSensor(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        val x = event.values[0]
                        val y = event.values[1]
                        val length = Math.sqrt(Math.pow(x.toDouble(), 2.0) + Math.pow(y.toDouble(), 2.0))

                        if (length > 0) {
                            SPAN_X = (x / length * 0.02f).toFloat()
                            SPAN_Y = (y / length * 0.02f).toFloat()

                            OFFSET_X -= SPAN_X
                            OFFSET_Y -= SPAN_Y

                            val angle = Math.toDegrees((length / 90f)).toFloat()

                            if (Math.abs(angle) != 0f && (Math.abs(SPAN_X) != 0f || Math.abs(SPAN_Y) != 0f)) {
                                Matrix.setRotateM(tempRotateMatrix, 0, angle, SPAN_Y, SPAN_X, 0f)
                                Matrix.multiplyMM(acceRotateMatrix, 0, tempRotateMatrix, 0, acceRotateMatrix, 0)
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                }
            }, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -60f)
        MatrixState.translate(OFFSET_X, OFFSET_Y, 1.2f)
        MatrixState.matrix(acceRotateMatrix)
        entities[0].drawSelf()
        MatrixState.popMatrix()
    }
}