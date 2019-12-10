package com.ja3son.gllib.demo.light.motion_blur

import android.opengl.GLES30
import android.opengl.Matrix
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MotionBlurRenderer : BaseRenderer() {
    private var viewProjMatrix = FloatArray(16)
    private var invertViewProjMatrix = FloatArray(16)

    val SHADOW_TEX_WIDTH = 1024

    var frameTextureId = 0
    var frameDepthTextureId = 0
    var frameBuffer = 0
    var renderBuffer = 0

    var textureId = 0

    var cz = 100f //摄像机的z位置坐标
    var targetz = 0f //摄像机的z目标点坐标
    var preCZ = cz //前一帧的摄像机的z位置坐标
    var preTargetZ = targetz //前一帧的摄像机的z目标点坐标
    val SPAN = 2.0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        textureId = ShaderUtils.initCubmapTexture(intArrayOf(
                R.drawable.skycubemap_right, R.drawable.skycubemap_left,
                R.drawable.skycubemap_up_cube, R.drawable.skycubemap_down,
                R.drawable.skycubemap_front, R.drawable.skycubemap_back
        ))

        initFBO(SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)

        object : Thread() {
            override fun run() {
                while (true) {
                    preCZ = cz
                    preTargetZ = targetz
                    cz -= SPAN
                    targetz -= SPAN
                    if (cz <= -35) {
                        cz = 100f
                        targetz = 0f
                        preCZ = cz
                        preTargetZ = targetz
                    }
                    try {
                        sleep(30)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()

        entities.add(MotionBlurRectEntity(ratio))
        entities.add(MotionBlurEntity("ch_t.obj"))
    }

    override fun onDrawFrame(gl: GL10?) {
        generate_frame_texture()

        draw_texture()
    }

    private fun draw_texture() {
        GLES30.glViewport(0, 0, width, height)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 1f, 300f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f)

        MatrixState.pushMatrix()
        (entities[0] as MotionBlurRectEntity).drawSelf(frameTextureId, frameDepthTextureId, viewProjMatrix, invertViewProjMatrix, 2)
        MatrixState.popMatrix()
    }

    private fun generate_frame_texture() {
        GLES30.glViewport(0, 0, SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 300f)
        MatrixState.setCamera(0f, 0f, preCZ, 0f, 0f, preTargetZ, 0f, 1f, 0f)
        viewProjMatrix = MatrixState.getViewProjMatrix()
        MatrixState.setCamera(0f, 0f, cz, 0f, 0f, targetz, 0f, 1f, 0f)
        Matrix.invertM(invertViewProjMatrix, 0, MatrixState.getViewProjMatrix(), 0)

        MatrixState.pushMatrix()
        MatrixState.translate(100f, 0f, -100f)
        entities[1].drawSelf(textureId)
        MatrixState.popMatrix()
    }

    private fun initFBO(width: Int, height: Int): Boolean {
        val buffers = IntArray(1)
        GLES30.glGenFramebuffers(1, buffers, 0)
        frameBuffer = buffers[0]
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)

        GLES30.glGenRenderbuffers(1, buffers, 0)
        renderBuffer = buffers[0]
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBuffer)
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, width, height)
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, renderBuffer)

        frameTextureId = ShaderUtils.genTexture(GLES30.GL_TEXTURE_2D, width, height)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameTextureId, 0)

        frameDepthTextureId = ShaderUtils.genDepthTexture(GLES30.GL_TEXTURE_2D, width, height)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT1, GLES30.GL_TEXTURE_2D, frameDepthTextureId, 0)

        GLES30.glDrawBuffers(2, intArrayOf(GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_COLOR_ATTACHMENT1), 0)

        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
            return false
        }

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)

        return true
    }
}