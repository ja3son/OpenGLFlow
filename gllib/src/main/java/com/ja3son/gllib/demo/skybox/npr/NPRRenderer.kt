package com.ja3son.gllib.demo.skybox.npr

import android.opengl.GLES32
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class NPRRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(NPREntity("ch_n.obj"))
        entities.add(EdgeEntity("ch_n.obj"))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        for (entity in entities) {
            entity.xAngle = xAngle
            entity.yAngle = yAngle
        }

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -20f)

        GLES32.glFrontFace(GLES32.GL_CW)
        entities[1].drawSelf()

        GLES32.glFrontFace(GLES32.GL_CCW)
        entities[0].drawSelf()
        MatrixState.popMatrix()
    }
}