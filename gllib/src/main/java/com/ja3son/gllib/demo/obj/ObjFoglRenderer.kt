package com.ja3son.gllib.demo.obj

import android.opengl.GLES32
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ObjFoglRenderer : BaseRenderer() {
    private val disWithCenter = 12.0f
    var cy = 150f
    var cz = 400f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ObjVertexNAvgEntity("ch_fog.obj"))
        entities.add(ObjVertexNAvgEntity("cft_fog.obj"))
        entities.add(ObjVertexNAvgEntity("qt_fog.obj"))
        entities.add(ObjVertexNAvgEntity("yh_fog.obj"))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        val a = 0.5f
        MatrixState.setProjectFrustum(-ratio * a, ratio * a, -1 * a, 1 * a, 2f, 1000f)
        MatrixState.setLightLocation(100f, 100f, 100f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        MatrixState.setCamera(
                cx,
                cy,
                cz,
                0f,
                0f,
                0f,
                0f,
                1f,
                0f
        )

        MatrixState.pushMatrix()

        MatrixState.scale(5.0f, 5.0f, 5.0f)
        MatrixState.pushMatrix()
        MatrixState.translate(-disWithCenter, 0f, 0f)
        entities[1].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(disWithCenter, 0f, 0f)
        entities[2].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -disWithCenter)
        entities[3].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, disWithCenter)
        entities[0].drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}