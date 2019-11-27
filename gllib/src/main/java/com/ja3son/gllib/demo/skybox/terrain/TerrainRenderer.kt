package com.ja3son.gllib.demo.skybox.terrain

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class TerrainRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        val texIdOne = ShaderUtils.initTexture(R.drawable.land)
        val texIdTwo = ShaderUtils.initMipMapTexture(R.drawable.grass)
        val texIdThree = ShaderUtils.initMipMapTexture(R.drawable.rock)
        entities.add(TerrainTransitionEntity(ShaderUtils.getWidth(R.drawable.land),
                ShaderUtils.getHeight(R.drawable.land),
                intArrayOf(texIdOne, texIdTwo, texIdThree)))
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
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}