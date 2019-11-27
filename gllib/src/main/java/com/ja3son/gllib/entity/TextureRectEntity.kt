package com.ja3son.gllib.entity

import android.opengl.GLES30
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TextureRectEntity(val textureId: Int, val sRange: Float, val tRange: Float) : BaseEntity() {
    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6
        val UNIT_SIZE = 0.3f
        val vertices = floatArrayOf(-4 * UNIT_SIZE, 4 * UNIT_SIZE, 0f, -4 * UNIT_SIZE, -4 * UNIT_SIZE, 0f, 4 * UNIT_SIZE, -4 * UNIT_SIZE, 0f,
                4 * UNIT_SIZE, -4 * UNIT_SIZE, 0f, 4 * UNIT_SIZE, 4 * UNIT_SIZE, 0f, -4 * UNIT_SIZE, 4 * UNIT_SIZE, 0f)

        val texCoor = floatArrayOf(
                0f, 0f, 0f, tRange, sRange, tRange,
                sRange, tRange, sRange, 0f, 0f, 0f
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
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        GLES30.glUseProgram(program)
        MatrixState.setInitStack()
        MatrixState.translate(0f, 0f, 1f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }
}