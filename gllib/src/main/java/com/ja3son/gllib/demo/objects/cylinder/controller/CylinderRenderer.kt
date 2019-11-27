package com.ja3son.gllib.demo.objects.cylinder.controller

import android.opengl.GLES30
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.objects.cylinder.entity.CylinderEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class CylinderRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        val textureID = ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.flower)
        entities.add(CylinderEntity(1.2f, 3.9f, 1f, 36,
                textureID, textureID, textureID))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 4f, 100f)
        MatrixState.setCamera(0f, 0f, 8.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        MatrixState.setInitStack()
        MatrixState.setLightLocation(10f, 0f, -10f)

        object : Thread() {
            override fun run() {
                var redAngle = 0f
                while (true) {
                    redAngle = (redAngle + 5) % 360
                    val rx = (15 * Math.sin(Math.toRadians(redAngle.toDouble()))).toFloat()
                    val rz = (15 * Math.cos(Math.toRadians(redAngle.toDouble()))).toFloat()
                    MatrixState.setLightLocation(rx, 0f, rz)

                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -10f)
        entities[0].drawSelf()
        MatrixState.popMatrix()
    }
}