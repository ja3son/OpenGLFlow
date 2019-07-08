package com.ja3son.gllib.controller

import com.ja3son.cgl.GL2JNILib
import javax.microedition.khronos.opengles.GL10


class CRenderer : BaseRenderer() {

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GL2JNILib.init(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GL2JNILib.step()
    }
}