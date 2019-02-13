package com.ja3son.gllib.entity

import android.opengl.Matrix
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

    abstract fun initVertexData()

    abstract fun initShader()

    abstract fun initShaderParams()

    abstract fun drawSelf()
}