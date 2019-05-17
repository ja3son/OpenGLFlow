package com.ja3son.gllib.controller

import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open class BaseRenderer : GLSurfaceView.Renderer {
    lateinit var entities: MutableList<BaseEntity>
    var ratio: Float = 0.0f
    var width = 0
    var height = 0
    var yAngle = 0f
    var xAngle = 0f
    var touchIndex = 0
    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES32.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glEnable(GLES32.GL_CULL_FACE)
        MatrixState.setInitStack()
        entities = arrayListOf()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES32.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
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
    }

    open fun onTouchEvent(event: MotionEvent?): Boolean {
        val y: Float = event!!.y
        val x: Float = event.x
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviousY
                val dx = x - mPreviousX
                yAngle += dx * TOUCH_SCALE_FACTOR
                xAngle += dy * TOUCH_SCALE_FACTOR
            }
        }
        mPreviousY = y
        mPreviousX = x
        return true
    }
}