package com.ja3son.gllib.demo.objects.cylinder.entity


import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CylinderSideEntity(val r: Float, val hight: Float, val scale: Float, val count: Int, val textureID: Int) : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val tempR = r * scale
        val tempH = hight * scale
        val angdegSpan = 360.0f / count
        vCounts = 3 * count * 4

        val vertices = FloatArray(vCounts * 3)
        val textures = FloatArray(vCounts * 2)
        var count = 0
        var stCount = 0

        var angdeg = 0f
        while (Math.ceil(angdeg.toDouble()) < 360) {
            val angrad = Math.toRadians(angdeg.toDouble())
            val angradNext = Math.toRadians((angdeg + angdegSpan).toDouble())

            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = 0f
            vertices[count++] = (-tempR * Math.cos(angrad)).toFloat()

            textures[stCount++] = (angrad / (2 * Math.PI)).toFloat()
            textures[stCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = tempH
            vertices[count++] = (-tempR * Math.cos(angradNext)).toFloat()

            textures[stCount++] = (angradNext / (2 * Math.PI)).toFloat()
            textures[stCount++] = 0f

            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = tempH
            vertices[count++] = (-tempR * Math.cos(angrad)).toFloat()

            textures[stCount++] = (angrad / (2 * Math.PI)).toFloat()
            textures[stCount++] = 0f

            vertices[count++] = (-tempR * Math.sin(angrad)).toFloat()
            vertices[count++] = 0f
            vertices[count++] = (-tempR * Math.cos(angrad)).toFloat()

            textures[stCount++] = (angrad / (2 * Math.PI)).toFloat()
            textures[stCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = 0f
            vertices[count++] = (-tempR * Math.cos(angradNext)).toFloat()

            textures[stCount++] = (angradNext / (2 * Math.PI)).toFloat()
            textures[stCount++] = 1f

            vertices[count++] = (-tempR * Math.sin(angradNext)).toFloat()
            vertices[count++] = tempH
            vertices[count++] = (-tempR * Math.cos(angradNext)).toFloat()

            textures[stCount++] = (angradNext / (2 * Math.PI)).toFloat()
            textures[stCount++] = 0f
            angdeg += angdegSpan
        }

        val normals = FloatArray(vertices.size)
        for (i in 0 until vertices.size) {
            if (i % 3 == 1) {
                normals[i] = 0f
            } else {
                normals[i] = vertices[i]
            }
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(normals).position(0) as FloatBuffer

        texCoorBuffer = ByteBuffer.allocateDirect(textures.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(textures).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("tex_light_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("tex_light_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
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
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)
        GLES30.glEnableVertexAttribArray(aNormal)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }
}