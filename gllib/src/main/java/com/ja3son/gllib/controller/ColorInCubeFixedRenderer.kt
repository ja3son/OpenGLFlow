package com.ja3son.gllib.controller

import android.opengl.GLES30
import com.ja3son.gllib.entity.ColorInRectCubeEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ColorInCubeFixedRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ColorInRectCubeEntity(500f, floatArrayOf(0f, 1f, 1f, 0f)))
        entities.add(ColorInRectCubeEntity(499.5f, floatArrayOf(1f, 1f, 0f, 0f)))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)

        val NEAR = 300.0f
        val FAR = 10000.0f
        val LEFT = -NEAR * ratio * 0.25f
        val RIGHT = NEAR * ratio * 0.25f
        val BOTTOM = -NEAR * 0.25f
        val TOP = NEAR * 0.25f

        MatrixState.setProjectFrustum(LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR)

        MatrixState.setCamera(5000f, 0.5f, 0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        MatrixState.pushMatrix()

        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)

        MatrixState.pushMatrix()
        MatrixState.translate(-250f, 0f, 0f)
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(250f, 0f, 0f)
        entities[1].drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}