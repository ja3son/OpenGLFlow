package com.ja3son.gllib.demo.skybox.mirror

import android.opengl.GLES30
import android.view.MotionEvent
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MirrorRenderer : BaseRenderer() {
    var texOneId: Int = 0
    var texTwoId: Int = 0
    var texThreeId: Int = 0
    lateinit var ballController: BallController

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        GLES30.glDisable(GLES30.GL_DEPTH_TEST)
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
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, Constant.FLOOR_Y, 0f)
        entities[0].drawSelf(texTwoId)
        MatrixState.popMatrix()

        ballController.drawSelfMirror(texOneId)
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, Constant.FLOOR_Y, 0f)
        entities[0].drawSelf(texThreeId)
        MatrixState.popMatrix()

        GLES30.glDisable(GLES30.GL_BLEND)

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