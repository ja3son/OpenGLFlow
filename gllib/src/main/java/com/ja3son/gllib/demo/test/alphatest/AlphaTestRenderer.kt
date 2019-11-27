package com.ja3son.gllib.demo.test.alphatest

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.skybox.npr.NPREntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AlphaTestRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(NPREntity("ch_n.obj"))
        entities.add(AlphaTextRectEntity())
        textureId = ShaderUtils.initTexture(R.drawable.mask)
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

        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -20f)
        entities[0].drawSelf()
        MatrixState.popMatrix()

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)
        MatrixState.pushMatrix()
        MatrixState.scale(2.0f, 1.8f, 1.0f)
        MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 1f, 100f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        entities[1].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}