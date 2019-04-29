package com.ja3son.gllib.demo.skybox.mirror

import android.opengl.GLES30
import com.ja3son.gllib.demo.skybox.mirror.Constant.BALL_SCALE
import com.ja3son.gllib.demo.skybox.mirror.Constant.FLOOR_Y
import com.ja3son.gllib.demo.skybox.mirror.Constant.UNIT_SIZE
import com.ja3son.gllib.util.MatrixState


class BallController(val btv: BallEntity, startYIn: Float) {

    val TIME_SPAN = 0.05f
    val G = 0.8f

    var startY: Float = 0.toFloat()
    var timeLive = 0f
    var currentY = 0f
    var vy = 0f

    init {
        this.startY = startYIn
        currentY = startYIn
        object : Thread() {

            override fun run() {
                while (true) {
                    timeLive += TIME_SPAN
                    val tempCurrY = startY - 0.5f * G * timeLive * timeLive + vy * timeLive

                    if (tempCurrY <= FLOOR_Y) {
                        startY = FLOOR_Y
                        vy = -(vy - G * timeLive) * 0.8f
                        timeLive = 0f
                        if (vy < 0.35f) {
                            currentY = FLOOR_Y
                            break
                        }
                    } else {
                        currentY = tempCurrY
                    }

                    try {
                        sleep(20)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    fun drawSelf(texId: Int) {
        MatrixState.pushMatrix()
        MatrixState.translate(0f, UNIT_SIZE * BALL_SCALE + currentY, 0f)
        btv.drawSelf(texId)
        MatrixState.popMatrix()
    }

    fun drawSelfMirror(texId: Int) {
        GLES30.glFrontFace(GLES30.GL_CW)
        MatrixState.pushMatrix()
        MatrixState.scale(1f, -1f, 1f)
        MatrixState.translate(0f, UNIT_SIZE * BALL_SCALE + currentY - 2 * FLOOR_Y, 0f)
        btv.drawSelf(texId)
        MatrixState.popMatrix()
        GLES30.glFrontFace(GLES30.GL_CCW)
    }
}