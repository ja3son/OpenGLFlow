package com.ja3son.gllib.controller

import android.opengl.GLES32
import android.os.Build
import com.ja3son.gllib.entity.TDTexEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class TDTexRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        MatrixState.setLightLocation(0f, 100f, 200f)
        val texData = byteArrayOf(
                //3d 1
                80, 80, 80, 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 80, 80, 80, 255.toByte(),
                //3d 2
                255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 80, 80, 80, 255.toByte(), 80, 80, 80, 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte(), 255.toByte())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            entities.add(TDTexEntity(ShaderUtils.init3DTexture(texData, 2, 2, 2)))
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 10f)
        MatrixState.setCamera(0f, 0f, 1.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.pushMatrix()
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}