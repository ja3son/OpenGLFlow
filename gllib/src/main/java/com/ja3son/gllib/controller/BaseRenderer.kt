package com.ja3son.gllib.controller

import android.opengl.GLES32
import android.opengl.GLSurfaceView
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open class BaseRenderer : GLSurfaceView.Renderer {
    lateinit var entities: MutableList<BaseEntity>

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        entities = arrayListOf()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
        val ratio: Float = width / height.toFloat()
        MatrixState.setProjectFrustum(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1f, 50f)

        MatrixState.setCamera(
                0f, 0f, 3f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        for (entity in entities) {
            entity.drawSelf()
        }
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
    }
}