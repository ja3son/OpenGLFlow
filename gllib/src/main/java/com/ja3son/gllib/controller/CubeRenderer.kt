package com.ja3son.gllib.controller

import android.opengl.GLES30
import com.ja3son.gllib.entity.CubeEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class CubeRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(CubeEntity())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio * 0.8f, ratio * 1.2f, -1f, 1f, 20f, 100f)
        MatrixState.setCamera(-16f, 8f, 70f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        MatrixState.pushMatrix()
        for (entity in entities) {
            entity.drawSelf()
        }
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(3.5f, 0f, 0f)
        MatrixState.rotate(30f, 0f, 0f, 1f)
        MatrixState.scale(0.4f, 2f, 0.6f)
        for (entity in entities) {
            entity.drawSelf()
        }
        MatrixState.popMatrix()
    }
}