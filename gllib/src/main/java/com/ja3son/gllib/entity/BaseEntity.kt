package com.ja3son.gllib.entity

import android.opengl.Matrix
import java.nio.FloatBuffer

abstract class BaseEntity() {
    protected val FLOAT_SIZE: Int = 4

    protected var program: Int = 0
    protected var aPosition: Int = 0
    protected var aColor: Int = 0
    protected var uMVPMatrix: Int = 0
    protected var viewMatrix: FloatArray = FloatArray(16)
    protected var modelMatrix: FloatArray = FloatArray(16)
    protected var projMatrix: FloatArray = FloatArray(16)
    protected var mvpMatrix: FloatArray = FloatArray(16)

    protected lateinit var verticesBuffer: FloatBuffer
    protected lateinit var colorsBuffer: FloatBuffer

    init {
        initData()

        initVertexData()
        initShader()
        initShaderParams()
    }

    private fun initData() {
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setIdentityM(projMatrix, 0)
        Matrix.setIdentityM(mvpMatrix, 0)
    }

    abstract fun initVertexData()

    abstract fun initShader()

    abstract fun initShaderParams()

    open fun drawSelf() {
        multiplyMatrix()
    }

    fun frustum(offset: Int, left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        Matrix.frustumM(projMatrix, offset, left, right, bottom, top, near, far)
    }

    fun lookAt(offset: Int, eyeX: Float, eyeY: Float, eyeZ: Float, centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float, upZ: Float) {
        Matrix.setLookAtM(viewMatrix, offset, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
    }

    private fun multiplyMatrix() {
        Matrix.setIdentityM(mvpMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvpMatrix, 0)
    }
}