package com.ja3son.gllib.demo.fragment.ball

import android.opengl.GLES32
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin


class VolleyBallEntity : BaseEntity() {

    init {
        init()
    }

    var aLongLat = 0
    lateinit var longlatBuffer: FloatBuffer

    override fun initVertexData() {
        val r = 0.8f
        val alVertix = ArrayList<Float>()
        val alLongLat = ArrayList<Float>()
        val angleSpan = 10
        var vAngle = -90
        while (vAngle < 90) {
            var hAngle = 0
            while (hAngle <= 360) {
                val x0 = (r * UNIT_SIZE * cos(Math.toRadians(vAngle.toDouble())) * cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y0 = (r * UNIT_SIZE * cos(Math.toRadians(vAngle.toDouble())) * sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z0 = (r * UNIT_SIZE * sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                val long0 = hAngle.toFloat()
                val lat0 = vAngle.toFloat()

                val x1 = (r * UNIT_SIZE * cos(Math.toRadians(vAngle.toDouble())) * cos(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y1 = (r * UNIT_SIZE * cos(Math.toRadians(vAngle.toDouble())) * sin(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z1 = (r * UNIT_SIZE * sin(Math.toRadians(vAngle.toDouble()))).toFloat()
                val long1 = (hAngle + angleSpan).toFloat()
                val lat1 = vAngle.toFloat()

                val x2 = (r * UNIT_SIZE * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * cos(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y2 = (r * UNIT_SIZE * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * sin(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z2 = (r * UNIT_SIZE * sin(Math.toRadians((vAngle + angleSpan).toDouble()))).toFloat()
                val long2 = (hAngle + angleSpan).toFloat()
                val lat2 = (vAngle + angleSpan).toFloat()

                val x3 = (r * UNIT_SIZE * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y3 = (r * UNIT_SIZE * cos(Math.toRadians((vAngle + angleSpan).toDouble())) * sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z3 = (r * UNIT_SIZE * sin(Math.toRadians((vAngle + angleSpan).toDouble()))).toFloat()
                val long3 = hAngle.toFloat()
                val lat3 = (vAngle + angleSpan).toFloat()

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

                alLongLat.add(long1)
                alLongLat.add(lat1)
                alLongLat.add(long3)
                alLongLat.add(lat3)
                alLongLat.add(long0)
                alLongLat.add(lat0)

                alLongLat.add(long1)
                alLongLat.add(lat1)
                alLongLat.add(long2)
                alLongLat.add(lat2)
                alLongLat.add(long3)
                alLongLat.add(lat3)
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

        val longlat = FloatArray(alLongLat.size)
        for (i in alLongLat.indices) {
            longlat[i] = alLongLat[i]
        }

        longlatBuffer = ByteBuffer.allocateDirect(longlat.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(longlat).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("ball_frag_anim_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("volley_ball_frag_anim_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aLongLat = GLES32.glGetAttribLocation(program, "aLongLat")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        GLES32.glUseProgram(program)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)

        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aLongLat)

        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aLongLat, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, longlatBuffer)

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)

        GLES32.glDisableVertexAttribArray(aPosition)
        GLES32.glDisableVertexAttribArray(aLongLat)
    }
}