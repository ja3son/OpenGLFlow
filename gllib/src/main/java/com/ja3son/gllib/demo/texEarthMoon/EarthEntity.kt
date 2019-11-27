package com.ja3son.gllib.demo.texEarthMoon


import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class EarthEntity(val r: Float, val texDay: Int, val texNight: Int) : BaseEntity() {
    var UNIT_SIZE_TEMP: Float = 0f
    var uDayTex: Int = 0
    var uNightTex: Int = 0
    var uLightLocationSun: Int = 0

    init {
        init()
    }

    override fun initVertexData() {
        UNIT_SIZE_TEMP = 0.5f
        val alVertix = ArrayList<Float>()
        val angleSpan = 10f
        var vAngle = 90f
        while (vAngle > -90) {
            var hAngle = 360f
            while (hAngle > 0) {
                var xozLength = r * UNIT_SIZE_TEMP * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x1 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y1 = (r * UNIT_SIZE_TEMP * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                xozLength = r * UNIT_SIZE_TEMP * Math.cos(Math.toRadians((vAngle - angleSpan).toDouble()))
                val x2 = (xozLength * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z2 = (xozLength * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y2 = (r * UNIT_SIZE_TEMP * Math.sin(Math.toRadians((vAngle - angleSpan).toDouble()))).toFloat()
                xozLength = r * UNIT_SIZE_TEMP * Math.cos(Math.toRadians((vAngle - angleSpan).toDouble()))
                val x3 = (xozLength * Math.cos(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val z3 = (xozLength * Math.sin(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val y3 = (r * UNIT_SIZE_TEMP * Math.sin(Math.toRadians((vAngle - angleSpan).toDouble()))).toFloat()
                xozLength = r * UNIT_SIZE_TEMP * Math.cos(Math.toRadians(vAngle.toDouble()))
                val x4 = (xozLength * Math.cos(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val z4 = (xozLength * Math.sin(Math.toRadians((hAngle - angleSpan).toDouble()))).toFloat()
                val y4 = (r * UNIT_SIZE_TEMP * Math.sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x4)
                alVertix.add(y4)
                alVertix.add(z4)
                alVertix.add(x4)
                alVertix.add(y4)
                alVertix.add(z4)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                hAngle = hAngle - angleSpan
            }
            vAngle = vAngle - angleSpan
        }
        vCounts = alVertix.size / 3
        val vertices = FloatArray(vCounts * 3)
        for (i in 0 until alVertix.size) {
            vertices[i] = alVertix[i]
        }

        val texCoor = generateTexCoor(
                (360 / angleSpan).toInt(),
                (180 / angleSpan).toInt()
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
                ShaderUtils.loadFromAssetsFile("earth_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("earth_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uLightLocationSun = GLES30.glGetUniformLocation(program, "uLightLocationSun")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
        uDayTex = GLES30.glGetUniformLocation(program, "sTextureDay")
        uNightTex = GLES30.glGetUniformLocation(program, "sTextureNight")
    }

    override fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)

        GLES30.glUniform3fv(uLightLocationSun, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aTexCoor, posLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)
        GLES30.glEnableVertexAttribArray(aNormal)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texDay)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texNight)
        GLES30.glUniform1i(uDayTex, 0)
        GLES30.glUniform1i(uNightTex, 1)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
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
                result[c++] = s
                result[c++] = t + sizeh
                result[c++] = s + sizew
                result[c++] = t
                result[c++] = s + sizew
                result[c++] = t
                result[c++] = s
                result[c++] = t + sizeh
                result[c++] = s + sizew
                result[c++] = t + sizeh
            }
        }
        return result
    }
}