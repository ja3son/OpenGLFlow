package com.ja3son.gllib.entity

import android.opengl.GLES30
import com.ja3son.gllib.util.Constants
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class PointsOrLinesEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 5
        val vertices: FloatArray = floatArrayOf(
                0f, 0f, 0f, UNIT_SIZE, UNIT_SIZE, 0f,
                -UNIT_SIZE, UNIT_SIZE, 0f,
                -UNIT_SIZE, -UNIT_SIZE, 0f,
                UNIT_SIZE, -UNIT_SIZE, 0f
        )

        val colors: FloatArray = floatArrayOf(
                1f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                0f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, 1f, 0f, 0f
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
                ShaderUtils.loadFromAssetsFile("point_size_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("point_size_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aColor = GLES30.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aColor, colorLen, GLES30.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aColor)
        GLES30.glLineWidth(10f)
        Constants.curDrawState = Constants.DrawState.values()[touchIndex % Constants.DrawState.values().size]
        when (Constants.curDrawState) {
            Constants.DrawState.GL_POINTS -> {
                GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCounts)
            }
            Constants.DrawState.GL_LINES -> {
                GLES30.glDrawArrays(GLES30.GL_LINES, 0, vCounts)
            }
            Constants.DrawState.GL_LINE_STRIP -> {
                GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, vCounts)
            }
            Constants.DrawState.GL_LINE_LOOP -> {
                GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, vCounts)
            }
        }
    }
}