package com.ja3son.gllib.demo.senior.instance.grass

import android.opengl.GLES30
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class GrassRenderer : BaseRenderer() {
    var mPreviousY: Float = 0f
    var mPreviousX: Float = 0f
    val TOUCH_SCALE_FACTOR = 180.0f / 320

    var cAngle = 0f
    var num = 40000

    var r = sqrt(num.toDouble()).toFloat() / 4 - 1

    var targetX = r
    var targetY = 0f
    var targetZ = r

    var upX = 0f
    var upY = 1f
    var upZ = 0f

    var CameraX = r
    var CameraY = 6f
    var CameraZ = r

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(GrassEntity("instance/grass/grass.obj", num))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100000f)
        calculateCamera(cAngle)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        entities[0].drawSelf()
        MatrixState.popMatrix()
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e != null) {
            val x = e.x
            when (e.action) {
                MotionEvent.ACTION_MOVE -> {
                    val dx = x - mPreviousX
                    cAngle += dx * TOUCH_SCALE_FACTOR
                }
            }
            mPreviousX = x
            calculateCamera(cAngle)
        }
        return true
    }

    private fun calculateCamera(angle: Float) {
        CameraX = (r * sin(Math.toRadians(angle.toDouble()))).toFloat() + targetX
        CameraZ = (r * cos(Math.toRadians(angle.toDouble()))).toFloat() + targetZ
        MatrixState.setCamera(CameraX, CameraY, CameraZ, targetX, targetY, targetZ, upX, upY, upZ)
    }
}