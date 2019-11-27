package com.ja3son.gllib.entity

import android.opengl.GLES30
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class BeltEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val n = 6
        vCounts = 2 * (n + 1)
        val angdegBegin = -90f
        val angdegEnd = 90f
        val angdegSpan = (angdegEnd - angdegBegin) / n

        val vertices = FloatArray(vCounts * 3)
        var tempCount = 0
        var angdeg = angdegBegin
        while (angdeg <= angdegEnd) {
            val angrad = Math.toRadians(angdeg.toDouble())
            vertices[tempCount++] = (-0.6f * UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (0.6f * UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f
            vertices[tempCount++] = (-UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f
            angdeg += angdegSpan
        }

        tempCount = 0

        val colors = FloatArray(vCounts * 4)
        var i = 0
        while (i < colors.size) {
            colors[tempCount++] = 1f
            colors[tempCount++] = 1f
            colors[tempCount++] = 1f
            colors[tempCount++] = 0f

            colors[tempCount++] = 0f
            colors[tempCount++] = 1f
            colors[tempCount++] = 1f
            colors[tempCount++] = 0f
            i += 8
        }

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
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, vCounts)
    }
}