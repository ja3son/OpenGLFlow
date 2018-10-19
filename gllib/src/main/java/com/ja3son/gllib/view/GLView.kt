package com.ja3son.gllib.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.ja3son.gllib.controller.BaseRenderer

class GLView(context: Context?, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    init {
        setEGLContextClientVersion(3)
        setRenderer(BaseRenderer())
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}