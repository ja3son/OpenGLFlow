package com.ja3son.gllib.demo.test.stenciltest

import android.opengl.GLES32
import android.view.MotionEvent
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.skybox.mirror.BallController
import com.ja3son.gllib.demo.skybox.mirror.BallEntity
import com.ja3son.gllib.demo.skybox.mirror.Constant
import com.ja3son.gllib.demo.skybox.mirror.TextRectEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class StencilRenderer : BaseRenderer() {
    var texOneId: Int = 0
    var texTwoId: Int = 0
    var texThreeId: Int = 0
    lateinit var ballController: BallController

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        GLES32.glDisable(GLES32.GL_DEPTH_TEST)
        texOneId = ShaderUtils.initTexture(R.drawable.basketball)
        texTwoId = ShaderUtils.initTexture(R.drawable.floor)
        texThreeId = ShaderUtils.initTexture(R.drawable.floor_transparent)
        val ballEntity = BallEntity(Constant.BALL_SCALE)
        ballController = BallController(ballEntity, Constant.BALL_SCALE)
        entities.add(TextRectEntity(4f, 2.568f))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 1000f)
        MatrixState.setCamera(0f, 0f, 8f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()

        GLES32.glClear(GLES32.GL_STENCIL_BUFFER_BIT)
        GLES32.glEnable(GLES32.GL_STENCIL_TEST)
        GLES32.glStencilFunc(GLES32.GL_ALWAYS, 1, 1)
        GLES32.glStencilOp(GLES32.GL_KEEP, GLES32.GL_KEEP, GLES32.GL_REPLACE)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, Constant.FLOOR_Y, 0f)
        MatrixState.rotate(10f, 1f, 0f, 0f)
        entities[0].drawSelf(texTwoId)
        MatrixState.popMatrix()

        GLES32.glStencilFunc(GLES32.GL_EQUAL, 1, 1)
        GLES32.glStencilOp(GLES32.GL_KEEP, GLES32.GL_KEEP, GLES32.GL_KEEP)
        ballController.drawSelfMirror(texOneId)
        GLES32.glDisable(GLES32.GL_STENCIL_TEST)

        GLES32.glEnable(GLES32.GL_BLEND)
        GLES32.glBlendFunc(GLES32.GL_SRC_ALPHA, GLES32.GL_ONE_MINUS_SRC_ALPHA)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, Constant.FLOOR_Y, 0f)
        MatrixState.rotate(10f, 1f, 0f, 0f)
        entities[0].drawSelf(texThreeId)
        MatrixState.popMatrix()

        GLES32.glDisable(GLES32.GL_BLEND)

        ballController.drawSelf(texOneId)

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