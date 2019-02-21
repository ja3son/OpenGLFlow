package com.ja3son.gllib.entity

import android.opengl.GLES32
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TriangleTexEntity(val textureId: Int) : BaseEntity() {
    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 3

        val UNIT_SIZE = 0.15f

        val vertices = floatArrayOf(
                0 * UNIT_SIZE, 11 * UNIT_SIZE, 0f,
                -11 * UNIT_SIZE, -11 * UNIT_SIZE, 0f,
                11 * UNIT_SIZE, -11 * UNIT_SIZE, 0f
        )

        val texCoor = floatArrayOf(
                0.5f, 0f,
                0f, 1f,
                1f, 1f
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
                ShaderUtils.loadFromAssetsFile("triangle_tex_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES32.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        GLES32.glUseProgram(program)
        MatrixState.setInitStack()
        MatrixState.translate(0f, 0f, 1f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aTexCoor, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aTexCoor)
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)
    }
}