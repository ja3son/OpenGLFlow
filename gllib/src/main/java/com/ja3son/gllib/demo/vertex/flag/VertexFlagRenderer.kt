package com.ja3son.gllib.demo.vertex.flag

import android.opengl.GLES32
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class VertexFlagRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(VertexFlagRectEntity(0))
        entities.add(VertexFlagRectEntity(1))
        entities.add(VertexFlagRectEntity(2))
        textureId = ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.sky)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        for (entity in entities) {
            entity.xAngle = xAngle
            entity.yAngle = yAngle
        }

        MatrixState.pushMatrix()
        MatrixState.translate(-2f, 2f, -10f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(2f, 2f, -10f)
        entities[1].drawSelf(textureId)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(-2f, -2f, -10f)
        entities[2].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}