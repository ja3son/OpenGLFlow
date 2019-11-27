package com.ja3son.gllib.demo.test.scissortest


import android.opengl.GLES30
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.skybox.npr.NPREntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ScissorTestRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(NPREntity("ch_n.obj"))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        for (entity in entities) {
            entity.xAngle = xAngle
            entity.yAngle = yAngle
        }

        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, -2f, -25f)
        entities[0].drawSelf()

        GLES30.glEnable(GL10.GL_SCISSOR_TEST)
        GLES30.glScissor(0, width - 300, 430, 300)
        GLES30.glClearColor(0.7f, 0.7f, 0.7f, 1.0f)
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        MatrixState.setProjectFrustum(-0.62f * ratio, 4.18f * ratio, -2.55f, 0.45f, 2f, 100f)
        MatrixState.setCamera(0f, 50f, -30f, 0f, -2f, -25f, 0f, 0.0f, -1.0f)
        entities[0].drawSelf()
        GLES30.glDisable(GL10.GL_SCISSOR_TEST)

        MatrixState.popMatrix()
    }
}