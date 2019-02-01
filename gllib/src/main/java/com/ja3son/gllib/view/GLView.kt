package com.ja3son.gllib.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.controller.BletCircleRenderer


class GLView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    private var renderer: BaseRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = BletCircleRenderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val y: Float = event!!.y
        val x: Float = event.x
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviousY
                val dx = x - mPreviousX
                for (entity in renderer.entities) {
                    entity.yAngle += dx * TOUCH_SCALE_FACTOR
                    entity.xAngle += dy * TOUCH_SCALE_FACTOR
                }
            }
            MotionEvent.ACTION_DOWN -> {
                for (entity in renderer.entities) {
                    entity.touchIndex++
                }
            }
        }
        mPreviousY = y
        mPreviousX = x
        return true
    }
}