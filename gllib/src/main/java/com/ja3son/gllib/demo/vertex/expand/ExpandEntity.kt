package com.ja3son.gllib.demo.vertex.expand


import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class ExpandEntity(private val fileName: String) : BaseEntity() {

    private val bufferIds = IntArray(3)
    private var vertexBufferId = 0
    private var texCoordBufferId = 0
    private var normalBufferId = 0
    private var uFatFactor = 0

    var fatFacror = 0f
    var fatFacrorStep = 0.001f

    init {
        init()
    }

    override fun initVertexData() {
        GLES30.glGenBuffers(3, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]
        normalBufferId = bufferIds[2]

        ShaderUtils.loadObj(fileName)
        val vertices = ShaderUtils.vXYZ
        val texCoords = ShaderUtils.tST
        val normals = ShaderUtils.nXYZ

        if (texCoords != null) {
            texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(texCoords).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (normals != null) {
            normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(normals).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, normals.size * FLOAT_SIZE, normalBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("expand_obj_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("expand_obj_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
        uFatFactor = GLES30.glGetUniformLocation(program, "uFatFactor")
    }

    override fun drawSelf(textureId: Int) {
        fatFacror += fatFacrorStep
        if (fatFacror > 0.05f || fatFacror < 0f) {
            fatFacrorStep = -fatFacrorStep
        }

        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
        GLES30.glUniform1f(uFatFactor, fatFacror)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aNormal)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferId)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aNormal)
        GLES30.glDisableVertexAttribArray(aTexCoor)
    }
}