package com.ja3son.gllib.demo.buffer.array_buffer

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ArrayBufferRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        MatrixState.setInitStack()
        entities = arrayListOf()
        entities.add(ArrayBufferEntity("ch_t.obj"))
        entities.add(ArrayBufferEntity("rg.obj"))

        textureId = ShaderUtils.initTexture(R.drawable.qhc)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle
        entities[1].xAngle = xAngle
        entities[1].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.translate(-40f, -16f, -150f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(40f, -16f, -150f)
        entities[1].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}