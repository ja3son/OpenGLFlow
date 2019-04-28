package com.ja3son.gllib.demo.skybox.skybox

import android.opengl.GLES32
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class SkyBoxRenderer : BaseRenderer() {
    private lateinit var texArray: IntArray
    val UNIT_SIZE: Int = 28

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        texArray = intArrayOf(
                ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.skycubemap_back),
                ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.skycubemap_left),
                ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.skycubemap_right),
                ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.skycubemap_down),
                ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.skycubemap_up),
                ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.skycubemap_front)
        )
        entities.add(SkyBoxEntity())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        MatrixState.setCamera(cx, cy, cz, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

//        entities[0].xAngle = xAngle
//        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -UNIT_SIZE + tzz)
        entities[0].drawSelf(texArray[0])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, UNIT_SIZE - tzz)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        entities[0].drawSelf(texArray[5])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(-UNIT_SIZE + tzz, 0f, 0f)
        MatrixState.rotate(90f, 0f, 1f, 0f)
        entities[0].drawSelf(texArray[1])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(UNIT_SIZE - tzz, 0f, 0f)
        MatrixState.rotate(-90f, 0f, 1f, 0f)
        entities[0].drawSelf(texArray[2])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, -UNIT_SIZE + tzz, 0f)
        MatrixState.rotate(-90f, 1f, 0f, 0f)
        entities[0].drawSelf(texArray[3])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, UNIT_SIZE - tzz, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        entities[0].drawSelf(texArray[4])
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }

    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    var cx = 0f
    var cy = 2f
    var cz = 24f
    var cr = 24f
    var cAngle = 0f
    val tzz = 0.4f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val y = event.y
            val x = event.x
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val dy = y - mPreviousY
                    val dx = x - mPreviousX
                    cAngle += dx * TOUCH_SCALE_FACTOR
                    cx = (Math.sin(Math.toRadians(cAngle.toDouble())) * cr).toFloat()
                    cz = (Math.cos(Math.toRadians(cAngle.toDouble())) * cr).toFloat()
                    cy += dy / 10.0f
                }
            }
            mPreviousY = y
            mPreviousX = x
        }
        return true
    }
}