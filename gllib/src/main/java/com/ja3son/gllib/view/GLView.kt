package com.ja3son.gllib.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.skybox.skybox.SkyBoxRenderer
import com.ja3son.gllib.util.MatrixState


class GLView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    private var renderer: BaseRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = SkyBoxRenderer()
        setLightOffset(-4f)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return renderer.onTouchEvent(event)
    }

    fun setLightOffset(lightOffset: Float) {
        MatrixState.setLightLocation(lightOffset, 0f, 1.5f)
    }
}