package com.ja3son.gllib.demo.senior.computer

import android.opengl.GLES30
import android.opengl.GLES31
import android.view.MotionEvent
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.senior.computer.Constant.calCamera
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ComputerRenderer : BaseRenderer() {
    var textureId = 0
    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ComputerEntity())
        textureId = ShaderUtils.initTexture(R.drawable.wave)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 1000f)
        MatrixState.setInitStack()
        MatrixState.setLightLocation(400f, 400f, 400f)
        calCamera()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BUFFER_BIT)

        MatrixState.setCamera(
                Constant.cx, Constant.cy, Constant.cz,
                Constant.tx, Constant.ty, Constant.tz,
                Constant.upx, Constant.upy, Constant.upz
        )

        MatrixState.pushMatrix()
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val dy = y - mPreviousY
                    val dx = x - mPreviousX
                    Constant.direction += dx * TOUCH_SCALE_FACTOR
                    Constant.direction = Constant.direction % 360
                    Constant.yj += dy
                    if (Constant.yj > 90) {
                        Constant.yj = 90f
                    }
                    if (Constant.yj < 20) {
                        Constant.yj = 20f
                    }
                    calCamera()
                }
            }
            mPreviousY = y
            mPreviousX = x
        }
        return true
    }
}