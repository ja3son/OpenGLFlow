package com.ja3son.gllib.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.senior.computer.ComputerRenderer
import com.ja3son.gllib.util.MatrixState

class GLView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    private var renderer: BaseRenderer

    init {
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 16, 8)
        renderer = ComputerRenderer()
        setLightOffset(20f)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return renderer.onTouchEvent(event)
    }

    fun setLightOffset(lightOffset: Float) {
        MatrixState.setLightLocation(lightOffset, 20f, 20f)
    }
}