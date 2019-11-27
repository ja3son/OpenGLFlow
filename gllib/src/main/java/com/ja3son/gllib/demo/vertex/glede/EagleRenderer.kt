package com.ja3son.gllib.demo.vertex.glede

import android.opengl.GLES30
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EagleRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        textureId = ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.eagle)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
        entities.add(EagleEntity())
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        for (entity in entities) {
            entity.xAngle = xAngle
            entity.yAngle = yAngle
        }

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -70f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}