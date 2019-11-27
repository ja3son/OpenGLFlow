package com.ja3son.gllib.demo.sensor


import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SensorEntity(private val fName: String, private val texID: Int) : BaseEntity() {
    private var uParams: Int = -1
    lateinit var params:FloatArray

    init {
        init()
    }

    override fun initVertexData() {

        ShaderUtils.loadObjOrigin(fName)
        val vertices = ShaderUtils.vXYZ
        val normals = ShaderUtils.nXYZ
        val texCoords = ShaderUtils.tST

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer
        }

        if (normals != null) {
            normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(normals).position(0) as FloatBuffer
        }

        if (texCoords != null) {
            texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(texCoords).position(0) as FloatBuffer
        }

        var countE = 0f
        var spanE = 0.01f
        if (countE >= 2) {
            spanE = -0.01f
        } else if (countE <= 0) {
            spanE = 0.01f
        }
        countE += spanE
        params = floatArrayOf(1f, countE - 1, -countE + 1, 0f)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("tailoring_obj_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("tailoring_obj_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
        uParams = GLES30.glGetUniformLocation(program, "uParams")
    }

    override fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
        GLES30.glUniform4fv(uParams, 1, params, 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aNormal)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aNormal)
        GLES30.glDisableVertexAttribArray(aPosition)
    }
}