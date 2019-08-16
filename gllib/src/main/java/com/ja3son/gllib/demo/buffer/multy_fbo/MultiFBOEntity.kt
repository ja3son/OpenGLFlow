package com.ja3son.gllib.demo.buffer.multy_fbo

import android.opengl.GLES30
import android.opengl.GLES32
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MultiFBOEntity(private val fName: String) : BaseEntity() {

    val bufferIds = IntArray(3)
    var vertexBufferId = 0
    var normalBufferId = 0
    var texCoordBufferId = 0

    init {
        init()
    }

    override fun initVertexData() {

        ShaderUtils.loadObjWithNormal(fName)
        val vertices = ShaderUtils.vXYZ
        val normals = ShaderUtils.nXYZ
        val texCoords = ShaderUtils.tST

        GLES32.glGenBuffers(3, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        normalBufferId = bufferIds[1]
        texCoordBufferId = bufferIds[2]

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES32.GL_STATIC_DRAW)
        }

        if (normals != null) {
            normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(normals).position(0) as FloatBuffer

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, normalBufferId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, normals.size * FLOAT_SIZE, normalBuffer, GLES32.GL_STATIC_DRAW)
        }

        if (texCoords != null) {
            texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(texCoords).position(0) as FloatBuffer

            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
            GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES32.GL_STATIC_DRAW)
        }

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("obj_tex_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("obj_fbo_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES32.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES32.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aNormal)
        GLES32.glEnableVertexAttribArray(aTexCoor)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, normalBufferId)
        GLES32.glVertexAttribPointer(aNormal, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES32.glVertexAttribPointer(aTexCoor, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)

        GLES32.glDisableVertexAttribArray(aNormal)
        GLES32.glDisableVertexAttribArray(aPosition)
        GLES32.glDisableVertexAttribArray(aTexCoor)
    }
}