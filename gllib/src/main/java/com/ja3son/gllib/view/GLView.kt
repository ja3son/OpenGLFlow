package com.ja3son.gllib.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.skybox.TerrainRenderer
import com.ja3son.gllib.util.MatrixState


class GLView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    var cx = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    private var renderer: BaseRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = TerrainRenderer()
        setLightOffset(-4f)
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
                renderer.yAngle += dx * TOUCH_SCALE_FACTOR
                renderer.xAngle += dy * TOUCH_SCALE_FACTOR

                cx += dx * TOUCH_SCALE_FACTOR
                cx = Math.max(cx, -200f)
                cx = Math.min(cx, 200f)
                renderer.cx = cx
            }
            MotionEvent.ACTION_DOWN -> {
                renderer.touchIndex++
            }
        }
        mPreviousY = y
        mPreviousX = x
        return true
    }

    fun setLightOffset(lightOffset: Float) {
        MatrixState.setLightLocation(lightOffset, 0f, 1.5f)
    }
}