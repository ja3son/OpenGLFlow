package com.ja3son.gllib.demo.senior.instance.grass

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class GrassEntity(private val fName: String, private val nums: Int) : BaseEntity() {
    var textureId = 0
    var noiseId = 0
    var gradualId = 0

    var sourceTex = 0
    var noiseTex = 0
    var gradualTex = 0
    var totalNum = 0

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var texCoordBufferId = 0

    init {
        textureId = ShaderUtils.initTexture(R.drawable.grass)
        noiseId = ShaderUtils.initTexture(R.drawable.noise2)
        gradualId = ShaderUtils.initTexture(R.drawable.jb)
        init()
    }

    override fun initVertexData() {
        ShaderUtils.loadObj(fName)
        val vertices = ShaderUtils.vXYZ
        val texCoords = ShaderUtils.tST

        GLES30.glGenBuffers(2, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (texCoords != null) {
            texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(texCoords).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES30.GL_STATIC_DRAW)
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("instance/grass/grass_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("instance/grass/grass_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        sourceTex = GLES30.glGetUniformLocation(program, "sourceTex")
        noiseTex = GLES30.glGetUniformLocation(program, "noiseTex")
        gradualTex = GLES30.glGetUniformLocation(program, "gradualTex")
        totalNum = GLES30.glGetUniformLocation(program, "totalNum")
    }

    override fun drawSelf() {
        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uViewProjMatrix, 1, false, MatrixState.getViewProjMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, noiseId)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE2)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, gradualId)

        GLES30.glUniform1i(sourceTex, 0)
        GLES30.glUniform1i(noiseTex, 1)
        GLES30.glUniform1i(gradualTex, 2)

        GLES30.glUniform1f(totalNum, nums.toFloat())

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glDrawArraysInstanced(GLES30.GL_TRIANGLES, 0, vCounts, nums)

        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aTexCoor)
    }
}