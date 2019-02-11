package com.ja3son.gllib.entity

import android.opengl.GLES32
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MultBeltEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val n1 = 3
        val n2 = 5
        counts = 2 * (n1 + n2 + 2) + 2
        val angdegBegin1 = 0f
        val angdegEnd1 = 90f
        val angdegSpan1 = (angdegEnd1 - angdegBegin1) / n1

        val angdegBegin2 = 180f
        val angdegEnd2 = 270f
        val angdegSpan2 = (angdegEnd2 - angdegBegin2) / n2
        val vertices = FloatArray(counts * 3)


        var tempCount = 0
        var angdeg = angdegBegin1
        while (angdeg <= angdegEnd1) {
            val angrad = Math.toRadians(angdeg.toDouble())

            vertices[tempCount++] = (-0.6f * UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (0.6f * UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f

            vertices[tempCount++] = (-UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f
            angdeg += angdegSpan1
        }

        vertices[tempCount++] = vertices[tempCount - 4]
        vertices[tempCount++] = vertices[tempCount - 4]
        vertices[tempCount++] = 0f

        angdeg = angdegBegin2
        while (angdeg <= angdegEnd2) {
            val angrad = Math.toRadians(angdeg.toDouble())

            if (angdeg == angdegBegin2) {
                vertices[tempCount++] = (-0.6f * UNIT_SIZE * Math.sin(angrad)).toFloat()
                vertices[tempCount++] = (0.6f * UNIT_SIZE * Math.cos(angrad)).toFloat()
                vertices[tempCount++] = 0f
            }

            vertices[tempCount++] = (-0.6f * UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (0.6f * UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f

            vertices[tempCount++] = (-UNIT_SIZE * Math.sin(angrad)).toFloat()
            vertices[tempCount++] = (UNIT_SIZE * Math.cos(angrad)).toFloat()
            vertices[tempCount++] = 0f
            angdeg += angdegSpan2
        }

        tempCount = 0

        val colors = FloatArray(counts * 4)
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
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 0, counts)
    }
}