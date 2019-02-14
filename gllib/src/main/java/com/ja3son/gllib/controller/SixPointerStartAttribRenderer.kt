package com.ja3son.gllib.controller

import com.ja3son.gllib.entity.SixPointedStarAttribEntity
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class SixPointerStartAttribRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        val color = arrayOf(floatArrayOf(1f, 0f, 0.1f),
                floatArrayOf(0.98f, 0.49f, 0.04f),
                floatArrayOf(1f, 1f, 0.04f),
                floatArrayOf(0.67f, 1f, 0f),
                floatArrayOf(0.27f, 0.41f, 1f),
                floatArrayOf(0.88f, 0.43f, 0.92f))
        for (i in 0 until 6) {
            entities.add(SixPointedStarAttribEntity(0.2f, 0.5f, -0.3f * i, color[i]))
        }
    }
}