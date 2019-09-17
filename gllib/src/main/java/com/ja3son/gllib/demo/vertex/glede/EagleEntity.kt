package com.ja3son.gllib.demo.vertex.glede

import android.opengl.GLES32
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
        GLES32.glGenBuffers(4, bufferIds, 0)

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

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES32.GL_STATIC_DRAW)
        }

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferOneId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES32.GL_STATIC_DRAW)
        }

        ShaderUtils.loadObj("eagle_two.obj")
        vertices = ShaderUtils.vXYZ

        if (vertices != null) {
            verticesBuffer.put(vertices).position(0)

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferTwoId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES32.GL_STATIC_DRAW)
        }

        ShaderUtils.loadObj("eagle_three.obj")
        vertices = ShaderUtils.vXYZ

        if (vertices != null) {
            verticesBuffer.put(vertices).position(0)

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferThreeId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES32.GL_STATIC_DRAW)
        }

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("eagle_obj_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("eagle_obj_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aOnePosition = GLES32.glGetAttribLocation(program, "aOnePosition")
        aTwoPosition = GLES32.glGetAttribLocation(program, "aTwoPosition")
        aThreePosition = GLES32.glGetAttribLocation(program, "aThreePosition")
        aTexCoor = GLES32.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
        uFlag = GLES32.glGetUniformLocation(program, "flag")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES32.glUseProgram(program)

        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glUniform1f(uFlag, flagValue)

        GLES32.glEnableVertexAttribArray(aOnePosition)
        GLES32.glEnableVertexAttribArray(aTwoPosition)
        GLES32.glEnableVertexAttribArray(aThreePosition)
        GLES32.glEnableVertexAttribArray(aTexCoor)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferOneId)
        GLES32.glVertexAttribPointer(aOnePosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferTwoId)
        GLES32.glVertexAttribPointer(aTwoPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferThreeId)
        GLES32.glVertexAttribPointer(aThreePosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES32.glVertexAttribPointer(aTexCoor, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)

        GLES32.glDisableVertexAttribArray(aOnePosition)
        GLES32.glDisableVertexAttribArray(aTwoPosition)
        GLES32.glDisableVertexAttribArray(aThreePosition)
        GLES32.glDisableVertexAttribArray(aTexCoor)
    }
}