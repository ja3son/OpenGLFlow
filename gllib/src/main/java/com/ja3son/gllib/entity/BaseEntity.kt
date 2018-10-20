package com.ja3son.gllib.entity

import java.nio.FloatBuffer

abstract class BaseEntity() {
    protected val FLOAT_SIZE: Int = 4
    protected var program: Int = 0
    protected var aPosition: Int = 0
    protected var aColor: Int = 0
    protected var uMVPMatrix: Int = 0

    protected lateinit var verticesBuffer: FloatBuffer
    protected lateinit var colorsBuffer: FloatBuffer

    init {
        initVertexData()
        initShader()
        initShaderParams()
    }

    abstract fun initVertexData()

    abstract fun initShader()

    abstract fun initShaderParams()

    abstract fun drawSelf()
}