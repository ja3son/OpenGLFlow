package com.ja3son.gllib.entity

import android.opengl.GLES32
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CubeEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        counts = 12 * 6
        val vertices: FloatArray = floatArrayOf(
                0f, 0f, UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                0f, 0f, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                0f, 0f, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                0f, 0f, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,

                0f, 0f, -UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                0f, 0f, -UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                0f, 0f, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                0f, 0f, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,

                -UNIT_SIZE, 0f, 0f,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, 0f, 0f,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, 0f, 0f,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, 0f, 0f,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,

                UNIT_SIZE, 0f, 0f,
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, 0f, 0f,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, 0f, 0f,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, 0f, 0f,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,

                0f, UNIT_SIZE, 0f,
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                0f, UNIT_SIZE, 0f,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                0f, UNIT_SIZE, 0f,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                0f, UNIT_SIZE, 0f,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,

                0f, -UNIT_SIZE, 0f,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                0f, -UNIT_SIZE, 0f,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                0f, -UNIT_SIZE, 0f,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                0f, -UNIT_SIZE, 0f,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE
        )

        val colors: FloatArray = floatArrayOf(
                1f, 1f, 1f, 0f,
                1f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f,

                1f, 1f, 1f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 1f, 0f,
                1f, 1f, 1f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 1f, 0f,
                1f, 1f, 1f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 1f, 0f,
                1f, 1f, 1f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 1f, 0f,

                1f, 1f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 1f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 1f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 1f, 1f, 0f,
                1f, 0f, 1f, 0f,
                1f, 0f, 1f, 0f,

                1f, 1f, 1f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 1f, 0f, 0f,
                1f, 1f, 0f, 0f,

                1f, 1f, 1f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,

                1f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f,
                0f, 1f, 1f, 0f
        )

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        colorsBuffer = ByteBuffer.allocateDirect(colors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colors).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aColor = GLES32.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aColor, colorLen, GLES32.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aColor)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, counts)
    }
}