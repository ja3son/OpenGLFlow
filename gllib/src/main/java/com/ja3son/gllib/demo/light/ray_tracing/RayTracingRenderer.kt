package com.ja3son.gllib.demo.light.ray_tracing

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class RayTracingRenderer : BaseRenderer() {
    var textureBG = 0
    var textureNormal = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(RayTracingEntity("ch_deviation.obj"))
        textureBG = ShaderUtils.initTexture(R.drawable.ch_bg)
        textureNormal = ShaderUtils.initTexture(R.drawable.gridnt)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -15f)
        entities[0].drawSelf(textureBG, textureNormal)
        MatrixState.popMatrix()
    }
}