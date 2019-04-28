package com.ja3son.gllib.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.skybox.skybox.SkyBoxRenderer


class GLView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    private var renderer: BaseRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = SkyBoxRenderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return renderer.onTouchEvent(event)
    }
}