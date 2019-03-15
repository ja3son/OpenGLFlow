package com.ja3son.gllib.demo.objects.cylinder.entity

import android.opengl.GLES30
import android.opengl.GLES32
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CylinderCircleLEntity(val r: Float, val scale: Float, val count: Int) : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val tempR = r * scale
        val angdegSpan = 360.0f / count
        vCounts = 3 * count

        val vertices = FloatArray(vCounts * 3)
        val colors = FloatArray(vCounts * 4)

        var count = 0
        var colorCount = 0
        var angdeg = 0f
        while (Math.ceil(angdeg.toDouble()) < 360) {
            val angrad = Math.toRadians(angdeg.toDouble())
            val angradNext = Math.toRadians((angdeg + angdegSpan).toDouble())

            vertices[count++] = 0f
            vertices[count++] = 0f
            vertices[count++] = 0f

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = (tempR * Math.cos(angrad)).toFloat()
            vertices[count++] = 0f


            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = (tempR * Math.cos(angradNext)).toFloat()
            vertices[count++] = 0f


            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            angdeg += angdegSpan
        }

        val normals = FloatArray(vertices.size)
        var i = 0
        while (i < normals.size) {
            normals[i] = 0f
            normals[i + 1] = 0f
            normals[i + 2] = 1f
            i += 3
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        colorsBuffer = ByteBuffer.allocateDirect(colors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colors).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(normals).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("color_light_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("color_light_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aColor = GLES30.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        aNormal = GLES30.glGetUniformLocation(program, "aNormal")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
    }

    override fun drawSelf() {
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES32.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
        GLES32.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aColor, colorLen, GLES32.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES32.glVertexAttribPointer(aNormal, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aColor)
        GLES32.glEnableVertexAttribArray(aNormal)
        GLES32.glLineWidth(2f)
        GLES32.glDrawArrays(GLES32.GL_LINE_STRIP, 0, vCounts)
    }
}