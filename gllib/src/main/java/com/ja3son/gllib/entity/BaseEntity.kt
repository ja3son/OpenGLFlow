package com.ja3son.gllib.entity

import android.opengl.GLES32
import android.opengl.Matrix
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.FloatBuffer

abstract class BaseEntity {
    protected val FLOAT_SIZE: Int = 4
    protected val UNIT_SIZE: Float = 1f

    protected var program: Int = 0
    protected var aPosition: Int = 0
    protected var aColor: Int = 0
    protected var uMVPMatrix: Int = 0
    protected var modelMatrix: FloatArray = FloatArray(16)
    protected val posLen: Int = 3
    protected val colorLen: Int = 4
    protected var vCounts: Int = 0
    protected var iCounts: Int = 0
    var yAngle = 0f
    var xAngle = 0f
    var touchIndex = 0

    protected lateinit var verticesBuffer: FloatBuffer
    protected lateinit var indicesBuffer: ByteBuffer
    protected lateinit var colorsBuffer: FloatBuffer

    fun init() {
        initData()

        initVertexData()
        initShader()
        initShaderParams()
    }

    private fun initData() {
        Matrix.setIdentityM(modelMatrix, 0)
    }

    open fun initVertexData() {

    }

    open fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_fragment.glsl")
        )
    }

    open fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aColor = GLES32.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    open fun drawSelf() {
        GLES32.glUseProgram(program)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aColor, colorLen, GLES32.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aColor)
    }
}