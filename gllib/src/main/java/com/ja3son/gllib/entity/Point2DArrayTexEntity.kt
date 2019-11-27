package com.ja3son.gllib.entity

import android.opengl.GLES30
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Point2DArrayTexEntity(val textureId: Int) : BaseEntity() {
    var uPointSize: Int = -1

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 9

        val vertices = floatArrayOf(
                0f, 0f, 0f,
                0f, UNIT_SIZE * 2, 0f,
                UNIT_SIZE, UNIT_SIZE / 2, 0f,
                -UNIT_SIZE / 3, UNIT_SIZE, 0f,
                -UNIT_SIZE * 0.4f, -UNIT_SIZE * 0.4f, 0f,
                -UNIT_SIZE, -UNIT_SIZE, 0f,
                UNIT_SIZE * 0.2f, -UNIT_SIZE * 0.7f, 0f,
                UNIT_SIZE / 2, -UNIT_SIZE * 3 / 2, 0f,
                -UNIT_SIZE * 4 / 5, -UNIT_SIZE * 3 / 2, 0f
        )

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("points_tex_array_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("points_tex_array_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uPointSize = GLES30.glGetUniformLocation(program, "uPointSize")
    }

    override fun drawSelf() {
        GLES30.glUseProgram(program)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES30.glUniform1f(uPointSize, 64.0f)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnable(GLES30.GL_TEXTURE_2D_ARRAY)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D_ARRAY, textureId)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCounts)
    }
}