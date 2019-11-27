package com.ja3son.gllib.demo.buffer.fbo

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class FBORenderer : BaseRenderer() {
    var textureId = 0
    var frameTextureId = 0
    var frameBuffer = 0
    var renderBuffer = 0

    private fun initFBO(width: Int, height: Int) {
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
    }

    private fun drawFrame(width: Int, height: Int, entity: BaseEntity) {
        GLES30.glViewport(0, 0, width, height)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -100f)
        entity.drawSelf(textureId)
        MatrixState.popMatrix()
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        textureId = ShaderUtils.initTexture(R.drawable.qhc)
        entities.add(FBOEntity("ch_t.obj"))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)

        entities.add(FBORectEntity(ratio))
        initFBO(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle
        entities[1].xAngle = xAngle
        entities[1].yAngle = yAngle

        drawFrame(width, height, entities[0])

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -5f)
        entities[1].drawSelf(frameTextureId)
        MatrixState.popMatrix()
    }
}