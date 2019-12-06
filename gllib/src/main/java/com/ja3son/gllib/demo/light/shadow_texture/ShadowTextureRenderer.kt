package com.ja3son.gllib.demo.light.shadow_texture

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin


class ShadowTextureRenderer : BaseRenderer() {
    private var viewProjMatrix = FloatArray(16)

    var light_x = 0f
    val light_y = 10f
    var light_z = 45f
    var light_angle = 0f
    val light_radius = 45f
    val center_dis = 15f

    var textureId = 0

    init {
        object : Thread() {
            override fun run() {
                while (true) {
                    light_angle += 0.5f
                    light_x = sin(Math.toRadians(light_angle.toDouble())).toFloat() * light_radius
                    light_z = cos(Math.toRadians(light_angle.toDouble())).toFloat() * light_radius
                    try {
                        sleep(40)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        MatrixState.setLightLocation(light_x, light_y, light_z)
        entities.add(ShadowTextureEntity("shadow_light/pm.obj", true))
        entities.add(ShadowTextureEntity("shadow_light/ver.obj", false))
        textureId = ShaderUtils.initTexture(R.drawable.shadow)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
    }

    override fun onDrawFrame(gl: GL10?) {
        MatrixState.setCamera(light_x, light_y, light_z, 0f, 0f, 0f, -3f, 2f, 0f)
        MatrixState.setProjectFrustum(-0.5f, 0.5f, -0.5f, 0.5f, 0.14f, 400f)
        viewProjMatrix = MatrixState.getViewProjMatrix()

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        MatrixState.setCamera(trans_x, trans_y, trans_z, 0f, 0f, 0f, 0f, 1f, 0f)
        MatrixState.setProjectFrustum(-ratio, ratio, -1.0f, 1.0f, 2f, 1000f)
        MatrixState.setLightLocation(light_x, light_y, light_z)

        MatrixState.pushMatrix()
        (entities[0] as ShadowTextureEntity).drawSelf(textureId, viewProjMatrix, 0)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(center_dis, 0f, 0f)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        MatrixState.scale(2f, 2f, 2f)
        (entities[1] as ShadowTextureEntity).drawSelf(textureId, viewProjMatrix, 0)
        (entities[1] as ShadowTextureEntity).drawSelf(textureId, viewProjMatrix, 1)
        MatrixState.popMatrix()
    }
}