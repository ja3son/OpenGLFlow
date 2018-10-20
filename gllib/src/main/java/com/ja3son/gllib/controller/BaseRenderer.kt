package com.ja3son.gllib.controller

import android.opengl.GLES32
import android.opengl.GLSurfaceView
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.entity.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BaseRenderer() : GLSurfaceView.Renderer {
    lateinit var entity: BaseEntity

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)
        entity.drawSelf()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES32.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        entity = Triangle()
    }
}