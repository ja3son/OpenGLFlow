package com.ja3son.gllib.demo.light.refraction

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class RefractionRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(RefractionEntity("ball.obj"))
        textureId = ShaderUtils.initCubmapTexture(intArrayOf(
                R.drawable.skycubemap_right, R.drawable.skycubemap_left,
                R.drawable.skycubemap_up_cube, R.drawable.skycubemap_down,
                R.drawable.skycubemap_front, R.drawable.skycubemap_back
        ))
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -50f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}