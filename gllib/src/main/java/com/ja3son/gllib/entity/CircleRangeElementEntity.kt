package com.ja3son.gllib.entity

import android.opengl.GLES30
import android.os.Build
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CircleRangeElementEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val n = 8
        vCounts = n + 2

        val angdegSpan = 360.0f / n
        val vertices = FloatArray(vCounts * 3)
        var tempCount = 0
        vertices[tempCount++] = 0f
        vertices[tempCount++] = 0f
        vertices[tempCount++] = 0f
        var angdeg = 0f
        while (Math.ceil(angdeg.toDouble()) <= 360) {
            val angrad = Math.toRadians(angdeg.toDouble())
            vertices[tempCount++] = (-UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f
            angdeg += angdegSpan
        }

        val indices = byteArrayOf(0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 6, 7, 0, 7, 8, 0, 8, 1)
        iCounts = indices.size

        tempCount = 0
        val colors = FloatArray(vCounts * 4)
        colors[tempCount++] = 1f
        colors[tempCount++] = 1f
        colors[tempCount++] = 1f
        colors[tempCount++] = 0f
        var i = 4
        while (i < colors.size) {
            colors[tempCount++] = 0f
            colors[tempCount++] = 1f
            colors[tempCount++] = 0f
            colors[tempCount++] = 0f
            i += 4
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        indicesBuffer = ByteBuffer.allocateDirect(indices.size)
                .order(ByteOrder.nativeOrder())
                .put(indices).position(0) as ByteBuffer

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
    }

    fun drawSelf(start: Int, count: Int) {
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aColor, colorLen, GLES30.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aColor)
        indicesBuffer.position(start)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES30.glDrawRangeElements(GLES30.GL_TRIANGLES, 0, 8, count, GLES30.GL_UNSIGNED_BYTE, indicesBuffer)
        }
    }
}