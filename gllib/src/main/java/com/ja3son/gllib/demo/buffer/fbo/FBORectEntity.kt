package com.ja3son.gllib.demo.buffer.fbo

import android.opengl.GLES30
import android.opengl.GLES32
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class FBORectEntity(private val ratio: Float) : BaseEntity() {

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var texCoordBufferId = 0

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6

        val vertices = floatArrayOf(
                -ratio * UNIT_SIZE, UNIT_SIZE, 0f,
                -ratio * UNIT_SIZE, -UNIT_SIZE, 0f,
                ratio * UNIT_SIZE, -UNIT_SIZE, 0f,

                ratio * UNIT_SIZE, -UNIT_SIZE, 0f,
                ratio * UNIT_SIZE, UNIT_SIZE, 0f,
                -ratio * UNIT_SIZE, UNIT_SIZE, 0f
        )

        val texCoords = floatArrayOf(
                0f, 1f,
                0f, 0f,
                1.0f, 0f,
                1.0f, 0f,
                1.0f, 1f,
                0f, 1f
        )

        GLES32.glGenBuffers(2, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES32.GL_STATIC_DRAW)

        texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoords).position(0) as FloatBuffer

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES32.GL_STATIC_DRAW)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_tex_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)

        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aTexCoor)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES32.glVertexAttribPointer(aTexCoor, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)
    }
}