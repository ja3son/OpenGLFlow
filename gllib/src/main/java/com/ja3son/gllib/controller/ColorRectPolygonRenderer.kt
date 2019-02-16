package com.ja3son.gllib.controller

import android.opengl.GLES32
import com.ja3son.gllib.entity.ColorInRectEntity
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ColorRectPolygonRenderer : BaseRenderer() {

    val polygonOffsetFactor = -1f
    val polygonOffsetUnits = -2f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ColorInRectEntity(600f, floatArrayOf(1f, 1f, 0f, 0f)))
        entities.add(ColorInRectEntity(600f, floatArrayOf(0f, 1f, 1f, 0f)))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)

        MatrixState.setProjectFrustum(-ratio * 75f, ratio * 75f, -75f, 75f, 300f, 10000f)
        MatrixState.setCamera(5000f, 0.5f, 0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        MatrixState.pushMatrix()

        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)

        MatrixState.pushMatrix()
        MatrixState.translate(-250f, 0f, -0.1f)
        entities[0].drawSelf()
        MatrixState.popMatrix()

        GLES32.glEnable(GLES32.GL_POLYGON_OFFSET_FILL)
        GLES32.glPolygonOffset(polygonOffsetFactor, polygonOffsetUnits)

        MatrixState.pushMatrix()
        MatrixState.translate(250f, 0f, 0f)
        entities[1].drawSelf()
        MatrixState.popMatrix()
        GLES32.glDisable(GLES32.GL_POLYGON_OFFSET_FILL)

        MatrixState.popMatrix()
    }
}