package com.ja3son.gllib.demo.texEarthMoon

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class CelestialEntity(val scale: Float, val vCount: Int) : BaseEntity() {
    var uPointSize: Int = 0

    init {
        init()
    }

    override fun initVertexData() {
        val vertices = FloatArray(vCount * 3)
        for (i in 0 until vCount) {
            val angleTempJD = Math.PI * 2.0 * Math.random()
            val angleTempWD = Math.PI * (Math.random() - 0.5f)
            vertices[i * 3] = (UNIT_SIZE.toDouble() * Math.cos(angleTempWD) * Math.sin(angleTempJD)).toFloat()
            vertices[i * 3 + 1] = (UNIT_SIZE * Math.sin(angleTempWD)).toFloat()
            vertices[i * 3 + 2] = (UNIT_SIZE.toDouble() * Math.cos(angleTempWD) * Math.cos(angleTempJD)).toFloat()
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("celestial_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("celestial_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uPointSize = GLES30.glGetUniformLocation(program, "uPointSize")
    }

    override fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniform1f(uPointSize, scale)
        GLES30.glVertexAttribPointer(
                aPosition,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                verticesBuffer
        )
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount)
    }
}