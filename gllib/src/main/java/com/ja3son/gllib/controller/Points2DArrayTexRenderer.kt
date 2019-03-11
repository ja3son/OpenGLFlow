package com.ja3son.gllib.controller

import android.opengl.GLES32
import android.os.Build
import com.ja3son.gllib.R
import com.ja3son.gllib.entity.Point2DArrayTexEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class Points2DArrayTexRenderer : BaseRenderer() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        val resIds = intArrayOf(
                R.drawable.tex1, R.drawable.tex2, R.drawable.tex3, R.drawable.tex4,
                R.drawable.tex5, R.drawable.tex6, R.drawable.tex7, R.drawable.tex8,
                R.drawable.tex9, R.drawable.tex10, R.drawable.tex11, R.drawable.tex12,
                R.drawable.tex13, R.drawable.tex14, R.drawable.tex15, R.drawable.tex16
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            entities.add(Point2DArrayTexEntity(ShaderUtils.initTextureArray(resIds, 128, 128)))
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 10f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
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