package com.ja3son.gllib.controller

import android.opengl.GLES30
import com.ja3son.gllib.entity.ColorRectCubeEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ColorCubeRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ColorRectCubeEntity())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 20f, 100f)
        MatrixState.setCamera(0f, 8f, 80f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        when (touchIndex % 2) {
            0 -> {
                MatrixState.setProjectFrustum(-ratio * 0.7f, ratio * 0.7f, -0.7f, 0.7f, 1f, 10f)
                MatrixState.setCamera(0f, 0.5f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            }
            1 -> {
                MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 20f, 100f)
                MatrixState.setCamera(0f, 8f, 45f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            }
        }

        MatrixState.pushMatrix()

        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        MatrixState.pushMatrix()
        MatrixState.translate(-3f, 0f, 0f)
        MatrixState.rotate(60f, 0f, 1f, 0f)
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(3f, 0f, 0f)
        MatrixState.rotate(-60f, 0f, 1f, 0f)
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}