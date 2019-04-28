package com.ja3son.gllib.demo.skybox

import android.opengl.GLES32
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class TerrainRenderer : BaseRenderer() {
    var texIdOne: Int = 0
    var texIdTwo: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        val yArray = ShaderUtils.loadLandforms(R.drawable.land, -2f, 40f)
        texIdOne = ShaderUtils.initTexture(R.drawable.grass)
        texIdTwo = ShaderUtils.initTexture(R.drawable.rock)
        entities.add(TerrainTransitionEntity((yArray.size - 1), (yArray[0].size - 1),
                yArray, intArrayOf(texIdOne, texIdTwo)))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 1000f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
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