package com.ja3son.gllib.demo.skybox.font

import android.graphics.Bitmap

import android.opengl.GLES30
import android.opengl.GLUtils
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class FontRenderer : BaseRenderer() {
    var timeStamp = System.currentTimeMillis()
    var texId = -1
    var wlWidth = 512
    var wlHeight = 512

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(TextRectEntity())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 1000f)
        MatrixState.setCamera(0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        entities[0].xAngle = xAngle
        entities[0].yAngle = yAngle

        val tts = System.currentTimeMillis()
        if (tts - timeStamp > 500) {
            timeStamp = tts
            FontUtil.cIndex = (FontUtil.cIndex + 1) % FontUtil.content.size
            FontUtil.updateRGB()
        }

        if (texId != -1) {
            GLES30.glDeleteTextures(1, intArrayOf(texId), 0)
        }

        val bm = FontUtil.generateWLT(FontUtil.getContent(FontUtil.cIndex, FontUtil.content), wlWidth, wlHeight)
        texId = initTexture(bm)

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -2f)
        entities[0].drawSelf(texId)
        MatrixState.popMatrix()
    }

    fun initTexture(bitmap: Bitmap): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT.toFloat())

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        return textureId
    }
}