package com.ja3son.gllib.controller

import android.opengl.GLES30
import com.ja3son.gllib.entity.BallEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class BallRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(BallEntity(0.8f))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 20f, 100f)
        MatrixState.setCamera(0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        MatrixState.pushMatrix()
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        entities[0].drawSelf()
        MatrixState.popMatrix()
    }
}