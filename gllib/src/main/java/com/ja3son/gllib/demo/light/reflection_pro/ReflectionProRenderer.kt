package com.ja3son.gllib.demo.light.reflection_pro

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.bottom
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.far
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.left
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.mainCameraX
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.mainCameraY
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.mainCameraZ
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.mirrorCameraX
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.mirrorCameraY
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.mirrorCameraZ
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.near
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.right
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.targetX
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.targetY
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.targetZ
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.top
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.upX
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.upY
import com.ja3son.gllib.demo.light.reflection_pro.ReflectionProConstants.upZ
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ReflectionProRenderer : BaseRenderer() {
    private var viewProjMatrix = FloatArray(16)

    var textureId = 0
    val SHADOW_TEX_WIDTH = 1024

    var frameTextureId = 0
    var frameBuffer = 0
    var renderBuffer = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        textureId = ShaderUtils.initTexture(R.drawable.skybox)
        entities.add(ReflectionProEntity("reflection_pro/skybox.obj"))
        entities.add(ReflectionProEntity("reflection_pro/bed.obj"))
        entities.add(ReflectionProEntity("reflection_pro/light.obj"))
        entities.add(ReflectionProRectEntity())
        ReflectionProConstants.calculateMainAndMirrorCamera(0f, 15f)
        initFBO(SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
        ReflectionProConstants.ratio = ratio
        ReflectionProConstants.initProject(1f)
    }

    override fun onDrawFrame(gl: GL10?) {
        ReflectionProConstants.calculateMainAndMirrorCamera(xAngle, yAngle)

        generate_tex()
        draw_mirror_tex()
    }

    private fun generate_tex() {
        GLES30.glViewport(0, 0, SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)

        MatrixState.setCamera(mirrorCameraX, mirrorCameraY, mirrorCameraZ, targetX, targetY, targetZ, upX, upY, upZ)
        MatrixState.setProjectFrustum(left, right, bottom, top, near, far)
        viewProjMatrix = MatrixState.getViewProjMatrix()

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

        drawThings()
    }

    private fun draw_mirror_tex() {
        GLES30.glViewport(0, 0, width, height)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

        MatrixState.setCamera(mainCameraX, mainCameraY, mainCameraZ, targetX, targetY, targetZ, upX, upY, upZ)
        MatrixState.setProjectFrustum(left, right, bottom, top, near, far)

        drawThings()

        MatrixState.pushMatrix()
        MatrixState.translate(2.0f, 12f, targetZ - 1.7f)
        (entities[3] as ReflectionProRectEntity).drawSelf(frameTextureId, viewProjMatrix)
        MatrixState.popMatrix()
    }

    private fun drawThings() {
        MatrixState.pushMatrix()
        MatrixState.translate(0f, -10f, 7f)
        entities[0].drawSelf(textureId)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(22f, 0f, 8f)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        entities[1].drawSelf(textureId)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(40f, 0f, -12f)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        entities[2].drawSelf(textureId)
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

        frameTextureId = ShaderUtils.genTexture(GLES30.GL_TEXTURE_2D, width, height)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameTextureId, 0)
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, renderBuffer)

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)

        return frameTextureId
    }
}