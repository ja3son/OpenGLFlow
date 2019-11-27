package com.ja3son.gllib.demo.texEarthMoon


import android.opengl.GLES30
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class TexEarthMoonRenderer : BaseRenderer() {
    val threadFlag: Boolean = true

    private var mPreviousX: Float = 0f
    private var mPreviousY: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320f

    var eAngle = 0f
    var cAngle = 0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(EarthEntity(2f, ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.earth), ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.earthn)))
        entities.add(MoonEntity(1f, ShaderUtils.initTexture(com.ja3son.gllib.R.drawable.moon)))
        entities.add(CelestialEntity(1f, 1000))
        entities.add(CelestialEntity(2f, 500))

        object : Thread() {
            override fun run() {
                while (threadFlag) {
                    eAngle = (eAngle + 2) % 360
                    cAngle = (cAngle + 0.2f) % 360
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 4f, 100f)
        MatrixState.setCamera(0f, 0f, 7.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        GLES30.glEnable(GLES30.GL_CULL_FACE)

        MatrixState.setInitStack()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        MatrixState.pushMatrix()
        MatrixState.rotate(eAngle, 0f, 1f, 0f)
        entities[0].drawSelf()
        MatrixState.translate(2f, 0f, 0f)
        MatrixState.rotate(eAngle, 0f, 1f, 0f)
        entities[1].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.rotate(cAngle, 0f, 1f, 0f)
        entities[2].drawSelf()
        entities[3].drawSelf()

        MatrixState.popMatrix()
    }
}