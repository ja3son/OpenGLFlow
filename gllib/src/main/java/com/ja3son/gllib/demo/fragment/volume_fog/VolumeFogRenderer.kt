package com.ja3son.gllib.demo.fragment.volume_fog

import android.opengl.GLES32
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class VolumeFogRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        val texGrass = ShaderUtils.initMipMapTexture(R.drawable.grass)
        val texRock = ShaderUtils.initMipMapTexture(R.drawable.rock)
        val landForms = ShaderUtils.loadLandForms(R.drawable.land)
        entities.add(VolumeFogEntity(ShaderUtils.getWidth(R.drawable.land),
                ShaderUtils.getHeight(R.drawable.land),
                intArrayOf(texGrass, texRock), landForms))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 1000f)
        MatrixState.setCamera(0f, 50f, 3f, 0f, 45f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        MatrixState.pushMatrix()
        MatrixState.pushMatrix()
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}