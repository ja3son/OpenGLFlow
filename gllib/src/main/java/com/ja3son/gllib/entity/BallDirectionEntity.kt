package com.ja3son.gllib.entity

import android.opengl.GLES30
import android.opengl.GLES32
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class BallDirectionEntity(val r: Float) : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        val alVertix = ArrayList<Float>()
        val angleSpan = 10
        var vAngle = -90
        while (vAngle < 90) {
            var hAngle = 0
            while (hAngle <= 360) {
                val x0 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.cos(Math
                        .toRadians(hAngle.toDouble()))).toFloat()
                val y0 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.sin(Math
                        .toRadians(hAngle.toDouble()))).toFloat()
                val z0 = (r * UNIT_SIZE * Math.sin(Math
                        .toRadians(vAngle.toDouble()))).toFloat()

                val x1 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.cos(Math
                        .toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y1 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.sin(Math
                        .toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z1 = (r * UNIT_SIZE * Math.sin(Math
                        .toRadians(vAngle.toDouble()))).toFloat()

                val x2 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .cos(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y2 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .sin(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z2 = (r * UNIT_SIZE * Math.sin(Math
                        .toRadians((vAngle + angleSpan).toDouble()))).toFloat()

                val x3 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y3 = (r * UNIT_SIZE
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z3 = (r * UNIT_SIZE * Math.sin(Math
                        .toRadians((vAngle + angleSpan).toDouble()))).toFloat()

                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                alVertix.add(x0)
                alVertix.add(y0)
                alVertix.add(z0)

                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                hAngle += angleSpan
            }
            vAngle += angleSpan
        }
        vCounts = alVertix.size / 3
        val vertices = FloatArray(vCounts * 3)
        for (i in 0 until alVertix.size) {
            vertices[i] = alVertix[i]
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("ball_direction_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("ball_direction_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uRHandler = GLES30.glGetUniformLocation(program, "uR")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        uLightDirection = GLES30.glGetUniformLocation(program, "uLightDirection")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
    }

    override fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)

        GLES32.glUniform1f(uRHandler, r * UNIT_SIZE)
        GLES30.glUniform3fv(uLightDirection, 1, MatrixState.lightDirectionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aNormal)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)
    }
}