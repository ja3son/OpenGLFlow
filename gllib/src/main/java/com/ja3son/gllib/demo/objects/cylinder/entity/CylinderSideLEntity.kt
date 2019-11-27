package com.ja3son.gllib.demo.objects.cylinder.entity


import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CylinderSideLEntity(val r: Float, val hight: Float, val scale: Float, val count: Int) : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val tempR = r * scale
        val tempH = hight * scale
        val angdegSpan = 360.0f / count
        vCounts = 3 * count * 4

        val vertices = FloatArray(vCounts * 3)
        val colors = FloatArray(vCounts * 4)
        var count = 0
        var colorCount = 0
        var angdeg = 0f
        while (Math.ceil(angdeg.toDouble()) < 360) {
            val angrad = Math.toRadians(angdeg.toDouble())
            val angradNext = Math.toRadians((angdeg + angdegSpan).toDouble())

            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = 0f
            vertices[count++] = (-tempR * Math.cos(angrad)).toFloat()

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = tempH
            vertices[count++] = (-tempR * Math.cos(angradNext)).toFloat()

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = tempH
            vertices[count++] = (-tempR * Math.cos(angrad)).toFloat()

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f


            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = 0f
            vertices[count++] = (-tempR * Math.cos(angrad)).toFloat()

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = 0f
            vertices[count++] = (-tempR * Math.cos(angradNext)).toFloat()

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = tempH
            vertices[count++] = (-tempR * Math.cos(angradNext)).toFloat()

            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            colors[colorCount++] = 1f
            angdeg += angdegSpan
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        for (i in 0 until vertices.size) {
            if (i % 3 == 1) {
                vertices[i] = 0f
            }
        }

        normalBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        colorsBuffer = ByteBuffer.allocateDirect(colors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colors).position(0) as FloatBuffer
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
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aColor, colorLen, GLES30.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aColor)
        GLES30.glEnableVertexAttribArray(aNormal)
        GLES30.glLineWidth(2f)
        GLES30.glDrawArrays(GLES30.GL_LINES, 0, vCounts)
    }
}