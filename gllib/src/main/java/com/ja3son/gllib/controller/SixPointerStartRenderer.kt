package com.ja3son.gllib.controller

import com.ja3son.gllib.entity.SixPointedStarEntity
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10



class SixPointerStartRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        for (i in 0 until 6) {
            entities.add(SixPointedStarEntity(0.2f, 0.5f, -0.3f * i))
        }
    }
}