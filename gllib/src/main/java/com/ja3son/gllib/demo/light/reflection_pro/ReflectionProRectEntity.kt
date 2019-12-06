package com.ja3son.gllib.demo.light.reflection_pro

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ReflectionProRectEntity : BaseEntity() {
    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6
        val UNIT_SIZE = 15f
        val vertices = floatArrayOf(
                -UNIT_SIZE, UNIT_SIZE, 0f,
                -UNIT_SIZE, -UNIT_SIZE, 0f,
                UNIT_SIZE, -UNIT_SIZE, 0f,
                UNIT_SIZE, -UNIT_SIZE, 0f,
                UNIT_SIZE, UNIT_SIZE, 0f,
                -UNIT_SIZE, UNIT_SIZE, 0f)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("reflection_pro/mirror_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("reflection_pro/mirror_fragment.glsl")
        )
    }

    fun drawSelf(textureId: Int, viewProjMatrix: FloatArray) {
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniformMatrix4fv(uViewProjatrix, 1, false, viewProjMatrix, 0)

        GLES30.glEnableVertexAttribArray(aPosition)

        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aPosition)
    }
}