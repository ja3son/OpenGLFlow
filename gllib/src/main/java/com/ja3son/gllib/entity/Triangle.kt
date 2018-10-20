package com.ja3son.gllib.entity

import android.opengl.GLES32
import android.util.Log
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle : BaseEntity() {
    private val counts: Int = 3

    override fun initVertexData() {
        val vertices: FloatArray = floatArrayOf(
                -4.0f, 0.0f, 0.0f,
                0.0f, -4.0f, 0.0f,
                4.0f, 0.0f, 0.0f
        )

        val colors: FloatArray = floatArrayOf(
                1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f
        )

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        colorsBuffer = ByteBuffer.allocateDirect(colors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colors).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aColor = GLES32.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        Log.e("ja333son", "drawSelf: $program, $aPosition, $aColor, $uMVPMatrix")
    }
}