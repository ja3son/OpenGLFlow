package com.ja3son.gllib.demo.skybox.skydome

import android.opengl.GLES30
import android.view.MotionEvent
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class SkyDomeRenderer : BaseRenderer() {
    var texId: Int = 0
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        texId = ShaderUtils.initTexture(R.drawable.sky)
        entities.add(SkyDomeEntity())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 1000f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.pushMatrix()
        entities[0].drawSelf(texId)
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }

    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320

    override fun onTouchEvent(event: MotionEvent?): Boolean {
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