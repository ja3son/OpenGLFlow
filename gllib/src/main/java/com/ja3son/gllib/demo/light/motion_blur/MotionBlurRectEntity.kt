package com.ja3son.gllib.demo.light.motion_blur

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MotionBlurRectEntity(val ratio: Float) : BaseEntity() {
    var sceneTextureHandle = 0
    var depthTextureHandle = 0
    var mViewProjectionInverseMatrixHandle = 0
    var mPreviousProjectionMatrixHandle = 0
    var mSampleNumberHandle = 0

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6
        val UNIT_SIZE = 1.0f
        val vertices = floatArrayOf(
                -ratio * UNIT_SIZE, UNIT_SIZE, 0f,
                -ratio * UNIT_SIZE, -UNIT_SIZE, 0f,
                ratio * UNIT_SIZE, -UNIT_SIZE, 0f,
                ratio * UNIT_SIZE, -UNIT_SIZE, 0f,
                ratio * UNIT_SIZE, UNIT_SIZE, 0f,
                -ratio * UNIT_SIZE, UNIT_SIZE, 0f)

        val texCoor = floatArrayOf(
                0f, 1f,
                0f, 0f,
                1.0f, 0f,
                1.0f, 0f,
                1.0f, 1.0f,
                0f, 1.0f
        )

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        texCoorBuffer = ByteBuffer.allocateDirect(texCoor.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoor).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("motion_blur/vertex_tex.glsl"),
                ShaderUtils.loadFromAssetsFile("motion_blur/frag_tex.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        sceneTextureHandle = GLES30.glGetUniformLocation(program, "sTexture")
        depthTextureHandle = GLES30.glGetUniformLocation(program, "depthTexture")
        mViewProjectionInverseMatrixHandle = GLES30.glGetUniformLocation(program, "uViewProjectionInverseMatrix")
        mPreviousProjectionMatrixHandle = GLES30.glGetUniformLocation(program, "uPreviousProjectionMatrix")
        mSampleNumberHandle = GLES30.glGetUniformLocation(program, "g_numSamples")
    }

    fun drawSelf(textureId: Int, depthId: Int, previousMatrix: FloatArray, viewProjMatrix: FloatArray, samplerNum: Int) {
        GLES30.glUseProgram(program)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, 1f)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(mPreviousProjectionMatrixHandle, 1, false, previousMatrix, 0)
        GLES30.glUniformMatrix4fv(mViewProjectionInverseMatrixHandle, 1, false, viewProjMatrix, 0)
        GLES30.glUniform1i(mSampleNumberHandle, samplerNum)

        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, depthId)

        GLES30.glUniform1i(sceneTextureHandle, 0)
        GLES30.glUniform1i(depthTextureHandle, 1)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aPosition)
        MatrixState.popMatrix()
    }
}