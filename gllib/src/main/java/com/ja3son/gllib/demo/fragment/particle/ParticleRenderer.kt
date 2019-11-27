package com.ja3son.gllib.demo.fragment.particle

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ParticleRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ParticleEntity(400))
        textureId = ShaderUtils.initTexture(R.drawable.fire)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -10f)
        MatrixState.scale(2.0f, 2.0f, 2.0f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}