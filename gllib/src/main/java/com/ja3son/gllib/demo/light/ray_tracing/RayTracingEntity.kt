package com.ja3son.gllib.demo.light.ray_tracing

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.OBJUtils
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class RayTracingEntity(private val fName: String) : BaseEntity() {

    val bufferIds = IntArray(4)
    var vertexBufferId = 0
    var normalBufferId = 0
    var texCoordBufferId = 0
    var noiseNormalBufferId = 0

    var aNoiseNormal: Int = 0
    var sTextureBG: Int = 0
    var sTextureNormal: Int = 0

    init {
        init()
    }

    override fun initVertexData() {
        OBJUtils.loadOBJ_vertex_st_normal_noise(fName)
        val vertices = OBJUtils.vXYZ
        val normals = OBJUtils.nXYZ
        val noiseNormal = OBJUtils.tnXYZ
        val texCoords = OBJUtils.tST

        GLES30.glGenBuffers(4, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        normalBufferId = bufferIds[1]
        texCoordBufferId = bufferIds[2]
        noiseNormalBufferId = bufferIds[3]

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (normals != null) {
            normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(normals).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, normals.size * FLOAT_SIZE, normalBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (texCoords != null) {
            texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(texCoords).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (noiseNormal != null) {
            noiseCoorBuffer = ByteBuffer.allocateDirect(noiseNormal.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(noiseNormal).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, noiseNormalBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, noiseNormal.size * FLOAT_SIZE, noiseCoorBuffer, GLES30.GL_STATIC_DRAW)
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("deviation_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("deviation_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        aNoiseNormal = GLES30.glGetAttribLocation(program, "aNoiseNormal")
        sTextureBG = GLES30.glGetUniformLocation(program, "sTextureBG")
        sTextureNormal = GLES30.glGetUniformLocation(program, "sTextureNormal")
    }

    override fun drawSelf(textureBG: Int, textureNormal: Int) {
        super.drawSelf(textureBG, textureNormal)

        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aNormal)
        GLES30.glEnableVertexAttribArray(aNoiseNormal)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferId)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, noiseNormalBufferId)
        GLES30.glVertexAttribPointer(aNoiseNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureBG)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureNormal)

        GLES30.glUniform1i(sTextureBG, 0)
        GLES30.glUniform1i(sTextureNormal, 1)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aNormal)
        GLES30.glDisableVertexAttribArray(aNoiseNormal)
        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aTexCoor)
    }
}