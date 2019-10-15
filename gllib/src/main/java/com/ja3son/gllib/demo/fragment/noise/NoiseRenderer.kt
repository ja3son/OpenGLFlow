package com.ja3son.gllib.demo.fragment.noise

import android.opengl.GLES32
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class NoiseRenderer : BaseRenderer() {
    var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(NoiseEntity("ch_t.obj"))
        val tex3D = ShaderUtils.loadTex3D("3dNoise.bn3dtex")
        if (tex3D.data != null) {
            textureId = ShaderUtils.init3DTexture(tex3D.data!!, tex3D.width, tex3D.height, tex3D.depth)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -100f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()
    }
}