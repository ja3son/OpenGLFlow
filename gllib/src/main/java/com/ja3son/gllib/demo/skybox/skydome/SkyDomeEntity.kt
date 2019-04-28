package com.ja3son.gllib.demo.skybox.skydome

import android.opengl.GLES32
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class SkyDomeEntity : BaseEntity() {
    override var UNIT_SIZE = 100f

    init {
        init()
    }

    override fun initVertexData() {
        val ANGLE_SPAN = 18f
        val angleV = 90f
        val alVertix = ArrayList<Float>()

        var vAngle = angleV
        while (vAngle > 0) {
            var hAngle = 360f
            while (hAngle > 0) {

                var xozLength = UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x1 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y1 = (UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()

                xozLength = UNIT_SIZE * Math.cos(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))
                val x2 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z2 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y2 = (UNIT_SIZE * Math.sin(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))).toFloat()

                xozLength = UNIT_SIZE * Math.cos(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))
                val x3 = (xozLength * Math.cos(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val z3 = (xozLength * Math.sin(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val y3 = (UNIT_SIZE * Math.sin(Math.toRadians((vAngle - ANGLE_SPAN).toDouble()))).toFloat()

                xozLength = UNIT_SIZE * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x4 = (xozLength * Math.cos(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val z4 = (xozLength * Math.sin(Math.toRadians((hAngle - ANGLE_SPAN).toDouble()))).toFloat()
                val y4 = (UNIT_SIZE * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()

                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x4)
                alVertix.add(y4)
                alVertix.add(z4)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)

                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x4)
                alVertix.add(y4)
                alVertix.add(z4)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                hAngle -= ANGLE_SPAN
            }
            vAngle -= ANGLE_SPAN
        }
        vCounts = alVertix.size / 3

        val vertices = FloatArray(vCounts * 3)
        for (i in 0 until alVertix.size) {
            vertices[i] = alVertix[i]
        }

        val texCoor = generateTexCoor(
                (360 / ANGLE_SPAN).toInt(),
                (angleV / ANGLE_SPAN).toInt()
        )

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        texCoorBuffer = ByteBuffer.allocateDirect(texCoor.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoor).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_tex_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES32.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf(textureId: Int) {
        GLES32.glUseProgram(program)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aTexCoor, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aTexCoor)
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)
    }

    fun generateTexCoor(bw: Int, bh: Int): FloatArray {
        val result = FloatArray(bw * bh * 6 * 2)
        val sizew = 1.0f / bw
        val sizeh = 1.0f / bh
        var c = 0
        for (i in 0 until bh) {
            for (j in 0 until bw) {

                val s = j * sizew
                val t = i * sizeh

                result[c++] = s
                result[c++] = t

                result[c++] = s + sizew
                result[c++] = t

                result[c++] = s
                result[c++] = t + sizeh

                result[c++] = s
                result[c++] = t + sizeh

                result[c++] = s + sizew
                result[c++] = t

                result[c++] = s + sizew
                result[c++] = t + sizeh
            }
        }
        return result
    }
}