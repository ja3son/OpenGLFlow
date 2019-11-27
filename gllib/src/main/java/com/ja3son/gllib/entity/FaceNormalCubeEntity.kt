package com.ja3son.gllib.entity


import android.opengl.GLES30
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class FaceNormalCubeEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6 * 6
        val vertices = floatArrayOf(
                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,

                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,

                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,

                UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE,
                -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE,

                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,

                UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE,
                -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE,
                UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE)

        val normals = floatArrayOf(
                0f, 0f, 1f, 0f,
                0f, 1f, 0f, 0f,
                1f, 0f, 0f, 1f,
                0f, 0f, 1f, 0f,
                0f, 1f, 0f, 0f,
                -1f, 0f, 0f, -1f,
                0f, 0f, -1f, 0f,
                0f, -1f, 0f, 0f,
                -1f, 0f, 0f, -1f,
                -1f, 0f, 0f, -1f,
                0f, 0f, -1f, 0f,
                0f, -1f, 0f, 0f,
                -1f, 0f, 0f, -1f,
                0f, 0f, 1f, 0f,
                0f, 1f, 0f, 0f,
                1f, 0f, 0f, 1f,
                0f, 0f, 1f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                1f, 0f, 0f, 1f,
                0f, 0f, 1f, 0f,
                0f, 1f, 0f, 0f,
                1f, 0f, 0f, -1f,
                0f, 0f, -1f, 0f,
                0f, -1f, 0f, 0f,
                -1f, 0f, 0f, -1f,
                0f, 0f, -1f, 0f)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(normals).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("ball_super_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("ball_super_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uRHandler = GLES30.glGetUniformLocation(program, "uR")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
    }

    override fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)

        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform1f(uRHandler, UNIT_SIZE)
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aNormal, colorLen, GLES30.GL_FLOAT, false, colorLen * FLOAT_SIZE, normalBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aNormal)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }
}