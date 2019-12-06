package com.ja3son.gllib.demo.light.shadow_map

import android.opengl.GLES30
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin


class ShadowMapRenderer : BaseRenderer() {
    private var viewProjMatrix = FloatArray(16)
    val SHADOW_TEX_WIDTH = 1024

    var frameTextureId = 0
    var frameBuffer = 0
    var renderBuffer = 0


    var light_x = 0f
    val light_y = 10f
    var light_z = 45f
    var light_angle = 0f
    val light_radius = 45f
    val center_dis = 15f

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
        entities.add(ShadowMapEntity("shadow_light/pm.obj", true))
        entities.add(ShadowMapEntity("shadow_light/cft.obj", true))
        entities.add(ShadowMapEntity("shadow_light/ch.obj", false))
        entities.add(ShadowMapEntity("shadow_light/qt.obj", false))
        entities.add(ShadowMapEntity("shadow_light/yh.obj", false))
        entities.add(ShadowTexRectEntity())
        initFBO(SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        ratio = width / height.toFloat()
    }

    override fun onDrawFrame(gl: GL10?) {
        MatrixState.setLightLocation(light_x, light_y, light_z)
        generateShadowDraw()
        shadowTextureDraw()
    }

    private fun generateShadowDraw() {
        GLES30.glViewport(0, 0, SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        MatrixState.setCamera(light_x, light_y, light_z, 0f, 0f, 0f, 0f, 1f, 0f)
        MatrixState.setProjectFrustum(-1.0f, 1.0f, -1.0f, 1.0f, 1.5f, 400.0f)
        viewProjMatrix = MatrixState.getViewProjMatrix()

        entities[0].drawSelf()

        MatrixState.pushMatrix()
        MatrixState.translate(-center_dis, 0f, 0f)
        entities[1].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(center_dis, 0f, 0f)
        entities[2].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -center_dis)
        entities[3].drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, center_dis)
        MatrixState.rotate(30f, 0f, 1f, 0f)
        entities[4].drawSelf()
        MatrixState.popMatrix()
    }

    private fun shadowTextureDraw() {
        GLES30.glViewport(0, 0, width, height)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        MatrixState.setCamera(trans_x, trans_y, trans_z, 0f, 0f, 0f, 0f, 1f, 0f)
        MatrixState.setProjectFrustum(-ratio, ratio, -1.0f, 1.0f, 2f, 1000f)

        (entities[0] as ShadowMapEntity).drawSelf(frameTextureId, viewProjMatrix)

        MatrixState.pushMatrix()
        MatrixState.translate(-center_dis, 0f, 0f)
        (entities[1] as ShadowMapEntity).drawSelf(frameTextureId, viewProjMatrix)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(center_dis, 0f, 0f)
        (entities[2] as ShadowMapEntity).drawSelf(frameTextureId, viewProjMatrix)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -center_dis)
        (entities[3] as ShadowMapEntity).drawSelf(frameTextureId, viewProjMatrix)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, center_dis)
        MatrixState.rotate(30f, 0f, 1f, 0f)
        (entities[4] as ShadowMapEntity).drawSelf(frameTextureId, viewProjMatrix)
        MatrixState.popMatrix()
    }

    private fun initFBO(width: Int, height: Int): Int {
        val buffers = IntArray(1)
        GLES30.glGenFramebuffers(1, buffers, 0)
        frameBuffer = buffers[0]
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)

        GLES30.glGenRenderbuffers(1, buffers, 0)
        renderBuffer = buffers[0]
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBuffer)
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, width, height)

        frameTextureId = ShaderUtils.genDepthTexture(GLES30.GL_TEXTURE_2D, width, height)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameTextureId, 0)
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, renderBuffer)

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)

        return frameTextureId
    }
}