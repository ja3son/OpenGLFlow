package com.ja3son.gllib.demo.buffer.multy_fbo

import android.opengl.GLES32
import android.util.Log
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MultiFBORenderer : BaseRenderer() {
    var textureId = 0
    var frameTextureIds = IntArray(4)
    var frameBuffer = 0
    var renderBuffer = 0

    private fun initFBO(width: Int, height: Int) {
        val attachments = intArrayOf(
                GLES32.GL_COLOR_ATTACHMENT0,
                GLES32.GL_COLOR_ATTACHMENT1,
                GLES32.GL_COLOR_ATTACHMENT2,
                GLES32.GL_COLOR_ATTACHMENT3
        )

        val buffers = IntArray(1)
        GLES32.glGenFramebuffers(1, buffers, 0)
        frameBuffer = buffers[0]
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, frameBuffer)

        GLES32.glGenRenderbuffers(1, buffers, 0)
        renderBuffer = buffers[0]
        GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER, renderBuffer)
        GLES32.glRenderbufferStorage(GLES32.GL_RENDERBUFFER, GLES32.GL_DEPTH_COMPONENT16, width, height)
        GLES32.glFramebufferRenderbuffer(GLES32.GL_FRAMEBUFFER, GLES32.GL_DEPTH_ATTACHMENT, GLES32.GL_RENDERBUFFER, renderBuffer)
        GLES32.glBindRenderbuffer(GLES32.GL_RENDERBUFFER, 0)

        for (i in frameTextureIds.indices) {
            frameTextureIds[i] = ShaderUtils.genTexture(GLES32.GL_TEXTURE_2D, width, height)
            GLES32.glFramebufferTexture2D(GLES32.GL_DRAW_FRAMEBUFFER, attachments[i], GLES32.GL_TEXTURE_2D, frameTextureIds[i], 0)
        }

        GLES32.glDrawBuffers(attachments.size, attachments, 0)

        if (GLES32.GL_FRAMEBUFFER_COMPLETE != GLES32.glCheckFramebufferStatus(GLES32.GL_FRAMEBUFFER)) {
            Log.e("ja333son", "initFBO: error")
        }

        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, 0)
    }

    private fun drawFrame(width: Int, height: Int, entity: BaseEntity) {
        GLES32.glViewport(0, 0, width, height)
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, frameBuffer)
        GLES32.glClear(GLES32.GL_DEPTH_BUFFER_BIT or GLES32.GL_COLOR_BUFFER_BIT)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -100f)
        entity.drawSelf(textureId)
        MatrixState.popMatrix()
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER, 0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        textureId = ShaderUtils.initTexture(R.drawable.qhc)
        entities.add(MultiFBOEntity("ch_t.obj"))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)
        MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1.0f, 0.0f)

        entities.add(MultiFBORectEntity(ratio))
        initFBO(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        drawFrame(width, height, entities[0])

        MatrixState.pushMatrix()
        MatrixState.translate(-ratio, 1f, -5f)
        entities[1].drawSelf(frameTextureIds[0])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(ratio, 1f, -5f)
        entities[1].drawSelf(frameTextureIds[1])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(-ratio, -1f, -5f)
        entities[1].drawSelf(frameTextureIds[2])
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(ratio, -1f, -5f)
        entities[1].drawSelf(frameTextureIds[3])
        MatrixState.popMatrix()
    }
}