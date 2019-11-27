package com.ja3son.gllib.demo.vertex.glede

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class EagleEntity : BaseEntity() {

    private val bufferIds = IntArray(4)
    private var vertexBufferOneId = 0
    private var vertexBufferTwoId = 0
    private var vertexBufferThreeId = 0
    private var texCoordBufferId = 0

    private var aOnePosition = 0
    private var aTwoPosition = 0
    private var aThreePosition = 0
    private var uFlag = 0
    private var flagValue: Float = 0.0f
    private var operator = 1
    private val span = 0.15f

    init {
        init()

        object : Thread() {
            override fun run() {
                while (true) {
                    flagValue += operator * span
                    if (flagValue > 2.0f) {
                        flagValue = 2.0f
                        operator = -operator
                    } else if (flagValue < 0) {
                        flagValue = 0F
                        operator = -operator
                    }
                    try {
                        sleep(50)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }.start()
    }

    override fun initVertexData() {
        GLES30.glGenBuffers(4, bufferIds, 0)

        vertexBufferOneId = bufferIds[0]
        vertexBufferTwoId = bufferIds[1]
        vertexBufferThreeId = bufferIds[2]
        texCoordBufferId = bufferIds[3]

        ShaderUtils.loadObj("eagle_one.obj")
        var vertices = ShaderUtils.vXYZ
        val texCoords = ShaderUtils.tST

        if (texCoords != null) {
            texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(texCoords).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferOneId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        ShaderUtils.loadObj("eagle_two.obj")
        vertices = ShaderUtils.vXYZ

        if (vertices != null) {
            verticesBuffer.put(vertices).position(0)

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferTwoId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        ShaderUtils.loadObj("eagle_three.obj")
        vertices = ShaderUtils.vXYZ

        if (vertices != null) {
            verticesBuffer.put(vertices).position(0)

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferThreeId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("eagle_obj_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("eagle_obj_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aOnePosition = GLES30.glGetAttribLocation(program, "aOnePosition")
        aTwoPosition = GLES30.glGetAttribLocation(program, "aTwoPosition")
        aThreePosition = GLES30.glGetAttribLocation(program, "aThreePosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uFlag = GLES30.glGetUniformLocation(program, "flag")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniform1f(uFlag, flagValue)

        GLES30.glEnableVertexAttribArray(aOnePosition)
        GLES30.glEnableVertexAttribArray(aTwoPosition)
        GLES30.glEnableVertexAttribArray(aThreePosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferOneId)
        GLES30.glVertexAttribPointer(aOnePosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferTwoId)
        GLES30.glVertexAttribPointer(aTwoPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferThreeId)
        GLES30.glVertexAttribPointer(aThreePosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glDisableVertexAttribArray(aOnePosition)
        GLES30.glDisableVertexAttribArray(aTwoPosition)
        GLES30.glDisableVertexAttribArray(aThreePosition)
        GLES30.glDisableVertexAttribArray(aTexCoor)
    }
}